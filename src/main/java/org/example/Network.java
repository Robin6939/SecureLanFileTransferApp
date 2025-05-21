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
            // Filter out virtual, loopback, down, or non-physical interfaces
            if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) {
                continue;
            }
            // Some adapters have names containing "vEthernet", "Virtual", "VMware", "Docker", etc.
            // You can exclude those by name if needed:
            String name = iface.getDisplayName().toLowerCase();
            if (name.contains("virtual") || name.contains("veth") || name.contains("vmware")
                    || name.contains("docker") || name.contains("hyper-v") || name.contains("wsl")) {
                continue;
            }
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
