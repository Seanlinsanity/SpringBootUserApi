package com.seanlindev.springframework.shared.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
    @Autowired
    private Environment env;

    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }

    public String getShipmentServerUrl() {
        return env.getProperty("shipment.server.url");
    }

    public String getShipmentAccessKey() {
        return env.getProperty("shipment.access.key");
    }

    public String getShipmentAccessSecret() {
        return env.getProperty("shipment.access.secret");
    }
}
