package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import org.example.entities.Device;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver {
    private static final Logger logger = LogManager.getLogger(Driver.class);
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    int serverPort = 5000;

    public void start() {
        Device thisDevice = new Device("Robin's PC");
        try(ServerSocket serverSocket = new ServerSocket(serverPort)) {
            logger.info("Started server at port: {}", serverPort);
        } catch(Exception e) {
            logger.error("Failed to start socket server with error: {}", e.getMessage());
        }
        executorService.submit(() -> {
            try {
                startLookingForSockets();
            } catch (SocketException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public void startLookingForSockets() throws SocketException {
        logger.info("Started searching for sockets");
        String subnet = Network.getSubnet();
        int timeout = 1000;

        for(int i = 1;i < 255; i++) {
            String host = subnet + i;
            InetSocketAddress address = new InetSocketAddress(host, serverPort);
            try(Socket socket = new Socket()){
                socket.connect(address, timeout);
                logger.info("Found device at host: {}", host);
            } catch(Exception e) {
                logger.info("No device at: {}", host);
            }
        }
    }
}
