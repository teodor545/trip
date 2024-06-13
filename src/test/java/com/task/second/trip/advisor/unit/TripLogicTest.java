package com.task.second.trip.advisor.unit;

import com.task.second.trip.advisor.data.CountryData;
import com.task.second.trip.advisor.dto.TripRequestDto;
import com.task.second.trip.advisor.service.TripServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class TripLogicTest {

    private final CountryData countryData = new CountryData();
    private final TripServiceImpl tripService = new TripServiceImpl(null, null, null);

    @Test
    public void testTripLogic_basic_BG() throws Exception {
        TripRequestDto tripRequestDto = new TripRequestDto("BG", 100d, 1200d, "BGN");

        Map<String, String> borders = countryData.findBorderCountries("BG").getLandBorders();

        Method calculateTrip = tripService.getClass().getDeclaredMethod("calculateTrip", TripRequestDto.class, Map.class);
        calculateTrip.setAccessible(true);

        Object result = calculateTrip.invoke(tripService, tripRequestDto, borders);
        Field tripCountField = result.getClass().getDeclaredField("tripCount");
        tripCountField.setAccessible(true);

        int tripCount = (int) tripCountField.get(result);

        Field leftOverAmountField = result.getClass().getDeclaredField("leftOverAmount");
        leftOverAmountField.setAccessible(true);

        double leftOverAmount = (double) leftOverAmountField.get(result);

       Assertions.assertEquals(2, tripCount);
       Assertions.assertEquals(200, leftOverAmount);
    }
}
