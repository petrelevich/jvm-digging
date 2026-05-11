package ru.demo.spring.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.demo.spring.domain.Notes;
import ru.demo.spring.domain.Person;
import ru.demo.spring.domain.PersonDto;
import ru.demo.spring.repository.NotesRepository;
import ru.demo.spring.repository.PersonRepository;
import ru.demo.spring.repository.PersonRepositoryCustom;

@RestController
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final NotesRepository notesRepository;

    private final PersonRepositoryCustom personRepositoryCustom;

    private final Scheduler workerPool;

    public PersonController(
            PersonRepository personRepository,
            NotesRepository notesRepository,
            PersonRepositoryCustom personRepositoryCustom,
            Scheduler workerPool) {
        this.personRepository = personRepository;
        this.notesRepository = notesRepository;
        this.personRepositoryCustom = personRepositoryCustom;
        this.workerPool = workerPool;
    }

    @GetMapping("/person")
    public Flux<PersonDto> all() {
        return personRepositoryCustom.findAll();
    }

    @GetMapping("/person/{id}")
    public Mono<ResponseEntity<PersonDto>> byId(@PathVariable("id") Long id) {
        logger.info("Get person by id {}", id);
        return personRepository
                .findById(id)
                .publishOn(workerPool)
                .flatMap(person -> notesRepository
                        .findByPersonId(person.getId())
                        .publishOn(workerPool)
                        .map(Notes::getNoteText)
                        .collectList()
                        .map(notes -> toDto(person, notes)))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()))
                .subscribeOn(workerPool);
    }

    @PostMapping("/person")
    public Mono<Person> save(@RequestBody Mono<Person> dto) {
        return personRepository.save(dto);
    }

    @GetMapping("/person/find")
    public Flux<Person> byName(@RequestParam("name") String name) {
        return personRepository.findAllByLastName(name);
    }

    private PersonDto toDto(Person person, List<String> notes) {
        logger.info("person={}", person);
        return new PersonDto(String.valueOf(person.getId()), person.getLastName(), person.getAge(), notes);
    }
}
