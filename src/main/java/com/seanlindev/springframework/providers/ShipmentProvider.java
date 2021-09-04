package com.seanlindev.springframework.providers;

import com.seanlindev.springframework.api.dto.ShipmentDto;
import com.seanlindev.springframework.model.ShipmentStatus;
import org.springframework.http.*;

import javax.crypto.Mac;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Component
public class ShipmentProvider {

    public ShipmentDto requestCreateShipment(ShipmentDto shipmentDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = apiAccessHeader();
            HttpEntity<ShipmentDto> request = new HttpEntity(shipmentDto, headers);
            ShipmentDto shipmentResult = restTemplate.postForObject(ShipmentProviderConstant.shipmentServerUrl, request, ShipmentDto.class);
            return shipmentResult;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public ShipmentDto getShipmentDetails(String shipmentId) throws Exception {
        String url = ShipmentProviderConstant.shipmentServerUrl + "/" + shipmentId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = apiAccessHeader();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<ShipmentDto> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, ShipmentDto.class);
            return response.getBody();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public ShipmentDto cancelShipment(String shipmentId) throws Exception {
        String url = ShipmentProviderConstant.shipmentServerUrl + "/" + shipmentId + "/status";
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = apiAccessHeader();
            ShipmentDto shipmentDto = new ShipmentDto();
            shipmentDto.setStatus(ShipmentStatus.CANCELLED);
            HttpEntity entity = new HttpEntity(headers);
            HttpEntity<ShipmentDto> request = new HttpEntity(shipmentDto, headers);
            ResponseEntity<ShipmentDto> response = restTemplate.exchange(
                    url, HttpMethod.PUT, request, ShipmentDto.class);
            return response.getBody();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private HttpHeaders apiAccessHeader() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Access-Key", ShipmentProviderConstant.accessKey);
        String requestId = UUID.randomUUID().toString() + ":" + String.valueOf(Instant.now().getEpochSecond());
        headers.add("X-Request-ID", requestId);
        try {
            String signature = generateAccessSignature(requestId + "." + ShipmentProviderConstant.accessKey);
            headers.add("Access-Signature", signature);
            return headers;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String generateAccessSignature(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(ShipmentProviderConstant.accessSecret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] encoded = Base64.getEncoder().encode(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        return new String(encoded);
    }
}
