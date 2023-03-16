package Customer;

import Acount.Account;
//import Acount.LoanAccount;
import Acount.SavingsAccount;

import java.text.DecimalFormat;

public class DigitalCustomer extends Customer{
    public DigitalCustomer(String name, String customerId) {
        super(name, customerId);
    }


    //kiem tra neu accountNumber co ton tai thi truy xuat doi tuong do
    public boolean withdraw(String accountNumber, double amount) {
        for(Account _account: getAccounts()) {
            if(_account.getAccountNumber() != accountNumber ) {
                System.out.println("Không tồn tại khách hàng có STK: "+ accountNumber);
                return false;
            } else {
                    if (_account.getType().equals(Account.ACCOUNT_TYPE_SAVINGS)) {
                SavingsAccount savAcc = (SavingsAccount) _account;
                return savAcc.withdraw(amount);
            }
        }
    }
        return true;
    }

    @Override
    public void displayInformation() {
        super.displayInformation();
        String isPrimiumNormal = " ";
        if(isPremium()) {
            isPrimiumNormal += "Premium";
        } else {
            isPrimiumNormal += "Normal";
        }
        DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");
        System.out.printf("%-15s | %-15s | %-10s | %20s \n", getCustomerId(), getName(), isPrimiumNormal, dFormat.format(getTotalAccountBalance()) + " VNĐ");
        int accNum = 0;
        for(Account _a: getAccounts()) {
            accNum ++;
            System.out.printf("%-5s %-55s \n",accNum ,_a.toString() +" VNĐ" );
        }
    }
}
