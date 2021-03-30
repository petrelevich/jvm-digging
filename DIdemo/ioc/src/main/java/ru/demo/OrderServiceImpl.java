package ru.demo;

public class OrderServiceImpl implements OrderService {

    private final LoyaltyService loyaltyService;

    public OrderServiceImpl(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @Override
    public void process(Client client) {
        var score = loyaltyService.getScore(client);
        System.out.println("done with score:" + score);
    }
}
