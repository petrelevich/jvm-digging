package ru.systema;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.function.IntConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CalculatorAlfaTest {

    @Test
    void addTest() {
        //given
        var calculator = new CalculatorAlfa();
        var x1 = 3;
        var x2 = 5;
        var expected = x1 + x2;

        //when
        var result = calculator.add(x1, x2);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void addExternalSummandTest() {
        //given
        var calculator = new CalculatorAlfa();
        var intSource = new IntSourceImpl();

        var x1 = 3;
        var x2 = 5;
        var expected = x1 + x2 + intSource.getInt();

        //when
        var result = calculator.addExternalSummand(x1, x2, intSource);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void addExternalSummandTestMock() {
        //given
        var x1 = 3;
        var x2 = 5;
        var intSourceX = 7;

        var calculator = new CalculatorAlfa();
        var intSource = mock(IntSourceImpl.class);
        when(intSource.getInt()).thenReturn(intSourceX);

        var expected = x1 + x2 + intSourceX;

        //when
        var result = calculator.addExternalSummand(x1, x2, intSource);

        //then
        assertThat(result).isEqualTo(expected);
        verify(intSource).getInt();
    }

    @Test
    void addAndGetTest() {
        //given
        var calculator = new CalculatorAlfa();
        var x1 = 3;
        var x2 = 5;
        var counter  = 3;

        var consumer = mock(IntConsumer.class);
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);

        //when
        calculator.addAndGet(x1, x2, counter, consumer);

        //then
        verify(consumer, times(counter)).accept(intCaptor.capture());
        var values = intCaptor.getAllValues();
        assertThat(values).containsExactly(8, 9, 10);
    }
}