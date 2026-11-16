# Banking Management Simulator

## 1. Introduction

This project is a desktop banking simulator implemented in Java using six design patterns. Its purpose is to demonstrate object creation, interchangeable calculations, composable notification channels, event notifications, and subsystem coordination in one application.

The team consists of two students. Ansar is responsible for Factory Method, Strategy, and Builder. The second student is responsible for Decorator, Observer, and Facade. Replace this description with both students' full names, IDs, and group before submission.

## 2. Body

### 2.1 Requirements and architecture

The application creates savings and current accounts, accepts deposits and withdrawals, estimates interest, builds loan agreements, and displays transaction history and fraud alerts. The interface is implemented using Swing. The business logic can also be demonstrated through BankingDemo without opening a window.

Packages separate the shared account model and the six patterns. BankingFacade coordinates business operations. BankingApp delegates requests to the facade rather than directly calling the notification decorators or event publisher. Data is stored in memory.

### 2.2 Factory Method — student 1

AccountCreator defines the creation method createAccount and calls it from openAccount. SavingsAccountCreator and CurrentAccountCreator override createAccount to return their corresponding Account products. The facade selects a concrete creator from the requested account type.

This separates account construction from the facade. A disadvantage is the extra creator class for every account type.

Java code: src/main/java/kz/aitu/bank/factory and model.

### 2.3 Strategy — student 1

InterestStrategy defines calculate. SavingsInterestStrategy calculates monthly compound interest, and LoanInterestStrategy calculates simple interest. InterestCalculator is the context: it validates inputs, delegates calculation, and rounds the result. Its setStrategy method supports runtime replacement, which the facade uses when the user changes the calculation type.

For principal P, annual percentage r, and duration m months:

- Savings interest = P × ((1 + r / 1200)^m − 1).
- Loan interest = P × r × m / 1200.

For P = 10000, r = 12, m = 12, the results are 1268.25 and 1200.00. These are educational calculations, not a real bank's repayment schedule.

Java code: src/main/java/kz/aitu/bank/strategy.

### 2.4 Builder — student 1

LoanAgreementBuilder collects required borrower, amount, annual rate, and duration, plus optional purpose and guarantor. build validates the data, uses the loan calculation, and returns an immutable LoanAgreement. LoanAgreementDirector supplies an optional personal-loan recipe; the UI uses the builder directly for user-entered terms.

The pattern makes construction readable and avoids a long public constructor. The tradeoff is mutable intermediate builder state and additional classes. Changes to a builder do not modify already built agreements.

Java code: src/main/java/kz/aitu/bank/builder.

### 2.5 Decorator — student 2

Notifier is the component interface, and AppNotifier is the concrete component that records in-app notifications. NotifierDecorator wraps another Notifier and delegates send to it. EmailNotifier and SmsNotifier extend this decorator and append their simulated deliveries after calling the wrapped notifier.

BankingFacade builds the chain from the user's checkbox selections. With both extra channels enabled, the chain is SmsNotifier → EmailNotifier → AppNotifier. A single send call results in APP, EMAIL, and SMS entries. All four channel combinations are supported, and configuration changes affect future alerts only.

The pattern adds delivery behavior without changing FraudMonitor or AppNotifier. It introduces extra wrapper objects and makes the execution order less direct to follow. Email and SMS are local simulations; no external messages are sent.

Java code: src/main/java/kz/aitu/bank/decorator.

### 2.6 Observer — student 2

TransactionPublisher registers and removes TransactionObserver subscribers. Successful transactions notify TransactionHistory and FraudMonitor. Duplicate registration does not cause duplicate notifications. Disabling the history observer does not disable fraud monitoring and does not backfill missed transactions.

FraudMonitor detects withdrawals strictly greater than 5000, records an alert, and calls the configured Notifier. The notification chain handles channel delivery. Alerts do not prevent subsequent transactions. This is a simple deterministic rule, not machine-learning fraud detection. Notifications are synchronous. The two built-in observers are expected to complete normally; asynchronous delivery and observer-failure isolation are future work.

Java code: src/main/java/kz/aitu/bank/observer.

### 2.7 Facade — student 2

BankingFacade exposes account creation, deposits, withdrawals, notification channel configuration, interest calculation, loan creation, and read access to history and alerts. It connects the factory, calculator, builder, notification decorators, and publisher. The UI does not need to know their construction or execution order.

The facade simplifies the client but can grow too large as features are added. This implementation keeps the course project's limited operations in one service.

Java code: src/main/java/kz/aitu/bank/facade/BankingFacade.java.

### 2.8 Coordinated workflows

Account opening: UI → Facade → Concrete AccountCreator → Account.

Loan agreement: UI → Facade → Builder → InterestCalculator → LoanInterestStrategy → immutable agreement.

Transaction: UI → Facade → Account balance → TransactionPublisher → History and FraudMonitor → Notifier decorators → notification deliveries when suspicious.

Rejected operations do not publish successful-transaction events. Loan agreement creation does not disburse money. Estimates do not change balances.

### 2.9 Verification

Run `sh banking-system/run.sh test`. At implementation time the results were:

- CorePatternsTest: 19 checks passed.
- BankingIntegrationTest: 51 checks passed.
- BankingUiTest: account creation, deposit, interest display, loan creation, decorated notification display, and subsequent withdrawal actions passed on Swing's event-dispatch thread.

Core checks cover concrete factory products, strategy replacement, formulas, validation, immutable agreements, and the director recipe. Integration checks cover the fraud threshold boundary, rejected transaction invariants, observer removal and re-registration, loan creation without balance changes, all four channel combinations, and decorator delegation order.

The automated Swing test runs headlessly. It verifies component actions and model results; it does not replace visual inspection of the native window.

### 2.10 Application screenshots and UML

Include screenshots of the Accounts, Interest calculator, Loan agreements, Transaction history, and Fraud alerts tabs. Include a rejected withdrawal with insufficient funds and notification deliveries with extra channels enabled. Use the README demonstration scenario and capture the submitted revision.

Editable PlantUML sources are in classes.puml and sequences.puml beside this report. Render them and include the resulting diagrams in the submitted report. Full Java source is under src/main/java; focused excerpts should accompany each pattern section in the final document.

## 3. Conclusion

The simulator implements six patterns in a unified application. Creation patterns construct accounts and agreements, Strategy handles calculation differences, Decorator adds notification channels, Observer distributes events, and Facade coordinates the system. The project demonstrates how the patterns can cooperate without all six participating in every operation.

Each student should add a personal paragraph describing their actual implementation, a difficulty they encountered, and a tradeoff they can explain during the defense.

## 4. Further work

Possible extensions include database persistence, customer authentication, configurable fraud rules, repayment schedules, more interest strategies, asynchronous notifications with error isolation, and exporting agreements. These features are outside the current implementation.

## 5. Submission checklist

- Add both students' names, IDs, group, instructor, and submission date.
- Ensure the instructor accepts the six chosen patterns.
- Rerun the tests against the final revision.
- Insert actual application screenshots and rendered UML diagrams.
- Add selected Java excerpts and attach or link the complete source.
- Add course texts and any sources actually consulted with accurate bibliographic details.
- Explain individual contributions honestly and practise the integrated demonstration.
