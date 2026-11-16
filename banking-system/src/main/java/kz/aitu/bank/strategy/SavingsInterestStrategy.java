package kz.aitu.bank.strategy;

import java.math.BigDecimal;
import java.math.MathContext;

public class SavingsInterestStrategy implements InterestStrategy {
    @Override
    public BigDecimal calculate(BigDecimal principal, BigDecimal annualRate, int months) {
        MathContext precision = MathContext.DECIMAL128;
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("1200"), precision);
        BigDecimal multiplier = BigDecimal.ONE.add(monthlyRate).pow(months, precision);
        return principal.multiply(multiplier.subtract(BigDecimal.ONE), precision);
    }
}
