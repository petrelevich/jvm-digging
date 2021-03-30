package ru.demo;


public class App {
    public static void main(String... args) {
        var client = new Client(203);

        var loyaltyService = new LoyaltyServiceImpl();
        var orderService = new OrderServiceImpl(loyaltyService);

        orderService.process(client);
    }
}
