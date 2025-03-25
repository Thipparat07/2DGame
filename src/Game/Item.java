package Game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Item {
    private float x, y;
    private final float size = 20;
    private boolean collected = false;
    private Random random = new Random();
    private int level;
    private int itemIndex;
    private Texture itemTexture;

    private static final int[][][] LEVEL_ITEM_POSITIONS = {
        {{5, 2}}, // Level 1 - 1 item
        {{3, 5}, {5, 2}}, // Level 2 - 2 items
        {{7, 7}, {3, 5}, {5, 2}} // Level 3 - 3 items
    };

    public Item(int level, int itemIndex) {
        this.level = level;
        this.itemIndex = itemIndex;
        loadItemTexture();
    }

    private void loadItemTexture() {
        try {
            String texturePath;
            switch (level) {
                case 1: texturePath = "C:\\Computer Graphics\\2DGame\\level1.png"; break;
                case 2: texturePath = "C:\\Computer Graphics\\2DGame\\level2.png"; break;
                case 3: texturePath = "C:\\Computer Graphics\\2DGame\\level3.png"; break;
                default: texturePath = "C:\\Computer Graphics\\2DGame\\level1.png"; break;
            }
            File textureFile = new File(texturePath);
            if (textureFile.exists()) {
                itemTexture = TextureIO.newTexture(textureFile, true);
            } else {
                System.err.println("Item texture file not found: " + texturePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to load item texture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void init(float mazeOffsetX, float mazeOffsetY, float cellWidth, float cellHeight, Maze maze) {
        respawnItem(mazeOffsetX, mazeOffsetY, cellWidth, cellHeight, maze);
    }

    public void checkCollision(float playerX, float playerY, float playerWidth, float playerHeight) {
        if (!collected) {
            boolean collisionX = playerX + playerWidth >= x && x + size >= playerX;
            boolean collisionY = playerY + playerHeight >= y && y + size >= playerY;

            if (collisionX && collisionY) {
                collected = true;
                System.out.println("Item " + itemIndex + " collected in level " + level);
            }
        }
    }

    public void respawnItem(float mazeOffsetX, float mazeOffsetY, float cellWidth, float cellHeight, Maze maze) {
        if (level <= LEVEL_ITEM_POSITIONS.length && itemIndex < LEVEL_ITEM_POSITIONS[level-1].length) {
            int[] pos = LEVEL_ITEM_POSITIONS[level-1][itemIndex];
            x = mazeOffsetX + pos[0] * cellWidth + (cellWidth - size) / 2;
            y = mazeOffsetY + pos[1] * cellHeight + (cellHeight - size) / 2;
            collected = false;
        }
    }

    public void draw(GL2 gl) {
        if (!collected && itemTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            itemTexture.bind(gl);
            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f(x, y);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f(x + size, y);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f(x + size, y + size);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f(x, y + size);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void updateForNewLevel(int newLevel, float mazeOffsetX, float mazeOffsetY, 
            float cellWidth, float cellHeight, Maze maze) {
        this.level = newLevel;
        loadItemTexture(); // โหลด Texture ใหม่เมื่อเปลี่ยนเลเวล
        init(mazeOffsetX, mazeOffsetY, cellWidth, cellHeight, maze);
    }

    public void updatePositions(float mazeOffsetX, float mazeOffsetY, float cellWidth, float cellHeight, Maze maze) {
        if (!collected) {
            int[] pos = LEVEL_ITEM_POSITIONS[level-1][itemIndex];
            x = mazeOffsetX + pos[0] * cellWidth + (cellWidth - size) / 2;
            y = mazeOffsetY + pos[1] * cellHeight + (cellHeight - size) / 2;
        }
    }

    public int getRemainingItems() {
        return collected ? 0 : 1;
    }

    public void reset(float mazeOffsetX, float mazeOffsetY, float cellWidth, float cellHeight, Maze maze) {
        collected = false;
        respawnItem(mazeOffsetX, mazeOffsetY, cellWidth, cellHeight, maze);
    }

    public static int getItemCountForLevel(int level) {
        if (level > 0 && level <= LEVEL_ITEM_POSITIONS.length) {
            return LEVEL_ITEM_POSITIONS[level-1].length;
        }
        return 0;
    }
}