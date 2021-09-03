package com.seanlindev.springframework.providers;

import com.seanlindev.springframework.api.dto.ShipmentDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import javax.crypto.Mac;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.UUID;

@Component
public class ShipmentServiceProvider {
    String shipmentServerUrl = "http://localhost:8081/api/shipment";

    private final String accessKey = "2d3hnyglkx";
    private final String accessSecret = "czkhepajdru7q5lgdjen6ebq2mnnee";

    public String requestCreateShipment(ShipmentDto shipmentDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Access-Key", accessKey);
        String requestId = UUID.randomUUID().toString();
        headers.add("X-Request-ID", requestId);
        try {
            String signature = generateAccessSignature(requestId + "." + accessKey);
            headers.add("Access-Signature", signature);
            HttpEntity<ShipmentDto> request = new HttpEntity(shipmentDto, headers);
            ShipmentDto shipmentResult = restTemplate.postForObject(shipmentServerUrl, request, ShipmentDto.class);
            return shipmentResult.getShipmentId();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String generateAccessSignature(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] encoded = Base64.getEncoder().encode(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        return new String(encoded);
    }
}
