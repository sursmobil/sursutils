package org.sursmobil.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by CJ on 31/08/2015.
 */
public class CachedFactoryTests {

    private CachedFactory subject;
    private DelayedInstantiate instantiate;

    @Before
    public void setup() {
        instantiate = new DelayedInstantiate();
        subject = CachedFactory.create(instantiate);
    }

    @Test
    public void onceCreatedInstanceIsCached() {
        allowInstanceCreation();
        TestClass testObject1 = syncGet();
        TestClass testObject2 = syncGet();
        Assert.assertSame(testObject1, testObject2);
    }

    @Test
    public void asyncGetsReturnSameInstance() throws ExecutionException, InterruptedException {
        CompletableFuture<TestClass> testObject1 = asyncGet();
        CompletableFuture<TestClass> testObject2 = asyncGet();
        allowInstanceCreation();
        Assert.assertSame(testObject1.get(), testObject2.get());
    }

    private CompletableFuture<TestClass> asyncGet() {
        return CompletableFuture.supplyAsync(this::syncGet);
    }

    private TestClass syncGet() {
        return subject.getInstance(TestClass.class);
    }

    private void allowInstanceCreation() {
        instantiate.run();
    }

    public static class TestClass{}

    private static class DelayedInstantiate implements Instantiate{
        private boolean wait = true;

        @Override
        public <T> T create(Class<T> type) {
            while(wait) {
            }
            return Instantiate.newInstance().create(type);
        }

        public void run() {
            wait = false;
        }
    }
}
