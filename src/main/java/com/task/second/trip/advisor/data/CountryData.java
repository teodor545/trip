package com.task.second.trip.advisor.data;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.task.second.trip.advisor.data.parse.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CountryData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryData.class);
    private static final List<Country> COUNTRY_DATA = new ArrayList<>();
    private static boolean loaded = false;

    public CountryData() {
        List<Country> countries = loadData();
        if (countries != null && !countries.isEmpty()) {
            COUNTRY_DATA.addAll(countries);
            loaded = true;
        }
    }

    public CountryByBorders findBorderCountries(String countryCode) {
        CountryByBorders countryByBorders = new CountryByBorders();
        Optional<Country> country = COUNTRY_DATA.stream()
                .filter(c -> c.getCountryCode().equalsIgnoreCase(countryCode))
                .findFirst();
        country.ifPresent(c -> {
            countryByBorders.setCountryCode(c.getCountryCode());
            countryByBorders.setCountryName(c.getCountryName());
        });

        HashMap<String, String> borders = COUNTRY_DATA.stream()
                .filter(c -> c.getCountryCode().equalsIgnoreCase(countryCode))
                .map(c -> new AbstractMap.SimpleEntry<>(c.getBorderCode(), c.getBorderName()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue, (x, y) -> y, HashMap::new));

        countryByBorders.setLandBorders(borders);

        return countryByBorders;
    }


    private List<Country> loadData() {
        LOGGER.info("Loading country data...");
        try (Reader reader = new FileReader(ResourceUtils.getFile("classpath:static/countries.csv"))) {
            CsvToBean<Country> csvToBean = new CsvToBeanBuilder<Country>(reader)
                    .withType(Country.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Country> parsed = csvToBean.parse();
            LOGGER.info("Finished country data...");
            return parsed;
        } catch (Exception e) {
            LOGGER.warn("Failed to load data", e);
            loaded = false;
            return null;
        }
    }
}
