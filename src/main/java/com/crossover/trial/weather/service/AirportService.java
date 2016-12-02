package com.crossover.trial.weather.service;

import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.exception.WeatherException;

/**
 * Created by user on 11/30/2016.
 */
public interface AirportService {

    void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException;
    AirportData addAirport(String iataCode, double latitude, double longitude);
    void deleteAirport(String iata);
}
