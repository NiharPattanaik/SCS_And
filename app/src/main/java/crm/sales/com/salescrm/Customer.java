package crm.sales.com.salescrm;

/**
 * Created by npattana on 31/03/17.
 */

public class Customer {

    int customerID;
    String customerName;
    public int getCustomerID() {
        return customerID;
    }
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String toString(){
        return customerName;
    }
}
