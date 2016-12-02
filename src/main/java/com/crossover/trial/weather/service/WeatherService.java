package com.crossover.trial.weather.service;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.service.impl.WeatherServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by user on 11/30/2016.
 */
public interface WeatherService {
    /** earth radius in KM */
    double R = 6372.8;

    double calculateDistance(AirportData ad1, AirportData ad2);
    void updateRequestFrequency(String iata, Double radius);
    int getAirportDataIdx(String iataCode);
    AirportData findAirportData(String iataCode);
}
