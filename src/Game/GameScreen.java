package Game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class GameScreen implements GLEventListener {
    private Maze maze;

    public GameScreen(Maze maze) {
        this.maze = maze;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        maze.init(drawable);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        maze.display(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        maze.dispose(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        maze.reshape(drawable, x, y, width, height);
    }
}