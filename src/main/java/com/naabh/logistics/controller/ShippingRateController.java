package com.naabh.logistics.controller;

import com.naabh.logistics.model.ShippingRateRequest;
import com.naabh.logistics.model.ShippingRateResponse;
import com.naabh.logistics.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShippingRateController {
    @Autowired
    private ShippingRateService shippingRateService;

    @PostMapping("/rates")
    public Map<String, List<ShippingRateResponse>> getShippingRates(@RequestBody ShippingRateRequest request) {
        List<ShippingRateResponse> rates = shippingRateService.getShippingRates(request);
        Map<String, List<ShippingRateResponse>> response = new HashMap<>();
        response.put("data", rates);
        return response;
    }
}
