package com.naabh.logistics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naabh.logistics.model.ShippingRateRequest;
import com.naabh.logistics.model.ShippingRateResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingRateService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ShippingRateResponse> getShippingRates(ShippingRateRequest request) {
        List<ShippingRateResponse> rates = new ArrayList<>();
        rates.add(getCitylinkRate(request));
//        rates.add(getJTRate(request));
        return rates;
    }

    private ShippingRateResponse getCitylinkRate(ShippingRateRequest request) {
        String url = "https://www.citylinkexpress.com/wp-json/wp/v2/getShippingRate";

        // Create a Map to hold the request parameters
        Map<String, Object> params = new HashMap<>();
        params.put("origin_country", request.getSenderCountry());
        params.put("origin_state", request.getSenderState());
        params.put("origin_postcode", request.getSenderPostcode());
        params.put("destination_country", request.getReceiverCountry());
        params.put("destination_state", request.getReceiverState());
        params.put("destination_postcode", request.getReceiverPostcode());
        params.put("length", request.getLength());
        params.put("width", request.getWidth());
        params.put("height", request.getHeight());
        params.put("selected_type", request.getShippingType());
        params.put("parcel_weight", request.getWeight());

        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                double rate = jsonNode.path("req").path("data").path("rate").asDouble();
                return new ShippingRateResponse("citylink", rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ShippingRateResponse("citylink", 0);
    }

//    private ShippingRateResponse getJTRate(ShippingRateRequest request) {
//        String url = String.format(
//                "https://www.jtexpress.my/shipping-rates?_token=iOQObcPflw4pKk4VerNQ0JVuZf7MNry1DlYkVUol&shipping_rates_type=domestic&sender_postcode=%s&receiver_postcode=%s&destination_country=%s&shipping_type=%s&weight=%.2f&length=%.2f&width=%.2f&height=%.2f&item_value",
//                request.getSenderPostcode(),
//                request.getReceiverPostcode(),
//                request.getReceiverCountry(),
//                request.getJtShippingType(),
//                request.getWeight(),
//                request.getLength(),
//                request.getWidth(),
//                request.getHeight()
//        );
//
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            try {
//                Document doc = Jsoup.parse(response.getBody());
//                String rateStr = doc.select("div.table-responsive.d-block.d-sm-none > table > tbody > tr:nth-child(2) > td:nth-child(2)").text().trim();
//                double rate = Double.parseDouble(rateStr);
//                return new ShippingRateResponse("jt", rate);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return new ShippingRateResponse("jt", 0);
//    }
}
