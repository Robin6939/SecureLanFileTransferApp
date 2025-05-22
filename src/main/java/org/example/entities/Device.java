package org.example.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Main;
import org.example.Network;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 */
public class Device {
    private static final Logger logger = LogManager.getLogger(Device.class);
    public String deviceName;
    UUID deviceId;
    String ipAddress;
    public Device(String deviceName, String ipAddress) {
        this.deviceName = deviceName;
        this.deviceId = UUID.randomUUID();
        try {
            this.ipAddress = Network.getLocalIp();
        } catch (SocketException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
