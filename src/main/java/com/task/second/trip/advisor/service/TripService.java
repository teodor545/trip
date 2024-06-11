package com.task.second.trip.advisor.service;

import com.task.second.trip.advisor.dto.TripRequestDto;
import com.task.second.trip.advisor.dto.TripResponseDto;

public interface TripService {
    TripResponseDto processTripRequest(TripRequestDto tripRequestDto);
}
