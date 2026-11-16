package kz.aitu.bank.decorator;

import java.util.List;

public class SmsNotifier extends NotifierDecorator {
    private final List<String> notifications;

    public SmsNotifier(Notifier wrapped, List<String> notifications) {
        super(wrapped);
        this.notifications = notifications;
    }

    @Override
    public void send(String message) {
        super.send(message);
        notifications.add("SMS (simulated): " + message);
    }
}
