package ru.demo.spring.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.demo.spring.BaseContainerTest;
import ru.demo.spring.domain.Person;

@SpringBootTest
class PersonRepositoryTest extends BaseContainerTest {

    @Autowired
    private PersonRepository repository;

    @Test
    void shouldSetIdOnSave() {
        Mono<Person> personMono = repository.save(new Person("Bill", 12));

        StepVerifier.create(personMono)
                .assertNext(person -> assertNotNull(person.getId()))
                .expectComplete()
                .verify();
    }
}
