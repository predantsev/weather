package com.crossover.trial.weather.service.impl;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.service.WeatherService;

/**
 * Created by predantsev on 05.12.2016.
 */
public abstract class AbstractWeatherService<T extends Number> implements WeatherService {

    protected static AbstractWeatherService instance;

    protected Repository<T> repository;

    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        T zero = getZero();

        T currentCountRequestFrequency = repository.getRequestFrequency().getOrDefault(airportData, zero);
        currentCountRequestFrequency = incrementFrequency(currentCountRequestFrequency);

        repository.getRequestFrequency().put(airportData, currentCountRequestFrequency);
        repository.getRadiusFreq().put(radius, repository.getRadiusFreq().getOrDefault(radius, zero));
    }

    protected abstract T getZero();

    protected abstract T incrementFrequency(T frequency);

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public AirportData findAirportData(String iataCode) {
        return repository.getAirportData().stream()                      //TODO need a comment
                .filter(ap -> ap.getIata().equals(iataCode))
                .findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return repository.getAirportData().indexOf(ad);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
