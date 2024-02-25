package com.microservices.vaccinationCenter.exceptionHandler;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FailedResponse {
    private String StatusCode;
    private String statusMsg;
    private String advice;
}
