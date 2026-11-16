package kz.aitu.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kz.aitu.bank.decorator.AppNotifier;
import kz.aitu.bank.decorator.EmailNotifier;
import kz.aitu.bank.decorator.Notifier;
import kz.aitu.bank.decorator.SmsNotifier;
import kz.aitu.bank.facade.BankingFacade;
import kz.aitu.bank.observer.Transaction;
import kz.aitu.bank.observer.TransactionHistory;
import kz.aitu.bank.observer.TransactionPublisher;

public class BankingIntegrationTest {
    private static int checks;

    public static void main(String[] args) {
        BankingFacade bank = new BankingFacade();
        var account = bank.openAccount("Savings", "Aruzhan");
        reject(() -> bank.openAccount("Other", "A"));
        reject(() -> bank.deposit("missing", value("10")));
        reject(() -> bank.deposit(account.getId(), value("-1")));
        reject(() -> bank.deposit(account.getId(), value("0.001")));
        check(bank.getHistory().isEmpty());
        bank.deposit(account.getId(), value("12000"));
        reject(() -> bank.withdraw(account.getId(), value("13000")));
        equal("12000", account.getBalance());
        check(bank.getHistory().size() == 1);
        check(bank.getAlerts().isEmpty());
        check(bank.getNotifications().isEmpty());
        bank.withdraw(account.getId(), value("5000"));
        check(bank.getAlerts().isEmpty());
        bank.withdraw(account.getId(), value("5000.01"));
        equal("1999.99", account.getBalance());
        check(bank.getAlerts().size() == 1);
        check(bank.getNotifications().size() == 1);
        check(bank.getNotifications().get(0).startsWith("APP:"));
        bank.withdraw(account.getId(), value("99.99"));
        equal("1900", account.getBalance());
        int historySize = bank.getHistory().size();
        reject(() -> bank.withdraw(account.getId(), value("6000")));
        check(bank.getHistory().size() == historySize);
        check(bank.getAlerts().size() == 1);
        check(bank.getNotifications().size() == 1);
        bank.createLoan(account.getId(), value("1000"), value("12"), 12, "Study", "None");
        check(bank.getAgreements().size() == 1);
        equal("120", bank.getAgreements().get(0).getInterest());
        equal("1900", account.getBalance());
        bank.setHistoryEnabled(false);
        bank.setNotificationChannels(true, true);
        bank.deposit(account.getId(), value("10000"));
        bank.withdraw(account.getId(), value("6000"));
        check(bank.getHistory().size() == historySize);
        check(bank.getAlerts().size() == 2);
        check(bank.getNotifications().size() == 4);
        check(bank.getNotifications().get(2).startsWith("EMAIL (simulated):"));
        check(bank.getNotifications().get(3).startsWith("SMS (simulated):"));
        bank.setHistoryEnabled(true);
        bank.setHistoryEnabled(true);
        bank.deposit(account.getId(), value("100"));
        check(bank.getHistory().size() == historySize + 1);
        equal("6000", account.getBalance());
        equal("1200", bank.calculateInterest("Loan", value("10000"), value("12"), 12));
        equal("1268.25", bank.calculateInterest("Savings", value("10000"), value("12"), 12));
        for (boolean email : new boolean[]{false, true}) {
            for (boolean sms : new boolean[]{false, true}) {
                bank.deposit(account.getId(), value("6000"));
                bank.setNotificationChannels(email, sms);
                int before = bank.getNotifications().size();
                bank.withdraw(account.getId(), value("6000"));
                List<String> deliveries = bank.getNotifications().subList(before, bank.getNotifications().size());
                check(deliveries.size() == 1 + (email ? 1 : 0) + (sms ? 1 : 0));
                check(deliveries.get(0).startsWith("APP:"));
                check(deliveries.stream().anyMatch(text -> text.startsWith("EMAIL")) == email);
                check(deliveries.stream().anyMatch(text -> text.startsWith("SMS")) == sms);
            }
        }
        List<String> deliveries = new ArrayList<>();
        Notifier notifier = new SmsNotifier(new EmailNotifier(new AppNotifier(deliveries), deliveries), deliveries);
        notifier.send("Test alert");
        check(deliveries.equals(List.of("APP: Test alert", "EMAIL (simulated): Test alert", "SMS (simulated): Test alert")));
        TransactionPublisher publisher = new TransactionPublisher();
        TransactionHistory history = new TransactionHistory();
        Transaction transaction = new Transaction("1", "DEPOSIT", value("10"), value("10"), LocalDateTime.now());
        publisher.register(history);
        publisher.register(history);
        publisher.publish(transaction);
        check(history.getTransactions().size() == 1);
        publisher.remove(history);
        publisher.publish(transaction);
        check(history.getTransactions().size() == 1);
        System.out.println("Banking integration: " + checks + " checks passed");
    }

    private static BigDecimal value(String text) {
        return new BigDecimal(text);
    }

    private static void equal(String expected, BigDecimal actual) {
        check(value(expected).compareTo(actual) == 0);
    }

    private static void reject(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException | IllegalStateException exception) {
            checks++;
            return;
        }
        throw new AssertionError("Expected rejection");
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError("Check failed: " + (checks + 1));
        }
        checks++;
    }
}
