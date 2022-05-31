package ru.code;

public class MicrowaveWifiDemo {
    public static void main(String[] args) {
        MicrowaveO microwave = new MicrowaveO();
        System.out.println("init:" + microwave.getPowerLevel());

        // команды по wifi
        microwave.wifiCommand(cmdToBytes("up"));
        System.out.println("wifi up:" + microwave.getPowerLevel());

        microwave.wifiCommand(cmdToBytes("down"));
        System.out.println("wifi down:" + microwave.getPowerLevel());

        microwave.wifiCommand(cmdToBytes("cmdOops"));
    }

    private static byte[] cmdToBytes(String cmd) {
        return cmd.getBytes();
    }
}
