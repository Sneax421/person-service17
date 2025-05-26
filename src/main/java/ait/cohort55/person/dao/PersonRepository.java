package ait.cohort55.person.dao;

import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {

//    List<Person> findPersonsByNameIgnoreCase(String name);
//
//    List<Person> findPersonsByAddress_CityIgnoreCase(String city);


    Stream<Person> findByNameIgnoreCase(String name);

    Stream<Person> findByAddressCityIgnoreCase(String city);

    Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);

}
