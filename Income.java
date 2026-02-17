package expensetracker;
import java.io.Serializable;
public class Income implements Serializable {
    private double amount;
    private String date;
    private String source;

    public Income(double amount, String date, String source) {
        this.amount = amount;
        this.date = date;
        this.source = source;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Amount: " + amount + ", Source: " + source;
    }
}
