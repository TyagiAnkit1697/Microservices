package com.microservices.vaccinationCenter.controller;

import com.microservices.vaccinationCenter.entity.VaccinationCenter;
import com.microservices.vaccinationCenter.exceptionHandler.NoDataFoundException;
import com.microservices.vaccinationCenter.model.Citizen;
import com.microservices.vaccinationCenter.model.VaccinationAndCitizenData;
import com.microservices.vaccinationCenter.repo.VaccinationCenterRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.Method;
import org.hibernate.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("vaccinationCenter/")
@Slf4j
public class VaccinationCenterController extends Exception {
    private Integer id;
    @Autowired
    private VaccinationCenterRepo repo;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("add")
    public ResponseEntity<VaccinationCenter> addCenter(@RequestBody VaccinationCenter center) {
        log.info("VaccinationCenterRequest {} ", center);
        VaccinationCenter returnResult = repo.saveAndFlush(center);
        log.info("Result {} ", returnResult);
        return (returnResult == null || returnResult.getCenterAddress() == null) ? new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR):new ResponseEntity<>(returnResult,HttpStatus.CREATED);
    }

    @GetMapping("id/{id}")
    @Cacheable(value = "centerResult", key = "#id")
    public ResponseEntity<VaccinationCenter> getVaccinationCenter(@PathVariable Integer id) throws NoDataFoundException {
        Optional<VaccinationCenter> result = repo.findById(id);

        if(result.isEmpty()){
            log.error("There is no data with ID {}", id);
            throw new NoDataFoundException();
        }
        VaccinationCenter centerResult = result.get();
        log.info("VaccinationCenter {} with id {} is ", centerResult, id);
        return new ResponseEntity<>(centerResult,HttpStatus.OK );
    }

    @GetMapping("centerAndCiizens/{id}")
    @CircuitBreaker(name = "CitizenVaccination", fallbackMethod = "handleCitizenIfDown")
    public ResponseEntity<VaccinationAndCitizenData> getVaccinationAndCitizenData(@PathVariable Integer id){
        this.id = id;
        VaccinationAndCitizenData data = new VaccinationAndCitizenData();
        data.setCenter( repo.findById(id).get());
       data.setCitizens(getCitizens(id));
       log.info("data of vaccination center and citizen: {}", data);
       return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("updateCenter")
    @CachePut(cacheNames = "centerResult", key = "#updateCenter.getId()")
    public ResponseEntity<VaccinationCenter> updateCitizen(@RequestBody VaccinationCenter updateCenter){
       if(repo.findById(updateCenter.getId()).isEmpty()){
           return new ResponseEntity<>(new VaccinationCenter(), HttpStatus.NO_CONTENT);
       }
       VaccinationCenter centerResult = repo.saveAndFlush(updateCenter);
       return new ResponseEntity<>(centerResult, HttpStatus.OK);
    }

    public ResponseEntity<VaccinationAndCitizenData> handleCitizenIfDown(Exception e){
        VaccinationAndCitizenData data = new VaccinationAndCitizenData();
        data.setCenter( repo.findById(id).get());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    private List<Citizen> getCitizens(Integer id) {
        List<Citizen> citizens = restTemplate.getForObject("http://CITIZEN-SERVICE/citizen/centerId/"+id, List.class);
        return citizens;
    }
}
