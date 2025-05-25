package ait.cohort55.person.dao;

import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Stream<Person> findPersonsByNameIgnoreCase(String name);

    Stream<Person> findPersonsByAddress_CityIgnoreCase(String city);




}
