package Acount;

import Customer.Customer;
import Model.BinaryFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    //quy định tên file,
    private final static String FILE_PATH = "store/accounts.dat";

    //lưu danh sách file
    public static void save(List<Account> accounts) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, accounts);
    }

    //lấy ra danh sách từ file
    public static List<Account> list() {
        return BinaryFileService.readFile(FILE_PATH);
    }

    //update số dư cho tài khoản
    public static void update(Account editAccount) throws IOException {
        List<Account> accounts = list();
        boolean hasExist = accounts.stream()
                .anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));

        List<Account> updatedAccounts;
        if(!hasExist) {
            updatedAccounts = new ArrayList<>(accounts);
            updatedAccounts.add(editAccount);
        } else {
            updatedAccounts = new ArrayList<>();
            for (Account account: accounts) {
                if (account.getAccountNumber().equals(editAccount.getAccountNumber())){
                    updatedAccounts.add(editAccount);
                } else {
                    updatedAccounts.add(account);
                }
            }
        }
        save(updatedAccounts);

    }
}
