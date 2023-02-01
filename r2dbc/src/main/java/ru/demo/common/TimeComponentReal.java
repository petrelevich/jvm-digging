package ru.demo.common;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimeComponentReal implements TimeComponent {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
