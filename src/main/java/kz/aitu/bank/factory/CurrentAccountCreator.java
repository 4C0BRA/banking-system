package kz.aitu.bank.factory;

import kz.aitu.bank.model.Account;
import kz.aitu.bank.model.CurrentAccount;

public class CurrentAccountCreator extends AccountCreator {
    @Override
    protected Account createAccount(String id, String owner) {
        return new CurrentAccount(id, owner);
    }
}
