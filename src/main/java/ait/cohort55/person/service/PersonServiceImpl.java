package ait.cohort55.person.service;

import ait.cohort55.configuration.ServiceConfiguration;
import ait.cohort55.person.dao.PersonRepository;
import ait.cohort55.person.dto.AddressDto;
import ait.cohort55.person.dto.CityPopulationDto;
import ait.cohort55.person.dto.PersonDto;
import ait.cohort55.person.dto.exception.ConflictException;
import ait.cohort55.person.dto.exception.NotFoundException;
import ait.cohort55.person.model.Address;
import ait.cohort55.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;


    @Transactional // eta tipa synchronized // 1 paluchit created a 2 i 3 oschibku
    @Override
    public void addPerson(PersonDto personDto) {
        if(personRepository.existsById(personDto.getId())) {
            throw new ConflictException("Person with id " + personDto.getId() + " already exists");
        }
        personRepository.save(modelMapper.map(personDto, Person.class));

    }

    @Override
    public PersonDto getPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        personRepository.delete(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setName(name);
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        Address address = new Address(addressDto.getCity(), addressDto.getStreet(), addressDto.getBuilding());
        person.setAddress(address);
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto[] findPersonsByName(String name) {
        return personRepository.findPersonsByNameIgnoreCase(name)
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findPersonsByAddress_CityIgnoreCase(city)
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        return null;
    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return null;
    }
}
