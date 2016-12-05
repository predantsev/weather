package com.crossover.trial.weather.multitThredCases.specialTestClasses;

import com.crossover.trial.weather.service.impl.AbstractWeatherService;

/**
 * Created by predantsev on 04.12.2016.
 */
public class ThreadUnsafeWeatherServiceImpl extends AbstractWeatherService<Integer> {

    private ThreadUnsafeWeatherServiceImpl() {
        repository = ThreadUnsafeMemoryRepository.getInstance();
    }

    public static ThreadUnsafeWeatherServiceImpl getInstance() {
        if (instance == null) {
            instance = new ThreadUnsafeWeatherServiceImpl();
        }
        return (ThreadUnsafeWeatherServiceImpl) instance;
    }

    @Override
    protected Integer getZero() {
        return 0;
    }

    @Override
    protected Integer incrementFrequency(Integer frequency) {
        return ++frequency;
    }
}
