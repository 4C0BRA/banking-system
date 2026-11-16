#!/bin/sh
export PATH="$PATH:/c/Users/atorc/.jdks/openjdk-27/bin/javac"
set -eu
cd "$(dirname "$0")"
mkdir -p out
find src/main/java -name '*.java' > out/sources.txt
javac --release 17 -encoding UTF-8 -d out @out/sources.txt
case "${1:-ui}" in
  ui) java -cp out kz.aitu.bank.ui.BankingApp ;;
  demo) java -cp out kz.aitu.bank.BankingDemo ;;
  test)
    find src/test/java -name '*.java' > out/tests.txt
    javac --release 17 -encoding UTF-8 -cp out -d out @out/tests.txt
    java -ea -Djava.awt.headless=true -cp out kz.aitu.bank.CorePatternsTest
    java -ea -Djava.awt.headless=true -cp out kz.aitu.bank.BankingIntegrationTest
    java -ea -Djava.awt.headless=true -cp out kz.aitu.bank.BankingUiTest
    ;;
  *) echo 'Usage: sh run.sh [ui|demo|test]' >&2; exit 1 ;;
esac
