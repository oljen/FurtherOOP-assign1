package shapes;

import geometry.Vec2d;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PuzzleModelTest {

    @Test
    void testIsSolved() {
        Tile box = createBox();
        ArrayList<Tile> tiles = new ArrayList<>();

        // Add a tile completely inside the box
        tiles.add(new Tile(new Vec2d(2, 2), createSquareVertices(), Color.RED));

        // Create PuzzleModel
        PuzzleModel model = new PuzzleModel(box, tiles);

        // Check when puzzle is solved
        assertTrue(model.isSolved());

        // Add an overlapping tile
        tiles.add(new Tile(new Vec2d(3, 3), createSquareVertices(), Color.BLUE));

        // Check when puzzle is not solved
        assertFalse(model.isSolved());
    }

    @Test
    void testGetTileAt() {
        Tile box = createBox();
        ArrayList<Tile> tiles = new ArrayList<>();

        Tile tile1 = new Tile(new Vec2d(2, 2), createSquareVertices(), Color.RED);
        Tile tile2 = new Tile(new Vec2d(4, 4), createSquareVertices(), Color.BLUE);

        tiles.add(tile1);
        tiles.add(tile2);

        PuzzleModel model = new PuzzleModel(box, tiles);

        // Check if topmost tile is returned
        assertEquals(tile2, model.getTileAt(new Vec2d(4, 4)));

        // Check if no tile is found at a given point
        assertNull(model.getTileAt(new Vec2d(0, 0)));
    }

    @Test
    void testCheckOverlaps() {
        Tile box = createBox();
        ArrayList<Tile> tiles = new ArrayList<>();

        Tile tile1 = new Tile(new Vec2d(2, 2), createSquareVertices(), Color.RED);
        Tile tile2 = new Tile(new Vec2d(3, 3), createSquareVertices(), Color.BLUE);

        tiles.add(tile1);
        tiles.add(tile2);

        PuzzleModel model = new PuzzleModel(box, tiles);

        // Check if overlaps are correctly detected
        assertTrue(model.checkOverlaps(tile1));
        assertTrue(model.checkOverlaps(tile2));

        // Add a non-overlapping tile
        Tile tile3 = new Tile(new Vec2d(6, 6), createSquareVertices(), Color.GREEN);
        tiles.add(tile3);

        assertFalse(model.checkOverlaps(tile3));
    }

    @Test
    void testCountOverlaps() {
        Tile box = createBox();
        ArrayList<Tile> tiles = new ArrayList<>();

        Tile tile1 = new Tile(new Vec2d(2, 2), createSquareVertices(), Color.RED);
        Tile tile2 = new Tile(new Vec2d(3, 3), createSquareVertices(), Color.BLUE);
        Tile tile3 = new Tile(new Vec2d(6, 6), createSquareVertices(), Color.GREEN);

        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);

        PuzzleModel model = new PuzzleModel(box, tiles);

        // Count tiles with overlaps
        assertEquals(2, model.countOverlaps());
    }

    @Test
    void testCountContains() {
        Tile box = createBox();
        ArrayList<Tile> tiles = new ArrayList<>();

        // Add a tile completely inside the box
        tiles.add(new Tile(new Vec2d(2, 2), createSquareVertices(), Color.RED));

        // Add a tile partially outside the box
        tiles.add(new Tile(new Vec2d(8, 8), createSquareVertices(), Color.BLUE));

        PuzzleModel model = new PuzzleModel(box, tiles);

        // Count tiles completely contained in the box
        assertEquals(1, model.countContains());
    }

    // Helper method to create a bounding box
    private Tile createBox() {
        ArrayList<Vec2d> vertices = new ArrayList<>();
        vertices.add(new Vec2d(0, 0));
        vertices.add(new Vec2d(10, 0));
        vertices.add(new Vec2d(10, 10));
        vertices.add(new Vec2d(0, 10));
        return new Tile(new Vec2d(0, 0), vertices, Color.BLACK);
    }

    // Helper method to create square vertices
    private ArrayList<Vec2d> createSquareVertices() {
        ArrayList<Vec2d> vertices = new ArrayList<>();
        vertices.add(new Vec2d(0, 0));
        vertices.add(new Vec2d(2, 0));
        vertices.add(new Vec2d(2, 2));
        vertices.add(new Vec2d(0, 2));
        return vertices;
    }
}
