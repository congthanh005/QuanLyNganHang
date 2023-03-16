package Bank;

import Acount.Account;
import Acount.AccountDao;

import Customer.Customer;
import Customer.CustomerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DigitalBankTest {
    DigitalBank activeBank;
    String fileName;
    Account newAccount;
    Customer newCustomer;
    List<Account> accountList;
    List<Customer> customerList;

    @BeforeEach
    void setup() {
        fileName = "Asm04/src/store/customers.txt" ;
         activeBank = new DigitalBank();
        newAccount = new Account("123456", 1000000,"Savings"); //tài khoản đã có
        accountList = AccountDao.list();
        customerList = CustomerDao.list();
        newCustomer = new Customer("123456789123","Nguyen A");
    }

    @Test
    void isAccountExisted(){
        assertTrue(activeBank.isAccountExisted(accountList, newAccount)); //đã có tài khoản -> true
//        assertTrue(activeBank.isAccountExisted(accountList, new Account("986421", 2000000, "Savings"))); // test true tk chưa có, -> false
        assertFalse(activeBank.isAccountExisted(accountList, new Account("986421", 2000000, "Savings"))); //test false tk chưa có, -> false
    }
    @Test
    void getCustomerById() {
        assertEquals("Nguyen A", activeBank.getCustomerById(customerList, "123456789123").getName()); //tìm ra customer chua customerId cần tìm
        //rồi sau đó getName trong customer đó ra
//        assertEquals("Công", activeBank.getCustomerById("123456798789").getName()); //tìm ra customer chua customerId cần tìm
//        //kiểm tra null nếu customerId ko có trong danh sách customers
//        assertNull(activeBank.getCustomerById("123236798789"),"Không có khách hàng có customerId 123123234234 ");
//        assertNotNull(activeBank.getCustomerById(CUSTOMER_ID)); //có CUSTOMER_ID -> true
//        assertNotNull(activeBank.getCustomerById("123236798789"), "Không có khách hàng có customerId này");
        //ko có 123236798789 -> null -> dòng lệnh trên sẽ false
    }

    @Test
    void isCustomerExisted() {
        assertTrue(activeBank.isCustomerExisted(customerList, newCustomer)); //đã tồn tại -> true
        assertFalse(activeBank.isCustomerExisted(customerList, new Customer("123123123321", "Khuat B"))); //sai vì đã tồn tại

    }

    @Test
    void checkSCCC() {
        assertTrue(activeBank.checkSoCCCD("123456789123")); //đúng là số có 12 chữ số -> true
        assertFalse(activeBank.checkSoCCCD("12345678912")); //đúng là số có 11 chữ số -> false
//        assertTrue(activeBank.checkSoCCCD("12345678912")); //đúng là số có 11 chữ số -> false
    }
    @Test
    void checkTK() {
        assertTrue(activeBank.checkSoTK("123456")); //đúng là số có 6 chữ số -> true
        assertFalse(activeBank.checkSoCCCD("12345678912")); //không phải 6 chữ số -> false
        assertFalse(activeBank.checkSoCCCD("1234sdfsdf2")); //không phải 6 chữ số -> false
//        assertTrue(activeBank.checkSoCCCD("12345678912")); //đúng là số có 11 chữ số -> false
    }
}