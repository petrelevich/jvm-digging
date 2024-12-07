package ru.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
-Xms5120m
-Xmx5120m
-verbose:gc
-XX:+UseG1GC
*/

public class ArrayLeak {
    private static final Logger logger = LoggerFactory.getLogger(ArrayLeak.class);

    private static final int ARRAY_SIZE = 10_000_000;
    private static final int OBJECT_SIZE = 102400;

    public static void main(String[] args) throws InterruptedException {
        logger.info("begin");
        List<Data> array = new ArrayList<>();

        for (var idx = 0; idx < ARRAY_SIZE; idx++) {
            array.add(new Data(new byte[OBJECT_SIZE]));
            Thread.sleep(10);
        }

        logger.info("end, size:{}", array.size());
    }

    public record Data(byte[] d) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Objects.deepEquals(d, data.d);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(d);
        }

        @Override
        public String toString() {
            return "Data{" +
                    "d=" + Arrays.toString(d) +
                    '}';
        }
    }
}
