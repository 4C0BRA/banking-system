package kz.aitu.bank.builder;

import java.math.BigDecimal;

public final class LoanAgreement {
    private final String borrower;
    private final BigDecimal amount;
    private final BigDecimal annualRate;
    private final int months;
    private final BigDecimal interest;
    private final String purpose;
    private final String guarantor;

    LoanAgreement(String borrower, BigDecimal amount, BigDecimal annualRate,
                  int months, BigDecimal interest, String purpose, String guarantor) {
        this.borrower = borrower;
        this.amount = amount;
        this.annualRate = annualRate;
        this.months = months;
        this.interest = interest;
        this.purpose = purpose;
        this.guarantor = guarantor;
    }

    public String getBorrower() {
        return borrower;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getTotal() {
        return amount.add(interest);
    }

    @Override
    public String toString() {
        return "LOAN AGREEMENT\nBorrower: " + borrower
                + "\nAmount: " + amount + "\nAnnual rate: " + annualRate + "%"
                + "\nDuration: " + months + " months\nPurpose: " + purpose
                + "\nGuarantor: " + guarantor + "\nEstimated interest: " + interest
                + "\nTotal repayment: " + getTotal();
    }
}
