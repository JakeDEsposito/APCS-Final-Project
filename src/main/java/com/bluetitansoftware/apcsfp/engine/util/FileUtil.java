package com.bluetitansoftware.apcsfp.engine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil
{
	public static String loadAsString(String path)
	{
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtil.class.getResourceAsStream(path))))
		{
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result.append(line).append("\n");
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.err.println("Could not find the file at " + path);
		}
		return result.toString();
	}
}
