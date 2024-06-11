package com.task.second.trip.advisor.data.parse;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country {
    @CsvBindByName(column = "country_code")
    private String countryCode;

    @CsvBindByName(column = "country_name")
    private String countryName;

    @CsvBindByName(column = "country_border_code")
    private String borderCode;

    @CsvBindByName(column = "country_border_name")
    private String borderName;
}
