package kz.aitu.bank.model;

public class CurrentAccount extends Account {
    public CurrentAccount(String id, String owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return "Current";
    }
}
