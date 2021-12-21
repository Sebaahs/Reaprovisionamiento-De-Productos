package sebastian.woowup.reaprovisionamientodeproductos.src.model;

import java.util.List;

public class Customer {

    private List<Purchase> purchase;

    public Customer(List<Purchase> purchase) {
        this.purchase = purchase;
    }

    public List<Purchase> getPurchase() {
        return purchase;
    }

    public void setPurchase(List<Purchase> purchase) {
        this.purchase = purchase;
    }
}
