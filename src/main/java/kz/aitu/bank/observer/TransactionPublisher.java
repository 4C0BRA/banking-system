package kz.aitu.bank.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionPublisher {
    private final List<TransactionObserver> observers = new ArrayList<>();

    public void register(TransactionObserver observer) {
        Objects.requireNonNull(observer);
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void remove(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void publish(Transaction transaction) {
        for (TransactionObserver observer : new ArrayList<>(observers)) {
            observer.update(transaction);
        }
    }
}
