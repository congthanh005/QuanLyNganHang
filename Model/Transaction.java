package Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Transaction implements Serializable {



    private static final long serialVersionUID =1L;
    private String type; //DEPOSIT/ WITHDRAW/ TRANSFERS
    private String accountNumber;
    private String customerId;
    private double amount;
    private String time;

    public Transaction(String customerId, String accountNumber, String time, double amount, String type) {
        this.customerId =customerId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.time = time;
    }



    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");
        return String.format("%-7s %-6s | %-10s |%1s%-13s| %25s \n","[GD]", getAccountNumber(),getType(),
                getType().equals("DEPOSIT") ? "" :"-" ,dFormat.format((long)getAmount()) +"đ", getTime());
    }

        //tạo chuỗi random a-z A-Z 0-9
    static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {
                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int)(AlphaNumericString.length()
                        * Math.random());
                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }
            return sb.toString();
    }
}
