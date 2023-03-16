package Acount;

import Interface.IReport;
import Interface.ITransfer;
import Interface.ReportService;
import Interface.Withdraw;
import Model.Transaction;
import Model.TransactionDao;
import Model.TransactionType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//TK atm
public class SavingsAccount extends Account implements ReportService, ITransfer, Withdraw{
    //định nghĩa hạn mức tối đa cho loại tài khoản 5.000.000đ
    public static final double SAVINGS_ACCOUNT_MAX_BALANCE = 5000000;

    //định nghĩa hạn mức tối thiểu cho loại tài khoản 5.000.000đ
    public static final double SAVINGS_ACCOUNT_MIN_BALANCE = 50000;

    //bội số rút tiền
    public static final double WITHDRAW_MULTIPLIER = 10000;


    public DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");

    //số dư còn lại sau rút hoặc chuyển
    private double balanceAfterWithdraw;

    public SavingsAccount(String customerId, String accountNumber, double balance) {
        super(customerId, accountNumber, balance, "Savings");
    }


    //báo cáo cho Savings
    @Override
    public void log(double amount) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString  = df.format(date);
        System.out.println("+--------------------------------------+");
        System.out.printf("| %30s%n","     "+ getTitle() +"      |");
        System.out.printf("| NGAY G/D: %28s%n", dateString+" |") ;
        System.out.printf("| ATM ID: %30s%n", "DIGITAL-BANK-ATM 2022" +" |");
        System.out.printf("| SO TK: %31s%n", getAccountNumber() +" |");
        System.out.printf("| SO TIEN: %29s%n", dFormat.format(amount) + "đ" +" |");
        System.out.printf("| SO DU: %31s%n", dFormat.format(getBalance()) + "đ" +" |");
        System.out.printf("| PHI + VAT: %27s%n", (dFormat.format(getFee(amount))) +"đ" +" |");
        System.out.println("+--------------------------------------+");
    }
        //phương thức rút tiền
    @Override
    public boolean withdraw(double amount) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString  = df.format(date);
        TransactionType transactionType;
        Transaction newTran;
        List<Transaction> transactionList = TransactionDao.list();

        if(isAccepted(amount)) {
            balanceAfterWithdraw = this.getBalance() - amount - getFee(amount);
            setBalance(balanceAfterWithdraw);
            SavingsAccount account = new SavingsAccount(super.getCustomerId(),super.getAccountNumber(),super.getBalance());
            try {
                AccountDao.update(account);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //lưu vào transaction
            newTran = new Transaction(super.getCustomerId(), super.getAccountNumber(), dateString,amount, "WITHDRAW" );
            transactionList.add(newTran);
            try {
                TransactionDao.save(transactionList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Giao dịch rút tiền thành công.");
            //in ra hóa đơn rút tiền
            log(amount);
            return true;
        } else { //không thỏa mãn điều kiện rút tiền
            balanceAfterWithdraw = getBalance();
            System.out.println("Giao dịch rút tiền không thành công.");
            return false;
        }
    }

    //PHƯƠNG thức chuyển tiền
    @Override
    public boolean transfer(Account receiveAccount, double amount) {
        List<Transaction> transactionList = TransactionDao.list();
        Transaction newTran;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString  = df.format(date);
        double newReceiveAccountBalance = 0; //số tiền trong tài khoản nhận

        if(isAccepted(amount)) {
            //trừ tiền từ tài khoản chuyển tiền
            balanceAfterWithdraw = this.getBalance() - amount - getFee(amount);
            setBalance(balanceAfterWithdraw);
            SavingsAccount account = new SavingsAccount(super.getCustomerId(),super.getAccountNumber(),super.getBalance());
            try {
                AccountDao.update(account);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //thêm vào lịch sử
            newTran = new Transaction(super.getCustomerId(), super.getAccountNumber(), dateString,amount, "TRANSFERS" );
            transactionList.add(newTran);
            try {
                TransactionDao.save(transactionList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //coongj tiền vào tài khoản nhận
            newReceiveAccountBalance = receiveAccount.getBalance() + amount;
            receiveAccount.setBalance(newReceiveAccountBalance);
            try {
                AccountDao.update(receiveAccount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //thêm vào lịch sử
            Transaction newTran2 = new Transaction(receiveAccount.getCustomerId(), receiveAccount.getAccountNumber(), dateString,amount, "DEPOSIT" );
            transactionList.add(newTran2);
            try {
                TransactionDao.save(transactionList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Giao dịch chuyển tiền thành công.");

            //in ra hóa đơn giao dịch
//            log(amount);
            System.out.println("+--------------------------------------+");
            System.out.printf("| %30s%n","     "+ this.getTitle() +"      |");
            System.out.printf("| NGAY G/D: %28s%n", dateString+" |") ;
            System.out.printf("| ATM ID: %30s%n", "DIGITAL-BANK-ATM 2022" +" |");
            System.out.printf("| SO TK: %31s%n", getAccountNumber() +" |");
            System.out.printf("| SO TK NHẬN: %26s%n", receiveAccount.getAccountNumber() +" |");
            System.out.printf("| SO TIEN: %29s%n", dFormat.format(amount) + "đ" +" |");
            System.out.printf("| SO DU: %31s%n", dFormat.format(getBalance()) + "đ" +" |");
            System.out.printf("| PHI + VAT: %27s%n", (dFormat.format(getFee(amount))) +"đ" +" |");
            System.out.println("+--------------------------------------+");
            return true;
        } else {
            balanceAfterWithdraw = getBalance();
            System.out.println("Giao dịch chuyển tiền không thành công.");
            return false;
        }
    }

    //điều kiện để tút tiền
    @Override
    public boolean isAccepted(double amount) {
        //phí rút tiền từ tài khoản Saving = 0
        balanceAfterWithdraw = getBalance() - amount;
        if(amount >0 && amount <SAVINGS_ACCOUNT_MIN_BALANCE) {
            System.out.println("Số tiền rút phải lớn hơn 50000đ.");
            return false;
        } else if(!isAccountPremium() && amount > SAVINGS_ACCOUNT_MAX_BALANCE) {
            System.out.println("Tài khoản thường không được rút quá " + dFormat.format(SAVINGS_ACCOUNT_MAX_BALANCE) + "VNĐ");
            return false;
        } else if(balanceAfterWithdraw < SAVINGS_ACCOUNT_MIN_BALANCE) {
            System.out.println("Số dư còn lại trong tài khoản phải lớn hơn " +dFormat.format(SAVINGS_ACCOUNT_MIN_BALANCE) +"VNĐ");
            return false;
        } else if(amount % 10000 != 0) {
            System.out.println("Số tiền rút phải là bội số của " + dFormat.format(WITHDRAW_MULTIPLIER) +"VNĐ");
            return false;
        }
        return true;
    }

    //tài khoản saving, phí giao dịch cho mỗi loại tài khoản = 0
    public double getFee(double amount) {
            return 0* amount;
    }
    public String getTitle () {
        return "BIÊN LAI GIAO DỊCH SAVINGS";
    }

    @Override
    public String toString() {
        DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");
        return String.format("%8s | %-25s | %26s",getAccountNumber(), getType(), dFormat.format(getBalance()) );
    }
}
