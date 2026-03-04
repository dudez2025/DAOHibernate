package com.example.demo.repository;

import com.example.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    // Базовые CRUD методы уже предоставлены JpaRepository:
    // save(), findById(), findAll(), deleteById(), count(), existsById() и т.д.
    
    // Дополнительные методы-запросы
    
    // 1. Поиск по городу
    List<Person> findByCity(String city);
    
    // 2. Поиск людей младше определенного возраста с сортировкой по возрасту
    List<Person> findByAgeLessThanOrderByAgeAsc(Integer age);
    
    // Альтернативный вариант с явным @Query
    @Query("SELECT p FROM Person p WHERE p.age < :age ORDER BY p.age ASC")
    List<Person> findYoungerThan(@Param("age") Integer age);
    
    // 3. Поиск по имени и фамилии (возвращает Optional)
    Optional<Person> findByNameAndSurname(String name, String surname);
    
    // 4. Поиск по имени (без учета регистра)
    List<Person> findByNameIgnoreCase(String name);
    
    // 5. Поиск по городу и возрасту
    List<Person> findByCityAndAgeLessThan(String city, Integer age);
    
    // 6. Проверка существования по имени и фамилии
    boolean existsByNameAndSurname(String name, String surname);
    
    // 7. Поиск с пагинацией (пример - можно добавить параметры Pageable)
    // Page<Person> findAll(Pageable pageable);
    
    // 8. Поиск по части имени (содержит)
    List<Person> findByNameContainingIgnoreCase(String namePart);
    
    // 9. Получение всех уникальных городов
    @Query("SELECT DISTINCT p.city FROM Person p ORDER BY p.city")
    List<String> findAllDistinctCities();
}