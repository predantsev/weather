package com.crossover.trial.weather.service.impl;

import com.crossover.trial.weather.repository.MemoryRepository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 11/30/2016.
 */
public class WeatherServiceImpl extends AbstractWeatherService<AtomicInteger> {

    private WeatherServiceImpl() {
        repository = MemoryRepository.getInstance();
    }

    public static WeatherServiceImpl getInstance() {
        if (instance == null) {
            instance = new WeatherServiceImpl();
        }
        return (WeatherServiceImpl) instance;
    }

    @Override
    protected AtomicInteger getZero() {
        return new AtomicInteger(0);
    }

    @Override
    protected AtomicInteger incrementFrequency(AtomicInteger frequency) {
        return new AtomicInteger(frequency.getAndIncrement());
    }
}
