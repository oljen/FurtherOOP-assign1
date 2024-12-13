package shapes;

import geometry.Vec2d;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @Test
    void testContainsPoint() {
        // Create a square Tile
        ArrayList<Vec2d> vertices = new ArrayList<>();
        vertices.add(new Vec2d(0, 0));
        vertices.add(new Vec2d(4, 0));
        vertices.add(new Vec2d(4, 4));
        vertices.add(new Vec2d(0, 4));
        Tile tile = new Tile(new Vec2d(0, 0), vertices, Color.RED);

        // Test a point inside the Tile
        assertTrue(tile.contains(new Vec2d(2, 2)));

        // Test a point outside the Tile
        assertFalse(tile.contains(new Vec2d(5, 5)));
    }

    @Test
    void testContainsTile() {
        // Create a larger Tile (box)
        ArrayList<Vec2d> outerVertices = new ArrayList<>();
        outerVertices.add(new Vec2d(0, 0));
        outerVertices.add(new Vec2d(10, 0));
        outerVertices.add(new Vec2d(10, 10));
        outerVertices.add(new Vec2d(0, 10));
        Tile outerTile = new Tile(new Vec2d(0, 0), outerVertices, Color.BLUE);

        // Create a smaller Tile fully inside the larger Tile
        ArrayList<Vec2d> innerVertices = new ArrayList<>();
        innerVertices.add(new Vec2d(2, 2));
        innerVertices.add(new Vec2d(8, 2));
        innerVertices.add(new Vec2d(8, 8));
        innerVertices.add(new Vec2d(2, 8));
        Tile innerTile = new Tile(new Vec2d(0, 0), innerVertices, Color.GREEN);

        // Test that the larger Tile contains the smaller Tile
        assertTrue(outerTile.contains(innerTile));

        // Test that the smaller Tile does not contain the larger Tile
        assertFalse(innerTile.contains(outerTile));
    }

    @Test
    void testIntersectsTile() {
        // Create the first Tile
        ArrayList<Vec2d> verticesA = new ArrayList<>();
        verticesA.add(new Vec2d(0, 0));
        verticesA.add(new Vec2d(4, 0));
        verticesA.add(new Vec2d(4, 4));
        verticesA.add(new Vec2d(0, 4));
        Tile tileA = new Tile(new Vec2d(0, 0), verticesA, Color.RED);

        // Create the second Tile that overlaps with the first
        ArrayList<Vec2d> verticesB = new ArrayList<>();
        verticesB.add(new Vec2d(2, 2));
        verticesB.add(new Vec2d(6, 2));
        verticesB.add(new Vec2d(6, 6));
        verticesB.add(new Vec2d(2, 6));
        Tile tileB = new Tile(new Vec2d(0, 0), verticesB, Color.GREEN);

        // Create a third Tile that does not overlap with the first
        ArrayList<Vec2d> verticesC = new ArrayList<>();
        verticesC.add(new Vec2d(6, 6));
        verticesC.add(new Vec2d(8, 6));
        verticesC.add(new Vec2d(8, 8));
        verticesC.add(new Vec2d(6, 8));
        Tile tileC = new Tile(new Vec2d(0, 0), verticesC, Color.BLUE);

        // Test intersection between overlapping Tiles
        assertTrue(tileA.intersects(tileB));
        assertTrue(tileB.intersects(tileA));

        // Test no intersection between non-overlapping Tiles
        assertFalse(tileA.intersects(tileC));

    }
}
