package org.sursmobil.utils;

/**
 * Created by CJ on 30/08/2015.
 */
public class RunUtils {
    private RunUtils() {}

    public static <T> T withUncheckedThrow(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
