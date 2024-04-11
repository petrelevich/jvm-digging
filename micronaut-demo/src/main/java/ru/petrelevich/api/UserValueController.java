package ru.petrelevich.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import ru.petrelevich.model.InputValue;
import ru.petrelevich.model.UserValue;
import ru.petrelevich.repositories.UserValueRepository;

@Controller
public class UserValueController {
    private static final Logger log = LoggerFactory.getLogger(UserValueController.class);

    private final UserValueRepository userValueRepository;

    public UserValueController(UserValueRepository userValueRepository) {
        this.userValueRepository = userValueRepository;
    }

    @Get(value = "/hello", produces = MediaType.APPLICATION_JSON)
    public String index() {
        return "Hello World";
    }

    @Post(value = "/value",  produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public Mono<Long> addValue(@Body InputValue val) {
        log.info("val:{}", val);

        return userValueRepository.save(new UserValue(val.val()))
                .doOnNext(savedValue -> log.info("saved value:{}", savedValue))
                .map(UserValue::getId);
    }

    @Get(value = "/value/{id}",  produces = MediaType.APPLICATION_JSON)
    public Mono<String> getValue(@PathVariable("id") long id) {
        log.info("id:{}", id);
        return userValueRepository.findById(id)
                .doOnNext(loadedValue -> log.info("loaded value:{}", loadedValue))
                .map(UserValue::getUsrValue);
    }
}