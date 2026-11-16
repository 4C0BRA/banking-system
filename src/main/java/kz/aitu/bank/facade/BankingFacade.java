package kz.aitu.bank.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kz.aitu.bank.builder.LoanAgreement;
import kz.aitu.bank.builder.LoanAgreementBuilder;
import kz.aitu.bank.factory.AccountCreator;
import kz.aitu.bank.factory.CurrentAccountCreator;
import kz.aitu.bank.factory.SavingsAccountCreator;
import kz.aitu.bank.model.Account;
import kz.aitu.bank.model.Money;
import kz.aitu.bank.observer.FraudMonitor;
import kz.aitu.bank.observer.Transaction;
import kz.aitu.bank.observer.TransactionHistory;
import kz.aitu.bank.observer.TransactionPublisher;
import kz.aitu.bank.decorator.AppNotifier;
import kz.aitu.bank.decorator.EmailNotifier;
import kz.aitu.bank.decorator.Notifier;
import kz.aitu.bank.decorator.SmsNotifier;
import kz.aitu.bank.strategy.InterestCalculator;
import kz.aitu.bank.strategy.LoanInterestStrategy;
import kz.aitu.bank.strategy.SavingsInterestStrategy;

public class BankingFacade {
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private final TransactionPublisher publisher = new TransactionPublisher();
    private final TransactionHistory history = new TransactionHistory();
    private final List<String> notifications = new ArrayList<>();
    private final FraudMonitor fraudMonitor = new FraudMonitor(new AppNotifier(notifications));
    private final List<LoanAgreement> agreements = new ArrayList<>();
    private final InterestCalculator calculator = new InterestCalculator(new SavingsInterestStrategy());
    private int nextId = 1;

    public BankingFacade() {
        publisher.register(history);
        publisher.register(fraudMonitor);
    }

    public Account openAccount(String type, String owner) {
        AccountCreator creator;
        if ("Savings".equals(type)) {
            creator = new SavingsAccountCreator();
        } else if ("Current".equals(type)) {
            creator = new CurrentAccountCreator();
        } else {
            throw new IllegalArgumentException("Unknown account type");
        }
        Account account = creator.openAccount("ACC-" + nextId, owner);
        accounts.put(account.getId(), account);
        nextId++;
        return account;
    }

    public void deposit(String id, BigDecimal amount) {
        Account account = find(id);
        account.deposit(amount);
        publish(account, "DEPOSIT", amount);
    }

    public void withdraw(String id, BigDecimal amount) {
        Account account = find(id);
        account.withdraw(amount);
        publish(account, "WITHDRAW", amount);
    }

    private void publish(Account account, String type, BigDecimal amount) {
        publisher.publish(new Transaction(account.getId(), type, Money.positive(amount),
                account.getBalance(), LocalDateTime.now()));
    }

    public void setNotificationChannels(boolean email, boolean sms) {
        Notifier notifier = new AppNotifier(notifications);
        if (email) {
            notifier = new EmailNotifier(notifier, notifications);
        }
        if (sms) {
            notifier = new SmsNotifier(notifier, notifications);
        }
        fraudMonitor.setNotifier(notifier);
    }

    public BigDecimal calculateInterest(String type, BigDecimal amount, BigDecimal rate, int months) {
        if ("Savings".equals(type)) {
            calculator.setStrategy(new SavingsInterestStrategy());
        } else if ("Loan".equals(type)) {
            calculator.setStrategy(new LoanInterestStrategy());
        } else {
            throw new IllegalArgumentException("Unknown interest type");
        }
        return calculator.calculate(amount, rate, months);
    }

    public LoanAgreement createLoan(String id, BigDecimal amount, BigDecimal rate,
                                    int months, String purpose, String guarantor) {
        Account account = find(id);
        LoanAgreement agreement = new LoanAgreementBuilder()
                .borrower(account.getOwner()).amount(amount).annualRate(rate)
                .months(months).purpose(purpose).guarantor(guarantor).build();
        agreements.add(agreement);
        return agreement;
    }

    public void setHistoryEnabled(boolean enabled) {
        if (enabled) {
            publisher.register(history);
        } else {
            publisher.remove(history);
        }
    }

    public List<Account> getAccounts() {
        return List.copyOf(accounts.values());
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }

    public List<Transaction> getHistory() {
        return history.getTransactions();
    }

    public List<String> getAlerts() {
        return fraudMonitor.getAlerts();
    }

    public List<LoanAgreement> getAgreements() {
        return List.copyOf(agreements);
    }

    private Account find(String id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }
}
