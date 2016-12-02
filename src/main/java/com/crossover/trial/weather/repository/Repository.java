package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 12/2/2016.
 */
public interface Repository {

     List<AirportData> getAirportData();

     List<AtmosphericInformation> getAtmosphericInformation();

     Map<AirportData, AtomicInteger> getRequestFrequency();

     Map<Double, AtomicInteger> getRadiusFreq();
}
