package ru.demo;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private final AtomicLong generator = new AtomicLong(0);

    @GetMapping("/data")
    public String data() {
        var buf = new byte[60 * 1024 * 1024];
        return String.format("Ok:%d:%d", buf.length, generator.incrementAndGet());
    }
}
