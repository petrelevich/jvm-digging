package main;

import main.demo.serivce.DogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo {
    public static void main(String[] args) {
        var context = SpringApplication.run(Demo.class, args);

        context.getBean("dogService", DogService.class).life();
    }
}
