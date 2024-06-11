package com.task.second.trip.advisor.unit;

import com.task.second.trip.advisor.data.CountryData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TripLogicTest {

    private CountryData countryData = new CountryData();

    @Test
    public void testTripLogic_basic() {
        int tripCount = 0;
        double totalBudget = 1200;
        double budgetPerCountry = 100;
        double currentAmount = 0;
        double leftOverAmount;

        int size = countryData.findBorderCountries("BG").getLandBorders().keySet().size();

        while (currentAmount + (budgetPerCountry * size) <= totalBudget) {
            currentAmount += budgetPerCountry * size;
            tripCount++;
        }

        leftOverAmount = totalBudget - currentAmount;;


        Assertions.assertEquals(2, tripCount);
        Assertions.assertEquals(200, leftOverAmount);
    }
}
