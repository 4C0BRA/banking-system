package kz.aitu.bank.builder;

import java.math.BigDecimal;

public class LoanAgreementDirector {
    public LoanAgreement createPersonalLoan(String borrower, BigDecimal amount,
                                             BigDecimal rate, int months) {
        return new LoanAgreementBuilder().borrower(borrower).amount(amount)
                .annualRate(rate).months(months).purpose("Personal loan").build();
    }
}
