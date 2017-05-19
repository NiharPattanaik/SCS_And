package crm.sales.com.salescrm.model;

import java.text.SimpleDateFormat;

/**
 * Created by npattana on 19/05/17.
 */

public class Order {


    private int orderID;

    private int orderBookingID;

    private int noOfLineItems;

    private double bookValue;

    private String remark;

    private int status;

    private int resellerID;

    private int customerID;

    public Order(int orderBookingID, int noOfLineItems, double bookValue, String remark){
        this.orderBookingID = orderBookingID;
        this.noOfLineItems = noOfLineItems;
        this.bookValue = bookValue;
        this.remark = remark;
    }


    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderBookingID() {
        return orderBookingID;
    }

    public void setOrderBookingID(int orderBookingID) {
        this.orderBookingID = orderBookingID;
    }

    public int getNoOfLineItems() {
        return noOfLineItems;
    }

    public void setNoOfLineItems(int noOfLineItems) {
        this.noOfLineItems = noOfLineItems;
    }

    public double getBookValue() {
        return bookValue;
    }

    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResellerID() {
        return resellerID;
    }

    public void setResellerID(int resellerID) {
        this.resellerID = resellerID;
    }


}
