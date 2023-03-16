package Customer;

import Acount.Account;
import Acount.AccountDao;
import Acount.SavingsAccount;
import Bank.DigitalBank;
import Model.Transaction;
import Model.TransactionDao;
import org.w3c.dom.ls.LSOutput;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

//thoong tin khach hang
public class Customer extends User implements Serializable {
    public static final long serialVersionUID = 1L;

    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String TRANSFERS = "TRANSFERS";

    public DecimalFormat dFormat = new DecimalFormat("####,###,###,###,###");


    private List<Account> accounts = new ArrayList<>();
    private static final DigitalBank digitalBank = new DigitalBank();


    private List<Account> accountList = AccountDao.list();

    public Customer(String customerId, String name) {
        super(customerId, name);
        this.accounts = new ArrayList<Account>();

    }

    //List<String> -> List<Customer>
    public Customer(List<String> values) {
        this(values.get(0), values.get(1));
    }


    //lấy ra những account có customerId bằng customerId hiện tại
    public List<Account> getAccounts() {
        List<Account> listAccount = AccountDao.list().stream()
                .filter(item -> item.getCustomerId().equals(this.getCustomerId()))
                .collect(Collectors.toList());
        return listAccount;
    }

    public Customer() {
    }


    public boolean isPremium() {
        int i = 0;
        for (Account _a : accountList) {
            if (_a.isAccountPremium()) {
                return true;
            }
        }
        return false;
    }

    //add account
    public void addAccount(Scanner scanner) {
        Account newAcc = new Account();
        newAcc = newAcc.input(scanner);

    }

    public Account getAccountByAccountNumber(List<Account> accounts, String accountNumber) {
        for (Account item : accounts) {
            if (item.getAccountNumber().equals(accountNumber)) {
                return item;
            }
        }
        return null; //khong co tai khoan
    }

    public void displayTransactionInformation() {
        List<Transaction> transactionList = TransactionDao.list().stream()
                .filter(transaction -> transaction.getCustomerId().equals(this.getCustomerId()))
                .collect(Collectors.toList());
        if(transactionList.size()== 0) {
            System.out.println("Tài khoản chưa thực hiện giao dịch.");
        } else {
            for (Transaction a : transactionList) {
                System.out.printf(a.toString());
            }
        }
    }

    public double getTotalAccountBalance() {
        List<Account> accList = AccountDao.list().stream()
                .filter(account -> account.getCustomerId().equals(this.getCustomerId()))
                .collect(Collectors.toList());
        int total = 0;
        for (Account account : accList) {
            total += account.getBalance();
        }
        return total;
    }

    public void displayInformation() {
        String isPremium = " ";
        if (getTotalAccountBalance() > 10000000) {
            isPremium += "Premium";
        } else {
            isPremium += "Normal ";
        }        System.out.println("*---------------------------------------------------------------------*");
        System.out.println("* " + getCustomerId() + " | " + String.format("%-25s", getName()) + " | " + String.format("%7s", isPremium) + " | " + String.format("%15s", String.format("%,d", (long) getTotalAccountBalance())) + "đ");
        List<Account> accList = AccountDao.list().stream()
                .filter(account -> account.getCustomerId().equals(this.getCustomerId()))
                .collect(Collectors.toList());
        int i = 1;
        for (Account a : accList) {
            System.out.printf("* %s.  %60sđ\n", (i), a.toString());
            i++;
        }
    }

    public void withdraw(Scanner scanner) {
        List<Account> accounts = getAccounts();
        if (!accounts.isEmpty()) {
            Account account;
            String amount;
            String accountNumber;
            do {
                System.out.println("Nhập số tài khoản dùng để rút tiền: ");
                accountNumber = scanner.nextLine();
                account = getAccountByAccountNumber(accounts, accountNumber);
            } while (account == null);

            do {
                System.out.println("Nhập số tiền rút: ");
                amount = scanner.nextLine();
            } while (!digitalBank.isNumeric(amount) || Long.parseLong(amount) <= 0);

            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).withdraw(Long.parseLong(amount));
            }
        } else {
            System.out.println("Khách hàng không có tài khoản nào, thao tác không thành công.");
        }
    }

    public void transfers(Scanner scanner) {
        List<Account> accounts =getAccounts();
        if (!accounts.isEmpty()) {
            Account accountTransferFrom;
            Account accountTransferTo;

            String amount;
            do {
                System.out.println("Nhập số tài khoản dùng để chuyển tiền: ");
                accountTransferFrom = getAccountByAccountNumber(accounts, scanner.nextLine());
            } while (accountTransferFrom == null);

            List<Account> accountList = AccountDao.list();
            String accountNumberTransferTo;
            do {
                System.out.println("Nhập số tài khoản nhận tiền: ");
                accountNumberTransferTo = scanner.nextLine();
                accountTransferTo = getAccountByAccountNumber(accountList,accountNumberTransferTo );
            } while (accountTransferTo == null);
            //hiển thị thông tin tài khoản nhận tiền
            Customer customerTransferTo = digitalBank.getCustomerById(CustomerDao.list(),accountTransferTo.getCustomerId());
            System.out.println("Gửi đến số tài khoản: " + accountTransferTo.getAccountNumber() +" | " + customerTransferTo.getName());
            //thêm 1 điều kiện là 2 tài khoản nhập vào phải khác nhau

            do {
                System.out.println("Nhập số tiền muốn chuyển: ");
                amount =scanner.nextLine();
            } while (!digitalBank.isNumeric(amount) || Long.parseLong(amount) <= 0);
            do{
                System.out.println("Xác nhận thực hiện chuyển " + Long.parseLong(amount) +"đ " +"từ tài khoản " +"["+accountTransferFrom.getAccountNumber() +"]" + " đến tài khoản " +"[" +accountNumberTransferTo + "]" + ". Yes(Y)/ No(N):");
                String op =scanner.nextLine();
                if(op.toUpperCase().equals("Y")) {
                    if (accountTransferFrom instanceof SavingsAccount) {
                        ((SavingsAccount) accountTransferFrom).transfer(accountTransferTo, Long.parseLong(amount));
//                        System.out.println("Thao tác chuyển tiền thành công.");
                        break;
                    } else {
                        System.out.println("Thao tác chuyển tiền thất bại.");
                        break;
                    }
                } else if(op.equals("N")) {
                    System.out.println("Đã hủy thao tác chuyển tiền.");
                    break;
                } else {
                    System.out.println("Vui lòng chọn Y/N để thao tác chuyển tiền:");
                }
            } while (true);

        } else {
            System.out.println("Khách hàng không có tài khoản nào, thao tác không thành công.");
        }
    }
}
