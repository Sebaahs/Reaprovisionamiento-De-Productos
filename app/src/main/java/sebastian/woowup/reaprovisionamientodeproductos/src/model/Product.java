package sebastian.woowup.reaprovisionamientodeproductos.src.model;

import java.time.LocalDate;

public class Product {

    private int sku;
    private String name;
    private LocalDate date;

    public Product(int sku, String name) {
        this.sku = sku;
        this.name = name;
    }

    public Product(int sku, LocalDate date) {
        this.sku = sku;
        this.date = date;
    }

    public int getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
