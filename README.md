# Banking System

A two-student course project built with Java 17 and Swing, without external libraries.

## Running the application

From the repository root:

```sh
sh banking-system/run.sh
```

Run the console demonstration or automated checks:

```sh
sh banking-system/run.sh demo
sh banking-system/run.sh test
```

In IntelliJ IDEA, select JDK 17 or later, mark `banking-system/src/main/java` as Sources Root and `banking-system/src/test/java` as Test Sources Root, then run `kz.aitu.bank.ui.BankingApp`.

On Windows, use IntelliJ IDEA or run these commands from the `banking-system` directory:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object { '"' + $_.FullName + '"' } | Set-Content out/sources.txt
javac --release 17 -encoding UTF-8 -d out '@out/sources.txt'
java -cp out kz.aitu.bank.ui.BankingApp
```

## Patterns and responsibilities

| Team member | Pattern | Package |
|---|---|---|
| Ansar | Factory Method | `factory`, with products in `model` |
| Ansar | Strategy | `strategy` |
| Ansar | Builder | `builder` |
| Second student | Decorator | `decorator` |
| Second student | Observer | `observer` |
| Second student | Facade | `facade` |

The interface is in `ui`, and the console demonstration is in `BankingDemo`. See [team commit instructions](docs/TEAM_COMMITS.md) for the second student's commits.

## Demonstration walkthrough

1. On the Accounts and transactions tab, enter a customer name and create a Savings account.
2. Deposit 12000. The balance should be 12000.00.
3. On the Interest calculator tab, calculate interest on 10000 at an annual rate of 12% for 12 months. Savings interest should be 1268.25; Loan interest should be 1200.00.
4. On the Loan agreements tab, create an agreement. It should appear in the list without changing the account balance.
5. On the Fraud alerts tab, enable Email and SMS. These channels are simulated; no messages are sent externally.
6. Withdraw 6000. The balance should become 6000.00. The Fraud alerts tab should display one alert and three notification deliveries: APP, EMAIL, and SMS. Observer triggers the fraud monitor; Decorator adds delivery channels.
7. Withdraw another 100. The balance should become 5900.00. Fraud notifications do not block transactions.
8. Disable Email and SMS, deposit 6000, and withdraw 6000 again. The new alert should have only an APP delivery. Previous deliveries remain visible.
9. On the Transaction history tab, unsubscribe the history observer, make a deposit, and confirm that no new history entry appears. FraudMonitor remains subscribed. Resubscribing history only affects future transactions.
10. Try withdrawing more than the available balance. The request should be rejected without changing the balance, transaction history, or notifications.

## Application rules

- Data is stored in memory and is lost when the application closes.
- All amounts use one fictional currency. Monetary values are rounded to two decimal places using HALF_UP.
- Transaction amounts must be positive. Overdrafts are not allowed.
- A successful withdrawal greater than 5000 triggers a fraud alert after the transaction completes. It does not block future operations.
- Notifications always include the in-app channel. Email and SMS decorators can be enabled independently for future alerts; both are simulated.
- Savings interest uses monthly compounding; Loan interest uses simple interest without amortization.
- Annual rates are entered as percentages: 12 means 12%. Rates must be between 0 and 100%, and durations between 1 and 600 months.
- A loan agreement is an educational document. Creating one does not disburse funds or change the account balance.

## Documentation

- [Project report draft](docs/REPORT.md)
- [UML class diagram](docs/classes.puml)
- [UML sequence diagrams](docs/sequences.puml)
- [Team commit instructions](docs/TEAM_COMMITS.md)

Capture report screenshots from the running application using the walkthrough above. Run `sh banking-system/run.sh test` to obtain the automated check results.
