package org.example.UserInterface;
import javax.swing.*;
import java.awt.*;

import org.example.Driver;
import org.example.entities.Device;

public class UI {
    public static void createAndShowGUI() throws InterruptedException {
        JFrame frame = new JFrame("Dynamic Icons Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        while(true) {
            for (Device device : Driver.peers.keySet()) {

                JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                rowPanel.add(new JLabel(device.deviceName + ":"));

                JLabel iconLabel = new JLabel("\uD83D\uDCBB");
                iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
                rowPanel.add(iconLabel);

                mainPanel.add(rowPanel);
            }
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            frame.add(scrollPane);

            frame.setVisible(true);
            Thread.sleep(5000);
        }
    }
}
