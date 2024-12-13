package graphics;

import javax.swing.*;
import java.awt.*;

public class ColorComponent extends JComponent {
    private final Color color;

    public ColorComponent(Color color, Dimension preferredSize) {
        this.color = color;
        if (preferredSize != null) {
            setPreferredSize(preferredSize);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}


