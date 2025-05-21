package org.example.UserInterface;
import javax.swing.*;

public class SwingDemo {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Robin doing his demo");
        frame.setSize(800, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel with FlowLayout
        JPanel panel = new JPanel();

        // Add a label and a button
        JLabel label = new JLabel("Hello, Swing!");
        JButton button = new JButton("Click Me");

        // Event Handling: Button Click
        button.addActionListener(e -> label.setText("Button Clicked!"));

        // Add components to the panel
        panel.add(label);
        panel.add(button);

        // Add the panel to the frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
