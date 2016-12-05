package com.crossover.trial.weather.multitThredCases;

import com.crossover.trial.weather.multitThredCases.specialTestClasses.SingleThreadMemoryRepository;
import com.crossover.trial.weather.multitThredCases.specialTestClasses.SingleThreadWeatherServiceImpl;
import org.testng.annotations.Test;

/**
 * Created by predantsev on 05.12.2016.
 */
public class MapValueFakeDataChangeTest extends AbstractMapValueChangeTest<Integer> {

    public MapValueFakeDataChangeTest() {
        this.memoryRepository = SingleThreadMemoryRepository.getInstance();
        this.weatherService = SingleThreadWeatherServiceImpl.getInstance();
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
