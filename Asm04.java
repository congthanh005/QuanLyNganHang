import Acount.Account;
//import Acount.LoanAccount;
import Acount.SavingsAccount;
import Bank.DigitalBank;
import Customer.Customer;
import Customer.CustomerDao;

import Model.BinaryFileService;
import Model.Transaction;
import Model.TransactionDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Asm04 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DigitalBank activeBank = new DigitalBank();

    //chức năng 1

    private static void showCustomer() {
        activeBank.showCustomers();
    }

    //chức năng 2: nhập danh sách khách hàng
    public static void addAccoutList() {
        System.out.println("Nhập đường dẫn đến tệp: (copy: store/customers.txt)");
        String fileName = scanner.nextLine();
        File f = new File(fileName);
        if (f.exists()) {
            activeBank.addCustomers(fileName);
        } else {
            System.out.println("Tệp không tồn tại!");
        }
    }

    //chức năng 3: thêm tài khoản ATM
    public static void  addAccoutToSavings() {
        //thêm vào SavinAccount
        List<Customer> customerList = CustomerDao.list();
        String customerId;
        do{
            System.out.print("Nhập mã số khách hàng (EXIT để thoát): ");
            customerId = scanner.nextLine();
            if(customerId.toUpperCase().equals("EXIT")){
                System.out.println("Thoát");
                break;
            }else if(!activeBank.checkSoCCCD(customerId) || (activeBank.getCustomerById(customerList, customerId) == null)){
                System.out.println("Mã số khách hàng không tồn tại! Nhập lại mã số khách hàng!");
            }else{
                activeBank.addSavingAccount(scanner, customerId);
                break;
            }
        }while (true);
    }

    //chức nng 4: chuyển tiền
    public static void transfers() {
        List<Customer> customerList = CustomerDao.list();
        String customerId;
        System.out.println("Chức năng chuyển tiền:");
        do{
            System.out.print("Nhập mã số khách hàng (hoặc EXIT để thoát): ");
            customerId = scanner.nextLine();
            if(customerId.toUpperCase().equals("EXIT")){
                System.out.println("Thoát");
                break;
            }else if(!activeBank.checkSoCCCD(customerId) || (activeBank.getCustomerById(customerList, customerId) == null)){
                System.out.println("Mã số khách hàng không tồn tại!");
            }else{
                activeBank.transfers(scanner, customerId);
                break;
            }
        }while (true);
    }
//
//    //chức năng 5: Rút tiền
    public static void Withdraw() {
        System.out.println("Chức năng rút tiền:");
        List<Customer> customerList = CustomerDao.list();
        String customerId;
        do{
            System.out.print("Nhập mã số khách hàng (hoặc EXIT để thoát): ");
            customerId = scanner.nextLine();
            if(customerId.toUpperCase().equals("EXIT")){
                System.out.println("Thoát");
                break;
            }else if(!activeBank.checkSoCCCD(customerId) || (activeBank.getCustomerById(customerList, customerId) == null)){
                System.out.println("Mã số khách hàng không tồn tại!");
            }else{
                activeBank.withdraw(scanner, customerId);
                break;
            }
        }while (true);
    }

//    //chức năng 6
    public static void transaction () {
        List<Customer> customerList = CustomerDao.list();
        String customerId;
        System.out.println("Chức năng tra cứu lịch sử giao dịch:");
        do{
            System.out.print("Nhập mã số khách hàng (hoặc EXIT để thoát): ");
            customerId = scanner.nextLine();
            if(customerId.toUpperCase().equals("EXIT")){
                System.out.println("Thoát");
                break;
            }else if(!activeBank.checkSoCCCD(customerId) || (activeBank.getCustomerById(customerList, customerId) == null)){
                System.out.println("Mã số khách hàng không tồn tại!");
            }else{
                activeBank.transaction(customerId);

                break;
            }
        }while (true);
    }

    //giao diện chức năng
    public static int showChoose() {
        int chucnang;
        System.out.println("+-----------+-------------------------------+-----------+");
        System.out.println("| NGAN HANG SO  |  FX18418@V3.0.0                       |");
        System.out.println("+-----------+-------------------------------+-----------+");
        System.out.println("1. Xem danh sách khách hàng");
        System.out.println("2. Nhập danh sách khách hàng");
        System.out.println("3. Thêm tài khoản ATM");
        System.out.println("4. Chuyển tiền");
        System.out.println("5. Rút tiền");
        System.out.println("6. Tra cứu lịch sử giao dịch");
        System.out.println("0. Thoát");
        System.out.println("+-----------+-------------------------------+-----------+");
        System.out.print("Chức năng: ");
        while (true) {
            try {
                chucnang = Integer.parseInt(scanner.nextLine());
                if (chucnang >= 0 && chucnang <= 6) {
                    break;
                } else {
                    System.err.println("Vui lòng chọn chức năng sử dụng:");
                }
            } catch (Exception e) {
                System.err.println("Vui lòng chọn chức năng sử dụng:");
            }
        }
        return chucnang;
    }

    public static void main(String[] args) {
        // hiển thị giao diện
        boolean flag = true;
        while (flag) {
            int chucnang = showChoose();
            switch (chucnang) {
                case 1:
                    showCustomer();
                    break;
                case 2:
                    addAccoutList();
                    break;
                case 3:
                    addAccoutToSavings();
                    break;
                case 4:
                    transfers();
                    break;
                case 5:
                    Withdraw();
                    break;
                case 6:
                    transaction();
                    break;
                case 0:
                    System.out.println("Kết thúc!");
                    flag = false;
                    break;
                default:
                    System.out.println("Chọn chức năng để sử dụng: ");
            }
        }
    }
}