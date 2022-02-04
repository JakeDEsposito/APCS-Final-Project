package com.bluetitansoftware.apcsfp.engine.io;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input
{
	
	private static boolean[] keys = new boolean[GLFW_KEY_LAST];
	private static boolean[] keysFlag = new boolean[GLFW_KEY_LAST];
	private static boolean[] keysToggle = new boolean[GLFW_KEY_LAST];
	
	private static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] buttonsFlag = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] buttonsToggle = new boolean[GLFW_MOUSE_BUTTON_LAST];
	
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;
	
	private static boolean inWindow;
	
	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButton;
	private GLFWScrollCallback mouseScroll;
	private GLFWCursorEnterCallback mouseWindow;
	
	private static long inputWindow;
	
	public Input(Window window) {
		
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW_RELEASE);
			}
		};
				
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButton = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW_RELEASE);
			}
		};
				
		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
		
		mouseWindow = new GLFWCursorEnterCallback() {
			public void invoke(long window, boolean entered) {
				inWindow = entered;
			}
		};
		
		inputWindow = window.glfwWindow;
		
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isKeyUp(int key) {
		return !keys[key];
	}
	
	public static boolean isKeyPress(int key) {
		if(isKeyDown(key) && !keysFlag[key]) {
			keysFlag[key] = true;
			keysToggle[key] = !keysToggle[key];
			return true;
		}
		if(isKeyUp(key)) keysFlag[key] = false;
		return false;
	}
	
	public static boolean keyToggle(int key) {
		return keysToggle[key];
	}
	
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public static boolean isButtonUp(int button) {
		return !buttons[button];
	}
	
	public static boolean isButtonPress(int button) {
		if(isButtonDown(button) && !buttonsFlag[button]) {
			buttonsFlag[button] = true;
			buttonsToggle[button] = !buttonsToggle[button];
			return true;
		}
		if(isButtonUp(button)) buttonsFlag[button] = false;
		return false;
	}
	
	public static boolean buttonToggle(int button) {
		return buttonsToggle[button];
	}
	
	public static void mouseState(boolean lock)
	{
		glfwSetInputMode(inputWindow, GLFW_CURSOR, lock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}
	
	public void close() {
		keyboard.free();
		mouseMove.free();
		mouseButton.free();
		mouseScroll.free();
		mouseWindow.free();
	}
	
	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getMouseOrthoX()
	{
		double currentX = getMouseX();
		currentX = (currentX / Window.width()) * 2 - 1;
		Vector4f temp = new Vector4f((float)currentX, 0, 0, 1);
		temp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
		currentX = temp.x();
		return currentX;
	}
	
	public static double getMouseOrthoY()
	{
		double currentY = getMouseY();
		currentY = (currentY / Window.height()) * 2 - 1;
		Vector4f temp = new Vector4f(0, (float)currentY, 0, 1);
		temp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
		currentY = Window.height() - temp.y() - 50;
		return currentY;
	}
	
	public static double getScrollX() {
		return scrollX;
	}

	public static double getScrollY() {
		return scrollY;
	}
	
	public static boolean inWindow() {
		return inWindow;
	}
	
	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButton;
	}
	
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
	
	public GLFWCursorEnterCallback getMouseWindowCallback() {
		return mouseWindow;
	}
	
}
