package ru.demo.mainpackage.transactions;

public interface TransactionRunner {

    void doInTransaction(TransactionAction action);
}
