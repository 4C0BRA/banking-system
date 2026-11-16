package kz.aitu.bank.observer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kz.aitu.bank.decorator.Notifier;

public class FraudMonitor implements TransactionObserver {
    public static final BigDecimal LIMIT = new BigDecimal("5000.00");
    private Notifier notifier;
    private final List<String> alerts = new ArrayList<>();

    public FraudMonitor(Notifier notifier) {
        setNotifier(notifier);
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = Objects.requireNonNull(notifier);
    }

    @Override
    public void update(Transaction transaction) {
        if (transaction.type().equals("WITHDRAW") && transaction.amount().compareTo(LIMIT) > 0) {
            String alert = "Suspicious withdrawal: " + transaction.accountId() + " | " + transaction.amount();
            alerts.add(alert);
            notifier.send(alert);
        }
    }

    public List<String> getAlerts() {
        return List.copyOf(alerts);
    }
}
