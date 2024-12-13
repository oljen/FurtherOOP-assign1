package blocks;

import blocks.BlockShapes.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelSet extends StateSet implements ModelInterface {

    Set<Cell> locations = new HashSet<>();
    List<Shape> regions = new RegionHelper().allRegions();

    // Streak-related variables
    private int streakCount = 0;
    private long lastPopTime = 0;
    private static final long STREAK_TIME_LIMIT = 5000; // 5 seconds for streak window

    // Getter for streak count
    public int getStreakCount() {
        return streakCount;
    }


    // Constructor to initialize locations and regions
    public ModelSet() {
        super();
        initialiseLocations();
    }

    public int getScore() {
        return score;
    }

    private void initialiseLocations() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                locations.add(new Cell(i, j));
            }
        }
    }

    @Override
    public void place(Piece piece) {
        // Add the piece cells to the occupied cells set
        occupiedCells.addAll(piece.cells());

        // Check for completed regions after placing the piece
        List<Shape> poppableRegions = getPoppableRegions(piece);

        if (!poppableRegions.isEmpty()) {
            // Regions were popped, increase the streak count
            streakCount++;

            // Award points based on the streak count
            int basePoints = 10;
            int bonusPoints = basePoints * streakCount;

            for (Shape region : poppableRegions) {
                remove(region);
                score += bonusPoints; // Apply bonus points for each cleared region
            }
        } else {
            // No regions were popped, reset the streak count
            streakCount = 0;
        }
    }

    @Override
    public boolean canPlace(Piece piece) {
        for (Cell cell : piece.cells()) {
            if (cell.x() < 0 || cell.x() >= width || cell.y() < 0 || cell.y() >= height || occupiedCells.contains(cell)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void remove(Shape region) {
        occupiedCells.removeAll(region);
    }

    @Override
    public boolean isComplete(Shape region) {
        return region.stream().allMatch(cell -> occupiedCells.contains(cell));
    }

    @Override
    public List<Shape> getPoppableRegions(Piece piece) {
        List<Shape> poppable = new ArrayList<>();
        for (Shape region : regions) {
            Set<Cell> tempOccupied = new HashSet<>(occupiedCells);
            tempOccupied.addAll(piece.cells());
            if (region.stream().allMatch(tempOccupied::contains)) {
                poppable.add(region);
            }
        }
        return poppable;
    }

    @Override
    public Set<Cell> getOccupiedCells() {
        return new HashSet<>(occupiedCells);
    }

    @Override
    public boolean isGameOver(List<Shape> palettePieces) {
        for (Shape shape : palettePieces) {
            if (canPlaceAnywhere(shape)) {
                return false;
            }
        }
        return true;
    }

    public boolean canPlaceAnywhere(Shape shape) {
        for (Cell loc : locations) {
            Piece testPiece = new Piece(shape, loc);
            if (canPlace(testPiece)) {
                return true;
            }
        }
        return false;
    }
}
