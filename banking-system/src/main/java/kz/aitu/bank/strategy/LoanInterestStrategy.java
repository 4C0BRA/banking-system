package kz.aitu.bank.strategy;

import java.math.BigDecimal;
import java.math.MathContext;

public class LoanInterestStrategy implements InterestStrategy {
    @Override
    public BigDecimal calculate(BigDecimal principal, BigDecimal annualRate, int months) {
        return principal.multiply(annualRate).multiply(BigDecimal.valueOf(months))
                .divide(new BigDecimal("1200"), MathContext.DECIMAL128);
    }
}
