package kz.aitu.bank;

import java.math.BigDecimal;
import kz.aitu.bank.facade.BankingFacade;

public class BankingDemo {
    public static void main(String[] args) {
        BankingFacade bank = new BankingFacade();
        var savings = bank.openAccount("Savings", "Aruzhan");
        var current = bank.openAccount("Current", "Daniyar");
        System.out.println("Accounts: " + bank.getAccounts());
        System.out.println("Savings interest: " + bank.calculateInterest("Savings", new BigDecimal("10000"), new BigDecimal("12"), 12));
        System.out.println("Loan interest: " + bank.calculateInterest("Loan", new BigDecimal("10000"), new BigDecimal("12"), 12));
        System.out.println(bank.createLoan(savings.getId(), new BigDecimal("10000"), new BigDecimal("12"), 12, "Education", "None"));
        bank.setNotificationChannels(true, true);
        bank.deposit(savings.getId(), new BigDecimal("12000"));
        bank.withdraw(savings.getId(), new BigDecimal("6000"));
        bank.withdraw(savings.getId(), new BigDecimal("100"));
        bank.setHistoryEnabled(false);
        bank.deposit(current.getId(), new BigDecimal("200"));
        bank.setHistoryEnabled(true);
        bank.deposit(current.getId(), new BigDecimal("50"));
        System.out.println("History:");
        bank.getHistory().forEach(System.out::println);
        System.out.println("Alerts: " + bank.getAlerts());
        System.out.println("Notification deliveries:");
        bank.getNotifications().forEach(System.out::println);
        System.out.println("Final savings balance: " + savings.getBalance());
    }
}
