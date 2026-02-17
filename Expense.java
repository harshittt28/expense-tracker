package groupexpensetracker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Expense implements Serializable {
    private String payer;
    private double amount;
    private String description;
    private List<String> sharedAmong;
    private String date; 

    public Expense(String payer, double amount, String description, List<String> sharedAmong, String date) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
        this.sharedAmong = sharedAmong;
        this.date = date;
    }

    public Expense(String payer2, double amount2, String description2, ArrayList<String> sharedAmong2) {
        //TODO Auto-generated constructor stub
    }

    public String getPayer() { return payer; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public List<String> getSharedAmong() { return sharedAmong; }
    public String getDate() { return date; }
}