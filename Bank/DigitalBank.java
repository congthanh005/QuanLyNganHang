package Bank;

import Acount.Account;
import Acount.AccountDao;
import Acount.SavingsAccount;
import Customer.Customer;
import Customer.User;
import Customer.CustomerDao;
import Customer.DigitalCustomer;
import Model.BinaryFileService;
import Model.TextFileService;
import Model.Transaction;
import Model.TransactionDao;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static Acount.Account.ACCOUNT_TYPE_SAVINGS;

public class DigitalBank extends Bank implements Serializable {

    // rỗng
    public DigitalBank() {
    }
    //lấy danh sách và hiển thị
    public void showCustomers() {
        List<Customer> customerList = CustomerDao.list();
        if(customerList.size() != 0){
            for(Customer cus : customerList){
                cus.displayInformation();
            }
        }else{
            System.out.println("Chưa có khách hàng nào trong danh sách!");
        }
    }

    //đọc file text và thêm vaof file dat
    public void addCustomers(String fileName) {
        //đọc file text
        List<List<String>> listCus = TextFileService.readFile(fileName);
        List<Customer> toList = new ArrayList<>();
        for (List<String> cus : listCus) {
            Customer customer = new Customer(cus);
            toList.add(customer);
        }
        //đọc trong .dat
        List<Customer> customerList = CustomerDao.list();
        for (Customer c : toList) {//duyệt trong text
            boolean pass = customerList.stream().anyMatch(cus -> cus.getCustomerId().equals(c.getCustomerId()));
            if (!User.validateCustomerId(c.getCustomerId())) {
                System.out.println("Mã khách hàng " + c.getCustomerId() + " không hợp lệ.");
            } else if (pass) {
                System.out.println("Đã tồn tại: " + c.getCustomerId());
            } else {
                customerList.add(c);
                System.out.println("Đã thêm " + c.getCustomerId() + " vào danh sách.");
            }
        }
        try {
            CustomerDao.save(customerList);
        } catch (IOException e) {
        }
    }

    //tạo mới tài khoản
    public void addSavingAccount(Scanner scanner, String customerId) {
        List<Account> accountList = AccountDao.list();
        List<Customer> customerList = CustomerDao.list();
        Customer activeCustomer;
        Account newAccount;
        boolean flag;
        String accountNumber = null;
        double balance;

        do{
            if(!User.validateCustomerId(customerId)){
                System.out.print("Nhập mã số khách hàng: ");
                customerId = scanner.nextLine();
            }else if(this.getCustomerById(customerList, customerId) == null){
                System.out.println("Mã số khách hàng không tồn tại, vui lòng nhập lại:");
                customerId = scanner.nextLine();
            }else{
                activeCustomer = this.getCustomerById(customerList, customerId);
                if(this.isCustomerExisted(customerList, activeCustomer)){
                    flag = true;
                    break;
                }
            }
        }while (true);
        //thêm tài khoản saving
        if(flag){
            Customer cus = new Customer();
            do{
                System.out.print("Nhập STK gồm 6 chữ số: ");
                accountNumber = scanner.nextLine();
                Account checkAcc = cus.getAccountByAccountNumber(accountList, accountNumber);
                boolean checkAccNumber = this.isAccountExisted(accountList, checkAcc);
                if(!this.checkSoTK(accountNumber)){
                    System.out.println("Nhập STK gồm 6 chữ số và không chữ");
                }else if(checkAccNumber){
                    System.out.println("STK đã tồn tại!!");
                }else{
                    flag = true;
                    break;
                }
            }while(true);
        }
        if(flag){
            do{
                System.out.print("Nhập số dư tài khoản >= 50000đ: ");
                balance = scanner.nextDouble();
                scanner.nextLine();
            }while (!(balance >= 50000));
            newAccount = new SavingsAccount(customerId, accountNumber, balance);
            try {
                AccountDao.update(newAccount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thao tác thành công!!");
        }
    }

    //phương thức rút tiền
    public void withdraw(Scanner scanner, String customerId) {
        List<Customer> customerList = CustomerDao.list();
        customerList.forEach(customer -> {
            if(customer.getCustomerId().equals(customerId)) {
                //thông tin tài khoản muốn rút
                customer.displayInformation();
                customer.withdraw(scanner);
            }
        });
    }

    //phương thức chuyển tiền
    public void transfers(Scanner scanner, String customerId) {
        List<Customer> customerList = CustomerDao.list();
        customerList.forEach(customer -> {
            if(customer.getCustomerId().equals(customerId)) {
                //thông tin tài khoản chuyển tiền
                customer.displayInformation();
                customer.transfers(scanner);
            }
        });
    }

    //phương thức hiển thị toàn bộ lịch sử giao dịch
    public void transaction(String customerId) {
        //hiển thị thông tin tài khoản
        List<Customer> customerList = CustomerDao.list();
        customerList.forEach(customer -> {
            if(customer.getCustomerId().equals(customerId)) {
                //thông tin tài khoản chuyển tiền
                customer.displayInformation();
                //hiển thị lịch sử giao dịch
                customer.displayTransactionInformation();
            }
        });
    }

    //kiểm tra 1 account đã tồn tại trong mảng không
    public boolean isAccountExisted(List<Account> accountList, Account newAccount) {
        boolean check = false;
        if(newAccount == null) {
            check = false;
        } else {
            for (Account account : accountList) {
                if (account.getAccountNumber().equals(newAccount.getAccountNumber())) {
                    check = true;
                }
            }
        }
        return check;
    }

    // kieerm tra customer ton tai trong mang, mảng này là trong file txt
    public boolean isCustomerExisted(List<Customer> customers, Customer newCustomer) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(newCustomer.getCustomerId())) {
                return true;
            }
        }
        return false;
    }

    //laasy ra 1 customer có id bằng id cho trước, lấy ra id trong file .dat
    public Customer getCustomerById(List<Customer> customerList, String customerId) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
    // kiểm tra xem có phải là dãy chữ 12 số
    public boolean checkSoCCCD(String accNumber){
        try{
            Long.parseLong(accNumber);
            if(accNumber.length() == 12){
                return true;
            }
        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }
    //kiểm tra xem dãy số đã nhập có 6 chữ số
    public boolean checkSoTK(String accNumber){
        try{
            Long.parseLong(accNumber);
            if(accNumber.length() == 6){
                return true;
            }
        }catch (NumberFormatException e){
            return false;
        }
        return false;
    }
    //kiểm tra có là dãy chữ số
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
