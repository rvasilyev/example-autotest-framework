package com.example.autotest.support.frontend;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.element.IndexedWebElement;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.WebElementWithText;
import com.example.autotest.frontend.element.common.table.Row;
import com.example.autotest.frontend.element.container.WebElementContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import com.example.autotest.support.ExceptionUtils;
import com.example.autotest.support.FileUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public final class WebElementUtils {

    private static final String GET_SELF_METHOD = "getSelf";
    private static final String SET_NAME_METHOD = "setName";

    private WebElementUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static <T> T wrapElementHandling(RegularWebElement decorator, Function<SelenideElement, T> codeToWrap) {
        try {
            SelenideElement self = (SelenideElement) invokeMethod(decorator, GET_SELF_METHOD);
            return codeToWrap.apply(self);
        } catch (AssertionError e) { // ошибки Selenide не должны отображаться в отчете как AssertionError
            throw new AutotestException(e);
        }
    }

    public static boolean isScrollingEnabled(RegularWebElement decorator, boolean vertical) {
        return WebElementUtils.wrapElementHandling(decorator, self -> {
            if (vertical) {
                int clientHeight = Integer.parseInt(Optional.ofNullable(self.getAttribute("clientHeight")).orElse("0"));
                int scrollHeight = Integer.parseInt(Optional.ofNullable(self.getAttribute("scrollHeight")).orElse("0"));
                return clientHeight < scrollHeight;
            } else {
                int clientWidth = Integer.parseInt(Optional.ofNullable(self.getAttribute("clientWidth")).orElse("0"));
                int scrollWidth = Integer.parseInt(Optional.ofNullable(self.getAttribute("scrollWidth")).orElse("0"));
                return clientWidth < scrollWidth;
            }
        });
    }

    public static void scrollElement(RegularWebElement decorator, int pixels, boolean vertical) {
        WebElementUtils.wrapElementHandling(decorator, self -> {
            String jsScript;
            if (vertical) {
                jsScript = FileUtils.getResourceContent("scripts/js/scrollVerticalScript.js");
            } else {
                jsScript = FileUtils.getResourceContent("scripts/js/scrollHorizontalScript.js");
            }
            ((JavascriptExecutor) self.getWrappedDriver()).executeScript(jsScript, self, pixels);

            return null;
        });
    }

    public static <T extends RegularWebElement> void scrollElement(RegularWebElement decorator, List<T> components, T targetComponent, boolean vertical) {
        WebElementUtils.wrapElementHandling(decorator, self -> {
            JavascriptExecutor executor = ((JavascriptExecutor) self.getWrappedDriver());
            Rectangle selfRect = self.getRect();
            T component = components.stream()
                    .filter(targetElement -> targetElement.equals(targetComponent))
                    .findFirst()
                    .orElseThrow(() -> new AutotestException(String.format("Элемент '%s' не найден в элементе '%s'", targetComponent, decorator)));

            String jsScript;
            int pixels;
            boolean reverse;
            if (vertical) {
                jsScript = FileUtils.getResourceContent("scripts/js/scrollVerticalScript.js");
                pixels = selfRect.getHeight();
                reverse = (selfRect.getY() + selfRect.getHeight() / 2) > component.getRect().getY();
            } else {
                jsScript = FileUtils.getResourceContent("scripts/js/scrollHorizontalScript.js");
                pixels = selfRect.getWidth();
                reverse = (selfRect.getX() + selfRect.getWidth() / 2) > component.getRect().getX();
            }

            while (!component.isExist() && !component.isDisplayed()) {
                if (reverse) {
                    executor.executeScript(jsScript, self, -pixels);
                } else {
                    executor.executeScript(jsScript, self, pixels);
                }
            }

            return null;
        });
    }

    public static String getLocatorExpression(By locator) {
        if (locator instanceof By.Remotable remotable) {
            return remotable.getRemoteParameters().value().toString();
        } else {
            throw new AutotestException("Не удалось получить выражение для локатора " + locator);
        }
    }

    public static <T extends RegularWebElement> void setComponentNames(WebElementContainer container, List<T> components) {
        components.forEach(
                component -> invokeMethod(
                        component, SET_NAME_METHOD, String.format("%s -> %s", container, component)
                )
        );
    }

    @SuppressWarnings("all")
    public static <T extends RegularWebElement> List<T> generateComponents(By baseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        ElementsCollection elements = Selenide.elements(baseLocator);
        if (!elements.isEmpty()) {
            List<T> components = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                AbstractMap.SimpleImmutableEntry<By, SelenideElement> entry = new AbstractMap.SimpleImmutableEntry<>(baseLocator, elements.get(i));
                components.add(componentCreator.apply(entry, i));
            }

            return components;
        } else {
            return Collections.emptyList();
        }
    }

    public static <T extends RegularWebElement> List<T> generateComponents(WebElementContainer container, By componentLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        String componentLocatorExpression = WebElementUtils.getLocatorExpression(componentLocator);
        By updatedLocator = componentLocator;
        if (componentLocatorExpression.startsWith(".")) {
            String fullLocator = container.getLocatorAsString() + componentLocatorExpression.substring(componentLocatorExpression.indexOf("/"));
            updatedLocator = By.xpath(fullLocator);
        }
        List<T> components = generateComponents(updatedLocator, componentCreator);
        setComponentNames(container, components);
        return components;
    }

    public static boolean hasComponent(String text, boolean mustEqual, List<? extends RegularWebElement> components) {
        return components.stream().anyMatch(getTextPredicate(text, mustEqual));
    }

    public static boolean hasComponent(int componentIndex, List<? extends IndexedWebElement> components) {
        return components.stream().anyMatch(component -> component.getIndex() == componentIndex);
    }

    public static boolean hasComponent(int componentIndex, String valueToSearch, boolean mustEqual, List<? extends IndexedWebElement> components) {
        return components.stream().anyMatch(getComponentPredicate(componentIndex, valueToSearch, mustEqual));
    }

    public static <T extends RegularWebElement> T getComponent(String text, boolean mustEqual, List<T> components) {
        return components.stream()
                .filter(getTextPredicate(text, mustEqual))
                .findFirst()
                .orElseThrow(() -> new AutotestException(String.format("В списке '%s' отсутствует компонент с текстом '%s'", components, text)));
    }

    public static <T extends IndexedWebElement> T getComponent(int componentIndex, List<T> components) {
        return components.stream()
                .filter(component -> component.getIndex() == componentIndex)
                .findFirst()
                .orElseThrow(() -> new AutotestException(String.format("В списке '%s' отсутствует компонент с индексом %d", components, componentIndex)));
    }

    public static <T extends IndexedWebElement> T getComponent(int componentIndex, String valueToSearch, boolean mustEqual, List<T> components) {
        return components.stream()
                .filter(getComponentPredicate(componentIndex, valueToSearch, mustEqual))
                .findFirst()
                .orElseThrow(() -> new AutotestException(String.format("В списке '%s' отсутствует компонент со значением '%s' и индексом %d", components, valueToSearch, componentIndex)));
    }

    @SuppressWarnings("all")
    private static Object invokeMethod(Object object, String targetMethodName, Object... args) {
        Class<?> objectClass = object.getClass();
        Class<?>[] paramTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        Method targetMethod = null;
        while (objectClass != null) {
            try {
                targetMethod = objectClass.getDeclaredMethod(targetMethodName, paramTypes);
                break;
            } catch (NoSuchMethodException ex) {
                objectClass = objectClass.getSuperclass(); // если метод не найден, продолжить поиск
            }
        }

        if (targetMethod != null) {
            Object result;
            boolean isAccessible = targetMethod.canAccess(object);
            if (!isAccessible) {
                targetMethod.setAccessible(true);
            }
            try {
                result = targetMethod.invoke(object, args);
            } catch (Exception e) {
                throw new AutotestException(e);
            } finally {
                if (!isAccessible) {
                    targetMethod.setAccessible(false);
                }
            }
            return result;
        } else {
            throw new AutotestException(String.format("Метод '%s' не найден в иерархии класса %s", targetMethodName, object.getClass().getName()));
        }
    }

    @SuppressWarnings("all")
    private static <T extends IndexedWebElement> Predicate<T> getComponentPredicate(int index, String valueToSearch, boolean mustEqual) {
        return component -> {
            if (component instanceof Row row) {
                return row.hasCell(index, valueToSearch, mustEqual);
            } else if (component instanceof WebElementWithText) {
                boolean hasIndex = component.getIndex() == index;
                String text = ((WebElementWithText) component).getText().trim();
                return hasIndex && (mustEqual ? text.equals(valueToSearch) : text.contains(valueToSearch));
            } else {
                return false;
            }
        };
    }

    private static <T extends RegularWebElement> Predicate<T> getTextPredicate(String text, boolean mustEqual) {
        return component -> {
            if (component instanceof WebElementWithText webElementWithText) {
                String componentText = webElementWithText.getText();
                return mustEqual ? componentText.equals(text) : componentText.contains(text);
            }

            return false;
        };
    }
}
