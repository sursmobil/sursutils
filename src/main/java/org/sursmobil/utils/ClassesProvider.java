package org.sursmobil.utils;

import java.util.Collection;

/**
 * Created by CJ on 31/08/2015.
 */
public interface ClassesProvider<T> {
    Collection<Class<? extends T>> classes();
}
