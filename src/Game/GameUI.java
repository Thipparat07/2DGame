// GameUI
package Game;

import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.geom.Rectangle2D;

public class GameUI {
    private Rectangle2D exitButtonRect;
    private boolean isExitHovering = false;
    private float[] exitButtonColor = { 0.8f, 0.3f, 0.3f };
    private float[] exitHoverColor = { 0.9f, 0.4f, 0.4f };

    private Rectangle2D restartButtonRect;
    private boolean isRestartHovering = false;
    private float[] restartButtonColor = { 0.3f, 0.7f, 0.3f };
    private float[] restartHoverColor = { 0.4f, 0.8f, 0.4f };

    private int screenWidth;
    private int screenHeight;

    public GameUI() {
    }

    public void init(int width, int height) {
        updateDimensions(width, height);
    }

    public void updateDimensions(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (width - buttonWidth) / 2;

        int restartButtonY = height / 2 - buttonHeight - 10;
        restartButtonRect = new Rectangle2D.Float(buttonX, restartButtonY, buttonWidth, buttonHeight);

        int exitButtonY = height / 2 + 10;
        exitButtonRect = new Rectangle2D.Float(buttonX, exitButtonY, buttonWidth, buttonHeight);
    }

    public void drawPauseScreen(TextRenderer textRenderer, int width, int height) {
        textRenderer.beginRendering(width, height);
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        String pauseText = "PAUSED";
        Rectangle2D pauseBounds = textRenderer.getBounds(pauseText);
        int textX = (int) (width / 2 - pauseBounds.getWidth() / 2);
        int textY = (int) (height / 2 - pauseBounds.getHeight() / 2);

        textRenderer.draw(pauseText, textX, textY);
        textRenderer.endRendering();
    }

    public void drawGameOverScreen(TextRenderer textRenderer, int width, int height) {
        textRenderer.beginRendering(width, height);
        textRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);

        String gameOverText = "GAME OVER";
        Rectangle2D gameOverBounds = textRenderer.getBounds(gameOverText);
        int textX = (int) (width / 2 - gameOverBounds.getWidth() / 2);
        int textY = (int) (height / 2 + 100);

        textRenderer.draw(gameOverText, textX, textY);

        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        String restartText = "Press R to Restart";
        Rectangle2D restartBounds = textRenderer.getBounds(restartText);
        int restartX = (int) (width / 2 - restartBounds.getWidth() / 2);
        int restartY = (int) (height / 2 - 50);

        textRenderer.draw(restartText, restartX, restartY);
        textRenderer.endRendering();
    }

    public void drawWinScreen(TextRenderer textRenderer, int width, int height) {
        textRenderer.beginRendering(width, height);
        textRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);

        String winText = "CONGRATULATIONS!";
        Rectangle2D winBounds = textRenderer.getBounds(winText);
        int textX = (int) (width / 2 - winBounds.getWidth() / 2);
        int textY = (int) (height / 2 + 100);
        textRenderer.draw(winText, textX, textY);

        String subText = "You've completed the maze!";
        Rectangle2D subBounds = textRenderer.getBounds(subText);
        int subX = (int) (width / 2 - subBounds.getWidth() / 2);
        int subY = (int) (height / 2 + 50);
        textRenderer.draw(subText, subX, subY);

        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        String restartText = "Press R to Play Again";
        Rectangle2D restartBounds = textRenderer.getBounds(restartText);
        int restartX = (int) (width / 2 - restartBounds.getWidth() / 2);
        int restartY = (int) (height / 2 - 50);
        textRenderer.draw(restartText, restartX, restartY);

        textRenderer.endRendering();
    }

    public boolean isMouseOverExit(int mouseX, int mouseY) {
        return exitButtonRect != null && exitButtonRect.contains(mouseX, mouseY);
    }

    public boolean isMouseOverRestart(int mouseX, int mouseY) {
        return restartButtonRect != null && restartButtonRect.contains(mouseX, mouseY);
    }

    public void setExitHovering(boolean hovering) {
        isExitHovering = hovering;
    }

    public void setRestartHovering(boolean hovering) {
        isRestartHovering = hovering;
    }
}