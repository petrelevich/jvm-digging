package ru.petrelevich.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.model.UserValue;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface UserValueRepository extends ReactiveStreamsCrudRepository<UserValue, Long> {
    @NonNull
    @Override
    Mono<UserValue> findById(@NonNull Long id);

    @NonNull
    @SingleResult
    <S extends UserValue> Mono<S> save(@NonNull S entity);


    @NonNull
    @Override
    Flux<UserValue> findAll();
}