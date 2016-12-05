package com.crossover.trial.weather.rest.impl;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.dto.AtmosphericInformation;
import com.crossover.trial.weather.repository.MemoryRepository;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.rest.WeatherQueryEndpoint;
import com.crossover.trial.weather.service.WeatherService;
import com.crossover.trial.weather.service.impl.WeatherServiceImpl;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    public final static Logger LOGGER = Logger.getLogger("RestWeatherQueryEndpoint");

    private WeatherService weatherService = WeatherServiceImpl.getInstance();
    private Repository<AtomicInteger> repository = MemoryRepository.getInstance();

    /** shared gson json to object factory */
    public static final Gson gson = new Gson();

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        final Map<String, Object> retval = new HashMap<>();

        int datasize = 0;
        for (AtmosphericInformation ai : repository.getAtmosphericInformation()) {
            // we only count recent readings
            if (ai.getCloudCover() != null
                || ai.getHumidity() != null
                || ai.getPressure() != null
                || ai.getPrecipitation() != null
                || ai.getTemperature() != null
                || ai.getWind() != null) {
                // updated in the last day
                if (ai.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                    datasize++;
                }
            }
        }
        retval.put("datasize", datasize);

        final Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : repository.getAirportData()) {
            double frac = (double)repository.getRequestFrequency().getOrDefault(data, new AtomicInteger(0)).get() / repository.getRequestFrequency().size();
            freq.put(data.getIata(), frac);
        }
        retval.put("iata_freq", freq);

        final int m = repository.getRadiusFreq().keySet().stream()
                .max(Double::compare)
                .orElse(1000.0).intValue() + 1;

        final int[] hist = new int[m];
        for (Map.Entry<Double, AtomicInteger> e : repository.getRadiusFreq().entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue().get();
        }
        retval.put("radius_freq", hist);

        return gson.toJson(retval);
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radiusString the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        weatherService.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            int idx = weatherService.getAirportDataIdx(iata);
            retval.add(repository.getAtmosphericInformation().get(idx));
        } else {
            AirportData ad = weatherService.findAirportData(iata);
            for (int i=0;i< repository.getAirportData().size(); i++){
                if (weatherService.calculateDistance(ad, repository.getAirportData().get(i)) <= radius){
                    AtmosphericInformation ai = repository.getAtmosphericInformation().get(i);
                    if (ai.getCloudCover() != null || ai.getHumidity() != null || ai.getPrecipitation() != null
                       || ai.getPressure() != null || ai.getTemperature() != null || ai.getWind() != null){
                        retval.add(ai);
                    }
                }
            }
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }

}
