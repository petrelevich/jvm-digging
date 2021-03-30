package ru.demo;

public class ServiceLocator {
    private static final LoyaltyService loyaltyService = new LoyaltyService();
    private static final OrderService orderService = new OrderService();

    private ServiceLocator() {
    }

    public static LoyaltyService getLoyaltyService() {
        return loyaltyService;
    }

    public static OrderService getOrderService() {
        return orderService;
    }
}
