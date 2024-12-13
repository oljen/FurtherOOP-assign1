package shapes;

import geometry.Vec2d;

import java.util.ArrayList;
import java.util.List;

public class PuzzleModel {
    Tile box; // The boundary box
    final private ArrayList<Tile> tiles; // The tiles in the puzzle

    public PuzzleModel(Tile box, ArrayList<Tile> tiles) {
        this.box = box;
        this.tiles = tiles;
        System.out.println("PuzzleModel created with " + tiles.size() + " tiles.");
    }

    public boolean isSolved() {
        // Check if all tiles are fully contained within the box and no tiles overlap
        return countContains() == tiles.size() && countOverlaps() == 0;
    }

    public String getStatusText() {
        // Return the status of the puzzle
        return "n Overlaps " + countOverlaps() + " Contains? " +
                countContains() + " Solved? " + isSolved();
    }

    public Tile getTileAt(Vec2d point) {
        // Return the 'top' tile that contains the point, or null if no tile contains the point
        for (int i = tiles.size() - 1; i >= 0; i--) { // Iterate from topmost to bottommost
            Tile tile = tiles.get(i);
            if (tile.contains(point)) {
                return tile;
            }
        }
        return null;
    }

    public boolean checkOverlaps(Tile currentShape) {
        // Return true if the currentShape overlaps any other shape
        for (Tile tile : tiles) {
            if (tile != currentShape && tile.intersects(currentShape)) {
                return true;
            }
        }
        return false;
    }

    public int countOverlaps() {
        // Return the number of tiles that overlap at least one other tile
        int total = 0;
        for (int i = 0; i < tiles.size(); i++) {
            Tile tileA = tiles.get(i);
            for (int j = 0; j < tiles.size(); j++) {
                if (i != j) {
                    Tile tileB = tiles.get(j);
                    if (tileA.intersects(tileB)) {
                        total++;
                        break; // Count each tile only once
                    }
                }
            }
        }
        return total;
    }

    public int countContains() {
        // Return the number of tiles that are completely contained in the box
        int total = 0;
        for (Tile tile : tiles) {
            if (box.contains(tile)) {
                total++;
            }
        }
        return total;
    }

    public List<Tile> getTiles() {
        // Return the list of tiles
        return tiles;
    }
}


