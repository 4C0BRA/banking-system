package kz.aitu.bank.observer;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements TransactionObserver {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void update(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }
}
