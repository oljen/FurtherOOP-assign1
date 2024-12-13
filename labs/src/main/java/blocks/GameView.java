package blocks;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

import blocks.BlockShapes.Piece;
import blocks.BlockShapes.Shape;
import blocks.BlockShapes.Cell;
import blocks.BlockShapes.ShapeSet;
import blocks.BlockShapes.SpriteState;
import blocks.BlockShapes.Sprite;

public class GameView extends JComponent {
    ModelInterface model;
    Palette palette;
    int margin = 5;
    int shapeRegionHeight;
    int cellSize = 40;
    int paletteCellSize = 20;
    int shrinkSize = 30;
    Piece ghostShape = null;
    List<Shape> poppableRegions = null;

    public GameView(ModelInterface model, Palette palette) {
        this.model = model;
        this.palette = palette;
        this.shapeRegionHeight = cellSize * ModelInterface.height / 2;
    }

    private void paintShapePalette(Graphics g, int cellSize) {
        // Paint the background of the palette area
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(margin, margin + ModelInterface.height * cellSize,
                ModelInterface.width * cellSize, shapeRegionHeight);

        // Draw each sprite in the palette using the smaller cell size
        for (Sprite sprite : palette.getSprites()) {
            g.setColor(Color.BLUE);
            for (Cell cell : sprite.shape) {
                int x = sprite.px + cell.x() * paletteCellSize;
                int y = sprite.py + cell.y() * paletteCellSize;
                g.fillRect(x, y, paletteCellSize, paletteCellSize);
            }
        }
    }

    private void paintPoppableRegions(Graphics g, int cellSize) {
        if (poppableRegions != null) {
            g.setColor(new Color(169, 169, 169, 128)); // Semi-transparent grey color

            for (Shape region : poppableRegions) {
                for (Cell cell : region) {
                    int x = margin + cell.x() * cellSize;
                    int y = margin + cell.y() * cellSize;
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }


    private void paintGhostShape(Graphics g, int cellSize) {
        if (ghostShape != null) {
            g.setColor(new Color(0, 255, 255, 128)); // Semi-transparent cyan for ghost shape
            for (Cell cell : ghostShape.cells()) {
                int x = margin + cell.x() * cellSize;
                int y = margin + cell.y() * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
    }



    private void paintGrid(Graphics g) {
        int x0 = margin;
        int y0 = margin;
        int width = ModelInterface.width * cellSize;
        int height = ModelInterface.height * cellSize;
        Set<Cell> occupiedCells = model.getOccupiedCells();

        // Draw the grid background
        g.setColor(Color.WHITE);
        g.fillRect(x0, y0, width, height);

        // Draw the grid lines
        g.setColor(Color.BLACK);
        for (int x = 0; x < ModelInterface.width; x++) {
            for (int y = 0; y < ModelInterface.height; y++) {
                g.drawRect(x0 + x * cellSize, y0 + y * cellSize, cellSize, cellSize);
            }
        }

        // Draw the occupied cells with padding
        int padding = 1; // Add a small padding to keep grid lines visible
        g.setColor(Color.GREEN);
        for (Cell cell : occupiedCells) {
            int x = x0 + cell.x() * cellSize + padding;
            int y = y0 + cell.y() * cellSize + padding;
            int size = cellSize - 2 * padding;
            g.fillRect(x, y, size, size);
        }
    }

    private void paintMiniGrids(Graphics2D g) {
        // Draw thicker lines to delineate 3x3 mini-squares
        int s = ModelInterface.subSize;
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        for (int x = 0; x < ModelInterface.width; x += s) {
            for (int y = 0; y < ModelInterface.height; y += s) {
                g.drawRect(margin + x * cellSize, margin + y * cellSize, s * cellSize, s * cellSize);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintGrid(g);
        paintMiniGrids((Graphics2D) g);
        paintGhostShape(g, cellSize);
        paintPoppableRegions(g, cellSize);
        paintShapePalette(g, cellSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                ModelInterface.width * cellSize + 2 * margin,
                ModelInterface.height * cellSize + 2 * margin + shapeRegionHeight
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Clean Blocks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ModelInterface model = new ModelSet();
        Shape shape = new ShapeSet().getShapes().get(0);
        Piece piece = new Piece(shape, new Cell(0, 0));
        Palette palette = new Palette();
        model.place(piece);
        frame.add(new GameView(model, palette));
        frame.pack();
        frame.setVisible(true);
    }
}
