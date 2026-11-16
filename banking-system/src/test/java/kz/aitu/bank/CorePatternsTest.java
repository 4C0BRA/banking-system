package kz.aitu.bank;

import java.math.BigDecimal;
import kz.aitu.bank.builder.LoanAgreementBuilder;
import kz.aitu.bank.builder.LoanAgreementDirector;
import kz.aitu.bank.factory.CurrentAccountCreator;
import kz.aitu.bank.factory.SavingsAccountCreator;
import kz.aitu.bank.model.CurrentAccount;
import kz.aitu.bank.model.SavingsAccount;
import kz.aitu.bank.strategy.InterestCalculator;
import kz.aitu.bank.strategy.LoanInterestStrategy;
import kz.aitu.bank.strategy.SavingsInterestStrategy;

public class CorePatternsTest {
    private static int checks;

    public static void main(String[] args) {
        check(new SavingsAccountCreator().openAccount("1", "A") instanceof SavingsAccount);
        check(new CurrentAccountCreator().openAccount("2", "B") instanceof CurrentAccount);
        reject(() -> new SavingsAccountCreator().openAccount("1", " "));
        InterestCalculator calculator = new InterestCalculator(new SavingsInterestStrategy());
        equal("1268.25", calculator.calculate(value("10000"), value("12"), 12));
        calculator.setStrategy(new LoanInterestStrategy());
        equal("1200.00", calculator.calculate(value("10000"), value("12"), 12));
        equal("0.00", calculator.calculate(value("10000"), value("0"), 12));
        reject(() -> calculator.calculate(value("-10"), value("12"), 12));
        reject(() -> calculator.calculate(value("100"), value("-1"), 12));
        reject(() -> calculator.calculate(value("100"), value("101"), 12));
        reject(() -> calculator.calculate(value("100"), value("12"), 0));
        reject(() -> calculator.calculate(value("100"), value("12"), 601));
        reject(() -> calculator.calculate(value("0.001"), value("12"), 12));
        reject(() -> new LoanAgreementBuilder().build());
        reject(() -> new LoanAgreementBuilder().borrower("A").amount(value("10")).months(12).build());
        LoanAgreementBuilder builder = new LoanAgreementBuilder().borrower("A").amount(value("10000"))
                .annualRate(value("12")).months(12);
        var first = builder.build();
        var second = builder.amount(value("20000")).build();
        equal("10000.00", first.getAmount());
        equal("20000.00", second.getAmount());
        equal("11200.00", first.getTotal());
        check(first.toString().contains("Guarantor: None"));
        var directed = new LoanAgreementDirector().createPersonalLoan("B", value("1000"), value("12"), 12);
        equal("120.00", directed.getInterest());
        System.out.println("Core patterns: " + checks + " checks passed");
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
