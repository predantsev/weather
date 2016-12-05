package com.crossover.trial.weather.multitThredCases;


import com.crossover.trial.weather.repository.MemoryRepository;
import com.crossover.trial.weather.service.impl.WeatherServiceImpl;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by predantsev on 02.12.2016.
 */
public class MapValueRealDataChangeTest extends AbstractMapValueChangeTest<AtomicInteger> {

    public MapValueRealDataChangeTest() {
        this.memoryRepository = MemoryRepository.getInstance();
        this.weatherService = WeatherServiceImpl.getInstance();
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
