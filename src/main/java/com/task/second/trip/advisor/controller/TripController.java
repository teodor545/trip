package com.task.second.trip.advisor.controller;

import com.task.second.trip.advisor.dto.TripRequestDto;
import com.task.second.trip.advisor.dto.TripResponseDto;
import com.task.second.trip.advisor.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/trip")
public class TripController {

    private TripService tripService;

    @PostMapping(path = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripResponseDto> calculateTrip(@RequestBody TripRequestDto rateRequestDto) {
       TripResponseDto tripResponseDto = tripService.processTripRequest(rateRequestDto);
       return new ResponseEntity<>(tripResponseDto, HttpStatus.OK);
    }
}
