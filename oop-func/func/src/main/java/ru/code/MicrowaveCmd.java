package ru.code;

import java.io.Serializable;
import java.util.function.UnaryOperator;

public interface MicrowaveCmd extends UnaryOperator<MicrowaveF>, Serializable {
}
