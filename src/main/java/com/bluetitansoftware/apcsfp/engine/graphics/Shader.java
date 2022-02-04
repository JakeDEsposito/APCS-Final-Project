package com.bluetitansoftware.apcsfp.engine.graphics;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader
{
	
	private String vertexSource, fragmentSource, filePath;
	private int vertexID, fragmentID, programID;
	
	private boolean isBeingUsed;
	
	public Shader(String filePath)
	{
		this.filePath = filePath;
		try
		{
			String source = new String(Files.readAllBytes(Paths.get(filePath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
			
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\r\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\r\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if(firstPattern.equals("vertex"))
				vertexSource = splitString[1];
			else if(firstPattern.equals("fragment"))
				fragmentSource = splitString[1];
			else
				throw new IOException("Unexpected token '" + firstPattern + "'");
			
			if(secondPattern.equals("vertex"))
				vertexSource = splitString[2];
			else if(secondPattern.equals("fragment"))
				fragmentSource = splitString[2];
			else
				throw new IOException("Unexpected token '" + secondPattern + "'");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			assert false : "Error: Could not open file for shader: '" + filePath + "'";
		}
	}
	
	public void create()
	{
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);
		
		if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Vertex Shader in '" + filePath + "':\n" + glGetShaderInfoLog(vertexID));
			return;
		}
		
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);
		
		if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Fragment Shader in '" + filePath + "':\n" + glGetShaderInfoLog(fragmentID));
			return;
		}
		
		programID = glCreateProgram();
		
		glAttachShader(programID, vertexID);
		glAttachShader(programID, fragmentID);
		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
		{
			System.err.println("Program Linking in '" + filePath + "':\n" + glGetProgramInfoLog(programID));
			return;
		}
		
		glValidateProgram(programID);
		if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE)
		{
			System.err.println("Program Validation in '" + filePath + "'\n" + glGetProgramInfoLog(programID));
			return;
		}
	}
	
	public int getUniformLocation(String name) {
		return glGetUniformLocation(programID, name);
	}
	
	public void setUniform(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int[] value) {
		glUniform1iv(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, boolean value) {
		glUniform1i(getUniformLocation(name), value ? 1 : 0);
	}
	
	public void setUniform(String name, Vector2f value) {
		glUniform2f(getUniformLocation(name), value.x(), value.y());
	}
	
	public void setUniform(String name, Vector3f value) {
		glUniform3f(getUniformLocation(name), value.x(), value.y(), value.z());
	}
	
	public void setUniform(String name, Vector4f value) {
		glUniform4f(getUniformLocation(name), value.w(), value.x(), value.y(), value.z());
	}
	
	public void setUniform(String name, Matrix3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(9);
	        value.get(fb);
	        glUniformMatrix3fv(getUniformLocation(name), false, fb);
	    }
	}
	
	public void setUniform(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        glUniformMatrix4fv(getUniformLocation(name), false, fb);
	    }
	}
	
	public void setTexture(String name, int slot) {
		glUniform1i(getUniformLocation(name), slot);
	}
	
	public void bind()
	{
		if(!isBeingUsed)
		{
			glUseProgram(programID);
			isBeingUsed = true;
		}
	}
	
	public void unbind()
	{
		glUseProgram(0);
		isBeingUsed = false;
	}
	
	public void close()
	{
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		glDeleteProgram(programID);
	}
	
}
