package com.seanlindev.springframework.providers;

import com.seanlindev.springframework.shared.SpringApplicationContext;
import com.seanlindev.springframework.shared.utils.AppProperties;

public class ShipmentProviderConstant {
    static final String shipmentServerUrl = getServerUrl();
    static final String accessKey = getAccessKey();
    static final String accessSecret = getAccessSecret();

    private static String getServerUrl() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getShipmentServerUrl();
    }

    private static String getAccessKey() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getShipmentAccessKey();
    }

    private static String getAccessSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getShipmentAccessSecret();
    }
}
