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
        try {
            TripResponseDto tripResponseDto = tripService.processTripRequest(rateRequestDto);
            ResponseEntity<TripResponseDto> response;
            if (tripResponseDto.getErrorMessage() != null) {
                response = new ResponseEntity<>(tripResponseDto, HttpStatus.BAD_REQUEST);
            } else {
                response = new ResponseEntity<>(tripResponseDto, HttpStatus.OK);
            }
            return response;
        } catch (Exception ex) {
            TripResponseDto tripResponseDto = new TripResponseDto();
            tripResponseDto.setErrorMessage(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tripResponseDto);
        }
    }
}
