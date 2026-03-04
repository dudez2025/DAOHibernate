package com.example.hibernatedemo.repository;

import com.example.hibernatedemo.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Находит всех людей, проживающих в указанном городе
     * @param city название города
     * @return список людей из этого города
     */
    public List<Person> getPersonsByCity(String city) {
        // Используем JPQL запрос для фильтрации по городу
        TypedQuery<Person> query = entityManager.createQuery(
            "SELECT p FROM Person p WHERE p.cityOfLiving = :city", 
            Person.class
        );
        query.setParameter("city", city);
        
        return query.getResultList();
    }

    /**
     * Альтернативный метод с использованием Criteria API
     */
    /*
    public List<Person> getPersonsByCityCriteria(String city) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        
        cq.select(root).where(cb.equal(root.get("cityOfLiving"), city));
        
        return entityManager.createQuery(cq).getResultList();
    }
    */

    /**
     * Вспомогательный метод для сохранения человека
     */
    public void save(Person person) {
        entityManager.persist(person);
    }

    /**
     * Вспомогательный метод для поиска всех людей
     */
    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class)
                .getResultList();
    }
}