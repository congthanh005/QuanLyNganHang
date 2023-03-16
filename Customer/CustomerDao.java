package Customer;

import Model.BinaryFileService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class CustomerDao implements Serializable {
    //quy định tên file
    private final static String FILE_PATH = "store/customers.dat";

    //lưu danh sách file
    public static void save(List<Customer> customers) throws IOException {

        BinaryFileService.writeFile(FILE_PATH, customers);
    }

    //lấy ra danh sách từ file
    public static List<Customer> list() {
        return BinaryFileService.readFile(FILE_PATH);
    }
}
