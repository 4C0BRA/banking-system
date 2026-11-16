package kz.aitu.bank.observer;

public interface TransactionObserver {
    void update(Transaction transaction);
}
