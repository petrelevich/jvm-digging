package ru.demo;

public class LoyaltyService {

    public long getScore(Client client) {
        return client.getId() % 2;
    }
}
