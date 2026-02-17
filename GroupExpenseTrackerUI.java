package groupexpensetracker;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class GroupExpenseTrackerUI extends JFrame {
    private final ExpenseTracker tracker;
    private DefaultListModel<String> memberModel;
    private DefaultListModel<String> expenseModel;
    private DefaultListModel<String> historyModel;

    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 34);
    private final Font menuFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);

    private final Color primaryColor = new Color(39, 174, 96);        // Green
    private final Color secondaryColor = new Color(41, 128, 185);     // Blue
    private final Color accentColor = new Color(241, 196, 15);        // Yellow
    private final Color panelBg = new Color(236, 240, 241);           // Light
    private final Color darkBg = new Color(44, 62, 80);               // Dark blue

    public GroupExpenseTrackerUI(JFrame parent) {
        this.tracker = new ExpenseTracker();
        promptForGroup();

        setTitle("Group Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(menuFont);

        tabs.addTab("Home", getHomePanel());
        tabs.addTab("Members", getMembersPanel());
        tabs.addTab("Expenses", getExpensesPanel());
        tabs.addTab("Balances", getBalancesPanel());
        tabs.addTab("Settle Up", getSettlementPanel());
        tabs.addTab("History", getHistoryPanel());
        tabs.addTab("About", getAboutPanel());

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(buttonFont);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(accentColor);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            this.dispose();
            if (parent != null) parent.setVisible(true);
        });

        JButton switchGroupBtn = new JButton("Switch Group");
        switchGroupBtn.setFont(buttonFont);
        switchGroupBtn.setForeground(Color.WHITE);
        switchGroupBtn.setBackground(primaryColor.darker());
        switchGroupBtn.setFocusPainted(false);
        switchGroupBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        switchGroupBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchGroupBtn.addActionListener(e -> {
            promptForGroup();
            reloadAll();
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        backPanel.add(backBtn);
        backPanel.add(Box.createHorizontalStrut(10));
        backPanel.add(switchGroupBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(backPanel, BorderLayout.NORTH);
        main.add(tabs, BorderLayout.CENTER);

        setContentPane(new GradientBackgroundPanel());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(main, BorderLayout.CENTER);

        setVisible(true);
    }

    private void promptForGroup() {
        List<String> groupNames = tracker.getAllGroupNames();
        String[] namesArr = groupNames.toArray(new String[0]);
        JComboBox<String> groupCombo = new JComboBox<>(namesArr);
        JTextField newGroupField = new JTextField(16);

        Object[] message = {
            "Choose existing group:", groupCombo,
            "Or enter new group name:", newGroupField
        };
        int res = JOptionPane.showConfirmDialog(this, message, "Select/Create Group", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            String selected = (String) groupCombo.getSelectedItem();
            String newGroup = newGroupField.getText().trim();
            if (!newGroup.isEmpty()) {
                tracker.createOrSelectGroup(newGroup);
            } else if (selected != null) {
                tracker.createOrSelectGroup(selected);
            } else {
                JOptionPane.showMessageDialog(this, "No group selected! Exiting.");
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private JPanel getHomePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), secondaryColor));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Welcome to Group Expense Tracker");
        title.setFont(headerFont);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("<html><center>Organize, Split, and Settle Group Expenses<br>with ease and style!</center></html>");
        subtitle.setFont(menuFont);
        subtitle.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(title, gbc);
        gbc.gridy++;
        panel.add(subtitle, gbc);

        return panel;
    }

    private JPanel getMembersPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(16, 16));
        JLabel header = new JLabel("Manage Group Members", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(primaryColor);
        panel.add(header, BorderLayout.NORTH);

        memberModel = new DefaultListModel<>();
        tracker.getBalances().keySet().forEach(memberModel::addElement);

        JList<String> memberList = new JList<>(memberModel);
        memberList.setFont(labelFont);
        memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(memberList);

        JButton addBtn = new JButton("Add Member");
        addBtn.setFont(buttonFont);
        addBtn.setBackground(primaryColor);
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(panel, "Enter member name:", "Add Member", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                tracker.addMember(name.trim());
                if (!memberModel.contains(name.trim())) {
                    memberModel.addElement(name.trim());
                }
                JOptionPane.showMessageDialog(panel, name.trim() + " added to the group!", "Member", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton deleteBtn = new JButton("Delete Member");
        deleteBtn.setFont(buttonFont);
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> {
            String selected = memberList.getSelectedValue();
            if (selected != null) {
                tracker.removeMember(selected);
                memberModel.removeElement(selected);
                JOptionPane.showMessageDialog(panel, selected + " removed from group.");
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getExpensesPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(16, 16));
        JLabel header = new JLabel("Add & View Group Expenses", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(secondaryColor);
        panel.add(header, BorderLayout.NORTH);

        expenseModel = new DefaultListModel<>();
        for (Expense expense : tracker.getAllExpenses()) {
            expenseModel.addElement(formatExpense(expense));
        }
        JList<String> expenseList = new JList<>(expenseModel);
        expenseList.setFont(labelFont);
        JScrollPane scroll = new JScrollPane(expenseList);

        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.setFont(buttonFont);
        addExpenseBtn.setBackground(secondaryColor);
        addExpenseBtn.setForeground(Color.WHITE);

        addExpenseBtn.addActionListener(e -> {
            List<String> members = new ArrayList<>(tracker.getBalances().keySet());
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Add at least one member first!", "No Members", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> payerCombo = new JComboBox<>(members.toArray(new String[0]));
            payerCombo.setFont(labelFont);

            JTextField amountField = new JTextField(12);
            amountField.setFont(labelFont);

            JTextField descField = new JTextField(18);
            descField.setFont(labelFont);

            JList<String> peopleList = new JList<>(members.toArray(new String[0]));
            peopleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            peopleList.setFont(labelFont);
            JScrollPane peopleScroll = new JScrollPane(peopleList);
            peopleScroll.setPreferredSize(new Dimension(220, 85));

            JDateChooser dateChooser = new JDateChooser();
            dateChooser.setFont(labelFont);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(panelBg);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);

            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
            form.add(new JLabel("Payer:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(payerCombo, gbc);

            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Amount:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(amountField, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(descField, gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("People sharing:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(peopleScroll, gbc);

            gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(dateChooser, gbc);

            int res = JOptionPane.showConfirmDialog(panel, form, "Add Group Expense", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String payer = (String) payerCombo.getSelectedItem();
                    String amtStr = amountField.getText().trim();
                    String desc = descField.getText().trim();
                    if (amtStr.isEmpty() || desc.isEmpty()) throw new Exception("Amount/Description empty");
                    float amt;
                    try {
                        amt = Float.parseFloat(amtStr);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(panel, "Amount must be a decimal number!", "Amount Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    List<String> shared = peopleList.getSelectedValuesList();
                    if (!shared.contains(payer)) shared.add(payer); // Ensure payer is included
                    if (shared.isEmpty()) throw new Exception("No people selected");
                    Date dateObj = dateChooser.getDate();
                    if (dateObj == null) throw new Exception("No date picked");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(dateObj);

                    tracker.addExpense(payer, amt, desc, shared, date);

                    expenseModel.addElement(payer + " paid Rs" + amt + " for " + desc + " [" + String.join(", ", shared) + "] on " + date);
                    historyModel.addElement(payer + " paid Rs" + amt + " for " + desc + " [" + String.join(", ", shared) + "] on " + date);
                    JOptionPane.showMessageDialog(panel, "Expense added: " + desc + " (Rs" + amt + ")", "Expense Added", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(addExpenseBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getBalancesPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(12, 12));
        JLabel header = new JLabel("Group Balances", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(primaryColor);
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(labelFont);
        area.setEditable(false);
        area.setBackground(panelBg);

        JButton refreshBtn = new JButton("Refresh Balances");
        refreshBtn.setFont(buttonFont);
        refreshBtn.setBackground(primaryColor);
        refreshBtn.setForeground(Color.WHITE);

        refreshBtn.addActionListener(e -> updateBalancesText(area));
        updateBalancesText(area);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void updateBalancesText(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        for (var entry : tracker.getBalances().entrySet()) {
            sb.append(entry.getKey()).append(": Rs ").append(String.format("%.2f", entry.getValue())).append("\n");
        }
        area.setText(sb.toString());
    }

    private JPanel getSettlementPanel() {
    JPanel panel = new RoundedPanel(30, panelBg);
    panel.setLayout(new BorderLayout(12, 12));
    JLabel header = new JLabel("Settle Group Expenses", SwingConstants.CENTER);
    header.setFont(menuFont);
    header.setForeground(secondaryColor);
    panel.add(header, BorderLayout.NORTH);

    JTextArea area = new JTextArea(tracker.getSettlementSummary());
    area.setFont(labelFont);
    area.setEditable(false);
    area.setBackground(panelBg);

    JButton refreshBtn = new JButton("Show Settlements");
    refreshBtn.setFont(buttonFont);
    refreshBtn.setBackground(secondaryColor);
    refreshBtn.setForeground(Color.WHITE);
    refreshBtn.addActionListener(e -> area.setText(tracker.getSettlementSummary()));

    JPanel btnPanel = new JPanel();
    btnPanel.setOpaque(false);
    btnPanel.add(refreshBtn);

    panel.add(new JScrollPane(area), BorderLayout.CENTER);
    panel.add(btnPanel, BorderLayout.SOUTH);
    return panel;
}

    private JPanel getHistoryPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(16, 16));
        JLabel header = new JLabel("Expense History", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(accentColor);
        panel.add(header, BorderLayout.NORTH);

        historyModel = new DefaultListModel<>();
        for (Expense expense : tracker.getAllExpenses()) {
            historyModel.addElement(formatExpense(expense));
        }
        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(labelFont);
        JScrollPane scroll = new JScrollPane(historyList);

        JButton reloadBtn = new JButton("Reload/Refresh");
        reloadBtn.setFont(buttonFont);
        reloadBtn.setBackground(accentColor);
        reloadBtn.setForeground(Color.DARK_GRAY);
        reloadBtn.addActionListener(e -> {
            historyModel.clear();
            for (Expense expense : tracker.getAllExpenses()) {
                historyModel.addElement(formatExpense(expense));
            }
            JOptionPane.showMessageDialog(panel, "History reloaded!");
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(reloadBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private String formatExpense(Expense expense) {
        return expense.getPayer() + " paid Rs" + expense.getAmount()
                + " for " + expense.getDescription()
                + " [" + String.join(", ", expense.getSharedAmong()) + "]"
                + " on " + expense.getDate();
    }

    private JPanel getAboutPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new GridBagLayout());
        JLabel about = new JLabel("<html><center>"
                + "<h2 style='color:#2980b9;'>Group Expense Tracker</h2>"
                + "<p style='font-size:13pt;'>Track, split, and settle group expenses with a modern & easy-to-use interface.<br>"
                + "<b>Features:</b>"
                + "<ul>"
                + "<li>Add/manage members</li>"
                + "<li>Add/group expenses</li>"
                + "<li>See balances</li>"
                + "<li>Automatic settlements</li>"
                + "</ul>"
                + "<br><span style='color:#888'>By Javengers</span>"
                + "</p></center></html>");
        about.setFont(menuFont);
        about.setForeground(darkBg);

        panel.add(about);
        return panel;
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;
        public RoundedPanel(int radius, Color bg) {
            super();
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    static class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(39, 174, 96), getWidth(), getHeight(), new Color(41, 128, 185)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    private void reloadAll() {
        if (memberModel != null) {
            memberModel.clear();
            tracker.getBalances().keySet().forEach(memberModel::addElement);
        }
        if (expenseModel != null) {
            expenseModel.clear();
            for (Expense expense : tracker.getAllExpenses()) {
                expenseModel.addElement(formatExpense(expense));
            }
        }
        if (historyModel != null) {
            historyModel.clear();
            for (Expense expense : tracker.getAllExpenses()) {
                historyModel.addElement(formatExpense(expense));
            }
        }
    }
}