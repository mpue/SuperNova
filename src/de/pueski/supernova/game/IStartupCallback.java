package de.pueski.supernova.game;

import org.lwjgl.opengl.DisplayMode;

public interface IStartupCallback {

	public void startup(DisplayMode mode, boolean fullscreen, boolean vsync) throws Exception;
	
}
