package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.datasrc.ServiceRedis;

@RestController
@RequestMapping("api/v1")
public class Api {
    private static final Logger log = LoggerFactory.getLogger(Api.class);

    private final ServiceRedis serviceRedis;

    public Api(ServiceRedis serviceRedis) {
        this.serviceRedis = serviceRedis;
    }

    // http://localhost:8080/api/v1/find/43
    @GetMapping(value = "/find/{id}")
    public String find(@PathVariable("id") long id) {
        log.info("find, id:{}", id);
        var respContent = serviceRedis.action(id);
        log.info("find id:{}, response:{}", id, respContent);
        return respContent;
    }
}
