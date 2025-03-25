package Game;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class MainPage implements GLEventListener {

    private TextRenderer textRenderer;
    public Rectangle2D startButtonRect;
    public boolean isHovering = false;

    private Rectangle2D exitButtonRect;

    // เพิ่ม texture พื้นหลัง
    private Texture backgroundTexture;
    private Texture startButtonTexture;
    private Texture ExitButtonTexture;
    private Texture nameGameTexture;

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // วาดพื้นหลัง
        drawBackground(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());

        // Draw the nameGameTexture
        if (nameGameTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            nameGameTexture.bind(gl);

            float imageWidth = nameGameTexture.getWidth();
            float imageHeight = nameGameTexture.getHeight();
            float imageX = (drawable.getSurfaceWidth() - imageWidth) / 2;
            float imageY = drawable.getSurfaceHeight() * 0.7f - imageHeight / 2;

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(imageX, imageY);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(imageX + imageWidth, imageY);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(imageX + imageWidth, imageY + imageHeight);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(imageX, imageY + imageHeight);
            gl.glEnd();

            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

        // Draw the start button as an image with increased size
        float buttonWidth = 250; // Increased width
        float buttonHeight = 75; // Increased height
        float buttonX = (drawable.getSurfaceWidth() - buttonWidth) / 2;
        float buttonY = drawable.getSurfaceHeight() / 2 - buttonHeight / 2;

        startButtonRect = new Rectangle2D.Float(buttonX, buttonY, buttonWidth, buttonHeight);

        if (startButtonTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            startButtonTexture.bind(gl);

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(buttonX, buttonY);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(buttonX + buttonWidth, buttonY);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(buttonX + buttonWidth, buttonY + buttonHeight);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(buttonX, buttonY + buttonHeight);
            gl.glEnd();

            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

        // Draw the exit button as an image
        float exitButtonWidth = 250; // Same width as start button
        float exitButtonHeight = 75; // Same height as start button
        float exitButtonX = (drawable.getSurfaceWidth() - exitButtonWidth) / 2;
        float exitButtonY = buttonY - 120; // Adjust position to be below the start button

        exitButtonRect = new Rectangle2D.Float(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);

        if (ExitButtonTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            ExitButtonTexture.bind(gl);

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(exitButtonX, exitButtonY);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(exitButtonX + exitButtonWidth, exitButtonY);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(exitButtonX + exitButtonWidth, exitButtonY + exitButtonHeight);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(exitButtonX, exitButtonY + exitButtonHeight);
            gl.glEnd();

            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

        // ทำให้แน่ใจว่าทุกคำสั่งถูกประมวลผล
        gl.glFlush();
    }

    private void drawBackground(GL2 gl, int width, int height) {
        if (backgroundTexture != null) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            backgroundTexture.bind(gl);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(width, 0);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(width, height);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(0, height);
            gl.glEnd();

            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        if (textRenderer != null) {
            textRenderer.dispose();
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

        System.out.println("MainPage initialized"); // Debugging statement

        try {
            File backgroundFile = new File("C:\\Computer Graphics\\2DGame\\BG.jpg");
            if (backgroundFile.exists()) {
                backgroundTexture = TextureIO.newTexture(backgroundFile, true);
            } else {
                System.err.println("ไม่พบไฟล์รูปภาพพื้นหลัง: " + backgroundFile.getAbsolutePath());
            }

            // Load the heart texture
            File nameFile = new File("C:\\Computer Graphics\\2DGame\\NameGame.png");
            if (nameFile.exists()) {
                nameGameTexture = TextureIO.newTexture(nameFile, true);
                System.out.println("nameGameTexture loaded successfully");

                // Enable blending for transparency
                gl.glEnable(GL2.GL_BLEND);
                gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                System.err.println("Heart texture file not found: " + nameFile.getAbsolutePath());
            }

         // Load the start button texture
            File startButtonFile = new File("C:\\Computer Graphics\\2DGame\\Play.png");
            if (startButtonFile.exists()) {
                startButtonTexture = TextureIO.newTexture(startButtonFile, true);
                System.out.println("Start button texture loaded successfully");
            } else {
                System.err.println("Start button texture file not found: " + startButtonFile.getAbsolutePath());
            }
            
         // Load the ExitButtonTexture texture
            File ExitButtonFile = new File("C:\\Computer Graphics\\2DGame\\Exit.png");
            if (ExitButtonFile.exists()) {
            	ExitButtonTexture = TextureIO.newTexture(ExitButtonFile, true);
                System.out.println("ExitButtonTexture loaded successfully");
            } else {
                System.err.println("ExitButtonTexture file not found: " + ExitButtonFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to load textures: " + e.getMessage());
            e.printStackTrace();
        }

        Randers.getWindow().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                if (startButtonRect != null && startButtonRect.contains(mouseX, mouseY)) {
                    System.out.println("กดปุ่มเริ่มเกม!");
                    goToLevelSelection(); // Transition to LevelSelection
                }

                if (exitButtonRect != null && exitButtonRect.contains(mouseX, drawable.getSurfaceHeight() - mouseY)) {
                    System.out.println("ออกจากเกม!");
                    Randers.getWindow().destroy(); // Close JOGL window
                    System.exit(0); // Exit program
                }
            }
        });
    }

    private void goToLevelSelection() {
        Randers.setGLEventListener(new LevelSelection());
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
    }
}