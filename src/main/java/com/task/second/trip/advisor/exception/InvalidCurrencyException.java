package com.task.second.trip.advisor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Country has no land bordering countries")
public class InvalidCurrencyException extends RuntimeException {
}
