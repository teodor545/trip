package com.task.second.trip.advisor.service;

import com.task.second.trip.advisor.configuration.CurrencyProviderConfig;
import com.task.second.trip.advisor.data.CountryData;
import com.task.second.trip.advisor.dto.*;
import com.task.second.trip.advisor.exception.InvalidCountryException;
import com.task.second.trip.advisor.exception.InvalidCurrencyException;
import com.task.second.trip.advisor.exception.NoLandBorderException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {

    private CountryData countryData;
    private RestTemplate restTemplate;
    private CurrencyProviderConfig currencyProviderConfig;

    public TripResponseDto processTripRequest(TripRequestDto tripRequestDto) {
        String startingCountryCode = tripRequestDto.getStartingCountryCode();
        String currency = tripRequestDto.getCurrency();

        int tripCount = 0;
        double totalBudget = tripRequestDto.getTotalBudget();
        double budgetPerCountry = tripRequestDto.getBudgetPerCountry();
        double currentAmount = 0;
        double leftOverAmount;

        Map<String, String> borders = countryData.findBorderCountries(startingCountryCode).getLandBorders();

        validateData(currency, borders);

        int borderCount = borders.keySet().size();
        while (currentAmount + (budgetPerCountry * borderCount) <= totalBudget) {
            currentAmount += budgetPerCountry * borderCount;
            tripCount++;
        }
        leftOverAmount = totalBudget - currentAmount;

        CurrencyRateDto currencyRate = requestCurrencyRates(currency, borders.keySet());

        return createResponse(tripRequestDto, currencyRate, borders, tripCount, leftOverAmount);
    }

    private CurrencyRateDto requestCurrencyRates(String baseCurrency, Set<String> targetCurrencies) {
        String currenciesForRequest = targetCurrencies.stream()
                .map(countryCode -> Currency.getInstance(new Locale("", countryCode)).getCurrencyCode())
                .collect(Collectors.joining(","));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(currencyProviderConfig.getRatesUrl())
                .queryParam("access_key", currencyProviderConfig.getApiKey())
                .queryParam("base", baseCurrency)
                .queryParam("symbols", currenciesForRequest);

        CurrencyRateDto currencyResponse = restTemplate.getForObject(builder.toUriString(), CurrencyRateDto.class);

        if (currencyResponse == null || !currencyResponse.isSuccess()) {
            throw new RuntimeException("Currency provider failed to provide data");
        }

        return currencyResponse;
    }

    private void validateData(String currency, Map<String, String> borders) {
        if (borders.isEmpty()) {
            throw new InvalidCountryException();
        } else if (borders.containsKey("") ) {
            throw new NoLandBorderException();
        }
        try {
            Currency.getInstance(currency);
        } catch (Exception ex) {
            throw new InvalidCurrencyException();
        }
    }

    private TripResponseDto createResponse(TripRequestDto tripRequestDto,
                                           CurrencyRateDto currencyRateDto,
                                           Map<String, String> borders,
                                           int tripCount,
                                           double leftOverAmount) {
        TripResponseDto tripResponseDto = new TripResponseDto();
        tripResponseDto.setTrips((long) tripCount);
        tripResponseDto.setCurrency(tripRequestDto.getCurrency());
        tripResponseDto.setRemainingBudget(leftOverAmount);
        tripResponseDto.setCountries(new ArrayList<>());
        if (tripCount > 0) {
            for (Map.Entry<String, String> borderEntry : borders.entrySet()) {
                String borderCountryCode = borderEntry.getKey();
                String borderCountryName = borderEntry.getValue();
                String currencyCode = Currency.getInstance(new Locale("", borderCountryCode)).getCurrencyCode();
                double payAmount = BigDecimal.valueOf((tripRequestDto.getBudgetPerCountry() * tripCount) * currencyRateDto.getRates().get(currencyCode))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                tripResponseDto.getCountries().add(new CountryResponseDto(borderCountryCode, borderCountryName, payAmount, currencyCode));
            }
        }
        return tripResponseDto;
    }
}
