package kz.aitu.bank;

import java.awt.Component;
import java.awt.Container;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import kz.aitu.bank.facade.BankingFacade;
import kz.aitu.bank.ui.BankingApp;

public class BankingUiTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            BankingFacade bank = new BankingFacade();
            BankingApp app = new BankingApp(bank);
            JTabbedPane tabs = find(app, JTabbedPane.class);
            Container accounts = (Container) tabs.getComponentAt(0);
            find(accounts, JTextField.class).setText("Test Student");
            click(accounts, "Open account");
            if (bank.getAccounts().size() != 1) {
                throw new AssertionError("Account creation failed");
            }
            click(accounts, "Deposit");
            if (bank.getAccounts().get(0).getBalance().compareTo(new BigDecimal("100")) != 0) {
                throw new AssertionError("Deposit failed");
            }
            Container interest = (Container) tabs.getComponentAt(1);
            find(interest, JComboBox.class).setSelectedItem("Loan");
            click(interest, "Calculate");
            if (!find(interest, JTextArea.class).getText().contains("1200.00")) {
                throw new AssertionError("Interest display failed");
            }
            click((Container) tabs.getComponentAt(2), "Build agreement");
            if (bank.getAgreements().size() != 1) {
                throw new AssertionError("Loan creation failed");
            }
            Container fraud = (Container) tabs.getComponentAt(4);
            find(fraud, JCheckBox.class).doClick();
            bank.deposit("ACC-1", new BigDecimal("12000"));
            JTextField amount = findAmount(accounts);
            amount.setText("6000");
            click(accounts, "Withdraw");
            if (bank.getNotifications().size() != 2
                    || !find(fraud, JTextArea.class).getText().contains("EMAIL (simulated):")) {
                throw new AssertionError("Decorated notification display failed");
            }
            amount.setText("100");
            click(accounts, "Withdraw");
            if (bank.getAccounts().get(0).getBalance().compareTo(new BigDecimal("6000")) != 0) {
                throw new AssertionError("Subsequent withdrawal failed");
            }
            System.out.println("Swing UI: account, deposit, calculation, loan and decorated fraud notifications passed");
        });
    }

    private static JTextField findAmount(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JTextField field && field.getText().equals("100.00")) {
                return field;
            }
            if (component instanceof Container child) {
                JTextField found = findAmount(child);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static void click(Container parent, String text) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JButton button && button.getText().equals(text)) {
                button.doClick();
                return;
            }
            if (component instanceof Container child && hasButton(child, text)) {
                click(child, text);
                return;
            }
        }
        throw new AssertionError("Button not found: " + text);
    }

    private static boolean hasButton(Container parent, String text) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JButton button && button.getText().equals(text)) {
                return true;
            }
            if (component instanceof Container child && hasButton(child, text)) {
                return true;
            }
        }
        return false;
    }

    private static <T> T find(Container parent, Class<T> type) {
        for (Component component : parent.getComponents()) {
            if (type.isInstance(component)) {
                return type.cast(component);
            }
            if (component instanceof Container child) {
                T found = find(child, type);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
