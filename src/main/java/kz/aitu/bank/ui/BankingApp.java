package kz.aitu.bank.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import kz.aitu.bank.facade.BankingFacade;
import kz.aitu.bank.model.Account;

public class BankingApp extends JPanel {
    private final BankingFacade bank;
    private final JComboBox<Account> accountBox = new JComboBox<>();
    private final DefaultTableModel accountTable = new DefaultTableModel(
            new String[]{"ID", "Owner", "Type", "Balance"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTextArea history = output();
    private final JTextArea alerts = output();
    private final JTextArea agreements = output();
    private final JLabel message = new JLabel("Ready. All amounts use demo currency units.");

    public BankingApp(BankingFacade bank) {
        this.bank = bank;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(1000, 650));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Accounts and transactions", accountPanel());
        tabs.addTab("Interest calculator", interestPanel());
        tabs.addTab("Loan agreements", loanPanel());
        tabs.addTab("Transaction history", historyPanel());
        tabs.addTab("Fraud alerts", fraudPanel());
        add(tabs, BorderLayout.CENTER);
        add(message, BorderLayout.SOUTH);
        refresh();
    }

    private JPanel accountPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel controls = new JPanel(new GridLayout(3, 1, 4, 4));
        JTextField owner = new JTextField(16);
        JComboBox<String> type = new JComboBox<>(new String[]{"Savings", "Current"});
        JPanel creation = row();
        creation.add(new JLabel("Owner:"));
        creation.add(owner);
        creation.add(type);
        creation.add(button("Open account", () -> bank.openAccount((String) type.getSelectedItem(), owner.getText())));
        controls.add(creation);
        JPanel transactions = row();
        JTextField amount = new JTextField("100.00", 10);
        accountBox.setPreferredSize(new Dimension(300, 28));
        transactions.add(accountBox);
        transactions.add(new JLabel("Amount:"));
        transactions.add(amount);
        transactions.add(button("Deposit", () -> bank.deposit(selectedId(), number(amount))));
        transactions.add(button("Withdraw", () -> bank.withdraw(selectedId(), number(amount))));
        controls.add(transactions);
        JPanel information = row();
        information.add(new JLabel("Withdrawals above 5000 trigger fraud notifications."));
        controls.add(information);
        panel.add(controls, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(accountTable)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel interestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        JComboBox<String> strategy = new JComboBox<>(new String[]{"Savings", "Loan"});
        JTextField principal = new JTextField("10000");
        JTextField rate = new JTextField("12");
        JTextField months = new JTextField("12");
        JTextArea result = output();
        field(form, "Calculation type", strategy);
        field(form, "Principal", principal);
        field(form, "Annual rate (%)", rate);
        field(form, "Duration (months)", months);
        form.add(button("Calculate", () -> {
            BigDecimal interest = bank.calculateInterest((String) strategy.getSelectedItem(),
                    number(principal), number(rate), Integer.parseInt(months.getText().strip()));
            result.setText("Estimated interest: " + interest
                    + "\nSavings: monthly compounding. Loan: simple interest."
                    + "\nThe estimate does not change account balances.");
        }));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(result), BorderLayout.CENTER);
        return panel;
    }

    private JPanel loanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 80, 12, 80));
        JTextField amount = new JTextField("10000");
        JTextField rate = new JTextField("12");
        JTextField months = new JTextField("12");
        JTextField purpose = new JTextField("Education");
        JTextField guarantor = new JTextField("None");
        form.add(new JLabel("Uses the account selected in the Accounts tab."));
        form.add(new JLabel("Creates a document; no funds are disbursed."));
        field(form, "Loan amount", amount);
        field(form, "Annual rate (%)", rate);
        field(form, "Duration (months)", months);
        field(form, "Purpose", purpose);
        field(form, "Guarantor (or None)", guarantor);
        form.add(button("Build agreement", () -> bank.createLoan(selectedId(), number(amount),
                number(rate), Integer.parseInt(months.getText().strip()), purpose.getText(), guarantor.getText())));
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(agreements), BorderLayout.CENTER);
        return panel;
    }

    private JPanel historyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JCheckBox enabled = new JCheckBox("Transaction history observer subscribed", true);
        enabled.addActionListener(event -> {
            bank.setHistoryEnabled(enabled.isSelected());
            message.setText(enabled.isSelected() ? "History observer registered" : "History observer removed; fraud monitoring remains active");
        });
        panel.add(enabled, BorderLayout.NORTH);
        panel.add(new JScrollPane(history), BorderLayout.CENTER);
        return panel;
    }

    private JPanel fraudPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel channels = row();
        JCheckBox email = new JCheckBox("Email (simulated)");
        JCheckBox sms = new JCheckBox("SMS (simulated)");
        channels.add(new JLabel("In-app notifications always enabled. Additional channels:"));
        channels.add(email);
        channels.add(sms);
        email.addActionListener(event -> bank.setNotificationChannels(email.isSelected(), sms.isSelected()));
        sms.addActionListener(event -> bank.setNotificationChannels(email.isSelected(), sms.isSelected()));
        panel.add(channels, BorderLayout.NORTH);
        panel.add(new JScrollPane(alerts), BorderLayout.CENTER);
        return panel;
    }

    private JButton button(String title, Runnable action) {
        JButton button = new JButton(title);
        button.addActionListener(event -> {
            try {
                action.run();
                refresh();
                message.setText(title + ": completed");
            } catch (IllegalArgumentException | IllegalStateException exception) {
                message.setText(exception.getMessage());
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Operation rejected", JOptionPane.WARNING_MESSAGE);
            }
        });
        return button;
    }

    private void refresh() {
        Account selected = (Account) accountBox.getSelectedItem();
        accountBox.removeAllItems();
        accountTable.setRowCount(0);
        for (Account account : bank.getAccounts()) {
            accountBox.addItem(account);
            accountTable.addRow(new Object[]{account.getId(), account.getOwner(), account.getType(),
                    account.getBalance()});
        }
        if (selected != null) {
            accountBox.setSelectedItem(selected);
        }
        history.setText("");
        for (var transaction : bank.getHistory()) {
            history.append(transaction + "\n");
        }
        alerts.setText("Fraud alerts:\n");
        for (String alert : bank.getAlerts()) {
            alerts.append(alert + "\n");
        }
        alerts.append("\nNotification deliveries:\n");
        for (String notification : bank.getNotifications()) {
            alerts.append(notification + "\n");
        }
        agreements.setText("");
        for (var agreement : bank.getAgreements()) {
            agreements.append(agreement + "\n\n");
        }
    }

    private String selectedId() {
        Account account = (Account) accountBox.getSelectedItem();
        if (account == null) {
            throw new IllegalArgumentException("Create and select an account first");
        }
        return account.getId();
    }

    private static BigDecimal number(JTextField field) {
        try {
            return new BigDecimal(field.getText().strip());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a valid number using a decimal point");
        }
    }

    private static JPanel row() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    private static JTextArea output() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }

    private static void field(JPanel panel, String name, java.awt.Component input) {
        panel.add(new JLabel(name));
        panel.add(input);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Banking System - Design Patterns Project");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BankingApp(new BankingFacade()));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
