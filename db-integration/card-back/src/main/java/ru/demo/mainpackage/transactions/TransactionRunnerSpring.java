package ru.demo.mainpackage.transactions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionRunnerSpring implements TransactionRunner {

    @Override
    @Transactional
    public void doInTransaction(TransactionAction action) {
        action.run();
    }
}
