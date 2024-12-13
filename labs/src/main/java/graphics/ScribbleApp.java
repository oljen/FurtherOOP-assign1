package graphics;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

// Line class to hold a list of points
class Line {
    ArrayList<Point2D> points = new ArrayList<>();
}

// DrawComponent class to handle drawing and mouse interactions
class DrawComponent extends JComponent {
    // Define a list to store multiple lines
    final ArrayList<Line> lines = new ArrayList<>();
    // Defines how many points back in the list to draw the line to
    int pointSpan = 10;

    // Mouse listener class to handle drawing
    class MouseDrawListener implements MouseListener, MouseMotionListener {
        Line currentLine = null;

        @Override
        public void mouseClicked(MouseEvent e) {
            // No action needed for mouseClicked in this context
        }

        @Override
        public void mousePressed(MouseEvent e) {
            currentLine = new Line();
            currentLine.points.add(new Point2D.Double(e.getX(), e.getY()));
            lines.add(currentLine);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            currentLine = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // No action needed for mouseEntered in this context
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // No action needed for mouseExited in this context
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentLine != null) {
                currentLine.points.add(new Point2D.Double(e.getX(), e.getY()));
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // No action needed for mouseMoved in this context
        }
    }

    // Constructor to attach the mouse listener
    public DrawComponent() {
        MouseDrawListener mouseListener = new MouseDrawListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    // Reset the drawing
    public void reset() {
        lines.clear();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Cast to Graphics2D - more powerful API
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getWidth(), getHeight());

        // Draw all the lines
        for (Line line : lines) {
            for (int i = 1; i < line.points.size(); i++) {
                int startIndex = Math.max(0, i - pointSpan);
                Point2D p1 = line.points.get(startIndex);
                Point2D p2 = line.points.get(i);
                g2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}

// Main class to launch the Scribble App
public class ScribbleApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Scribble App - Mouse Event Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the drawing component
        DrawComponent drawComponent = new DrawComponent();

        // Create the reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawComponent.reset();
            }
        });

        // Set up the layout
        frame.setLayout(new BorderLayout());
        frame.add(drawComponent, BorderLayout.CENTER);
        frame.add(resetButton, BorderLayout.SOUTH);

        // Finalize and show the frame
        frame.pack();
        frame.setVisible(true);
    }
}

