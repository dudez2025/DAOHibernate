package com.example.hibernatedemo.controller;

import com.example.hibernatedemo.entity.Person;
import com.example.hibernatedemo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/persons/by-city")
    public List<Person> getPersonsByCity(@RequestParam("city") String city) {
        return personRepository.getPersonsByCity(city);
    }

    /**
     * Дополнительный эндпоинт для проверки всех записей
     */
    @GetMapping("/persons/all")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
}