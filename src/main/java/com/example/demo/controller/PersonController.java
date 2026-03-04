package com.example.demo.controller;

import com.example.demo.dto.PersonResponse;
import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // CRUD операции
    
    // Create - создание нового человека
    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        return new ResponseEntity<>(new PersonResponse(savedPerson), HttpStatus.CREATED);
    }

    // Read all - получение всех людей
    @GetMapping
    public List<PersonResponse> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // Read by id - получение человека по ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(value -> new ResponseEntity<>(new PersonResponse(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update - обновление человека
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setName(personDetails.getName());
            person.setSurname(personDetails.getSurname());
            person.setAge(personDetails.getAge());
            person.setPhoneNumber(personDetails.getPhoneNumber());
            person.setCity(personDetails.getCity());
            
            Person updatedPerson = personRepository.save(person);
            return new ResponseEntity<>(new PersonResponse(updatedPerson), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete - удаление человека
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Специализированные методы-запросы
    
    // 1. Поиск по городу
    @GetMapping("/by-city")
    public List<PersonResponse> getPersonsByCity(@RequestParam String city) {
        return personRepository.findByCity(city)
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // 2. Поиск людей младше указанного возраста (отсортированных по возрасту)
    @GetMapping("/younger-than")
    public List<PersonResponse> getPersonsYoungerThan(@RequestParam Integer age) {
        return personRepository.findByAgeLessThanOrderByAgeAsc(age)
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // 3. Поиск по имени и фамилии
    @GetMapping("/by-full-name")
    public ResponseEntity<PersonResponse> getPersonByNameAndSurname(
            @RequestParam String name, 
            @RequestParam String surname) {
        Optional<Person> person = personRepository.findByNameAndSurname(name, surname);
        return person.map(value -> new ResponseEntity<>(new PersonResponse(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. Поиск по имени (без учета регистра)
    @GetMapping("/by-name")
    public List<PersonResponse> getPersonsByName(@RequestParam String name) {
        return personRepository.findByNameIgnoreCase(name)
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // 5. Поиск по городу и возрасту
    @GetMapping("/by-city-and-age")
    public List<PersonResponse> getPersonsByCityAndAge(
            @RequestParam String city,
            @RequestParam Integer maxAge) {
        return personRepository.findByCityAndAgeLessThan(city, maxAge)
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // 6. Проверка существования
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkPersonExists(
            @RequestParam String name,
            @RequestParam String surname) {
        boolean exists = personRepository.existsByNameAndSurname(name, surname);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // 7. Поиск по части имени
    @GetMapping("/search")
    public List<PersonResponse> searchPersonsByName(@RequestParam String namePart) {
        return personRepository.findByNameContainingIgnoreCase(namePart)
                .stream()
                .map(PersonResponse::new)
                .collect(Collectors.toList());
    }

    // 8. Получение всех уникальных городов
    @GetMapping("/cities")
    public List<String> getAllCities() {
        return personRepository.findAllDistinctCities();
    }
}