package blocks;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import blocks.BlockShapes.Sprite;
import blocks.BlockShapes.PixelLoc;
import blocks.BlockShapes.SpriteState;
import blocks.BlockShapes.Piece;

public class Controller extends MouseAdapter {
    GameView view;
    ModelInterface model;
    Palette palette;
    JFrame frame;
    Sprite selectedSprite = null;
    Piece ghostShape = null;
    String title = "WELCOME TO THE GAME!"; // Initial title
    boolean gameOver = false;

    public Controller(GameView view, ModelInterface model, Palette palette, JFrame frame) {
        this.view = view;
        this.model = model;
        this.palette = palette;
        this.frame = frame;
        frame.setTitle(title); // Set the initial title to "WELCOME TO THE GAME!"
        palette.doLayout(view.margin, view.margin + ModelInterface.height * view.cellSize, view.paletteCellSize);
        System.out.println("Palette layout done: " + palette.sprites);
    }

    public void mousePressed(MouseEvent e) {
        PixelLoc loc = new PixelLoc(e.getX(), e.getY());
        selectedSprite = palette.getSprite(loc, view.paletteCellSize);
        if (selectedSprite == null) {
            return;
        }
        selectedSprite.state = SpriteState.IN_PLAY;
        System.out.println("Selected sprite: " + selectedSprite);
        view.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedSprite != null) {
            selectedSprite.px = e.getX();
            selectedSprite.py = e.getY();

            // Create a ghost piece that snaps to the grid
            Piece potentialGhost = selectedSprite.snapToGrid(view.margin, view.cellSize);

            // Check if the placement is legal before showing the ghost shape
            if (model.canPlace(potentialGhost)) {
                ghostShape = potentialGhost;
                view.ghostShape = ghostShape;
            } else {
                ghostShape = null;
                view.ghostShape = null;
            }

            view.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedSprite != null) {
            Piece piece = selectedSprite.snapToGrid(view.margin, view.cellSize);

            if (model.canPlace(piece)) {
                model.place(piece);
                selectedSprite.state = SpriteState.PLACED;
                palette.getSprites().remove(selectedSprite);
                selectedSprite = null;
                ghostShape = null;
                view.ghostShape = null;

                palette.replenish();

                if (model.isGameOver(palette.getShapesToPlace())) {
                    gameOver = true;
                    frame.setTitle("Blocks Puzzle Score: " + model.getScore() + getStreakText() + " - Game Over!");
                    JOptionPane.showMessageDialog(frame, "Game Over!\nYour Score: " + model.getScore() + getStreakText(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                selectedSprite.state = SpriteState.IN_PALETTE;
                palette.doLayout(view.margin, view.margin + ModelInterface.height * view.cellSize, view.paletteCellSize);
            }

            frame.setTitle(getTitle());
            view.repaint();
        }
    }

    private String getTitle() {
        String updatedTitle = "Blocks Puzzle Score: " + model.getScore();
        if (model instanceof ModelSet) {
            int streakCount = ((ModelSet) model).getStreakCount();
            if (streakCount > 1) {
                updatedTitle += " | Streak: " + streakCount + "x!";
            }
        }
        if (gameOver) {
            updatedTitle += " - Game Over!";
        }
        return updatedTitle;
    }

    private String getStreakText() {
        if (model instanceof ModelSet) {
            int streakCount = ((ModelSet) model).getStreakCount();
            if (streakCount > 1) {
                return " | Streak: " + streakCount + "x!";
            }
        }
        return "";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ModelInterface model = new ModelSet(); // Ensure this is the correct model (ModelSet or Model2dArray)
        Palette palette = new Palette();
        GameView view = new GameView(model, palette);
        Controller controller = new Controller(view, model, palette, frame);
        view.addMouseListener(controller);
        view.addMouseMotionListener(controller);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
