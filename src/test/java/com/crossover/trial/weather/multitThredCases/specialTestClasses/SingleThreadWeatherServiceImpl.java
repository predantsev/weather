package com.crossover.trial.weather.multitThredCases.specialTestClasses;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.service.WeatherService;

/**
 * Created by predantsev on 04.12.2016.
 */
public class SingleThreadWeatherServiceImpl implements WeatherService {

    private static SingleThreadWeatherServiceImpl instance;

    private Repository<Integer> repository = SingleThreadMemoryRepository.getInstance();

    private SingleThreadWeatherServiceImpl() {
    }

    public static SingleThreadWeatherServiceImpl getInstance() {
        if(instance == null) {
            instance = new SingleThreadWeatherServiceImpl();
        }
        return instance;
    }

    @Override
    public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    @Override
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        int zero = 0;

        int currentCountRequestFrequency = repository.getRequestFrequency().getOrDefault(airportData, zero);
        currentCountRequestFrequency++;

        repository.getRequestFrequency().put(airportData, currentCountRequestFrequency);
        repository.getRadiusFreq().put(radius, repository.getRadiusFreq().getOrDefault(radius, zero));
    }

    @Override
    public int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return repository.getAirportData().indexOf(ad);
    }

    @Override
    public AirportData findAirportData(String iataCode) {
        return repository.getAirportData().stream()                      //TODO need a comment
                .filter(ap -> ap.getIata().equals(iataCode))
                .findFirst().orElse(null);
    }
}
