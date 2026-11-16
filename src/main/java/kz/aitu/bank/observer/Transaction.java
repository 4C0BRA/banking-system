package kz.aitu.bank.observer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(String accountId, String type, BigDecimal amount,
                          BigDecimal balance, LocalDateTime time) {
    @Override
    public String toString() {
        return time.withNano(0) + " | " + accountId + " | " + type
                + " | " + amount + " | balance: " + balance;
    }
}
