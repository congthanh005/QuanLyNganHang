package Acount;

import Interface.Withdraw;
import Model.BinaryFileService;
import Model.Transaction;
import Model.TransactionDao;
import Model.TransactionType;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

//thong tin tai khoan khach hang
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private double balance;

    private String type;
    private String customerId;

    public static String ACCOUNT_TYPE_SAVINGS = "SAVINGS";

    public Account(String customerId, String accountNumber, double balance, String type) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
    }
    public Account(String accountNumber, double balance, String type) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
    }

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    //laasy ra toan bo giao dich
    public List<Transaction> getTransactionList() {
        List<Transaction> transactionList = TransactionDao.list();
        return transactionList;
    }

    public Transaction getTransactions() {
        List<Transaction> accTran = TransactionDao.list().stream()
                .filter(transaction -> transaction.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
        if (accTran.size() != 0) {
            for (Transaction a : accTran) {
                return a;
            }
        }
        return null;
    }

    public void getCustomer() {
        return;
    }

    public void displayTransactionsList() {
        if (getTransactions()!= null) {
            getTransactions().toString();
        }
    }

    public double getFee(double amount) {
        return 0* amount;
    }

    public void createTransaction(double amount, String time, String type) {
        if(type.equals("WITHDRAW")) { //rut tien
            Transaction newTran = new Transaction(getCustomerId(),getAccountNumber(),time, amount,type);
            addTransaction(newTran);
        } else if(type.equals("TRANSFERS")) {
            Transaction newTran = new Transaction(getCustomerId(),getAccountNumber(),time, amount,type);
            addTransaction(newTran);
        } else if(type.equals("DEPOSIT")) {
            Transaction newTran = new Transaction(getCustomerId(),getAccountNumber(),time, amount,type);
            addTransaction(newTran);
    }
    }

    //thêm tài khoản vào danh sách
    public Account input(Scanner scanner) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString  = df.format(date);
        System.out.println("Nhập số tài khoản muốn thêm có 6 chữ số: ");
        String newAccountNumber = scanner.nextLine();
        while (true) {
            if(newAccountNumber == getAccountNumber()) {
                System.out.println("STK vừa nhập đã tồn tại. Vui lòng nhập lại: ");
                newAccountNumber= scanner.nextLine();
            } else {
                break;
            }
        }
        System.out.println("Nhập số tiền ban đầu: ");
        double newBalance = scanner.nextDouble();
        scanner.nextLine();
        while (true) {
            if(newBalance < 0) {
                System.out.println("Số tiền nhập vào phải > 50.000VNĐ. Nhập lại: ");
                newBalance = scanner.nextDouble();
                scanner.nextLine();
            } else {
                break;
            }
        }
        System.out.println("Đã thêm "+ newBalance +" vào số tài khoản " + newAccountNumber );
        //thêm số tiền DEPOSIT
//        createTransaction(newBalance, dateString, true, new TransactionType("DEPOSIT") );
        return new Account(getCustomerId(), getAccountNumber(), getBalance(), getType() );
    }
    public void addTransaction(Transaction transaction) {
        getTransactionList().add(transaction);
        try {
            TransactionDao.save(getTransactionList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getType() {
        return "SAVINGS";
    }

    public void setType(String type) {
        this.type = type;
    }


    public Account() {
    }

    public static boolean validateAccount(String strNum) {
        //Kiểm tra xem tai khoan account có chính xác là dãy chữ số cos 6 chữ số và ko phải số âm
        //sử dụng regex
        if (strNum == null || strNum.matches("-?\\d+(\\.\\d+)?") == false) {
            return false;
        }
        if (strNum.length() != 6) {
            return false;
        }
        if (strNum.contains("-")) {
            return false;
        }
        return true;
    }

    public void setAccountNumber(String accountNumber) {
        //tk 6 so
        if (validateAccount(accountNumber)) {
            this.accountNumber = accountNumber;
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isAccountPremium() {
        if (this.balance >= 10000000) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");
        return String.format("%3s| %25s |  %35s", getAccountNumber(), getType(), dFormat.format(balance));
    }

    public void display() {
        System.out.println(toString());
    }

    public void displayTransactions() {
        if (getTransactionList() != null) {
            //kiểm tra account này có transaction hay không, duyệt từng account và hiển thị thông tin dùng toString của class Account
            ArrayList<Transaction> transactions = new ArrayList<>(getTransactionList());
            for (Transaction transaction : transactions) {
                System.out.print(String.format(transaction.toString()));
            }
        } else {
            System.out.println("Tài khoản chưa thực hiện giao dịch.");
        }
    }
}

