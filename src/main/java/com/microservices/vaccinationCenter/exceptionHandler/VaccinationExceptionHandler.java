package com.microservices.vaccinationCenter.exceptionHandler;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VaccinationExceptionHandler {
    @Autowired
    private FailedResponse failedResponse;

    @ExceptionHandler(value = NoDataFoundException.class)
    public ResponseEntity<FailedResponse> handler(){
        failedResponse.setStatusCode("404");
        failedResponse.setStatusMsg("No data found");
        failedResponse.setAdvice("Please contact Center for more information");
        return new ResponseEntity<>(failedResponse, HttpStatus.EXPECTATION_FAILED);

    }
}
