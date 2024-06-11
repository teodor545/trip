package com.task.second.trip.advisor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TripRequestDto {
    @JsonProperty(required = true)
    private String startingCountryCode;
    @JsonProperty(required = true)
    private Double budgetPerCountry;
    @JsonProperty(required = true)
    private Double totalBudget;
    @JsonProperty(required = true)
    private String currency;
}
