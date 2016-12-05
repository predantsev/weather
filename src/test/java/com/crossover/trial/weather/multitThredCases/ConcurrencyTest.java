package com.crossover.trial.weather.multitThredCases;

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

    @Test(threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 10000)
    public void testMapOperations() throws Exception {
        testMapOperationsGeneric();
    }

    @Test(singleThreaded = true, invocationCount = 100, invocationTimeOut = 10000)
    public void testMapOperationsSafe() throws Exception {
        testMapOperationsGeneric();
    }

    private void testMapOperationsGeneric() {
        data.put("1", "111");
        data.put("2", "112");
        data.put("3", "113");
        data.put("4", "114");
        data.put("5", "115");
        data.put("6", "116");
        data.put("7", "117");
        data.entrySet().forEach(System.out::println);
        data.clear();
    }
}
