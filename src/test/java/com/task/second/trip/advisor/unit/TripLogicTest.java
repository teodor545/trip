package com.task.second.trip.advisor.unit;

import com.task.second.trip.advisor.data.CountryData;
import com.task.second.trip.advisor.dto.TripRequestDto;
import com.task.second.trip.advisor.service.TripServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TripLogicTest {

    private final CountryData countryData = new CountryData();
    private final TripServiceImpl tripService = new TripServiceImpl(null, null, null);

    private static Stream<Arguments> testParamsBasic() {
        return Stream.of(
                Arguments.of(new TripRequestDto("BG", 100d, 1200d, "BGN"), 2 ,200),
                Arguments.of(new TripRequestDto("GR", 100d, 500d, "EUR"), 1 ,100),
                Arguments.of(new TripRequestDto("MC", 1d, 20d, "EUR"), 20 ,0));
    }

    @ParameterizedTest
    @MethodSource("testParamsBasic")
    public void testTripLogic_basic(TripRequestDto tripRequestDto, double expectedTripCount, double expectedLeftOverAmount) throws Exception {
        Map<String, String> borders = countryData.findBorderCountries(tripRequestDto.getStartingCountryCode()).getLandBorders();

        Method calculateTrip = tripService.getClass().getDeclaredMethod("calculateTrip", TripRequestDto.class, Map.class);
        calculateTrip.setAccessible(true);

        Object result = calculateTrip.invoke(tripService, tripRequestDto, borders);
        Field tripCountField = result.getClass().getDeclaredField("tripCount");
        tripCountField.setAccessible(true);

        int tripCount = (int) tripCountField.get(result);

        Field leftOverAmountField = result.getClass().getDeclaredField("leftOverAmount");
        leftOverAmountField.setAccessible(true);

        double leftOverAmount = (double) leftOverAmountField.get(result);

        Assertions.assertEquals(expectedTripCount, tripCount);
        Assertions.assertEquals(expectedLeftOverAmount, leftOverAmount);
    }
}
