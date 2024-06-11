package com.task.second.trip.advisor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripResponseDto {
    private Double remainingBudget;
    private String currency;
    private String startingCountry;
    private String errorMessage;
    private Long trips;
    private List<CountryResponseDto> countries;
}
