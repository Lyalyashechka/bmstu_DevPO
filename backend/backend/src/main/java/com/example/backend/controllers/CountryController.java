package com.example.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.backend.models.Country;
import com.example.backend.repositories.CountryRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CountryController {
    @Autowired
    CountryRepository countryRepository;
    @GetMapping("/countries")
    public List
    getAllCountries() {
        return countryRepository.findAll();
    }

    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody Country country) {
        try {
            Country nc = countryRepository.save(country);
            return ResponseEntity.ok(nc);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                error = "country_already_exists";
            else
                error = "undefined_error";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId,
                                                 @Validated @RequestBody Country countryDetails) {
        Country country = null;
        Optional<Country> cc = countryRepository.findById(countryId);
        if (cc.isPresent()) {
            country = cc.get();
            country.name = countryDetails.name;
            countryRepository.save(country);
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "country not found"
            );
        return ResponseEntity.ok(country);
    }

    @DeleteMapping("/countries/{id}")
    public Map<String, Boolean> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        Map<String, Boolean> response = new HashMap<>();
        if (country.isPresent()) {
            countryRepository.delete(country.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
            response.put("deleted", Boolean.FALSE);
        return response;
    }

}