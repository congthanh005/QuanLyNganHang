package Acount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {
    private SavingsAccount sav;

    //mentor nào chấm bài thì vui lòng chọn lại path trong AccountDao, CustomerDao, TransactionDao

    @BeforeEach
    void setup() {
        //tạo tài khoản cho Saving Account
        sav = new SavingsAccount("123456789123", "123456",10000000);
    }
    @Test
    void withdraw() {
        assertTrue(sav.withdraw(100000)); //rút bình thường
        assertFalse(sav.withdraw(40000));  //rút nhỏ hơn 50.000đ -> false
        assertFalse(sav.withdraw(400025));  //rút số tiền ko phải bội so của 10000 -> false
        assertFalse(sav.withdraw(1860000));  // số dư không đủ 50.000đ -> false
        assertFalse(sav.withdraw(6000000)); //rút quá 5tr -> false vì tk thường
        //thêm balance để tk thành premium
        sav.setBalance(11000000);
        assertTrue(sav.withdraw(6000000)); //rút quá 5tr -> true vì tk đã thành premium
    }

    @Test
    void isAccepted() {
        assertFalse(sav.isAccepted(40000)); //số tiền rút <50.000d -> false
        assertFalse(sav.isAccepted(6000000)); //số tiền rút quá 5tr do là tk thường ->false
        assertFalse(sav.isAccepted(1960000)); //số dư trong tài khoản <50.000d -> false
        assertFalse(sav.isAccepted(1000025)); //không phải bội số của 10.000d -> false
        //thêm balance thành tk preimum
        sav.setBalance(15000000);
        assertTrue(sav.isAccepted(6000000)); //số tiền rút thỏa mãn điều kiện -> true
    }

    @Test
    void getFee() {
        //khoong phân biệt tk thường hay premium
        //phí mặc định amount*0
        //đang ở trạng thái tk thường
        assertEquals(0*600000,sav.getFee(600000)); //true
        //set thành tk premium
        sav.setBalance(150000000);
        assertEquals(0*2000000,sav.getFee(2000000)); //true
    }

    @Test
    void validateAccount() {
        //account có 6 ký tự đều là chữ số
        assertFalse(Account.validateAccount("qewf12")); //ko phải chữ số -> false
        assertFalse(Account.validateAccount("qewf"));  //ko đủ 6 ký tự ->false
        assertFalse(Account.validateAccount("-12332")); //là số âm ->false
        assertFalse(Account.validateAccount("1231233")); //>6 ký tự -> false
        assertTrue(Account.validateAccount("123456"));  //thỏa mãn điều kiện -> true
    }

    @Test
    void isAccountPremium() {
        assertFalse(sav.isAccountPremium()); //ban đầu balance = 2.000.000 -> tk thường ->false
        sav.setBalance(15000000); //set balance thành 15.000.000 -> tk premium
        assertTrue(sav.isAccountPremium()); //true
    }


}