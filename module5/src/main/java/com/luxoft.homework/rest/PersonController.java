package com.luxoft.homework.rest;

import com.kafka.rest.exceptions.NotFoundException;
import com.luxoft.homework.domain.Person;
import com.luxoft.homework.event.PersonEvent;
import com.luxoft.homework.repostory.PersonRepository;
import com.luxoft.homework.rest.dto.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, PersonEvent> personKafkaTemplate;


    @RequestMapping(value = "/persons/all", method = RequestMethod.GET)
    public List<PersonDto> getAllPersons() {
        return repository.findAll().stream()
                .map(PersonDto::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public PersonDto getPersonByIdInRequest(@RequestParam("id") long id) {
        Person person = repository.findById(id).orElseThrow(NotFoundException::new);
        return PersonDto.toDto(person);
    }

    @GetMapping("/persons/{id}")
    public PersonDto getPersonByIdInPath(@PathVariable("id") long id) {
        Person person = repository.findById(id).orElseThrow(NotFoundException::new);
        return PersonDto.toDto(person);
    }



    @PostMapping("/persons")
    @Transactional
    public PersonDto createNewPerson(@RequestBody PersonDto dto) {
        Person person = PersonDto.toDomainObject(dto);
        Person savedPerson = repository.save(person);

        PersonEvent personEvent = new PersonEvent(dto.getId(), dto.getName(), PersonEvent.Level.INSERT);
//        personKafkaTemplate.send("zuiev-topic-vo", personEvent);
        personKafkaTemplate
                .executeInTransaction(kafkaTemplate -> kafkaTemplate.send("zuiev-topic", personEvent));

        return PersonDto.toDto(savedPerson);
    }

    @PatchMapping("/persons/{id}/name")
    public PersonDto updateNameById(@PathVariable("id") long id, @RequestParam("name") String name) {

        /**
         * Kafka event sending
         * */

        Person person = repository.findById(id).orElseThrow(NotFoundException::new);
        person.setName(name);
        PersonDto dto = PersonDto.toDto(repository.save(person));

        PersonEvent personEvent = new PersonEvent(dto.getId(), dto.getName(), PersonEvent.Level.UPDATE);

        personKafkaTemplate
                .executeInTransaction(kafkaTemplate -> kafkaTemplate.send("zuiev-topic", personEvent));

        /**
         * Kafka event sending
         * */

        return dto;
    }

    @DeleteMapping("/persons/{id}")
    public void deleteById(@PathVariable("id") long id) {

        Person person = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(person);

        PersonDto dto = PersonDto.toDto(person);

        PersonEvent personEvent = new PersonEvent(dto.getId(), dto.getName(), PersonEvent.Level.DELETE);

        personKafkaTemplate
                .executeInTransaction(kafkaTemplate -> kafkaTemplate.send("zuiev-topic", personEvent));

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        return ResponseEntity.badRequest().body("No answer");
    }
}
