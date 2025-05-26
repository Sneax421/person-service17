package ait.cohort55.person.service;


import ait.cohort55.person.dao.PersonRepository;
import ait.cohort55.person.dto.*;
import ait.cohort55.person.dto.exception.ConflictException;
import ait.cohort55.person.dto.exception.NotFoundException;
import ait.cohort55.person.model.Address;
import ait.cohort55.person.model.Child;
import ait.cohort55.person.model.Employee;
import ait.cohort55.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PersonModelDtoMapper mapper;


    @Transactional
    @Override
    public void addPerson(PersonDto personDto) {
        if(personRepository.existsById(personDto.getId())) {
            throw new ConflictException("Person with id " + personDto.getId() + " already exists");
        }
        Person person = mapper.mapToModel(personDto);

        personRepository.save(person);

    }

    @Override
    public PersonDto getPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        personRepository.delete(person);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setName(name);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setAddress(modelMapper.map(addressDto, Address.class));
        return mapper.mapToDto(person);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByName(String name) {
        return personRepository.findByNameIgnoreCase(name)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        LocalDate from = LocalDate.now().minusYears(maxAge);
        LocalDate to = LocalDate.now().minusYears(minAge);
        return personRepository.findByBirthDateBetween(from, to)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
    }

    @Override
    public List<ChildDto> findAllChildren() {
        return personRepository.findAll().stream()
                .filter(person -> person instanceof Child )
                .map(person -> (ChildDto) mapper.mapToDto(person))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> findAllEmployeesBySalary(Integer minSalary, Integer maxSalary) {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Employee)
                .map(p -> (Employee) p)
                .filter(e -> e.getSalary() >= minSalary && e.getSalary() <= maxSalary)
                .map(e -> (EmployeeDto) mapper.mapToDto(e))
                .collect(Collectors.toList());
    }

    @Override
    public void run(String... args) throws Exception {
        if(personRepository.count() == 0 ){
            Person person = new Person(1000, "John", LocalDate.of(1985, 3 , 11),
            new Address("Berlin", "Berlinerstrasse", 15));
            Child child = new Child(2000, "Peter", LocalDate.of(2019, 7, 5),
                    new Address("Menden", "Haupstrasse", 10), "Kita");
            Employee employee = new Employee(3000, "MAria", LocalDate.of(1995, 11, 25),
                    new Address("Dortmund", "Kaisralle", 1), "Google", 8000);
            personRepository.save(person);
            personRepository.save(child);
            personRepository.save(employee);
        }
    }
}
