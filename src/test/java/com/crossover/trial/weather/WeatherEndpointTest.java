package com.crossover.trial.weather;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.dto.DataPoint;
import com.crossover.trial.weather.repository.MemoryRepository;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.rest.impl.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.rest.impl.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.service.AirportService;
import com.crossover.trial.weather.service.WeatherService;
import com.crossover.trial.weather.service.impl.AirportServiceImpl;
import com.crossover.trial.weather.service.impl.WeatherServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class WeatherEndpointTest {

    private RestWeatherQueryEndpoint _query = new RestWeatherQueryEndpoint();

    private RestWeatherCollectorEndpoint _update = new RestWeatherCollectorEndpoint();

    private AirportService airportService = AirportServiceImpl.getInstance();
    private WeatherService weatherService = WeatherServiceImpl.getInstance();
    private Repository<AtomicInteger> repository = MemoryRepository.getInstance();

    private Gson _gson = new Gson();

    private DataPoint _dp;

    @Before
    public void setUp() throws Exception {
        airportService.addAirport("BOS",42.364347,-71.005181); // TODO need fill data directly
        airportService.addAirport("EWR",40.6925,-74.168667);
        airportService.addAirport("JFK",40.639751,-73.778925);
        airportService.addAirport("LGA",40.777245,-73.872608);
        airportService.addAirport("MMU",40.79935,-74.4148747);

        _dp = new DataPoint.Builder()
                .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(_dp));
        _query.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);

        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());

        Response response = Response.status(Response.Status.OK).entity("ready").build();
        int collectorPing = _update.ping().getStatus();
        int inspection = response.getStatus();
        assertEquals(collectorPing, inspection);
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), _dp);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        _update.updateWeather("JFK", "wind", _gson.toJson(_dp));
        _dp.setMean(40);
        _update.updateWeather("EWR", "wind", _gson.toJson(_dp));
        _dp.setMean(30);
        _update.updateWeather("LGA", "wind", _gson.toJson(_dp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder()
                .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(windDp));
        _query.weather("BOS", "0").getEntity();

        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(4, pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint.Builder()
                .withCount(4).withFirst(10).withMedian(60).withLast(100).withMean(50).build();
        _update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

    @Test
    public void testGetAirports() throws Exception {

        Set<String> retval = new HashSet<>();
        for (AirportData ad : repository.getAirportData()) {
            retval.add(ad.getIata());
        }
        Response response = Response.status(Response.Status.OK).entity(retval).build();
        int airportsAnswer = _update.getAirports().getStatus();
        int inspection = response.getStatus();
        assertEquals(airportsAnswer, inspection);
    }

    @Test
    public void testGetAirport() throws Exception {

        AirportData ad = weatherService.findAirportData("JFK");
        Response response = Response.status(Response.Status.OK).entity(ad).build();
        int airportAnswer = _update.getAirport("JFK").getStatus();
        int inspection = response.getStatus();
        assertEquals(airportAnswer, inspection);
    }

    @Test
    public void testAddAirport() throws Exception {

        int addAirportAnswer = _update.addAirport("RUS", "55", "66").getStatus();
        Response response = Response.status(Response.Status.CREATED).build();
        int inspection = response.getStatus();
        assertEquals(addAirportAnswer, inspection);
    }
    @Test
    public void testDeleteAirport() throws Exception {

        int deleteAirportAnswer = _update.deleteAirport("JFK").getStatus();
        Response response = Response.status(Response.Status.OK).build();
        int inspection = response.getStatus();
        assertEquals(deleteAirportAnswer, inspection);
    }
}