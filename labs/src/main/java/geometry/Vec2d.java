package geometry;

public record Vec2d(double x, double y) {
    public double distance(Vec2d other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public double magnitude() {
        // Magnitude (length) of the vector: sqrt(x^2 + y^2)
        return Math.sqrt(x * x + y * y);
    }

    public Vec2d add(Vec2d other) {
        return new Vec2d(x + other.x, y + other.y);
    }

    public Vec2d subtract(Vec2d other) {
        // Subtraction: (x1 - x2, y1 - y2)
        return new Vec2d(x - other.x, y - other.y);
    }

    public Vec2d multiply(double scalar) {
        // Scalar multiplication: (x * scalar, y * scalar)
        return new Vec2d(x * scalar, y * scalar);
    }

    public double dot(Vec2d other) {
        // Dot product: x1*x2 + y1*y2
        return x * other.x + y * other.y;
    }

    public Vec2d perpendicular() {
        // Returns a vector perpendicular to this one: (-y, x)
        return new Vec2d(-y, x);
    }

    public double angleRadians() {
        // Angle of the vector in radians from the positive x-axis
        return Math.atan2(y, x);
    }
}
