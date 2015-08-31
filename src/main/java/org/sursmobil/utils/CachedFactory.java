package org.sursmobil.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by CJ on 29/08/2015.
 */
public class CachedFactory {

    /**
     * This method will create new CachedFactory using default implementation of Map.
     *
     * @param instantiate Method to instantiate new instances
     * @return New CachedFactory
     */
    public static CachedFactory create(Instantiate instantiate) {
        return new CachedFactory(
                new HashMap<>(),
                instantiate
        );
    }

    /**
     * This method will return existing instance of instanceClass if already cached or new provided by given provider.
     * Instance created by provider will be added to cache before returning, so next call will return same value.
     *
     * @param instanceClass Class of instance
     * @param <T> Type to instantiate
     * @return Instance of requested class
      */
    public <T> T getInstance(Class<T> instanceClass) {
        createCellIfMissing(instanceClass);
        return getPresentInstance(instanceClass);
    }

    /**
     * This method forces creation of instance of instanceClass if its missing in cache.
     * @param instanceClass class of instance to create
     * @return  True is returned if new instance was created, false otherwise.
     */
    public boolean createInstance(Class<?> instanceClass) {
        return createCellIfMissing(instanceClass);
    }

    private synchronized <T> boolean createCellIfMissing(Class<T> instanceClass) {
        boolean willBeCreated = !hasInstance(instanceClass);
        if(willBeCreated) {
            Cell<T> instance = createInstanceCell(instanceClass);
            putInstanceCell(instanceClass, instance);
        }
        return willBeCreated;
    }

    private <T> void putInstanceCell(Class<T> instanceClass, Cell<T> instance) {
        cache.put(instanceClass, instance);
    }

    @SuppressWarnings("unchecked")
    private <T> T getPresentInstance(Class<T> instanceClass) {
        return (T) cache.get(instanceClass).getInstance();
    }

    private <T> Cell<T> createInstanceCell(Class<T> instanceClass) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(() ->
                instantiate.create(instanceClass)
        );
        return new Cell<>(future);
    }

    private boolean hasInstance(Class<?> instanceClass) {
        return cache.containsKey(instanceClass);
    }

    private final Map<Class<?>, Cell<?>> cache;
    private final Instantiate instantiate;

    private CachedFactory(Map<Class<?>, Cell<?>> cache, Instantiate instantiate) {
        this.cache = cache;
        this.instantiate = instantiate;
    }

    private class Cell<T> {
        private final CompletableFuture<T> future;

        private Cell(CompletableFuture<T> future) {
            this.future = future;
        }

        private T getInstance() {
            return RunUtils.uncheckedSupply(future::get);
        }
    }
}
