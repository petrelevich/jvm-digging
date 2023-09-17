package main.transactions;

public interface TransactionRunner {

    <T> T doInTransaction(TransactionAction<T> action);
}
