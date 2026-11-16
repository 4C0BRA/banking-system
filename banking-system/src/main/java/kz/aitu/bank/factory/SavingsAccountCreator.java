package kz.aitu.bank.factory;

import kz.aitu.bank.model.Account;
import kz.aitu.bank.model.SavingsAccount;

public class SavingsAccountCreator extends AccountCreator {
    @Override
    protected Account createAccount(String id, String owner) {
        return new SavingsAccount(id, owner);
    }
}
