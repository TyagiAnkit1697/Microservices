package com.microservices.vaccinationCenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Citizen {

    private Integer id;
    private String name;
    private Integer vaccinationCenterId;
}
