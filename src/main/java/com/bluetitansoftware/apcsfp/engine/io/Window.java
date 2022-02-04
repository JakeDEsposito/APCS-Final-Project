package com.bluetitansoftware.apcsfp.engine.io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import com.bluetitansoftware.apcsfp.engine.scene.*;

public class Window
{
	
	private static int width;
	private static int height;
	private String title;
	private boolean vsync;
	
	protected long glfwWindow;
	public Input input;
	private GLFWWindowSizeCallback sizeCallback;
	public static boolean exit;
	public static Vector3f background = new Vector3f();
	
	private static Scene currentScene = null;
	
	public Window(int width, int height, String title, boolean vsync)
	{
		Window.width = width;
		Window.height = height;
		this.title = title;
		this.vsync = vsync;
		Window.exit = false;
	}

	public Window()
	{
		Window.width = 1080;
		Window.height = 720;
		this.title = "APCSFP";
		this.vsync = true;
		Window.exit = false;
	}
	
	public void run()
	{
		init();
		loop();
		close();
	}
	
	private void init()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
		if (glfwWindow == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		input = new Input(this);
		
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
			}
		};
		glfwSetKeyCallback(glfwWindow, input.getKeyboardCallback());
		glfwSetCursorPosCallback(glfwWindow, input.getMouseMoveCallback());
		glfwSetMouseButtonCallback(glfwWindow, input.getMouseButtonCallback());
		glfwSetScrollCallback(glfwWindow, input.getMouseScrollCallback());
		glfwSetCursorEnterCallback(glfwWindow, input.getMouseWindowCallback());
		glfwSetWindowSizeCallback(glfwWindow, sizeCallback);
		
		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(vsync ? 1 : 0);
		glfwShowWindow(glfwWindow);
		
		GL.createCapabilities();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		Window.changeScene(0);
	}
	
	private void loop()
	{
		float beginTime = (float)glfwGetTime();
		float endTime;
		float dt = -1;
		
		while (!glfwWindowShouldClose(glfwWindow) && !exit)
		{
			glfwPollEvents();
			
			glClearColor(background.x(), background.y(), background.z(), 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			if(dt >= 0)
				currentScene.update(dt);
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
			
			if(Input.isKeyDown(GLFW_KEY_ESCAPE)) exit = true;
		}
	}
	
	private void close()
	{
		currentScene.close();
		
		input.close();
		sizeCallback.free();
		glfwWindowShouldClose(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static void changeScene(int newScene)
	{
		if(currentScene != null)
			currentScene.close();
		
		switch(newScene)
		{
		case 0:
			currentScene = new TitleScene();
			currentScene.init();
			currentScene.start();
			break;
		case 1:
			currentScene = new GameScene();
			currentScene.init();
			currentScene.start();
			break;
		default:
			assert false : "Unknown scene '" + newScene + "'";
			break;
		}
	}
	
	public static Scene getScene()
	{
		return currentScene;
	}

	public static Vector3f background()
	{
		return background;
	}
	
	public static double width()
	{
		return width;
	}
	
	public static double height()
	{
		return height;
	}
	
}
