package Customer;

import java.io.Serializable;

//thong tin nguoi dung
public abstract class User implements Serializable {
    public static final long serialVersionUID = 1L;

    private String name;
    private String customerId;

    public static boolean validateCustomerId(String strNum) {
        //Kiểm tra xem số cccd có chính xác là dãy chữ số cos 12 chữ số và ko phải số âm
        //sử dụng regex
        if (strNum == null || strNum.matches("-?\\d+(\\.\\d+)?")== false) {
            return false;
        }
        if(strNum.length() != 12) {
            return false;
        }
            if(strNum.contains("-")){
                return false;
            }
        return true;
    }
    public User() {
    }

    public User(String customerId, String name) {
        this.name = name;
        this.customerId = customerId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        //yyêu cầu nâng cao: kiểm tra xem có đúng là cccd hay ko
        if (validateCustomerId(customerId)) {
            this.customerId = customerId;
        }
    }
}
