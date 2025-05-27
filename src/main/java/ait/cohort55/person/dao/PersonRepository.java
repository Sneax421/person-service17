package ait.cohort55.person.dao;

import ait.cohort55.person.dto.CityPopulationDto;
import ait.cohort55.person.model.Child;
import ait.cohort55.person.model.Employee;
import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {

//    @Query("select p from Person p where p.name=?1")
    Stream<Person> findByNameIgnoreCase(String name);

//    @Query("select p from Person p where p.address.city=?1")
    Stream<Person> findByAddressCityIgnoreCase(String city);

    Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);

    @Query("select new ait.cohort55.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p" +
            " group by p.address.city order by count (p) desc")
    List<CityPopulationDto> getCitiesPopulation();

    Child[] getChildrenBy();

    Employee[] getEmployeesBySalaryBetween(int min, int max);

}
