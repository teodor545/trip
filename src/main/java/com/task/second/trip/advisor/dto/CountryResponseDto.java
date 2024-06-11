package com.task.second.trip.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDto {
    private String code;
    private String name;
    private Double currencyAmount;
    private String currencyCode;
}
