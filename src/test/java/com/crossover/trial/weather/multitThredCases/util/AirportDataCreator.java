package com.crossover.trial.weather.multitThredCases.util;

import com.crossover.trial.weather.dto.AirportData;

import java.util.Optional;

/**
 * Created by predantsev on 03.12.2016.
 */
public class AirportDataCreator {

    private static final double DEFAULT_VALUE = 0d;

    public static AirportData createSimpleAirportDataEntity(String iata) {
        AirportData airportData = new AirportData();
        airportData.setIata(iata);
        return airportData;
    }

    public static AirportData createCompleteAirportDataEntity(String iata, String arg1, String arg2) {
        AirportData airportData = new AirportData();
        if (iata == null || iata.isEmpty()) {
            throw new UnsupportedOperationException("Couldn't create an entity with empty data");
        }
        Optional<Double> latitudeOptional = Optional.ofNullable(arg1 == null ? null : Double.valueOf(arg1));
        Optional<Double> longitudeOptional = Optional.ofNullable(arg2 == null ? null : Double.valueOf(arg2));
        airportData.setIata(iata);
        airportData.setLatitude(latitudeOptional.orElse(DEFAULT_VALUE));
        airportData.setLongitude(longitudeOptional.orElse(DEFAULT_VALUE));
        return airportData;
    }
}
