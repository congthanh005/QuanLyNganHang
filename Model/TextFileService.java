package Model;

import Acount.Account;
import Customer.Customer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TextFileService {
    //dấu phân cách trong file
    private static final String COMMA_DELIMITER  = ",";
    public static List<Customer> printFile (String fileName) {
        try(BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            List<Customer> listCus = new ArrayList<>();
            String line;
            while((line = fileReader.readLine())!= null) {
                String[] s = line.split(COMMA_DELIMITER);
                List<String> list = Arrays.asList(s);
                Customer customer = new Customer(list);
                listCus.add(customer);
            }
            return listCus;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<List<String>> readFile(String filePath) {
        List<List<String>> objects = new ArrayList<>();
        File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()){
                String str = sc.nextLine();
                objects.add(parseLine(str));
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        sc.close();

        return objects;
    }

    private static List<String> parseLine(String str){
        Scanner sc = new Scanner(str);
        List<String> list = null;
        list = new ArrayList<String>(Arrays.asList(str.split(COMMA_DELIMITER)));
//        System.out.println(list.get(0) + "customerName " + list.get(1));
        sc.close();
        return  list;
    }
}
