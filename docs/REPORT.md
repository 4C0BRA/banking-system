# Banking System

## Overview

This Java 17 desktop application demonstrates account operations and six design patterns in a small, in-memory banking simulator. The Swing interface calls a banking facade; a console demo is also available. There are no external library dependencies.

## Features

- Open savings and current accounts, deposit funds, and withdraw funds without overdrawing.
- Estimate savings interest with monthly compounding and loan interest with simple interest.
- Build immutable loan agreements without disbursing funds.
- View transaction history and alerts for successful withdrawals greater than 5000.
- Record in-app notifications and optionally add simulated email and SMS deliveries.

All amounts use one fictional currency and are rounded to two decimal places. Annual rates are percentages from 0 to 100; durations range from 1 to 600 months. State is held in memory and is cleared when the application closes. Email and SMS are simulations and send no external messages.

## Design

| Pattern | Implementation | Role |
|---|---|---|
| Factory Method | `factory` and `model` | Creates savings and current account products. |
| Strategy | `strategy` | Selects the savings or loan interest formula. |
| Builder | `builder` | Validates terms and creates immutable loan agreements. |
| Decorator | `decorator` | Adds optional simulated delivery channels around in-app notifications. |
| Observer | `observer` | Publishes successful transactions to history and fraud monitoring. |
| Facade | `facade` | Coordinates account, loan, interest, and notification operations for the UI. |

The Swing application is in `src/main/java/kz/aitu/bank/ui/BankingApp.java`. `BankingDemo` provides a console walkthrough. Editable UML diagrams are in `classes.puml` and `sequences.puml`.

## Build and checks

Use JDK 17 or later. From the repository root:

```sh
sh run.sh test
sh run.sh demo
sh run.sh
```

The test command compiles production and test sources, then runs the core, integration, and headless Swing checks. No test result is stated here because it depends on the environment used to run the current revision.
