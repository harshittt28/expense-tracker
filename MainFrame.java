import javax.swing.*;
import java.awt.*;
import expensetracker.PersonalExpenseTrackerUI;
import groupexpensetracker.GroupExpenseTrackerUI;

public class MainFrame extends JFrame {
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 36);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 22);

    public MainFrame() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        setContentPane(new GradientBackgroundPanel());
        getContentPane().setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(32, new Color(255, 255, 255, 235));
        card.setLayout(new BorderLayout(20, 20));
        card.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));

        JLabel label = new JLabel("Expense Tracker", SwingConstants.CENTER);
        label.setFont(titleFont);
        label.setForeground(new Color(41, 128, 185));
        card.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 38, 10));
        buttonPanel.setOpaque(false);

        JButton personalBtn = new JButton("Personal Expense Tracker");
        JButton groupBtn = new JButton("Group Expense Tracker");
        for (JButton btn : new JButton[]{personalBtn, groupBtn}) {
            btn.setFont(buttonFont);
            btn.setBackground(new Color(39, 174, 96));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(41, 128, 185)); }
                public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(39, 174, 96)); }
            });
        }

        personalBtn.addActionListener(e -> {
            new expensetracker.PersonalExpenseTrackerUI(this);
            setVisible(false);
        });
        groupBtn.addActionListener(e -> {
            new groupexpensetracker.GroupExpenseTrackerUI(this);
            setVisible(false);
        });

        buttonPanel.add(personalBtn);
        buttonPanel.add(groupBtn);
        card.add(buttonPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel("By Javengers", SwingConstants.RIGHT);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        footer.setForeground(new Color(127, 140, 141));
        card.add(footer, BorderLayout.SOUTH);

        getContentPane().add(card, new GridBagConstraints());
        setVisible(true);
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
            g2.setPaint(new GradientPaint(0, 0, new Color(41, 128, 185), getWidth(), getHeight(), new Color(39, 174, 96)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
}
