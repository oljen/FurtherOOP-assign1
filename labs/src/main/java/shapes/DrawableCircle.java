package shapes;

import geometry.Vec2d;

import java.awt.*;

public class DrawableCircle extends Circle implements Drawable {
    private Color color;

    public DrawableCircle(Vec2d p, double radius, Color color) {
        super(p, radius);
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);

        // Calculate the top-left corner of the bounding square to center the circle
        int x = (int) (getPosition().x() - radius);
        int y = (int) (getPosition().y() - radius);

        // Diameter of the circle
        int diameter = (int) (2 * radius);

        // Draw the circle
        g.fillOval(x, y, diameter, diameter);
    }
}
