package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws SocketException {
        Driver driver = new Driver();
        logger.info("Turning on the driver");
        driver.start();
    }
}