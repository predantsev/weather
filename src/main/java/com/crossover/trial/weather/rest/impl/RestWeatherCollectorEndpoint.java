package com.crossover.trial.weather.rest.impl;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.repository.MemoryRepository;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.rest.WeatherCollectorEndpoint;
import com.crossover.trial.weather.service.AirportService;
import com.crossover.trial.weather.service.WeatherService;
import com.crossover.trial.weather.service.impl.AirportServiceImpl;
import com.crossover.trial.weather.service.impl.WeatherServiceImpl;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

    /** shared gson json to object factory */
    public final static Gson gson = new Gson();

    private WeatherService weatherService = WeatherServiceImpl.getInstance();
    private AirportService airportService = AirportServiceImpl.getInstance();
    private Repository repository = MemoryRepository.getInstance();

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(String iataCode,
                                  String pointType,
                                  String datapointJson) {
        try {
            airportService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response getAirports() {
        Set<String> retval = new HashSet<>();
        for (AirportData ad : repository.getAirportData()) {
            retval.add(ad.getIata());
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }


    @Override
    public Response getAirport(String iata) {
        AirportData ad = weatherService.findAirportData(iata);
        return Response.status(Response.Status.OK).entity(ad).build();
    }


    @Override
    public Response addAirport(String iata,
                               String latString,
                               String longString) {
        airportService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.CREATED).build();
    }


    @Override
    public Response deleteAirport(String iata) {
        airportService.deleteAirport(iata);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }

}
