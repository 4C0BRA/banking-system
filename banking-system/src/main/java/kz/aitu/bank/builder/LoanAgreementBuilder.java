package kz.aitu.bank.builder;

import java.math.BigDecimal;
import kz.aitu.bank.model.Money;
import kz.aitu.bank.strategy.InterestCalculator;
import kz.aitu.bank.strategy.LoanInterestStrategy;

public class LoanAgreementBuilder {
    private String borrower;
    private BigDecimal amount;
    private BigDecimal annualRate;
    private int months;
    private String purpose = "Personal needs";
    private String guarantor = "None";

    public LoanAgreementBuilder borrower(String borrower) {
        this.borrower = borrower;
        return this;
    }

    public LoanAgreementBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LoanAgreementBuilder annualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
        return this;
    }

    public LoanAgreementBuilder months(int months) {
        this.months = months;
        return this;
    }

    public LoanAgreementBuilder purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public LoanAgreementBuilder guarantor(String guarantor) {
        this.guarantor = guarantor;
        return this;
    }

    public LoanAgreement build() {
        if (borrower == null || borrower.isBlank()) {
            throw new IllegalArgumentException("Borrower is required");
        }
        if (purpose == null || purpose.isBlank() || guarantor == null || guarantor.isBlank()) {
            throw new IllegalArgumentException("Purpose and guarantor must not be blank");
        }
        InterestCalculator calculator = new InterestCalculator(new LoanInterestStrategy());
        BigDecimal interest = calculator.calculate(amount, annualRate, months);
        return new LoanAgreement(borrower.strip(), Money.positive(amount), annualRate,
                months, interest, purpose.strip(), guarantor.strip());
    }
}
