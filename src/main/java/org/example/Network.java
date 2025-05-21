package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Network {
    private static final Logger logger = LogManager.getLogger(Network.class);
    static String localIp = "";
    static String subnet = "";
    public static String getLocalIp() throws SocketException {
        if(!localIp.isEmpty()) {
            return localIp;
        }
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    logger.info("The local IP: {}", addr.getHostAddress());
                    return localIp = addr.getHostAddress();
                }
            }
        }
        logger.error("Couldn't find the local ip address");
        return null;
    }

    public static String getSubnet() throws SocketException {
        if(!subnet.isEmpty()) {
            return subnet;
        } else if(!localIp.isEmpty()) {
            subnet = localIp.substring(0, localIp.lastIndexOf('.')+1);
            logger.info("The subnet value is {}", subnet);
            return subnet;
        } else {
            getLocalIp();
            subnet = localIp.substring(0, localIp.lastIndexOf('.')+1);
            logger.info("The subnet value is {}", subnet);
            return subnet;
        }
    }
}
