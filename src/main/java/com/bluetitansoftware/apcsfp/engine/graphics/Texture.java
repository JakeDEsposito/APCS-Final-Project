package com.bluetitansoftware.apcsfp.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

public class Texture
{
	
	private String filePath;
	private int texID;
	
	private int width, height;
	
	public Texture(String filePath)
	{
		this.filePath = filePath;
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		
		if(image != null)
		{
			this.width = width.get(0);
			this.height = height.get(0);
			
			int format = -1;
			if(channels.get(0) == 3)
				format = GL_RGB;
			else if(channels.get(0) == 4)
				format = GL_RGBA;
			else
				assert false : "Error (Texture) Unknown number of channels '" + channels.get(0) + "'";
			
			glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, image);
		}
		else
			assert false : "Error: (Texture) Could not load image at '" + filePath + "'";
		
		stbi_image_free(image);
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getFilePath()
	{
		return filePath;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
}
