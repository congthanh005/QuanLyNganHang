package Bank;

import Acount.Account;
import Customer.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank {
    private String bankId;

    private String bankName;

    public Bank(String bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
    }

    public Bank() {
    }

    public String getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}