package main.transactions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionRunnerSpring implements TransactionRunner {

    @Override
    @Transactional
    public <T> T doInTransaction(TransactionAction<T> action) {
        return action.get();
    }
}
