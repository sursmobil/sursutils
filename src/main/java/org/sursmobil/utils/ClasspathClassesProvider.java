package org.sursmobil.utils;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

/**
 * Created by CJ on 31/08/2015.
 */
public class ClasspathClassesProvider<T> implements ClassesProvider<T> {
    public interface Filter<T> extends Function<Reflections, Collection<Class<? extends T>>>{}

    private final Reflections reflections;
    private final Filter<T> filter;

    private ClasspathClassesProvider(Filter<T> filter) {
        this.filter = filter;
        reflections = createReflections();
    }

    private static Reflections createReflections() {
        return new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader())
        );
    }

    @Override
    public Collection<Class<? extends T>> classes() {
        return filter.apply(reflections);
    }

    public static <T> ClasspathClassesProvider<T> withFilter(Filter<T> filter) {
        return new ClasspathClassesProvider<>(filter);
    }

    public static ClasspathClassesProvider<?> annotatedWith(Class<? extends Annotation> annotationType) {
        return withFilter(reflections -> reflections.getTypesAnnotatedWith(annotationType));
    }
}
