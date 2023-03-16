package Model;


import Customer.Customer;
import Customer.CustomerDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class BinaryFileService {
    public static File fileObject(String fileName) {
        File f = new File(fileName);
        return f;
    }

    //doc file
    public static <T> List<T> readFile(String fileName) {
        File f = fileObject(fileName);
        List<T> objects = new ArrayList<>();
        try (ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            boolean eof = false;
            while (!eof) {
                try {
                    T object = (T) file.readObject();
                    objects.add(object);
                } catch (EOFException | ClassNotFoundException e) {
                    eof = true;
                }
            }
        } catch (FileNotFoundException e) {
            //file ko tồn tại hoặc chưa tao file -> báo ra lỗi: chưa có file
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException io) {
//            System.out.println("IO Exception: " + io.getMessage());
        }
        return objects;
    }

    //lưu file
    public static <T> void writeFile(String filePath, List<T> objects) {
        File f = fileObject(filePath);
        try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            for (T object : objects) {
                file.writeObject(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
