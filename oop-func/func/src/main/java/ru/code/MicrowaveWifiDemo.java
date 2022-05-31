package ru.code;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MicrowaveWifiDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MicrowaveF microwave = new MicrowaveF();
        System.out.println("init:" + microwave.getPowerLevel());

        // команды по wifi
        MicrowaveF microwaveUp = MicrowaveFuncs.wifiCommand(microwave, cmdToBytes(MicrowaveFuncs::powerLevelUp));
        System.out.println("wifi up:" + microwaveUp.getPowerLevel());

        MicrowaveF microwaveDown = MicrowaveFuncs.wifiCommand(microwaveUp, cmdToBytes(MicrowaveFuncs::powerLevelDown));
        System.out.println("wifi down:" + microwaveDown.getPowerLevel());

        //MicrowaveF microwaveOops = MicrowaveFuncs.wifiCommand(microwaveUp, cmdToBytes(MicrowaveFuncs::cmdOops));
        //System.out.println("wifi down:" + microwaveOops.getPowerLevel());
    }

    private static byte[] cmdToBytes(MicrowaveCmd cmd) throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            objectOutputStream.writeObject(cmd);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
