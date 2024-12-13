package graphics;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutExample {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("BorderLayout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add components to the BorderLayout regions
        frame.add(new ColorComponent(Color.RED, new Dimension(400, 50)), BorderLayout.NORTH);
        frame.add(new ColorComponent(Color.BLUE, new Dimension(400, 50)), BorderLayout.SOUTH);
        frame.add(new ColorComponent(Color.GREEN, new Dimension(50, 100)), BorderLayout.EAST);
        frame.add(new ColorComponent(Color.YELLOW, new Dimension(50, 200)), BorderLayout.WEST);
        frame.add(new ColorComponent(Color.ORANGE, null), BorderLayout.CENTER);


        // Pack and display the frame
        frame.setSize(600, 400); // Optional: Set initial size
        frame.setVisible(true);
    }
}
