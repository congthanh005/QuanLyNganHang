package Customer;//package Customer;

import Acount.Account;
import Bank.DigitalBank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalCustomerTest {

    DigitalBank activeBank;


    DigitalCustomer activeCustomer;

    @BeforeEach
    void setup() {
        activeBank = new DigitalBank();
        activeCustomer = new DigitalCustomer("Nguyen A", "123456789123");
    }

    @Test
    void getTotalAccountBalance() {
        assertEquals("4000000",activeCustomer.getTotalAccountBalance());
    }

    @Test
    void validateCustomerId() {
        assertTrue(DigitalCustomer.validateCustomerId("123456798789")); //true
        assertFalse(DigitalCustomer.validateCustomerId("23r23r2r32r3")); //false vì có chữ cái
        assertFalse(DigitalCustomer.validateCustomerId("23r23r2r3")); //false vì ko đủ 12 chữ
        assertFalse(DigitalCustomer.validateCustomerId("-12365498754")); //false vì dãy số âm
    }
}