package expensetracker;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

public class Budget implements Serializable {
    private double monthlyBudget;
    private double currentExpenses;
    private HashMap<String, Double> categoryExpenses;
    private HashMap<String, Double> dateWiseExpenses;
    private HashMap<String, Double> monthlyExpenses;
    private int rewardsPoints;

    private double dailyBudget;
    private int currentStreak;
    private int bestStreak;

    public Budget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
        this.currentExpenses = 0.0;
        this.categoryExpenses = new HashMap<>();
        this.dateWiseExpenses = new HashMap<>();
        this.monthlyExpenses = new HashMap<>();
        this.rewardsPoints = 0;
        this.dailyBudget = 0.0;
        this.currentStreak = 0;
        this.bestStreak = 0;
    }

    public void setBudget(double amount) {
        this.monthlyBudget = amount;
    }

    public void setDailyBudget(double amount) {
        this.dailyBudget = amount;
    }

    public void addExpense(String category, double amount, String date) {
        currentExpenses += amount;
        categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0.0) + amount);
        dateWiseExpenses.put(date, dateWiseExpenses.getOrDefault(date, 0.0) + amount);

        String month = date.substring(0, 7);
        monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + amount);
        updateStreak();
    }

    public String getMonthlyBudgetsSummary() {
        StringBuilder sb = new StringBuilder("===== Monthly Budget Comparison =====\n");
        for (Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
            String month = entry.getKey();
            double spent = entry.getValue();
            sb.append(month).append(": Spent Rs").append(spent).append(" / Budget: Rs").append(monthlyBudget).append("\n");
        }
        return sb.toString();
    }
    public String getDateWiseExpensesSummary(String date) {
        double spent = dateWiseExpenses.getOrDefault(date, 0.0);
        return "===== Expenses for " + date + " =====\nTotal Spent: Rs" + spent;
    }

    public String calculateRewardsForMonthAndReturnSummary(String month) {
        double spent = monthlyExpenses.getOrDefault(month, 0.0);
        if (spent <= monthlyBudget) {
            rewardsPoints += 10;
            return "Congratulations! You stayed under budget for " + month + ". Rewards Points Earned: 10";
        } else {
            return "No rewards for " + month + ". You exceeded the budget.";
        }
    }

    public String getRewardsPointsSummary() {
        return "===== Rewards Points =====\nTotal Rewards Points: " + rewardsPoints;
    }

    private void updateStreak() {
        if (dailyBudget <= 0.0) {
            return;
        }
        ArrayList<String> sortedDates = new ArrayList<>(dateWiseExpenses.keySet());
        Collections.sort(sortedDates);
        int streak = 0;
        int maxStreak = 0;
        for (String date : sortedDates) {
            double spent = dateWiseExpenses.get(date);
            if (spent <= dailyBudget) {
                streak++;
                if (streak > maxStreak) maxStreak = streak;
            } else {
                streak = 0;
            }
        }
        this.currentStreak = streak;
        this.bestStreak = maxStreak;
    }

    public String getStreaksSummary() {
        if (dailyBudget <= 0.0) {
            return "Set your daily budget first to use the Expense Streak Tracker.";
        }
        return "===== Expense Streak Tracker =====\nDaily Budget: Rs" + dailyBudget +
                "\nCurrent Streak: " + currentStreak + " day(s) in a row" +
                "\nBest Streak: " + bestStreak + " day(s)";
    }

    public void viewDateWiseExpenses(String date) {
        throw new UnsupportedOperationException("Unimplemented method 'viewDateWiseExpenses'");
    }

    public void viewStreaks() {
        throw new UnsupportedOperationException("Unimplemented method 'viewStreaks'");
    }

    public void viewRewardsPoints() {
        throw new UnsupportedOperationException("Unimplemented method 'viewRewardsPoints'");
    }

    public void compareMonthlyBudgets() {
        throw new UnsupportedOperationException("Unimplemented method 'compareMonthlyBudgets'");
    }
}
