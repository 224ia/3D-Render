package Input.Software;

import Input.Input;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public final class SoftwareInput extends Input implements KeyListener {
    private final boolean[] keys = new boolean[Keys.values().length];

    public SoftwareInput(JFrame frame) {
        frame.addKeyListener(this);
    }

    public boolean isKeyPressed(int key) {
        return keys[findKey(key)];
    }

    private int findKey(int key) {
        return switch (key) {
            case GLFW_KEY_W -> 0;
            case GLFW_KEY_S -> 1;
            case GLFW_KEY_A -> 2;
            case GLFW_KEY_D -> 3;
            case GLFW_KEY_Q -> 4;
            case GLFW_KEY_E -> 5;
            default -> -1;
        };
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (Keys key : Keys.values()) {
            if (key.getKeyCode() == keyCode) {
                keys[key.getIndex()] = true;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (Keys key : Keys.values()) {
            if (key.getKeyCode() == keyCode) {
                keys[key.getIndex()] = false;
                break;
            }
        }
    }
}
