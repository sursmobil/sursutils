package org.sursmobil.utils;

/**
 * Created by CJ on 30/08/2015.
 */
public interface Instantiate {
    <T> T create(Class<T> type);

    static Instantiate newInstance() {
        return new Instantiate() {
            @Override
            public <T> T create(Class<T> type) {
                return RunUtils.uncheckedSupply(type::newInstance);
            }
        };
    }
}
