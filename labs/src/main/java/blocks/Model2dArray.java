package blocks;

import blocks.BlockShapes.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Model2dArray extends State2dArray implements ModelInterface {
    List<Shape> regions = new RegionHelper().allRegions();

    private int streakCount = 0;          // Number of consecutive pops
    private static final int BASE_POINTS = 10; // Base points for each region popped

    public Model2dArray() {
        grid = new boolean[width][height];
        // Initialize all cells as empty (false)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = false;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public boolean canPlace(Piece piece) {
        // Check if each cell in the piece is within bounds and not already occupied
        for (Cell cell : piece.cells()) {
            int x = cell.x();
            int y = cell.y();
            if (x < 0 || x >= width || y < 0 || y >= height || grid[x][y]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void place(Piece piece) {
        // Mark the cells in the piece as occupied
        for (Cell cell : piece.cells()) {
            grid[cell.x()][cell.y()] = true;
        }

        // Check for completed regions and remove them
        List<Shape> poppableRegions = getPoppableRegions(piece);
        if (!poppableRegions.isEmpty()) {
            streakCount++; // Increment streak for consecutive pops
            for (Shape region : poppableRegions) {
                remove(region);
                score += BASE_POINTS * streakCount; // Apply streak bonus to the score
            }
        } else {
            streakCount = 0; // Reset streak if no regions were popped
        }
    }

    @Override
    public void remove(Shape region) {
        // Clear the cells in the completed region
        for (Cell cell : region) {
            grid[cell.x()][cell.y()] = false;
        }
    }

    @Override
    public boolean isComplete(Shape region) {
        // Check if all cells in the region are occupied
        for (Cell cell : region) {
            if (!grid[cell.x()][cell.y()]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGameOver(List<Shape> palettePieces) {
        // Check if any shape in the palette can be placed on the grid
        for (Shape shape : palettePieces) {
            if (canPlaceAnywhere(shape)) {
                return false; // If any shape can be placed, the game is not over
            }
        }
        return true; // No shapes can be placed, so the game is over
    }

    public boolean canPlaceAnywhere(Shape shape) {
        // Check if the shape can be placed anywhere on the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Piece testPiece = new Piece(shape, new Cell(x, y));
                if (canPlace(testPiece)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Shape> getPoppableRegions(Piece piece) {
        // Identify all regions (rows, columns, and subgrids) that are fully occupied after placing the piece
        List<Shape> poppable = new ArrayList<>();
        for (Shape region : regions) {
            if (isComplete(region)) {
                poppable.add(region);
            }
        }
        return poppable;
    }

    @Override
    public Set<Cell> getOccupiedCells() {
        // Return a set of all occupied cells based on the grid
        Set<Cell> occupiedCells = new HashSet<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y]) {
                    occupiedCells.add(new Cell(x, y));
                }
            }
        }
        return occupiedCells;
    }
}
