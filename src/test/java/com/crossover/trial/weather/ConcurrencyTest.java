package com.crossover.trial.weather;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by predantsev on 02.12.2016.
 */
public class ConcurrencyTest extends Assert {
    private Map<String, String> data;

    @BeforeClass
    void setUp() throws Exception {
//        data = new ConcurrentHashMap<>();
        data = new HashMap<>();
    }

    @AfterClass
    void tearDown() throws Exception {
        data = null;
    }

    @Test(threadPoolSize = 100, invocationCount = 100, invocationTimeOut = 10000)
    public void testMapOperations() throws Exception {
        data.put("1", "111");
        data.put("2", "111");
        data.put("3", "111");
        data.put("4", "111");
        data.put("5", "111");
        data.put("6", "111");
        data.put("7", "111");
        data.entrySet().forEach(System.out::println);
        data.clear();
    }

    @Test(singleThreaded = true, invocationCount = 100, invocationTimeOut = 10000)
    public void testMapOperationsSafe() throws Exception {
        data.put("10", "222");
        data.put("20", "222");
        data.put("30", "222");
        data.put("40", "222");
        data.put("50", "222");
        data.put("60", "222");
        data.put("70", "222");
        data.entrySet().forEach(System.out::println);
        data.clear();
    }
}
