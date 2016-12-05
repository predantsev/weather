package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 12/2/2016.
 */
public class MemoryRepository implements Repository<AtomicInteger> {

    /** all known airports */
    private final List<AirportData> airportData;

    /** atmospheric information for each airport, idx corresponds with airportData */
    private final List<AtmosphericInformation> atmosphericInformation;

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {@link #ping()}
     */
    private final Map<AirportData, AtomicInteger> requestFrequency;

    private final Map<Double, AtomicInteger> radiusFreq;

    private static final Repository<AtomicInteger> INSTANCE = new MemoryRepository();

    public static Repository<AtomicInteger> getInstance() {
        return INSTANCE;
    }

    private MemoryRepository() {
        atmosphericInformation = new CopyOnWriteArrayList<>();
        requestFrequency = new ConcurrentHashMap<>();
        radiusFreq = new ConcurrentHashMap<>();
        airportData = new CopyOnWriteArrayList<>();
    }

    public List<AirportData> getAirportData() {
        return airportData;
    }

    public  List<AtmosphericInformation> getAtmosphericInformation() {
        return atmosphericInformation;
    }

    public Map<AirportData, AtomicInteger> getRequestFrequency() {
        return requestFrequency;
    }

    public  Map<Double, AtomicInteger> getRadiusFreq() {
        return radiusFreq;
    }
}
