package reflection.uml;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
import java.util.Set;
import reflection.uml.UMLLayout.ClassLayout;
import reflection.uml.ReflectionData.*;

public class DisplayUML extends JPanel {
    private final Map<String, ClassLayout> layout;
    private final Set<Link> links; // Add links to store relationships

    public DisplayUML(Map<String, ClassLayout> layout, Set<Link> links) {
        this.layout = layout;
        this.links = links;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // First draw the links so they appear behind the class boxes
        drawLinks(g2d);

        // Then draw the class boxes
        drawClassBoxes(g2d);
    }

    private void drawClassBoxes(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        for (Map.Entry<String, ClassLayout> entry : layout.entrySet()) {
            String className = entry.getKey();
            ClassLayout cl = entry.getValue();

            double x = cl.centerX() - cl.width() / 2;
            double y = cl.centerY() - cl.height() / 2;
            double width = cl.width();
            double height = cl.height();

            // Fill box with white background
            g2d.setColor(Color.WHITE);
            g2d.fillRect((int) x, (int) y, (int) width, (int) height);

            // Draw box outline
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int) x, (int) y, (int) width, (int) height);

            // Draw the class name
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(className);
            int textX = (int) (x + (width - textWidth) / 2);
            int textY = (int) (y + fm.getAscent() + 5); // Add small padding
            g2d.drawString(className, textX, textY);
        }
    }

    private void drawLinks(Graphics2D g2d) {
        for (Link link : links) {
            ClassLayout from = layout.get(link.from());
            ClassLayout to = layout.get(link.to());

            if (from != null && to != null) {
                // Calculate center points of the classes
                Point2D fromCenter = new Point2D.Double(from.centerX(), from.centerY());
                Point2D toCenter = new Point2D.Double(to.centerX(), to.centerY());

                // Calculate the points where the line intersects with the class boxes
                Point2D fromPoint = getIntersectionPoint(from, fromCenter, toCenter);
                Point2D toPoint = getIntersectionPoint(to, toCenter, fromCenter);

                // Draw different line styles based on link type
                if (link.type() == LinkType.SUPERCLASS) {
                    drawInheritanceLine(g2d, fromPoint, toPoint);
                } else {
                    drawDependencyLine(g2d, fromPoint, toPoint);
                }
            }
        }
    }

    private void drawInheritanceLine(Graphics2D g2d, Point2D from, Point2D to) {
        // Use thicker lines for better visibility
        g2d.setStroke(new BasicStroke(2.0f));

        // Calculate the direction vector
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalize the direction vector
        double nx = dx / length;
        double ny = dy / length;

        // Arrow properties
        double arrowLength = 20;
        double arrowWidth = 12;

        // Calculate arrow tip position (slightly before 'to' point)
        Point2D tip = new Point2D.Double(
                to.getX() - nx * arrowLength/2,
                to.getY() - ny * arrowLength/2
        );

        // Calculate arrow base points
        double bx = -ny * arrowWidth/2;
        double by = nx * arrowWidth/2;

        Point2D base1 = new Point2D.Double(
                tip.getX() - nx * arrowLength + bx,
                tip.getY() - ny * arrowLength + by
        );

        Point2D base2 = new Point2D.Double(
                tip.getX() - nx * arrowLength - bx,
                tip.getY() - ny * arrowLength - by
        );

        // Draw the main line
        g2d.draw(new Line2D.Double(from.getX(), from.getY(), base1.getX(), base1.getY()));

        // Create and fill the arrow head
        Path2D.Double arrowHead = new Path2D.Double();
        arrowHead.moveTo(to.getX(), to.getY());
        arrowHead.lineTo(base1.getX(), base1.getY());
        arrowHead.lineTo(base2.getX(), base2.getY());
        arrowHead.closePath();

        g2d.fill(arrowHead);
    }

    private void drawDependencyLine(Graphics2D g2d, Point2D from, Point2D to) {
        // Use dashed line for dependencies
        float[] dash = {8.0f, 4.0f};
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

        g2d.draw(new Line2D.Double(from, to));
    }

    private Point2D getIntersectionPoint(ClassLayout cl, Point2D from, Point2D to) {
        // Box boundaries
        double boxLeft = cl.centerX() - cl.width() / 2;
        double boxRight = cl.centerX() + cl.width() / 2;
        double boxTop = cl.centerY() - cl.height() / 2;
        double boxBottom = cl.centerY() + cl.height() / 2;

        // Vector from 'from' to 'to' point
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();

        // Parametric form parameters for all sides
        double[] t = new double[4];
        Point2D[] intersections = new Point2D[4];

        // Left side
        t[0] = (boxLeft - from.getX()) / dx;
        intersections[0] = new Point2D.Double(
                boxLeft,
                from.getY() + t[0] * dy
        );

        // Right side
        t[1] = (boxRight - from.getX()) / dx;
        intersections[1] = new Point2D.Double(
                boxRight,
                from.getY() + t[1] * dy
        );

        // Top side
        t[2] = (boxTop - from.getY()) / dy;
        intersections[2] = new Point2D.Double(
                from.getX() + t[2] * dx,
                boxTop
        );

        // Bottom side
        t[3] = (boxBottom - from.getY()) / dy;
        intersections[3] = new Point2D.Double(
                from.getX() + t[3] * dx,
                boxBottom
        );

        // Find the valid intersection point closest to 'from'
        Point2D closestIntersection = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < 4; i++) {
            if (Double.isFinite(t[i]) && t[i] >= 0) {
                Point2D intersection = intersections[i];
                if (isPointOnBox(intersection, boxLeft, boxRight, boxTop, boxBottom)) {
                    double distance = from.distance(intersection);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection != null ? closestIntersection : from;
    }

    private boolean isPointOnBox(Point2D point, double left, double right, double top, double bottom) {
        double x = point.getX();
        double y = point.getY();

        boolean onVerticalSide = (Math.abs(x - left) < 0.1 || Math.abs(x - right) < 0.1)
                && y >= top && y <= bottom;
        boolean onHorizontalSide = (Math.abs(y - top) < 0.1 || Math.abs(y - bottom) < 0.1)
                && x >= left && x <= right;

        return onVerticalSide || onHorizontalSide;
    }


    public static void main(String[] args) {
        // Create example layout and links
        Map<String, ClassLayout> exampleLayout = Map.of(
                "MyShape", new ClassLayout(300, 100, 150, 60),
                "MyCircle", new ClassLayout(150, 300, 150, 120),
                "MyEllipse", new ClassLayout(500, 300, 150, 90),
                "Connector", new ClassLayout(300, 400, 150, 60)
        );

        Set<Link> exampleLinks = Set.of(
                new Link("MyCircle", "MyShape", LinkType.SUPERCLASS),
                new Link("MyEllipse", "MyShape", LinkType.SUPERCLASS),
                new Link("Connector", "MyShape", LinkType.DEPENDENCY)
        );

        DisplayUML display = new DisplayUML(exampleLayout, exampleLinks);

        JFrame frame = new JFrame("UML Class Diagram");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}