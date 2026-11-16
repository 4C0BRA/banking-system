# Second student's setup and commits

Ansar's Factory Method, Strategy, and Builder commits are already in the GitHub repository. A fourth commit removes redundant documentation. The second student's Decorator, Observer, Facade, and application integration files are supplied in `banking-system-friend-handoff.zip` and are not yet committed.

## 1. Repository access

The repository owner should invite the second student's GitHub account as a collaborator if necessary. The second student must accept the invitation before pushing. Git author configuration does not grant repository access.

## 2. Clone and configure Git

Install JDK 17 or later and Git, then clone the repository:

```sh
git clone https://github.com/4C0BRA/banking-system.git
cd banking-system
git config user.name "Your actual name"
git config user.email "Your GitHub verified email or GitHub noreply email"
git config --get user.name
git config --get user.email
```

Use your own values instead of the placeholders. These settings apply to this clone. Authenticate Git with your own GitHub account when pushing; do not use another student's credentials.

## 3. Add the supplied files

Extract `banking-system-friend-handoff.zip` into the clone root. The archive contains a `banking-system` directory. The resulting structure must include:

```text
banking-system/                 repository clone
    .git/
    .gitignore
    banking-system/            project directory from the archive
        README.md
        run.sh
        docs/
        src/
```

Merge the archive's project directory with the existing project directory. Do not create a third nested `banking-system` directory. The archive includes Ansar's unchanged source files so the complete project can be tested locally.

Run `git status --short`. You should see new decorator, observer, facade, UI, integration test, and documentation files. Existing committed model, factory, strategy, builder, and core test files should not appear as modified.

## 4. Run and review the project

From the clone root on macOS or Linux:

```sh
java --version
javac --version
sh banking-system/run.sh test
sh banking-system/run.sh demo
sh banking-system/run.sh
```

Expected automated results: 19 core checks, 51 integration checks, and a passing Swing UI test. On Windows, use the IntelliJ setup in the project README to run CorePatternsTest, BankingIntegrationTest, BankingUiTest, BankingDemo, and BankingApp.

Review and understand the implementation before committing. Be ready to explain decorator wrapping and delegation, observer registration/removal, and facade coordination during the defense.

## 5. Create three separate commits

Run these commands from the clone root. Stage the listed files explicitly.

### Decorator

```sh
git add banking-system/src/main/java/kz/aitu/bank/decorator
git diff --cached --stat
git commit -m "Add composable app email and SMS notifications"
```

### Observer

```sh
git add banking-system/src/main/java/kz/aitu/bank/observer
git diff --cached --stat
git commit -m "Add transaction observers and fraud alerts"
```

### Facade and application integration

```sh
git add banking-system/src/main/java/kz/aitu/bank/facade banking-system/src/main/java/kz/aitu/bank/ui banking-system/src/main/java/kz/aitu/bank/BankingDemo.java banking-system/src/test/java/kz/aitu/bank/BankingIntegrationTest.java banking-system/src/test/java/kz/aitu/bank/BankingUiTest.java banking-system/run.sh banking-system/README.md banking-system/docs
git diff --cached --stat
git commit -m "Connect banking features through facade and Swing interface"
```

## 6. Verify and push

```sh
sh banking-system/run.sh test
git log -3 --format="%h %an <%ae> %s"
git status --short
git push origin main
```

The latest three commits should show your author details. The working tree should be clean. If pushing is rejected because main has advanced, run `git pull --rebase origin main`, resolve any conflicts, rerun the tests, and push normally. Do not force push. If repository rules require a pull request, push a feature branch and open a pull request instead of changing the repository rules.

If GitHub returns a permission error, check your collaborator invitation and the account used for authentication.

## 7. Prepare the submission

Add both students' full names and IDs to the report, capture actual application screenshots, render the PlantUML diagrams, and practise each student's three patterns and the integrated demonstration.
