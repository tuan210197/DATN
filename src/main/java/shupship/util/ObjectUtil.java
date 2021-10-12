package shupship.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

public class ObjectUtil {


    public static Map<String, Method> getReadMethods(Object source, List<String> propertyNames) {
        if (source == null || propertyNames == null || propertyNames.isEmpty()) return null;
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
        return Arrays.stream(sourcePds)
                .filter(sourcePd -> propertyNames.contains(sourcePd.getName()) && sourcePd.getReadMethod() != null)
                .map(sourcePd -> Map.entry(sourcePd.getName(), sourcePd.getReadMethod()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Object getValue(Object source, Method readMethod) {
        if(source == null || readMethod == null) return null;
//        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
//            readMethod.setAccessible(true);
//        }
        try {
            return readMethod.invoke(source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValue(Map<String, String> map, String key) {
        if (map == null || key == null) return null;
        return map.get(key);
    }

    public static String getValueOrDefault(Map<String, String> map, String key) {
        if (map == null || key == null) return null;
        String value = map.get(key);
        return value == null ? map.get("") : value;
    }

    public static void copyProperties(Object source, Object target, boolean merge,
                                      @Nullable String... ignoreProperties) {

        if (source == null || target == null) return;

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                        ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

                        // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
                        boolean isAssignable =
                                (sourceResolvableType.hasUnresolvableGenerics() || targetResolvableType.hasUnresolvableGenerics() ?
                                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType()) :
                                        targetResolvableType.isAssignableFrom(sourceResolvableType));

                        if (isAssignable) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                if (!merge || value != null) {
                                    writeMethod.invoke(target, value);
                                }
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static Integer toInteger(Object value, Integer defaultValue) {
        return !(value instanceof Integer) || (Integer) value == 0 ? defaultValue : (Integer) value;
    }

    public static Byte toByte(Object value, Byte defaultValue) {
        return !(value instanceof Byte) || (Byte) value == 0 ? defaultValue : (Byte) value;
    }

    public static Short toShort(Object value, Short defaultValue) {
        return !(value instanceof Short) || (Short) value == 0 ? defaultValue : (Short) value;
    }

    public static Long toLong(Object value, Long defaultValue) {
        return !(value instanceof Long) || (Long) value == 0L ? defaultValue : (Long) value;
    }

    public static Double toDouble(Double value, Double defaultValue) {
        return value == null || value == 0.0D ? defaultValue : value;
    }

    public static Float toFloat(Object value, Float defaultValue) {
        return !(value instanceof Float) || (Float) value == 0.0F ? defaultValue : (Float) value;
    }

    public static String toString(Object value, String defaultValue) {
        if(value == null) return defaultValue;
        String valueInString = value.toString();
        return StringUtils.isEmpty(valueInString) ? defaultValue : valueInString;
    }
}
