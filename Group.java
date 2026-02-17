package groupexpensetracker;
import java.io.Serializable;
import java.util.HashMap;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Double> balances;

    public Group() {
        balances = new HashMap<>();
    }

    public boolean addMember(String name) {
        if (!balances.containsKey(name)) {
            balances.put(name, 0.0);
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, Double> getBalances() {
        return balances;
    }

    public void setBalances(HashMap<String, Double> balances) {
        this.balances = balances;
    }

    public void updateBalance(String name, double amount) {
        balances.put(name, balances.getOrDefault(name, 0.0) + amount);
    }

    public void removeMember(String name) {
        balances.remove(name);
    }
}