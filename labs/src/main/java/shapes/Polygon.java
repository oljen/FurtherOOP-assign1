package shapes;

import geometry.Vec2d;

import java.util.ArrayList;

public class Polygon extends MovableShape {
    ArrayList<Vec2d> vertices;

    public Polygon(Vec2d p, ArrayList<Vec2d> vertices) {
        super(p);
        this.vertices = vertices;
    }

    @Override
    public double area() {
        double sum = 0.0;
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            Vec2d current = vertices.get(i);
            Vec2d next = vertices.get((i + 1) % n); // Wrap around to the first vertex
            sum += (current.x() * next.y()) - (current.y() * next.x());
        }

        return Math.abs(sum) / 2.0; // Absolute value and divide by 2
    }

    @Override
    public double perimeter() {
        double sum = 0.0;
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            Vec2d current = vertices.get(i);
            Vec2d next = vertices.get((i + 1) % n); // Wrap around to the first vertex
            sum += current.distance(next); // Use Vec2d's distance method
        }

        return sum;
    }
}
