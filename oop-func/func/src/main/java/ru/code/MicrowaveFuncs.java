package ru.code;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MicrowaveFuncs {
    private MicrowaveFuncs() {
    }

    public static MicrowaveF powerLevelUp(MicrowaveF microwave) {
        return new MicrowaveF(microwave.getPowerLevel() + 1);
    }

    public static MicrowaveF powerLevelDown(MicrowaveF microwave) {
        return new MicrowaveF(microwave.getPowerLevel() - 1);
    }

    public static MicrowaveF loop(int loopCounter, MicrowaveF microwave, MicrowaveCmd func) {
        if (loopCounter == 0) {
            return microwave;
        }
        return loop(loopCounter - 1, func.apply(microwave), func);
    }

    public static MicrowaveF wifiCommand(MicrowaveF microwave, byte[] cmdAsByte) throws IOException, ClassNotFoundException {
        MicrowaveCmd func;
        try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(cmdAsByte))) {
            func = (MicrowaveCmd) objectInputStream.readObject();
        }
        return func.apply(microwave);
    }
}
