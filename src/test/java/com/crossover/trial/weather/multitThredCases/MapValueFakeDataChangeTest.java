package com.crossover.trial.weather.multitThredCases;

import com.crossover.trial.weather.multitThredCases.specialTestClasses.ThreadUnsafeMemoryRepository;
import com.crossover.trial.weather.multitThredCases.specialTestClasses.ThreadUnsafeWeatherServiceImpl;
import org.testng.annotations.Test;

/**
 * Created by predantsev on 05.12.2016.
 */
public class MapValueFakeDataChangeTest extends AbstractMapValueChangeTest<Integer> {

    public MapValueFakeDataChangeTest() {
        this.memoryRepository = ThreadUnsafeMemoryRepository.getInstance();
        this.weatherService = ThreadUnsafeWeatherServiceImpl.getInstance();
    }

    @Override
    @Test(singleThreaded = true, invocationCount = INVOCATION_COUNT, invocationTimeOut = INVOCATION_TIME_OUT)
    public void testUpdateRequestFrequencyWithSingleThread() throws Exception {
        super.testUpdateRequestFrequencyWithSingleThread();
    }

    @Override
    @Test(threadPoolSize = THREAD_POOL_SIZE, invocationCount = INVOCATION_COUNT, invocationTimeOut = INVOCATION_TIME_OUT)
    public void testUpdateRequestFrequencyWithMultiThread() throws Exception {
        super.testUpdateRequestFrequencyWithMultiThread();
    }
}
