package com.crossover.trial.weather.multitThredCases;

import com.crossover.trial.weather.dto.AirportData;
import com.crossover.trial.weather.multitThredCases.util.AirportDataCreator;
import com.crossover.trial.weather.repository.Repository;
import com.crossover.trial.weather.service.WeatherService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by predantsev on 05.12.2016.
 */
public abstract class AbstractMapValueChangeTest<T extends Number> {

    protected Repository<T> memoryRepository;
    protected WeatherService weatherService;

    private static final String FILE_PATH = "/Users/predantsev/IdeaProjects/weather/src/main/resources/airports.dat";

    private List<String> params = new ArrayList<>();

    private static final int STEP = 10;

    private int dataPosition = 4;
    private int latitudePosition = 6;
    private int longitudePosition = 7;

    private static final String DATA_VALUE = "data";
    private static final String LATITUDE_VALUE = "latitude";
    private static final String LONGITUDE_VALUE = "longitude";

    protected static final int INVOCATION_COUNT = 100;
    protected static final int INVOCATION_TIME_OUT = 10000;
    protected static final int THREAD_POOL_SIZE = 30;

    protected void completeAirportDataListFromFile() throws FileNotFoundException {
        File dataFile = new File(FILE_PATH);
        Scanner scanner = new Scanner(dataFile);
        scanner.useDelimiter(",");
        int i = 0;
        while (scanner.hasNext()){
            String e = scanner.next();
            filterAndAddToParams(params, e, i, dataPosition, DATA_VALUE);
            filterAndAddToParams(params, e, i, latitudePosition, LATITUDE_VALUE);
            filterAndAddToParams(params, e, i, longitudePosition, LONGITUDE_VALUE);
            i++;
        }
        scanner.close();
        completeAirportDataList(params);
    }

    private void filterAndAddToParams(List<String> params, String e, int i, int position, String value) {
        if (i == position) {
            params.add(e);
            incrementPositionBy(value);
        }
    }

    protected void incrementPositionBy(String value) {
        switch (value) {
            case DATA_VALUE:
                dataPosition += STEP;
                break;
            case LATITUDE_VALUE:
                latitudePosition += STEP;
                break;
            case LONGITUDE_VALUE:
                longitudePosition += STEP;
                break;

        }
    }

    private void completeAirportDataList(List<String> params) {
        List<AirportData> airportDataList = memoryRepository.getAirportData();
        int startPosition = 0;
        int step = 3;
        while (startPosition + step <= params.size()) {
            airportDataList
                    .add(AirportDataCreator.createCompleteAirportDataEntity(
                            params.get(startPosition),
                            params.get(startPosition + 1),
                            params.get(startPosition + 2)));
            startPosition += step;
        }
    }

    @BeforeClass
    void completeAirportDataList() throws Exception {
        completeAirportDataListFromFile();
    }

    @AfterClass
    void clearAirportDataList() throws Exception {
        memoryRepository.getAirportData().clear();
    }

    public void testUpdateRequestFrequencyWithSingleThread() throws Exception {
        testUpdateRequestFrequency();
    }

    public void testUpdateRequestFrequencyWithMultiThread() throws Exception {
        testUpdateRequestFrequency();
    }

    protected void testUpdateRequestFrequency() {
        Random r = new Random();
        for (AirportData e : memoryRepository.getAirportData()) {
            weatherService.updateRequestFrequency(e.getIata(), r.nextDouble());
        }
    }
}
