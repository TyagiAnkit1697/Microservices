package com.microservices.vaccinationCenter.repo;

import com.microservices.vaccinationCenter.entity.VaccinationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationCenterRepo extends JpaRepository<VaccinationCenter, Integer> {

}
