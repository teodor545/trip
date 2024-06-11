package com.task.second.trip.advisor.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CountryByBorders {

    private String countryCode;
    private String countryName;
    private Map<String, String> landBorders;

}
