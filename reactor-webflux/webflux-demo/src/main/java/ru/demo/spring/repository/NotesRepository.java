package ru.demo.spring.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.demo.spring.domain.Notes;

public interface NotesRepository extends ReactiveCrudRepository<Notes, Long> {

    Flux<Notes> findByPersonId(@NotNull Long personId);
}
