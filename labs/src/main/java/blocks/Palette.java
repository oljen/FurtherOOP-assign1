package blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import blocks.BlockShapes.Shape;
import blocks.BlockShapes.ShapeSet;
import blocks.BlockShapes.SpriteState;
import blocks.BlockShapes.Sprite;
import blocks.BlockShapes.PixelLoc;

public class Palette {
    ArrayList<Shape> shapes = new ArrayList<>();
    List<Sprite> sprites;
    int nShapes = 3; // Number of shapes to display in the palette

    public Palette() {
        shapes.addAll(new ShapeSet().getShapes());
        sprites = new ArrayList<>();
        replenish();
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public ArrayList<Shape> getShapesToPlace() {
        // Return a list of shapes that are still in the palette (i.e., not placed yet)
        ArrayList<Shape> shapesToPlace = new ArrayList<>();
        for (Sprite sprite : sprites) {
            if (sprite.state == SpriteState.IN_PALETTE) {
                shapesToPlace.add(sprite.shape);
            }
        }
        return shapesToPlace;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    // Return the sprite that contains the mouse point
    public Sprite getSprite(PixelLoc mousePoint, int cellSize) {
        for (Sprite sprite : sprites) {
            if (sprite.contains(mousePoint, cellSize)) {
                return sprite;
            }
        }
        return null;
    }


    private int nReadyPieces() {
        int count = 0;
        for (Sprite sprite : sprites) {
            if (sprite.state == SpriteState.IN_PALETTE || sprite.state == SpriteState.IN_PLAY) {
                count++;
            }
        }
        System.out.println("nReadyPieces: " + count);
        return count;
    }

    public void doLayout(int x0, int y0, int cellSize) {
        int spacing = cellSize * 5; // Spacing between sprites
        int currentX = x0;
        int currentY = y0;

        for (Sprite sprite : sprites) {
            sprite.px = currentX;
            sprite.py = currentY;
            currentX += spacing;
        }
    }


    public void replenish() {
        if (nReadyPieces() > 0) {
            return; // If there are still sprites left, do not replenish
        }

        // Clear the current sprites and add new randomly selected shapes
        sprites.clear();
        int defaultY = 400; // Fixed y position to place sprites at the bottom

        for (int i = 0; i < nShapes; i++) {
            Shape shape = shapes.get((int) (Math.random() * shapes.size()));
            sprites.add(new Sprite(shape, 0, defaultY));
        }

        // Perform layout after adding new sprites
        doLayout(10, defaultY, 20);
        System.out.println("Replenished: " + sprites);
    }





    public static void main(String[] args) {
        Palette palette = new Palette();
        System.out.println("Initial Shapes: " + palette.shapes);
        System.out.println("Initial Sprites: " + palette.sprites);

        palette.doLayout(20, 20, 20);
        System.out.println("Sprites after layout: " + palette.sprites);
    }
}
