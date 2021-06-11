package ru.calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private Integer sum = 0;
    private Integer prevValue = 0;
    private Integer prevPrevValue = 0;
    private Integer sumLastThreeValues = 0;
    private Integer someValue = 0;
    private final List<Data> logValues = new ArrayList<>();

    public void calc(Data data) {
        if (data.getValue() % 3 == 0) {
            logValues.add(data);
        }
        if (logValues.size() == 29_400_000) {
            logValues.clear();
        }
        sum += data.getValue();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + logValues.size();
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
