package shapes;

import geometry.Vec2d;

public class Rectangle extends MovableShape {
    double width;
    double height;

    public Rectangle(Vec2d p, double width, double height) {
        super(p);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        // Area of a rectangle: width * height
        return width * height;
    }

    @Override
    public double perimeter() {
        // Perimeter of a rectangle: 2 * (width + height)
        return 2 * (width + height);
    }
}
