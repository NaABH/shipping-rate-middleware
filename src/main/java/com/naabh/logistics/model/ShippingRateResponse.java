package com.naabh.logistics.model;

public class ShippingRateResponse {
    private String courier;
    private double rate;

    public ShippingRateResponse(String courier, double rate) {
        this.courier = courier;
        this.rate = rate;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
