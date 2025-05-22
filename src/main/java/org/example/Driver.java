package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import org.example.entities.Device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Driver {
    private static final Logger logger = LogManager.getLogger(Driver.class);
    ConcurrentHashMap<Device, Socket> peers = new ConcurrentHashMap<>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    int serverPort = 5001;

    public void start() throws SocketException {
        Device thisDevice = new Device("Robin's PC", Network.getLocalIp());
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(true);
            logger.info("Started server at port: {}", serverPort);
            executorService.submit(() -> {
                while(true) {
                    connectToClients(serverSocket);
                }
            });
        } catch(Exception e) {
            logger.error("Failed to start socket server with error: {}", e.getMessage());
        }
        executorService.submit(() -> {
            try {
                startLookingForSockets();
            } catch (SocketException | InterruptedException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    public void connectToClients(ServerSocket serverSocket) throws IOException {
        logger.info("Server is listening");
        Socket socket = serverSocket.accept();
        logger.info("Accepted connection to a client at: {}", socket.getInetAddress());
        handleServerHandshake(socket);
    }

    private void handleServerHandshake(Socket clientSocket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            String received = input.readLine();
            if(received.equals("Initiate Handshake from client")) {
                output.println("Initiate Handshake from server");
                String clientName = input.readLine();
                Device client = new Device(clientName, clientSocket.getInetAddress().toString());
                peers.put(client, clientSocket);
                logger.info("Handshake successful with {}", clientName);
            } else {
                logger.error("Handshake unsuccessful");
            }
        } catch (Exception e) {
            logger.error("Error setting up writing and reading streams with client: {}", e.getMessage());
        }

    }

    public void startLookingForSockets() throws SocketException, InterruptedException {
        Thread.sleep(2000);
        logger.info("Started searching for sockets");
        String subnet = Network.getSubnet();
        int timeout = 100;

        for(int i = 1;i < 255; i++) {
            String host = subnet + i;
            if(host.equals(Network.getLocalIp()))
                continue;
            InetSocketAddress address = new InetSocketAddress(host, serverPort);
            try(Socket socket = new Socket()){
                socket.connect(address, timeout);
                logger.info("Found device at host: {}", host);
                handleClientHandshake(socket);
            } catch(Exception e) {
                logger.debug("No device at: {}", host);
            }
        }
        logger.info("Done looking for open sockets");
    }

    private void handleClientHandshake(Socket serverSocket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            PrintWriter output = new PrintWriter(serverSocket.getOutputStream(), true);
            output.println("Initiate Handshake from client");
            String received = input.readLine();
            if(received.equals("Initiate Handshake from server")) {
                output.println(Constants.DEVICE_NAME);
            }
        } catch (Exception e) {
            logger.error("Error setting up writing and reading streams with server: {}", e.getMessage());
        }
    }
}
