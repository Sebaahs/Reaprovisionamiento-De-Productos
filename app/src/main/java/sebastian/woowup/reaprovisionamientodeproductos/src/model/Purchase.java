package sebastian.woowup.reaprovisionamientodeproductos.src.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Purchase {

    private String number;
    private String date;
    private List<Product> productList;

    //Constructors

    public Purchase() {    }

    public Purchase(String number, String date, List<Product> productList) {
        this.number = number;
        this.date = date;
        this.productList = productList;
    }

    //Getter

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public List<Product> getProductList() {
        return productList;
    }

    //Setters

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
