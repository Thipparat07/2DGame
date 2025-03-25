package Game;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Randers implements GLEventListener {
    private static GLWindow window = null;
    private static GLEventListener currentListener = null;

    public static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);

        window = GLWindow.create(caps);
        window.setSize(640, 640);
        window.setResizable(false);
        window.setTitle("หุ่นยนต์ 2D");

        // Set initial listener
        setGLEventListener(new MainPage());

        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start();
        window.setVisible(true);
    }

    public static void main(String[] args) {
        init();
    }

    public static void setGLEventListener(GLEventListener listener) {
        if (currentListener != null) {
            window.removeGLEventListener(currentListener);
        }
        currentListener = listener;
        window.addGLEventListener(listener);
    }

    public static GLWindow getWindow() {
        return window;
    }

    @Override
    public void display(GLAutoDrawable arg0) {
        // No implementation needed here
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // No implementation needed here
    }

    @Override
    public void init(GLAutoDrawable arg0) {
        // No implementation needed here
    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
        // No implementation needed here
    }
}