package ru.demo;


public class App {
    public static void main(String... args) {
        var client = new Client(203);

        var orderService = ServiceLocator.getOrderService();
        orderService.process(client);
    }
}
