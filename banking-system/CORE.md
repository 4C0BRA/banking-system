# Banking core — student 1

Java 17, no external dependencies.

Implemented patterns:

- Factory Method: AccountCreator, SavingsAccountCreator, CurrentAccountCreator.
- Strategy: InterestCalculator delegates to SavingsInterestStrategy or LoanInterestStrategy.
- Builder: LoanAgreementBuilder creates immutable agreements; LoanAgreementDirector provides a personal-loan recipe.

Shared model: Account, SavingsAccount, CurrentAccount, Money.

Compile and test this part from the banking-system directory:

```sh
mkdir -p out
find src/main/java/kz/aitu/bank/model src/main/java/kz/aitu/bank/factory src/main/java/kz/aitu/bank/strategy src/main/java/kz/aitu/bank/builder -name '*.java' > out/core-sources.txt
javac --release 17 -encoding UTF-8 -d out @out/core-sources.txt src/test/java/kz/aitu/bank/CorePatternsTest.java
java -ea -cp out kz.aitu.bank.CorePatternsTest
```

The complete application adds State, Observer and Facade, implemented in the second student's part. Full run instructions are in README.md once that part is included.
