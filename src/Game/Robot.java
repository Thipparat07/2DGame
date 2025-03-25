package Game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Robot implements GLEventListener {
    private Texture mainPageButtonTexture;
    private Texture pauseButtonTexture;
    private Texture restartButtonTexture;
    private Texture levelSelectionButtonTexture;

    private float x, y;
    private final float width = 50, height = 50;
    private final float speed = 10.0f;

    private int screenWidth, screenHeight;
    private int health = 3;
    private boolean gameOver = false;
    private boolean isPaused = false;
    private boolean hasWon = false;

    private TextRenderer textRenderer;
    private Texture heartTexture;
    private Texture robotTexture;
    private Texture wallTexture;
    private final int heartSize = 30;

    private float mazeOffsetX;
    private float mazeOffsetY;

    private Maze maze;
    private List<Item> items; // Changed from single Item to List<Item>
    private GameUI gameUI;
    private int level;

    private Rectangle2D mainPageButtonRect;
    private Rectangle2D pauseButtonRect;
    private Rectangle2D restartButtonRect;
    private Rectangle2D levelSelectionButtonRect;

    public Robot(Maze maze, int level) {
        this.maze = maze;
        this.level = level;
        this.items = new ArrayList<>();
        this.gameUI = new GameUI();
    }

    private void loadRobotTexture() {
        try {
            File robotFile = new File("C:\\Computer Graphics\\2DGame\\Human.jpg");
            if (robotFile.exists()) {
                robotTexture = TextureIO.newTexture(robotFile, true);
                System.out.println("Robot texture loaded successfully");
            } else {
                System.err.println("Robot texture file not found: " + robotFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to load robot texture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void calculateCenterPosition() {
        mazeOffsetX = (screenWidth - (maze.getWidth() * width)) / 2;
        mazeOffsetY = (screenHeight - (maze.getHeight() * height)) / 2;
    }

    private void resetPosition() {
        x = mazeOffsetX + width;
        y = mazeOffsetY + height;
    }

    private void move(float dx, float dy) {
        if (gameOver || isPaused)
            return;

        int nextX = (int) ((x + dx - mazeOffsetX) / width);
        int nextY = (int) ((y + dy - mazeOffsetY) / height);

        if (maze.isWalkable(nextX, nextY)) {
            if (maze.isExit(nextX, nextY)) {
                if (areAllItemsCollected()) {
                    System.out.println("All items collected!");
                    System.out.println("🎉 Game Win! 🎉");
                    gameOver = true;
                    hasWon = true;
                } else {
                    System.out.println("🚫 Need to collect " + getRemainingItemsCount() + " more items!");
                    gameOver = true;
                    hasWon = false;
                }
                return;
            }

            x += dx;
            y += dy;
            checkItemCollisions();
        } else {
            takeDamage();
        }
    }
    
    private void checkItemCollisions() {
        for (Item item : items) {
            item.checkCollision(x, y, width, height);
        }
    }
    
    private boolean areAllItemsCollected() {
        for (Item item : items) {
            if (!item.isCollected()) {
                return false;
            }
        }
        return true;
    }
    
    private int getRemainingItemsCount() {
        int count = 0;
        for (Item item : items) {
            if (!item.isCollected()) {
                count++;
            }
        }
        return count;
    }

    private void takeDamage() {
        health--;
        System.out.println("หุ่นยนต์ชนผนัง! ชีวิตที่เหลือ: " + health);

        if (health <= 0) {
            gameOver = true;
            hasWon = false;
            System.out.println("Game Over!");
        } else {
            resetPosition();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
        System.out.println("Paused: " + isPaused);
    }

    private void restartGame() {
        health = 3;
        gameOver = false;
        hasWon = false;
        resetPosition();
        for (Item item : items) {
            item.reset(mazeOffsetX, mazeOffsetY, width, height, maze);
        }
        System.out.println("เริ่มเกมใหม่!");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

        screenWidth = drawable.getSurfaceWidth();
        screenHeight = drawable.getSurfaceHeight();

        calculateCenterPosition();

        // Load textures
        try {
            // Wall texture
            File wallFile = new File("C:\\Computer Graphics\\2DGame\\wall.jpg");
            if (wallFile.exists()) {
                wallTexture = TextureIO.newTexture(wallFile, true);
            }

            // Heart texture
            File heartFile = new File("C:\\Computer Graphics\\2DGame\\heart.png");
            if (heartFile.exists()) {
                heartTexture = TextureIO.newTexture(heartFile, true);
            }

            // Button textures
            mainPageButtonTexture = TextureIO.newTexture(
                new File("C:\\Computer Graphics\\2DGame\\Back.png"), true);
            pauseButtonTexture = TextureIO.newTexture(
                new File("C:\\Computer Graphics\\2DGame\\Pause.png"), true);
            restartButtonTexture = TextureIO.newTexture(
                new File("C:\\Computer Graphics\\2DGame\\reset.png"), true);
            levelSelectionButtonTexture = TextureIO.newTexture(
                new File("C:\\Computer Graphics\\2DGame\\menu.png"), true);
        } catch (IOException e) {
            System.err.println("Failed to load textures: " + e.getMessage());
            e.printStackTrace();
        }

        // Button positions
        mainPageButtonRect = new Rectangle2D.Float(screenWidth - 60, screenHeight - 40, 50, 30);
        pauseButtonRect = new Rectangle2D.Float(screenWidth - 120, screenHeight - 40, 50, 30);
        restartButtonRect = new Rectangle2D.Float(screenWidth - 180, screenHeight - 40, 50, 30);
        levelSelectionButtonRect = new Rectangle2D.Float(screenWidth - 240, screenHeight - 40, 50, 30);

        loadRobotTexture();
        resetPosition();
        gameUI.init(screenWidth, screenHeight);
        initializeItems(); // Replaces item.init()

        // Input listeners
        GLWindow window = (GLWindow) drawable;
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void handleKeyPress(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
            return;
        }

        if (gameOver) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: move(0, speed); break;
            case KeyEvent.VK_S: move(0, -speed); break;
            case KeyEvent.VK_A: move(-speed, 0); break;
            case KeyEvent.VK_D: move(speed, 0); break;
            case KeyEvent.VK_P: togglePause(); break;
        }
    }

    private void handleMouseClick(MouseEvent e) {
        float mouseX = e.getX();
        float mouseY = screenHeight - e.getY();

        if (mainPageButtonRect.contains(mouseX, mouseY)) {
            goToMainPage();
        } else if (pauseButtonRect.contains(mouseX, mouseY)) {
            togglePause();
        } else if (restartButtonRect.contains(mouseX, mouseY)) {
            restartGame();
        } else if (levelSelectionButtonRect.contains(mouseX, mouseY)) {
            goToLevelSelection();
        }
    }

    private void goToMainPage() {
        Randers.setGLEventListener(new MainPage());
    }

    private void goToLevelSelection() {
        Randers.setGLEventListener(new LevelSelection());
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        drawMaze(gl, mazeOffsetX, mazeOffsetY, width, height);

        if (isPaused && !gameOver) {
            gameUI.drawPauseScreen(textRenderer, screenWidth, screenHeight);
            return;
        }

        if (!gameOver) {
            drawItemCounter();
            drawItems(gl);
            drawRobot(gl);
            drawHealth(gl);
        } else {
            if (hasWon) {
                gameUI.drawWinScreen(textRenderer, screenWidth, screenHeight);
            } else {
                gameUI.drawGameOverScreen(textRenderer, screenWidth, screenHeight);
            }
        }

        drawButtons(gl);
    }
    
    private void drawItems(GL2 gl) {
        for (Item item : items) {
            item.draw(gl);
        }
    }

    private void drawRobot(GL2 gl) {
        if (robotTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            robotTexture.bind(gl);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(x, y);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(x, y + height);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
    }

    private void drawItemCounter() {
    	textRenderer.beginRendering(screenWidth, screenHeight);
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        textRenderer.draw("Items: " + (items.size() - getRemainingItemsCount()) + "/" + items.size(), 
                         screenWidth - 600, screenHeight - 40);
        textRenderer.endRendering();

    }

    private void drawButtons(GL2 gl) {
        drawButton(gl, mainPageButtonTexture, mainPageButtonRect);
        drawButton(gl, pauseButtonTexture, pauseButtonRect);
        drawButton(gl, restartButtonTexture, restartButtonRect);
        drawButton(gl, levelSelectionButtonTexture, levelSelectionButtonRect);
    }

    private void drawButton(GL2 gl, Texture texture, Rectangle2D rect) {
        if (texture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            texture.bind(gl);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f((float)rect.getX(), (float)rect.getY());
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f((float)(rect.getX() + rect.getWidth()), (float)rect.getY());
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f((float)(rect.getX() + rect.getWidth()), (float)(rect.getY() + rect.getHeight()));
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f((float)rect.getX(), (float)(rect.getY() + rect.getHeight()));
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
    }

    private void drawMaze(GL2 gl, float offsetX, float offsetY, float cellWidth, float cellHeight) {
        for (int j = 0; j < maze.getHeight(); j++) {
            for (int i = 0; i < maze.getWidth(); i++) {
                float cellX = offsetX + i * cellWidth;
                float cellY = offsetY + j * cellHeight;

                if (maze.getMazeData()[j][i] == 1) { // Wall
                    drawWall(gl, cellX, cellY, cellWidth, cellHeight);
                } else if (maze.getMazeData()[j][i] == 2) { // Exit
                    drawExit(gl, cellX, cellY, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawWall(GL2 gl, float x, float y, float width, float height) {
        if (wallTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            wallTexture.bind(gl);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(x, y);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(x + width, y);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(x + width, y + height);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(x, y + height);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        } else {
            // Fallback color
            gl.glColor3f(0.5f, 0.2f, 0.2f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(x, y); gl.glVertex2f(x + width, y);
            gl.glVertex2f(x + width, y + height); gl.glVertex2f(x, y + height);
            gl.glEnd();
        }
    }

    private void drawExit(GL2 gl, float x, float y, float width, float height) {
        gl.glColor3f(0.0f, 0.8f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(x, y); gl.glVertex2f(x + width, y);
        gl.glVertex2f(x + width, y + height); gl.glVertex2f(x, y + height);
        gl.glEnd();
    }

    private void drawHealth(GL2 gl) {
        if (heartTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            heartTexture.bind(gl);
            for (int i = 0; i < health; i++) {
                float heartX = 20 + i * (heartSize + 5);
                float heartY = screenHeight - heartSize - 20;
                gl.glBegin(GL2.GL_QUADS);
                gl.glTexCoord2f(0, 0); gl.glVertex2f(heartX, heartY);
                gl.glTexCoord2f(1, 0); gl.glVertex2f(heartX + heartSize, heartY);
                gl.glTexCoord2f(1, 1); gl.glVertex2f(heartX + heartSize, heartY + heartSize);
                gl.glTexCoord2f(0, 1); gl.glVertex2f(heartX, heartY + heartSize);
                gl.glEnd();
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
        } else {
            textRenderer.beginRendering(screenWidth, screenHeight);
            textRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            textRenderer.draw("Health: " + health, 20, screenHeight - 40);
            textRenderer.endRendering();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (heartTexture != null) heartTexture.destroy(drawable.getGL().getGL2());
        if (wallTexture != null) wallTexture.destroy(drawable.getGL().getGL2());
        if (robotTexture != null) robotTexture.destroy(drawable.getGL().getGL2());
        if (textRenderer != null) textRenderer.dispose();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width, 0, height, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        screenWidth = width;
        screenHeight = height;
        calculateCenterPosition();
        resetPosition();
        for (Item item : items) {
            item.updatePositions(mazeOffsetX, mazeOffsetY, this.width, this.height, maze);
        }

        // Update button positions
        mainPageButtonRect.setRect(screenWidth - 60, screenHeight - 40, 50, 30);
        pauseButtonRect.setRect(screenWidth - 120, screenHeight - 40, 50, 30);
        restartButtonRect.setRect(screenWidth - 180, screenHeight - 40, 50, 30);
        levelSelectionButtonRect.setRect(screenWidth - 240, screenHeight - 40, 50, 30);
    }
    
    private void initializeItems() {
        items.clear();
        int itemCount = Item.getItemCountForLevel(level);
        for (int i = 0; i < itemCount; i++) {
            Item item = new Item(level, i);
            item.init(mazeOffsetX, mazeOffsetY, width, height, maze);
            items.add(item);
        }
    }
}