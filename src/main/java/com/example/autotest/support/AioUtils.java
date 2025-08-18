package com.example.autotest.support;

import com.example.autotest.exception.AutotestException;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class AioUtils {

    public static final String DEFAULT_REST_STEP_DESCRIPTION = "Отправить {httpMethod}-запрос на {resourcePath}";
    public static final String DEFAULT_REST_STEP_DESCRIPTION_SUFFIX = ", отправив {httpMethod}-запрос на {resourcePath}";

    private static final String LEFT_ROUND_BRACKET = "(";
    private static final String RIGHT_ROUND_BRACKET = ")";
    private static final String LEFT_ANGLE_BRACKET = "<";
    private static final String RIGHT_ANGLE_BRACKET = ">";
    private static final String ROUND_BRACKETS = "()";
    private static final String SQUARE_BRACKETS = "[]";
    private static final String SHARP = "#";
    private static final String GENERIC_STUB = "T";
    private static final String GENERIC_TYPE_STUB = GENERIC_STUB + ROUND_BRACKETS;
    private static final String GENERIC_ARRAY_STUB = GENERIC_TYPE_STUB + SQUARE_BRACKETS;

    private AioUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static String getDefaultRestStepExpectedResult(int statusCode, String responseBodyType) {
        return String.format("Код ответа %d. Ответ соответствует схеме%n{%s}", statusCode, responseBodyType);
    }

    public static String getDefaultRestStepExpectedResult(int statusCode, Class<?> responseBodyType) {
        return getDefaultRestStepExpectedResult(statusCode, responseBodyType.getName());
    }

    /**
     * Создает экземпляр класса по заданному имени со значением по умолчанию. Для пустой строки подставляется значение
     * "string". Имя должно быть полным квалифицированным именем класса, дополнительно содержащим список параметров нужного
     * конструктора в круглых скобках (). Для примитивных типов имена пакетов не указываются. Параметры в списке
     * перечисляются через запятую без пробелов. При отсутствии параметров указывается пустой список. Параметры в списке
     * именуются рекурсивно по тем же правилам, что и основной класс.<br>
     * Имя может содержать ссылку на статический инициализирующий метод, указываемый через #, при этом список параметров
     * указывается только для метода.<br>
     * Поддерживаются параметризованные типы, реализующие интерфейс {@link java.util.Collection}. Для этих типов обязательно
     * указание параметра типа в угловых скобках <> по вышеизложенным правилам. Ссылка на метод и список параметров
     * указываются после параметра типа. Если параметр в списке параметров является параметром типа, то указывается
     * подстановка вида T(). Созданная коллекция будет содержать один объект.<br>
     * Если требуется создать массив указанного типа, то в конце после списка параметров указываются квадратные скобки [].
     * Для подставляемых параметров в параметризованных типах указывается T()[]. Массив будет содержать один объект.<br>
     * Возможны довольно сложные варианты подстановки. Примеры:
     * <ul>
     *     <li>java.lang.String() создаст строку через вызов конструктора без параметров</li>
     *     <li>java.lang.String()[] создаст массив из одной строки через вызов конструктора без параметров</li>
     *     <li>java.lang.String(char()[],int(),int()) создаст строку через вызов конструктора {@link String#String(char[], int, int)}</li>
     *     <li>java.lang.String#valueOf(int()) создаст строку через вызов статического метода {@link String#valueOf(int)}</li>
     *     <li>java.lang.Integer#parseInt(java.lang.String#valueOf(int())) создаст число из строки, полученной через вызов {@link String#valueOf(int)}</li>
     *     <li>java.util.List<java.lang.String#valueOf(int())>#of(T()) создаст список из одной строки через вызов статического метода {@link List#of(Object)}</li>
     *     <li>и т.д.</li>
     * </ul>
     * @param fullQualifiedName имя, по которому будет создан экземпляр класса
     * @return экземпляр требуемого класса
     */
    public static Object createObject(String fullQualifiedName) {
        Class<?> objectClass = createObjectClass(fullQualifiedName);
        if (objectClass.isArray()) {
            return createArrayObject(fullQualifiedName);
        } else if (objectClass.isPrimitive()) {
            return createPrimitiveObject(objectClass);
        } else {
            return createObject(fullQualifiedName, objectClass);
        }
    }

    private static Object createObject(String fullQualifiedName, Class<?> objectClass) {
        try {
            int parsingStartIndex = getParsingStartIndex(fullQualifiedName);
            String[] typeNames = getTypeNames(
                    fullQualifiedName.substring(
                            fullQualifiedName.indexOf(LEFT_ROUND_BRACKET, parsingStartIndex) + 1,
                            fullQualifiedName.lastIndexOf(RIGHT_ROUND_BRACKET)
                    )
            );
            Class<?>[] parameterTypes = getParameterTypes(typeNames);
            Object[] initArgs = createInitArgs(parameterTypes, typeNames);

            if (hasMethodName(fullQualifiedName, parsingStartIndex)) {
                if (Collection.class.isAssignableFrom(objectClass)) {
                    for (int i = 0; i < typeNames.length; i++) {
                        if (typeNames[i].equals(GENERIC_TYPE_STUB)) {
                            initArgs[i] = createObject(getTypeParameterName(fullQualifiedName));
                        } else if (typeNames[i].equals(GENERIC_ARRAY_STUB)) {
                            initArgs[i] = createObject(getTypeParameterName(fullQualifiedName) + SQUARE_BRACKETS);
                        }
                    }
                }
                String methodName = fullQualifiedName.substring(
                        fullQualifiedName.indexOf(SHARP, parsingStartIndex) + 1,
                        fullQualifiedName.indexOf(LEFT_ROUND_BRACKET, parsingStartIndex)
                );
                Method staticMethod = objectClass.getMethod(methodName, parameterTypes);
                return staticMethod.invoke(null, initArgs);
            } else {
                Object instance = objectClass.getConstructor(parameterTypes).newInstance(initArgs);
                if (Collection.class.isAssignableFrom(objectClass)) {
                    objectClass.getMethod("add", Object.class).invoke(instance, createObject(getTypeParameterName(fullQualifiedName)));
                }
                return instance;
            }
        } catch (Exception e) {
            throw new AutotestException(e);
        }
    }

    private static int getParsingStartIndex(String name) {
        if (hasTypeParameter(name)) {
            String typeParameterName = getTypeParameterName(name);
            return name.indexOf(typeParameterName) + typeParameterName.length();
        } else {
            return 0;
        }
    }

    private static Class<?>[] getParameterTypes(String[] typeNames) {
        return Arrays.stream(typeNames)
                .map(AioUtils::createObjectClass)
                .toArray(Class<?>[]::new);
    }

    private static Object[] createInitArgs(Class<?>[] parameterTypes, String[] typeNames) {
        Object[] initArgs = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.isPrimitive()) {
                if (parameterType.equals(char.class)) {
                    initArgs[i] = 'c';
                } else if (parameterType.equals(boolean.class)) {
                    initArgs[i] = false;
                } else {
                    initArgs[i] = 0;
                }
            } else {
                initArgs[i] = createObject(typeNames[i]);
            }
        }

        return Arrays.stream(initArgs)
                .map(arg -> {
                    if (arg.equals("")) {
                        return "string";
                    } else {
                        return arg;
                    }
                })
                .toArray();
    }

    private static Object createArrayObject(String fullQualifiedName) {
        String arrayComponentName;
        String shortenedName = fullQualifiedName.substring(0, fullQualifiedName.indexOf(LEFT_ROUND_BRACKET) + 1);
        if (hasTypeParameter(shortenedName)) {
            arrayComponentName = fullQualifiedName.substring(0, fullQualifiedName.indexOf(LEFT_ANGLE_BRACKET));
        } else if (hasMethodName(shortenedName, 0)) {
            arrayComponentName = shortenedName.substring(0, shortenedName.indexOf(SHARP));
        } else {
            arrayComponentName = shortenedName.substring(0, shortenedName.indexOf(LEFT_ROUND_BRACKET));
        }
        Class<?> arrayClass = createObjectClass(arrayComponentName + ROUND_BRACKETS);
        Object array = Array.newInstance(arrayClass, 1);
        Object arrayMemberInstance = createObject(fullQualifiedName.substring(0, fullQualifiedName.lastIndexOf(SQUARE_BRACKETS)));
        Array.set(array, 0, arrayMemberInstance);

        return array;
    }

    private static Object createPrimitiveObject(Class<?> objectClass) {
        if (objectClass.equals(byte.class)) {
            return (byte) 0;
        } else if (objectClass.equals(short.class)) {
            return (short) 0;
        } else if (objectClass.equals(char.class)) {
            return 'c';
        } else if (objectClass.equals(int.class)) {
            return 0;
        } else if (objectClass.equals(long.class)) {
            return 0L;
        } else if (objectClass.equals(float.class)) {
            return 0F;
        } else if (objectClass.equals(double.class)) {
            return 0D;
        } else if (objectClass.equals(boolean.class)) {
            return false;
        } else {
            throw new AutotestException("Не найдено соответствие класса для примитивного типа " + objectClass);
        }
    }

    private static String getTypeParameterName(String fullQualifiedName) {
        int startIndex = fullQualifiedName.indexOf(LEFT_ANGLE_BRACKET);
        int endIndex = fullQualifiedName.indexOf(RIGHT_ANGLE_BRACKET) + 1;
        if (startIndex < 0 && endIndex == 0) {
            throw new AutotestException("Ошибка при анализе параметра подстановки: отсутствует параметр типа в имени параметризованного класса.");
        }
        if (startIndex >= endIndex) {
            throw new AutotestException("Ошибка при анализе параметра подстановки: неверный порядок '<' и '>' в имени параметризованного класса.");
        }

        String typeParameterName = fullQualifiedName.substring(startIndex + 1, endIndex - 1);
        if (typeParameterName.endsWith(SQUARE_BRACKETS)) {
            throw new AutotestException("Ошибка при анализе параметра подстановки: параметр типа не может быть массивом.");
        }

        while (endIndex > 0) {
            String currentSubstring = getSubstring(fullQualifiedName, startIndex, endIndex);
            if (StringUtils.countMatches(currentSubstring, LEFT_ANGLE_BRACKET) == StringUtils.countMatches(currentSubstring, RIGHT_ANGLE_BRACKET)) {
                typeParameterName = currentSubstring;
                break;
            } else {
                endIndex = fullQualifiedName.indexOf(RIGHT_ANGLE_BRACKET, endIndex) + 1;
            }
        }

        typeParameterName = typeParameterName.substring(1, typeParameterName.length() - 1);
        if (typeParameterName.isBlank() && !hasMethodName(fullQualifiedName, 0)) {
            throw new AutotestException("Ошибка при анализе параметра подстановки: пустой параметр типа в имени параметризованного класса.");
        }

        return typeParameterName;
    }

    private static String[] getTypeNames(String argString) {
        if (!argString.isEmpty()) {
            if (StringUtils.countMatches(argString, LEFT_ROUND_BRACKET) == StringUtils.countMatches(argString, RIGHT_ROUND_BRACKET)) {
                int startIndex = 0;
                int endIndex = hasTypeParameter(argString) ? argString.indexOf(RIGHT_ANGLE_BRACKET) + 1 : argString.indexOf(RIGHT_ROUND_BRACKET) + 1;
                if (startIndex >= endIndex) {
                    throw new AutotestException("Ошибка при анализе параметра подстановки: неверный порядок '(' и ')' в списке параметров.");
                }

                return getTypeNames(argString, startIndex, endIndex);
            } else {
                throw new AutotestException("Ошибка при анализе параметра подстановки: число '(' и ')' в списке параметров не совпадает.");
            }
        } else {
            return new String[0];
        }
    }

    private static String[] getTypeNames(String argString, int startIndex, int endIndex) {
        List<String> typeNames = new ArrayList<>();
        boolean typeParameterChecked = false;
        while (endIndex > 0) {
            String currentSubstring = getSubstring(argString, startIndex, endIndex);
            while (hasTypeParameter(currentSubstring) && !typeParameterChecked && endIndex > 0) {
                String subsubstring = getSubstring(currentSubstring, startIndex, endIndex);
                if (StringUtils.countMatches(subsubstring, LEFT_ANGLE_BRACKET) == StringUtils.countMatches(subsubstring, RIGHT_ANGLE_BRACKET)) {
                    endIndex = argString.indexOf(RIGHT_ROUND_BRACKET, endIndex) + 1;
                    currentSubstring = getSubstring(argString, startIndex, endIndex); //обновляем подстроку для обновленного индекса
                    typeParameterChecked = true;
                } else {
                    endIndex = argString.indexOf(RIGHT_ANGLE_BRACKET, endIndex) + 1;
                }
            }
            if (StringUtils.countMatches(currentSubstring, LEFT_ROUND_BRACKET) == StringUtils.countMatches(currentSubstring, RIGHT_ROUND_BRACKET)
                    && !currentSubstring.substring(1, currentSubstring.length() - 1).isBlank()) {
                if (endIndex < argString.length() && argString.charAt(endIndex) == '[') {
                    currentSubstring = currentSubstring + SQUARE_BRACKETS;
                    startIndex = endIndex + 2;
                } else {
                    startIndex = endIndex;
                }
                endIndex = argString.indexOf(RIGHT_ROUND_BRACKET, startIndex) + 1;
                if (currentSubstring.startsWith(",")) {
                    currentSubstring = currentSubstring.substring(1);
                }
                typeNames.add(currentSubstring);
                typeParameterChecked = false;
            } else {
                endIndex = argString.indexOf(RIGHT_ROUND_BRACKET, endIndex) + 1;
            }
        }

        return typeNames.toArray(String[]::new);
    }

    private static String getSubstring(String argString, int startIndex, int endIndex) {
        return endIndex == argString.length() ? argString.substring(startIndex) : argString.substring(startIndex, endIndex);
    }

    private static boolean hasTypeParameter(String name) {
        return name.contains(LEFT_ANGLE_BRACKET) && name.indexOf(LEFT_ANGLE_BRACKET) < name.indexOf(LEFT_ROUND_BRACKET);
    }

    private static boolean hasMethodName(String name, int parsingStartIndex) {
        int sharpIdx = name.indexOf(SHARP, parsingStartIndex);
        return name.contains(SHARP) && sharpIdx >= 0 && sharpIdx < name.indexOf(LEFT_ROUND_BRACKET, parsingStartIndex);
    }

    private static Class<?> createObjectClass(String fullQualifiedName) {
        if (!fullQualifiedName.isEmpty()) {
            int endIndex = fullQualifiedName.indexOf(LEFT_ROUND_BRACKET);
            if (endIndex == 0) {
                throw new AutotestException("Имя класса не может начинаться с символа '('.");
            } else if (endIndex < 0) {
                throw new AutotestException("Имя конструктора/метода в подставляемом параметре для генерации теста должно содержать список параметров в '()'.");
            }

            String shortenedClassName = fullQualifiedName.substring(0, endIndex + 1);
            if (hasTypeParameter(shortenedClassName)) {
                endIndex = shortenedClassName.indexOf(LEFT_ANGLE_BRACKET);
            } else if (hasMethodName(shortenedClassName, 0)) {
                endIndex = shortenedClassName.indexOf(SHARP);
            }

            Class<?> objectClass = createClassForName(fullQualifiedName.substring(0, endIndex));
            if (fullQualifiedName.endsWith(SQUARE_BRACKETS)) {
                return createArrayClass(objectClass);
            } else {
                return objectClass;
            }
        } else {
            throw new AutotestException("Пустое имя класса при подстановке параметра.");
        }
    }

    private static Class<?> createArrayClass(Class<?> objectClass) {
        try {
            if (objectClass.isPrimitive()) {
                if (objectClass.equals(long.class)) {
                    return Class.forName("[J");
                } else if (objectClass.equals(boolean.class)) {
                    return Class.forName("[Z");
                } else {
                    return Class.forName(String.format("[%s", objectClass.getName().toUpperCase().charAt(0)));
                }
            } else {
                return Class.forName(String.format("[L%s;", objectClass.getName()));
            }
        } catch (ClassNotFoundException e) {
            throw new AutotestException(e);
        }
    }

    private static Class<?> createClassForName(String className) {
        try {
            return switch (className) {
                case "byte" -> byte.class;
                case "short" -> short.class;
                case "char" -> char.class;
                case "int" -> int.class;
                case "long" -> long.class;
                case "float" -> float.class;
                case "double" -> double.class;
                case "boolean" -> boolean.class;
                case GENERIC_STUB -> Object.class;
                default -> Class.forName(className);
            };
        } catch (ClassNotFoundException e) {
            throw new AutotestException(e);
        }
    }
}
