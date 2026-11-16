package kz.aitu.bank.model;

import java.math.BigDecimal;

public abstract class Account {
    private final String id;
    private final String owner;
    private BigDecimal balance = new BigDecimal("0.00");

    protected Account(String id, String owner) {
        if (id == null || id.isBlank() || owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Account ID and owner are required");
        }
        this.id = id.strip();
        this.owner = owner.strip();
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(Money.positive(amount));
    }

    public void withdraw(BigDecimal amount) {
        BigDecimal value = Money.positive(amount);
        if (balance.compareTo(value) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance = balance.subtract(value);
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return id + " - " + owner + " (" + getType() + ")";
    }
}
