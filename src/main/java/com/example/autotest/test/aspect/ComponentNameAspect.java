package com.example.autotest.test.aspect;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.container.WebElementContainer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import com.example.autotest.support.frontend.WebElementUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Aspect
public final class ComponentNameAspect {

    @AfterReturning(pointcut = "call(com.example.autotest.frontend.element.container.WebElementContainer+.new(..))", returning = "container")
    public void updateComponentNames(WebElementContainer container) {
        setComponentNames(container, getComponents(container));
    }

    private void setComponentNames(WebElementContainer container, List<RegularWebElement> components) {
        WebElementUtils.setComponentNames(container, components);
        components.forEach(component -> {
            if (component instanceof WebElementContainer webElementContainer) {
                setComponentNames(webElementContainer, getComponents(webElementContainer));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<RegularWebElement> getComponents(WebElementContainer container) {
        List<RegularWebElement> components = new ArrayList<>();
        Class<?> objectClass = container.getClass();
        while (!objectClass.equals(Object.class)) {
            List<RegularWebElement> pageComponents = Arrays.stream(objectClass.getDeclaredFields())
                    .filter(field -> isRegularWebElement(container, field))
                    .flatMap(field -> accessFieldAndDo(container, field, fld -> {
                                try {
                                    if (List.class.isAssignableFrom(fld.getType())) {
                                        return ((List<RegularWebElement>) fld.get(container)).stream();
                                    } else {
                                        return Stream.of((RegularWebElement) fld.get(container));
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new AutotestException(e);
                                }
                            })
                    )
                    .toList();
            components.addAll(pageComponents);
            objectClass = objectClass.getSuperclass();
        }

        return components;
    }

    private boolean isRegularWebElement(WebElementContainer container, Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            return accessFieldAndDo(container, field, fld -> {
                try {
                    return RegularWebElement.class.isAssignableFrom(((List<?>) fld.get(container)).get(0).getClass());
                } catch (IllegalAccessException e) {
                    throw new AutotestException(e);
                }
            });
        } else {
            return RegularWebElement.class.isAssignableFrom(field.getType());
        }
    }

    @SuppressWarnings("all")
    private <T> T accessFieldAndDo(WebElementContainer container, Field field, Function<Field, T> action) {
        boolean isAccessible = field.canAccess(container);
        if (!isAccessible) {
            field.setAccessible(true);
        }
        try {
            return action.apply(field);
        } finally {
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }
}
