package kz.aitu.bank.strategy;

import java.math.BigDecimal;
import java.util.Objects;
import kz.aitu.bank.model.Money;

public class InterestCalculator {
    private InterestStrategy strategy;

    public InterestCalculator(InterestStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(InterestStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Strategy is required");
    }

    public BigDecimal calculate(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal amount = Money.positive(principal);
        if (annualRate == null || annualRate.signum() < 0 || annualRate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Annual rate must be between 0 and 100 percent");
        }
        if (months < 1 || months > 600) {
            throw new IllegalArgumentException("Duration must be between 1 and 600 months");
        }
        return Money.round(strategy.calculate(amount, annualRate, months));
    }
}
