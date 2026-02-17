package expensetracker;
import java.util.ArrayList;
import java.io.Serializable;

public class Category implements Serializable {
    private ArrayList<String> categories;

    public Category() {
        categories = new ArrayList<>();
        categories.add("Food");
        categories.add("Travel");
        categories.add("Utilities");
        categories.add("Rent");
        categories.add("Groceries");
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public boolean addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
            return true;
        } else {
            return false;
        }
    }
}
