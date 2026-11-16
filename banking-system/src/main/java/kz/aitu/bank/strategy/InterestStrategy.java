package kz.aitu.bank.strategy;

import java.math.BigDecimal;

public interface InterestStrategy {
    BigDecimal calculate(BigDecimal principal, BigDecimal annualRate, int months);
}
