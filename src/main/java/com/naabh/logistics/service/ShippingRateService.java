package com.naabh.logistics.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naabh.logistics.model.ShippingRateRequest;
import com.naabh.logistics.model.ShippingRateResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ShippingRateService {
    private final Logger logger = Logger.getLogger(ShippingRateService.class.getName());
    private final RestTemplate restTemplate = new RestTemplate();
//    @Autowired
//    private ShippingRateCacheRepository shippingRateCacheRepository;
    private String token;
    private String cookies;

    public List<ShippingRateResponse> getShippingRates(ShippingRateRequest request) {
        List<ShippingRateResponse> rates = new ArrayList<>();

//        // Check if the request is in the cache
//        ShippingRateCache cachedRate = shippingRateCacheRepository.findCachedRate(request.getSenderPostcode(), request.getReceiverPostcode(), request.getWeight(), request.getLength(), request.getWidth(), request.getHeight());
//        if (cachedRate != null) {
//            // If the request is in the cache, return the cached response
//            rates.add(new ShippingRateResponse(cachedRate.getProvider(), cachedRate.getRate()));
//        } else {
            // If the request is not in the cache, call the APIs
            ShippingRateResponse citylinkRate = getCitylinkRate(request);
            ShippingRateResponse jtRate = getJTRate(request);
            rates.add(citylinkRate);
            rates.add(jtRate);

//            // Store the new request-response pair in the cache
//            ShippingRateCache newRate = new ShippingRateCache();
//            // Set all the fields in newRate based on request and citylinkRate
//            shippingRateCacheRepository.save(newRate);
//            newRate = new ShippingRateCache();
//            // Set all the fields in newRate based on request and jtRate
//            shippingRateCacheRepository.save(newRate);
//        }

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

    private ShippingRateResponse getJTRate(ShippingRateRequest request) {
        String url = "https://www.jtexpress.my/shipping-rates";

        // Create a Map to hold the request parameters
        Map<String, Object> params = new HashMap<>();
        params.put("_token", token);
        params.put("shipping_rates_type", "domestic");
        params.put("sender_postcode", request.getSenderPostcode());
        params.put("receiver_postcode", request.getReceiverPostcode());
        params.put("destination_country", request.getReceiverCountry());
        params.put("shipping_type", request.getJtShippingType());
        params.put("weight", request.getWeight());
        params.put("length", request.getLength());
        params.put("width", request.getWidth());
        params.put("height", request.getHeight());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Requested-With", "XMLHttpRequest");
        headers.set("Cookie", cookies);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                retrieveCookiesAndToken();
                headers.set("Cookie", cookies);
                params.put("_token", token); // Update token in the request params
                entity = new HttpEntity<>(params, headers);
                response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            }
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                Document doc = Jsoup.parse(response.getBody());
                String rateStr = doc.select("div.table-responsive.d-block.d-sm-none > table > tbody > tr:nth-child(4) > td:nth-child(2)").text().trim();
                double rate = Double.parseDouble(rateStr);
                return new ShippingRateResponse("jt", rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ShippingRateResponse("jt", 0);
    }

    private void retrieveCookiesAndToken() {
        String url = "https://www.jtexpress.my/shipping-rates"; // JT Express website URL

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Extract cookies from headers
        List<String> setCookieList = response.getHeaders().get("Set-Cookie");
        if (setCookieList != null && !setCookieList.isEmpty()) {
            cookies = String.join("; ", setCookieList);
            logger.info("Cookies: " + cookies);
        }

        // Extract token from URL
        try {
            Document doc = Jsoup.parse(response.getBody());
            String tokenElement = doc.select("input[name=_token]").first().val();
            token = tokenElement;
            logger.info("Token: " + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
