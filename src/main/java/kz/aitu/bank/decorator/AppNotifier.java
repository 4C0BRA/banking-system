package kz.aitu.bank.decorator;

import java.util.List;

public class AppNotifier implements Notifier {
    private final List<String> notifications;

    public AppNotifier(List<String> notifications) {
        this.notifications = notifications;
    }

    @Override
    public void send(String message) {
        notifications.add("APP: " + message);
    }
}
