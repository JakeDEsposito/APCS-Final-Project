package com.bluetitansoftware.apcsfp;

import com.bluetitansoftware.apcsfp.engine.io.Window;

public class App 
{
	public static void main( String[] args )
    {
        System.out.println("Running apcsfp client");

        Window window = new Window();
        window.run();
        
        System.out.println("Shutting down apcsfp");
    }
	
}
