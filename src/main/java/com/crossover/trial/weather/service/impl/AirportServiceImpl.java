package com.crossover.trial.weather.service.impl;

import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.enums.DataPointType;
import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.repository.MemoryRepository;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.service.AirportService;
import com.crossover.trial.weather.service.WeatherService;

/**
 * Created by user on 11/30/2016.
 */
public class AirportServiceImpl implements AirportService {

    private static AirportService instance;

    private AirportServiceImpl() {
    }

    public static AirportService getInstance() {
        if (instance == null) {
            instance = new AirportServiceImpl();
        }
        return instance;
    }

    private WeatherService weatherService = WeatherServiceImpl.getInstance();
    private Repository repository = MemoryRepository.getInstance();

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
        int airportDataIdx = weatherService.getAirportDataIdx(iataCode);
        AtmosphericInformation ai = repository.getAtmosphericInformation().get(airportDataIdx);
        updateAtmosphericInformation(ai, pointType, dp);
    }

    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param ai        the atmospheric information object to update
     * @param pointType the data point type as a string
     * @param dp        the actual data point
     */
    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) throws WeatherException {
        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());

        switch (dptype) {
            case WIND:
                if (dp.getMean() >= 0) {
                    ai.setWind(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
            case TEMPERATURE:
                if (dp.getMean() >= -50 && dp.getMean() < 100) {
                    ai.setTemperature(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
            case HUMIDTY:
                if (dp.getMean() >= 0 && dp.getMean() < 100) {
                    ai.setHumidity(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
            case PRESSURE:
                if (dp.getMean() >= 650 && dp.getMean() < 800) {
                    ai.setPressure(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
            case CLOUDCOVER:
                if (dp.getMean() >= 0 && dp.getMean() < 100) {
                    ai.setCloudCover(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
            case PRECIPITATION:
                if (dp.getMean() >= 0 && dp.getMean() < 100) {
                    ai.setPrecipitation(dp);
                    ai.setLastUpdateTime(System.currentTimeMillis());
                    break;
                }
                throw new IllegalStateException("couldn't update atmospheric data");
        }
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     *
     * @return the added airport
     */
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData();
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLongitude(longitude);
        repository.getAirportData().add(ad);

        AtmosphericInformation ai = new AtmosphericInformation();
        repository.getAtmosphericInformation().add(ai);

        return ad;
    }

    public void deleteAirport(String iata) {
        repository.getAirportData().removeIf(i -> i.getIata().equals(iata));
    }
}