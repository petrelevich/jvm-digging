package ru.demo;

public class OrderService {
    private final LoyaltyService loyaltyService = new LoyaltyService();

    public void process(Client client) {
        var score = loyaltyService.getScore(client);
        System.out.println("done with score:" + score);
    }
}
