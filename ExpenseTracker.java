package groupexpensetracker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseTracker implements Serializable {

    private static final String SAVE_FILE = "groups_data.dat";

    private HashMap<String, GroupData> allGroups; 
    private String currentGroupName;

    public ExpenseTracker() {
        this.allGroups = new HashMap<>();
        this.currentGroupName = null;
        loadFromDisk();
    }

    public void createOrSelectGroup(String name) {
        if (!allGroups.containsKey(name)) {
            allGroups.put(name, new GroupData(name));
        }
        this.currentGroupName = name;
        saveToDisk();
    }

    public GroupData getCurrentGroupData() {
        return currentGroupName == null ? null : allGroups.get(currentGroupName);
    }

    public List<String> getAllGroupNames() {
        return new ArrayList<>(allGroups.keySet());
    }

    public void addMember(String name) {
        GroupData group = getCurrentGroupData();
        if (group != null) group.getBalances().putIfAbsent(name, 0.0);
        saveToDisk();
    }

    public void removeMember(String name) {
        GroupData group = getCurrentGroupData();
        if (group != null) group.getBalances().remove(name);
        saveToDisk();
    }

    public void addExpense(String payer, double amount, String description, List<String> sharedAmong, String date) {
        GroupData group = getCurrentGroupData();
        if (group == null) return;
        double share = amount / sharedAmong.size();
        for (String person : sharedAmong) {
            if (person.equals(payer)) {
                group.getBalances().put(person, group.getBalances().getOrDefault(person, 0.0) + (amount - share));
            } else {
                group.getBalances().put(person, group.getBalances().getOrDefault(person, 0.0) - share);
            }
        }
        group.getExpenses().add(new Expense(payer, amount, description, sharedAmong, date));
        saveToDisk();
    }

    public ArrayList<Expense> getAllExpenses() {
        GroupData group = getCurrentGroupData();
        return (group == null) ? new ArrayList<>() : group.getExpenses();
    }

    public HashMap<String, Double> getBalances() {
        GroupData group = getCurrentGroupData();
        return (group == null) ? new HashMap<>() : group.getBalances();
    }

    public void saveToDisk() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(this.allGroups);
            out.writeObject(this.currentGroupName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromDisk() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            this.allGroups = (HashMap<String, GroupData>) in.readObject();
            this.currentGroupName = (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            this.allGroups = new HashMap<>();
            this.currentGroupName = null;
        }
    }

    public void addExpense(Expense expense) {
        throw new UnsupportedOperationException("Unimplemented method 'addExpense'");
    }

    public void viewBalances() {
        throw new UnsupportedOperationException("Unimplemented method 'viewBalances'");
    }

    public void settleExpenses() {
        throw new UnsupportedOperationException("Unimplemented method 'settleExpenses'");
    }

    public String getSettlementSummary() {
    Map<String, Double> balances = getBalances();
    List<String> debtors = new ArrayList<>();
    List<String> creditors = new ArrayList<>();
    Map<String, Double> owes = new HashMap<>();
    Map<String, Double> gets = new HashMap<>();

    for (Map.Entry<String, Double> entry : balances.entrySet()) {
        if (entry.getValue() < -0.01) owes.put(entry.getKey(), -entry.getValue());
        else if (entry.getValue() > 0.01) gets.put(entry.getKey(), entry.getValue());
    }
    StringBuilder sb = new StringBuilder();
    if (owes.isEmpty() && gets.isEmpty()) {
        sb.append("All balances are settled! 🎉");
        return sb.toString();
    }
    List<Map.Entry<String, Double>> owesList = new ArrayList<>(owes.entrySet());
    List<Map.Entry<String, Double>> getsList = new ArrayList<>(gets.entrySet());
    int i = 0, j = 0;
    while (i < owesList.size() && j < getsList.size()) {
        String debtor = owesList.get(i).getKey();
        double oweAmt = owesList.get(i).getValue();
        String creditor = getsList.get(j).getKey();
        double getAmt = getsList.get(j).getValue();
        double minAmt = Math.min(oweAmt, getAmt);

        sb.append(debtor).append(" pays Rs").append(String.format("%.2f", minAmt)).append(" to ").append(creditor).append("\n");

        owesList.get(i).setValue(oweAmt - minAmt);
        getsList.get(j).setValue(getAmt - minAmt);
        if (owesList.get(i).getValue() < 0.01) i++;
        if (getsList.get(j).getValue() < 0.01) j++;
    }
    return sb.toString();
    }
}