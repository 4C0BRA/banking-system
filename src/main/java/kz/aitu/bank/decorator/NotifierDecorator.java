package kz.aitu.bank.decorator;

public abstract class NotifierDecorator implements Notifier {
    private final Notifier wrapped;

    protected NotifierDecorator(Notifier wrapped) {
        this.wrapped = java.util.Objects.requireNonNull(wrapped);
    }

    @Override
    public void send(String message) {
        wrapped.send(message);
    }
}
