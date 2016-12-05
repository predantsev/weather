package com.crossover.trial.weather.multitThredCases.specialTestClasses;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by predantsev on 04.12.2016.
 */
public class ThreadUnsafeMemoryRepository implements Repository<Integer> {

    private final List<AirportData> airportData;

    private final List<AtmosphericInformation> atmosphericInformation;

    private final Map<AirportData, Integer> requestFrequency;

    private final Map<Double, Integer> radiusFreq;

    private static final Repository<Integer> INSTANCE = new ThreadUnsafeMemoryRepository();

    public static Repository<Integer> getInstance() {
        return INSTANCE;
    }

    private ThreadUnsafeMemoryRepository() {
        atmosphericInformation = new ArrayList<>();
        requestFrequency = new HashMap<>();
        radiusFreq = new HashMap<>();
        airportData = new ArrayList<>();
    }

    public List<AirportData> getAirportData() {
        return airportData;
    }

    public List<AtmosphericInformation> getAtmosphericInformation() {
        return atmosphericInformation;
    }

    public Map<AirportData, Integer> getRequestFrequency() {
        return requestFrequency;
    }

    public Map<Double, Integer> getRadiusFreq() {
        return radiusFreq;
    }
}
