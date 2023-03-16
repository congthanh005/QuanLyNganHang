package Model;

import Customer.Customer;

import java.io.IOException;
import java.util.List;

public class TransactionDao {
    //quy định tên file
    private final static String FILE_PATH = "store/transactions.dat";

    //lưu danh sách file
    public static void save(List<Transaction> transactions) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, transactions);
    }

    //lấy ra danh sách từ file
    public static List<Transaction> list() {
        return BinaryFileService.readFile(FILE_PATH);
    }

}
