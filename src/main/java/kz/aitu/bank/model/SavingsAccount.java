package kz.aitu.bank.model;

public class SavingsAccount extends Account {
    public SavingsAccount(String id, String owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return "Savings";
    }
}
