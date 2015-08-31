package org.sursmobil.utils;

import java.lang.reflect.Method;

/**
 * Created by CJ on 15/08/2015.
 */
public class MethodUtil {
    private MethodUtil() {}

    public static String format(Method method) {
        return format(method, false);
    }

    public static String format(Method method, boolean beginOfSentence) {
        String begin = beginOfSentence ? "Method " : "method ";
        return begin + method.getName() + " in class " + method.getDeclaringClass().getName();
    }
}
