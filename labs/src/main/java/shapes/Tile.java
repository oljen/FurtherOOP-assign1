package shapes;

import geometry.PolyGeometry;
import geometry.Vec2d;

import java.awt.*;
import java.util.ArrayList;

public class Tile extends DrawablePolygon {

    public Tile(Vec2d p, ArrayList<Vec2d> vertices, Color color) {
        super(p, vertices, color);
    }

    // Check if a point is inside the tile

    public boolean contains(Vec2d point) {
        // Use PolyGeometry.contains(ArrayList<Vec2d>, Vec2d)
        return PolyGeometry.contains(getVertices(), point);
    }

    // Check if another tile is fully contained within this tile

    public boolean contains(Tile other) {
        // Use PolyGeometry.contains(ArrayList<Vec2d>, ArrayList<Vec2d>)
        return PolyGeometry.contains(getVertices(), other.getVertices());
    }

    // Check if this tile overlaps with another tile

    public boolean intersects(Tile other) {
        // Use PolyGeometry.polygonsOverlap(ArrayList<Vec2d>, ArrayList<Vec2d>)
        return PolyGeometry.polygonsOverlap(getVertices(), other.getVertices());
    }
}
