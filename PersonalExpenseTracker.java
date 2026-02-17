package expensetracker;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PersonalExpenseTracker implements Serializable {
    private ArrayList<Expense> expenses;
    private ArrayList<Income> incomes;
    private Category categoryManager;
    private Budget budgetManager;

    private static final String SAVE_FILE = "personal_data.dat";
    
    public PersonalExpenseTracker() {
        expenses = new ArrayList<>();
        incomes = new ArrayList<>();
        categoryManager = new Category();
        budgetManager = new Budget(0.0);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
    }

    public Category getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(Category categoryManager) {
        this.categoryManager = categoryManager;
    }

    public Budget getBudgetManager() {
        return budgetManager;
    }

    public void setBudgetManager(Budget budgetManager) {
        this.budgetManager = budgetManager;
    }

    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static PersonalExpenseTracker loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (PersonalExpenseTracker) in.readObject();
        } catch (Exception ex) {
            return new PersonalExpenseTracker();
        }
    }

}
