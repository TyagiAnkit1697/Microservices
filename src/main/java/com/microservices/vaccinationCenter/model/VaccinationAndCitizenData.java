package com.microservices.vaccinationCenter.model;

import com.microservices.vaccinationCenter.entity.VaccinationCenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationAndCitizenData {
    private VaccinationCenter center;
    private List<Citizen> citizens;

}
