package expensetracker;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PersonalExpenseTrackerUI extends JFrame {
    private PersonalExpenseTracker tracker;
    private final JFrame parentFrame;
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 34);
    private final Font menuFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);

    private final Color primaryColor = new Color(39, 174, 96);        // Green
    private final Color secondaryColor = new Color(41, 128, 185);     // Blue
    private final Color accentColor = new Color(241, 196, 15);        // Yellow
    private final Color panelBg = new Color(236, 240, 241);           // Light
    private final Color darkBg = new Color(44, 62, 80);               // Dark blue

    public PersonalExpenseTrackerUI(JFrame parent) {
        this.tracker = PersonalExpenseTracker.loadFromFile();
        this.parentFrame = parent;

        setTitle("Personal Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(menuFont);

        tabs.addTab("Home", getHomePanel());
        tabs.addTab("Expenses", getExpensesPanel());
        tabs.addTab("Income", getIncomePanel());
        tabs.addTab("Budgets", getBudgetsPanel());
        tabs.addTab("Categories", getCategoriesPanel());
        tabs.addTab("Streaks/Rewards", getStreaksPanel());
        tabs.addTab("History", getHistoryTab());
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
            parentFrame.setVisible(true);
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        backPanel.add(backBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(backPanel, BorderLayout.NORTH);
        main.add(tabs, BorderLayout.CENTER);

        setContentPane(new GradientBackgroundPanel());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(main, BorderLayout.CENTER);

        setVisible(true);
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
        JLabel title = new JLabel("Welcome to Personal Expense Tracker");
        title.setFont(headerFont);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("<html><center>Track, Manage, and Analyze Your Finances<br>with ease and style!</center></html>");
        subtitle.setFont(menuFont);
        subtitle.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(title, gbc);
        gbc.gridy++;
        panel.add(subtitle, gbc);

        return panel;
    }

    private JPanel getExpensesPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(16, 16));
        JLabel header = new JLabel("Add & View Expenses", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(primaryColor);
        panel.add(header, BorderLayout.NORTH);

        DefaultListModel<String> expenseModel = new DefaultListModel<>();
        for (Expense expense : tracker.getExpenses()) {
            expenseModel.addElement(expense.toString());
        }
        JList<String> expenseList = new JList<>(expenseModel);
        expenseList.setFont(labelFont);
        JScrollPane scroll = new JScrollPane(expenseList);

        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.setFont(buttonFont);
        addExpenseBtn.setBackground(primaryColor);
        addExpenseBtn.setForeground(Color.WHITE);

        addExpenseBtn.addActionListener(e -> {
            JTextField amountField = new JTextField(12);
            JDateChooser dateChooser = new JDateChooser();
            JTextField descField = new JTextField(18);
            ArrayList<String> categories = tracker.getCategoryManager().getCategories();
            JComboBox<String> categoryBox = new JComboBox<>(categories.toArray(new String[0]));

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(panelBg);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);

            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Amount:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(amountField, gbc);

            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(dateChooser, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(categoryBox, gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(descField, gbc);

            int res = JOptionPane.showConfirmDialog(panel, form, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
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
                    Date date = dateChooser.getDate();
                    if (date == null) throw new Exception("No date picked");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(date);
                    String cat = (String) categoryBox.getSelectedItem();
                    Expense expense = new Expense(amt, dateStr, cat, desc);
                    tracker.getExpenses().add(expense);
                    tracker.getBudgetManager().addExpense(cat, amt, dateStr);
                    tracker.saveToFile();
                    expenseModel.addElement(expense.toString());
                    JOptionPane.showMessageDialog(panel, "Expense added!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input: " + ex.getMessage());
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

    private JPanel getIncomePanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(16, 16));
        JLabel header = new JLabel("Add & View Income", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(secondaryColor);
        panel.add(header, BorderLayout.NORTH);

        DefaultListModel<String> incomeModel = new DefaultListModel<>();
        for (Income income : tracker.getIncomes()) {
            incomeModel.addElement(income.toString());
        }
        JList<String> incomeList = new JList<>(incomeModel);
        incomeList.setFont(labelFont);
        JScrollPane scroll = new JScrollPane(incomeList);

        JButton addIncomeBtn = new JButton("Add Income");
        addIncomeBtn.setFont(buttonFont);
        addIncomeBtn.setBackground(secondaryColor);
        addIncomeBtn.setForeground(Color.WHITE);

        addIncomeBtn.addActionListener(e -> {
            JTextField amountField = new JTextField(12);
            JDateChooser dateChooser = new JDateChooser();
            JTextField sourceField = new JTextField(18);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(panelBg);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);

            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Amount:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(amountField, gbc);

            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(dateChooser, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Source:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(sourceField, gbc);

            int res = JOptionPane.showConfirmDialog(panel, form, "Add Income", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String amtStr = amountField.getText().trim();
                    String src = sourceField.getText().trim();
                    if (amtStr.isEmpty() || src.isEmpty()) throw new Exception("Amount/Source empty");
                    float amt;
                    try {
                        amt = Float.parseFloat(amtStr);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(panel, "Amount must be a decimal number!", "Amount Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Date date = dateChooser.getDate();
                    if (date == null) throw new Exception("No date picked");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(date);
                    Income income = new Income(amt, dateStr, src);
                    tracker.getIncomes().add(income);
                    tracker.saveToFile();
                    incomeModel.addElement(income.toString());
                    JOptionPane.showMessageDialog(panel, "Income added!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input: " + ex.getMessage());
                }
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(addIncomeBtn);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getBudgetsPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(12, 12));
        JLabel header = new JLabel("Budgets", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(primaryColor);
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea(tracker.getBudgetManager().getMonthlyBudgetsSummary());
        area.setFont(labelFont);
        area.setEditable(false);
        area.setBackground(panelBg);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton setMonthlyBtn = new JButton("Set Monthly Budget");
        setMonthlyBtn.setFont(buttonFont);
        setMonthlyBtn.setBackground(primaryColor);
        setMonthlyBtn.setForeground(Color.WHITE);
        setMonthlyBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(panel, "Enter monthly budget:");
            if (amtStr != null) {
                try {
                    float amt = Float.parseFloat(amtStr);
                    tracker.getBudgetManager().setBudget(amt);
                    tracker.saveToFile();
                    area.setText(tracker.getBudgetManager().getMonthlyBudgetsSummary());
                    JOptionPane.showMessageDialog(panel, "Monthly budget set!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Amount must be a decimal number!", "Amount Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton setDailyBtn = new JButton("Set Daily Budget");
        setDailyBtn.setFont(buttonFont);
        setDailyBtn.setBackground(secondaryColor);
        setDailyBtn.setForeground(Color.WHITE);
        setDailyBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(panel, "Enter daily budget:");
            if (amtStr != null) {
                try {
                    float amt = Float.parseFloat(amtStr);
                    tracker.getBudgetManager().setDailyBudget(amt);
                    tracker.saveToFile();
                    JOptionPane.showMessageDialog(panel, "Daily budget set!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Amount must be a decimal number!", "Amount Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPanel.add(setMonthlyBtn);
        btnPanel.add(setDailyBtn);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getCategoriesPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(12, 12));
        JLabel header = new JLabel("Categories", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(secondaryColor);
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea("Categories: " + tracker.getCategoryManager().getCategories());
        area.setFont(labelFont);
        area.setEditable(false);
        area.setBackground(panelBg);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton viewBtn = new JButton("View Categories");
        viewBtn.setFont(buttonFont);
        viewBtn.setBackground(secondaryColor);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.addActionListener(e -> area.setText("Categories: " + tracker.getCategoryManager().getCategories()));

        JButton addBtn = new JButton("Add Category");
        addBtn.setFont(buttonFont);
        addBtn.setBackground(primaryColor);
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> {
            String cat = JOptionPane.showInputDialog(panel, "Enter new category name:");
            if (cat != null) {
                boolean added = tracker.getCategoryManager().addCategory(cat);
                tracker.saveToFile();
                area.setText("Categories: " + tracker.getCategoryManager().getCategories());
                JOptionPane.showMessageDialog(panel, added ? "Category added!" : "Category already exists!");
            }
        });

        btnPanel.add(viewBtn);
        btnPanel.add(addBtn);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getStreaksPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(12, 12));
        JLabel header = new JLabel("Streaks & Rewards", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(primaryColor);
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(labelFont);
        area.setEditable(false);
        area.setBackground(panelBg);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton streakBtn = new JButton("View Expense Streaks");
        streakBtn.setFont(buttonFont);
        streakBtn.setBackground(primaryColor);
        streakBtn.setForeground(Color.WHITE);
        streakBtn.addActionListener(e -> area.setText(tracker.getBudgetManager().getStreaksSummary()));

        JButton rewardsBtn = new JButton("View Rewards Points");
        rewardsBtn.setFont(buttonFont);
        rewardsBtn.setBackground(secondaryColor);
        rewardsBtn.setForeground(Color.WHITE);
        rewardsBtn.addActionListener(e -> area.setText(tracker.getBudgetManager().getRewardsPointsSummary()));

        JButton calcRewardsBtn = new JButton("Calculate Rewards");
        calcRewardsBtn.setFont(buttonFont);
        calcRewardsBtn.setBackground(accentColor);
        calcRewardsBtn.setForeground(Color.DARK_GRAY);
        calcRewardsBtn.addActionListener(e -> {
            String month = JOptionPane.showInputDialog(panel, "Enter month (YYYY-MM):");
            if (month != null) {
                String summary = tracker.getBudgetManager().calculateRewardsForMonthAndReturnSummary(month);
                tracker.saveToFile();
                area.setText(summary);
            }
        });

        btnPanel.add(streakBtn);
        btnPanel.add(rewardsBtn);
        btnPanel.add(calcRewardsBtn);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getHistoryTab() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new BorderLayout(12, 12));
        JLabel header = new JLabel("Expense & Income History", SwingConstants.CENTER);
        header.setFont(menuFont);
        header.setForeground(accentColor);
        panel.add(header, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setFont(labelFont);
        area.setEditable(false);
        area.setBackground(panelBg);

        JButton reloadBtn = new JButton("Reload/Show All");
        reloadBtn.setFont(buttonFont);
        reloadBtn.setBackground(accentColor);
        reloadBtn.setForeground(Color.DARK_GRAY);
        reloadBtn.addActionListener(e -> {
            PersonalExpenseTracker loaded = PersonalExpenseTracker.loadFromFile();
            StringBuilder sb = new StringBuilder();
            sb.append("Expenses:\n");
            for (Expense ex : loaded.getExpenses()) {
                sb.append(ex.toString()).append("\n");
            }
            sb.append("\nIncomes:\n");
            for (Income inObj : loaded.getIncomes()) {
                sb.append(inObj.toString()).append("\n");
            }
            area.setText(sb.toString());
        });

        reloadBtn.doClick(); 

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(reloadBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getAboutPanel() {
        JPanel panel = new RoundedPanel(30, panelBg);
        panel.setLayout(new GridBagLayout());
        JLabel about = new JLabel("<html><center>"
                + "<h2 style='color:#2980b9;'>Personal Expense Tracker</h2>"
                + "<p style='font-size:13pt;'>Track, manage, and analyze your personal finances with a modern & easy-to-use interface.<br>"
                + "<b>Features:</b>"
                + "<ul>"
                + "<li>Add/manage expenses & income</li>"
                + "<li>Set budgets</li>"
                + "<li>Manage categories</li>"
                + "<li>Track streaks & rewards</li>"
                + "<li>Persistent history tab</li>"
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
}