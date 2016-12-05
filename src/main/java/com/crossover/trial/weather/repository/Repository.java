package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 12/2/2016.
 */
public interface Repository<T extends Number> {

     List<AirportData> getAirportData();

     List<AtmosphericInformation> getAtmosphericInformation();

     Map<AirportData, T> getRequestFrequency();

     Map<Double, T> getRadiusFreq();
}
