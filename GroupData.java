package groupexpensetracker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String groupName;
    private HashMap<String, Double> balances;
    private ArrayList<Expense> expenses;

    public GroupData(String groupName) {
        this.groupName = groupName;
        this.balances = new HashMap<>();
        this.expenses = new ArrayList<>();
    }

    public String getGroupName() { return groupName; }
    public HashMap<String, Double> getBalances() { return balances; }
    public ArrayList<Expense> getExpenses() { return expenses; }
}