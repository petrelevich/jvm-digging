package main.transactions;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {}
