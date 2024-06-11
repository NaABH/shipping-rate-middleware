package com.naabh.logistics.model;

public class ShippingRateRequest {
    private String senderCountry = "MY";
    private String senderState;
    private String senderPostcode;
    private String receiverCountry = "MY";
    private String receiverState;
    private String receiverPostcode;
    private String shippingType = "1";
    private String jtShippingType = "EZ";
    private double weight;
    private double length;
    private double width;
    private double height;

    public void setSenderState(String senderState) {
        this.senderState = senderState;
    }

    public void setSenderPostcode(String senderPostcode) {
        this.senderPostcode = senderPostcode;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public void setReceiverPostcode(String receiverPostcode) {
        this.receiverPostcode = receiverPostcode;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getSenderState() {
        return senderState;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public String getSenderPostcode() {
        return senderPostcode;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public String getReceiverPostcode() {
        return receiverPostcode;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public String getJtShippingType() {
        return jtShippingType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public double getWeight() {
        return weight;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
