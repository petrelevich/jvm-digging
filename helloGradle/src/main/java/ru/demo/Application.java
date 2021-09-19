package ru.demo;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * To start the application:
 * ./gradlew build
 * java -jar ./build/libs/gradleHelloWorld-0.1.jar
 * <p>
 * To unzip the jar:
 * unzip -l gradleHelloWorld-0.1.jar
 * unzip -l helloGradle.jar
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        List<Integer> example = new ArrayList<>();
        int min = 0;
        int max = 100;
        for (int i = min; i < max; i++) {
            example.add(i);
        }
        logger.info("reverse list:{}", Lists.reverse(example));
    }
}
