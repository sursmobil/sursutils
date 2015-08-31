package org.sursmobil.utils;

/**
 * Created by CJ on 30/08/2015.
 */
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
