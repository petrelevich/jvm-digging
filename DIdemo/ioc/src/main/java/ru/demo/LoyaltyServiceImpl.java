package ru.demo;

public class LoyaltyServiceImpl implements LoyaltyService {

    @Override
    public long getScore(Client client) {
        return client.getId() % 2;
    }
}
