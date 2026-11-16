package kz.aitu.bank.decorator;

import java.util.List;

public class EmailNotifier extends NotifierDecorator {
    private final List<String> notifications;

    public EmailNotifier(Notifier wrapped, List<String> notifications) {
        super(wrapped);
        this.notifications = notifications;
    }

    @Override
    public void send(String message) {
        super.send(message);
        notifications.add("EMAIL (simulated): " + message);
    }
}
