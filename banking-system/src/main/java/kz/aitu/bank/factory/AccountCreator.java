package kz.aitu.bank.factory;

import kz.aitu.bank.model.Account;

public abstract class AccountCreator {
    public Account openAccount(String id, String owner) {
        return createAccount(id, owner);
    }

    protected abstract Account createAccount(String id, String owner);
}
