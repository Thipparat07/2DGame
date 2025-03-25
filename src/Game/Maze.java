package Game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;

public class Maze implements GLEventListener {
    protected Texture wallTexture;
    protected int[][] mazeData;
    protected int mazeWidth;
    protected int mazeHeight;

    public Maze() {
        // Default constructor
    }

    public Maze(int[][] mazeData) {
        this.mazeData = mazeData;
        this.mazeWidth = mazeData[0].length;
        this.mazeHeight = mazeData.length;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    public boolean isExit(int x, int y) {
        return y >= 0 && y < getHeight() && x >= 0 && x < getWidth() && mazeData[y][x] == 2;
    }

    public int getWidth() {
        return mazeWidth;
    }

    public int getHeight() {
        return mazeHeight;
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= mazeWidth || y < 0 || y >= mazeHeight) {
            return false;
        }
        return mazeData[y][x] == 0 || mazeData[y][x] == 2;
    }

    public int[][] getMazeData() {
        return mazeData;
    }

    public void draw(GL2 gl, float offsetX, float offsetY, float cellWidth, float cellHeight) {
        for (int j = 0; j < mazeHeight; j++) {
            for (int i = 0; i < mazeWidth; i++) {
                float cellX = offsetX + i * cellWidth;
                float cellY = offsetY + j * cellHeight;

                if (mazeData[j][i] == 1) {
                    if (wallTexture != null) {
                        gl.glEnable(GL2.GL_TEXTURE_2D);
                        wallTexture.bind(gl);
                        gl.glColor3f(1.0f, 1.0f, 1.0f);

                        gl.glBegin(GL2.GL_QUADS);
                        gl.glTexCoord2f(0.0f, 0.0f);
                        gl.glVertex2f(cellX, cellY);
                        gl.glTexCoord2f(1.0f, 0.0f);
                        gl.glVertex2f(cellX + cellWidth, cellY);
                        gl.glTexCoord2f(1.0f, 1.0f);
                        gl.glVertex2f(cellX + cellWidth, cellY + cellHeight);
                        gl.glTexCoord2f(0.0f, 1.0f);
                        gl.glVertex2f(cellX, cellY + cellHeight);
                        gl.glEnd();

                        gl.glDisable(GL2.GL_TEXTURE_2D);
                    } else {
                        // Fallback to alternating red shades
                        switch ((i + j) % 3) {
                            case 0:
                                gl.glColor3f(1.0f, 0.5f, 0.5f);
                                break;
                            case 1:
                                gl.glColor3f(1.0f, 0.3f, 0.3f);
                                break;
                            case 2:
                                gl.glColor3f(1.0f, 0.1f, 0.1f);
                                break;
                        }
                        gl.glBegin(GL2.GL_QUADS);
                        gl.glVertex2f(cellX, cellY);
                        gl.glVertex2f(cellX + cellWidth, cellY);
                        gl.glVertex2f(cellX + cellWidth, cellY + cellHeight);
                        gl.glVertex2f(cellX, cellY + cellHeight);
                        gl.glEnd();
                    }
                } else if (mazeData[j][i] == 2) {
                    gl.glColor3f(0.0f, 0.8f, 0.0f);
                    gl.glBegin(GL2.GL_QUADS);
                    gl.glVertex2f(cellX, cellY);
                    gl.glVertex2f(cellX + cellWidth, cellY);
                    gl.glVertex2f(cellX + cellWidth, cellY + cellHeight);
                    gl.glVertex2f(cellX, cellY + cellHeight);
                    gl.glEnd();
                }
            }
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Implement display logic if needed
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (wallTexture != null) {
            wallTexture.destroy(drawable.getGL().getGL2());
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Implement reshape logic if needed
    }
}

//Maze Level 1
class MazeLV_1 extends Maze {
    public MazeLV_1() {
        super(new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,1,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,1,0,1},
            {1,0,1,1,1,1,1,1,1,0,1,0,1,0,1},
            {1,0,1,0,0,0,0,0,1,0,1,0,1,0,1},
            {1,0,1,0,1,1,1,0,1,0,1,0,1,0,1},
            {1,0,1,0,1,0,0,0,1,0,1,0,1,0,1},
            {1,0,1,0,1,0,1,1,1,0,1,0,1,0,1},
            {1,0,1,0,1,0,0,0,0,0,1,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,2,1}
        });
    }
}

//Maze Level 2
class MazeLV_2 extends Maze {
    public MazeLV_2() {
        super(new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,1,1,0,1,0,1},
            {1,0,1,0,1,0,0,0,0,0,1,0,1,0,1},
            {1,0,1,0,1,0,1,1,1,0,1,0,1,0,1},
            {1,0,1,0,1,0,1,0,0,0,1,0,1,0,1},
            {1,0,1,0,1,0,1,0,1,1,1,0,1,0,1},
            {1,0,1,0,1,0,1,0,0,0,0,0,1,0,1},
            {1,0,1,0,1,0,1,1,1,1,1,1,1,0,1},
            {1,0,1,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,2,1}
        });
    }
}

//Maze Level 3
class MazeLV_3 extends Maze {
    public MazeLV_3() {
        super(new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1},
            {1,0,0,0,1,0,1,0,0,0,1,0,1,0,1},  // Added dead end after 3rd column
            {1,1,1,0,1,0,1,1,1,0,1,0,1,0,1},  // Added wall to create dead end
            {1,0,0,0,1,0,0,0,1,0,1,0,1,0,1},  // Forced turn creates dead end
            {1,0,1,1,1,1,1,0,1,0,1,0,1,0,1},  // Added wall to block straight path
            {1,0,0,0,0,0,1,0,1,0,1,0,1,0,1},  // Extended dead end path
            {1,1,1,1,1,0,1,0,1,0,1,0,1,0,1},  // Added winding section
            {1,0,0,0,0,0,1,0,1,0,1,0,1,0,1},  // Parallel dead end path
            {1,0,1,1,1,1,1,0,1,0,1,0,1,0,1},  // Added more barriers
            {1,0,0,0,0,0,0,0,1,0,1,0,1,0,1},  // False exit path
            {1,0,1,1,1,1,1,1,1,0,1,0,1,0,1},  // Extended dead end
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},  // Main path remains
            {1,1,1,1,1,1,1,1,1,1,1,1,1,2,1}
        });
    }
}