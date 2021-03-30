package ru.demo;

public class OrderService {

    public void process(Client client) {
        var score = ServiceLocator.getLoyaltyService().getScore(client);
        System.out.println("done with score:" + score);
    }
}
