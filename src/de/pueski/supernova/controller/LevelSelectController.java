package de.pueski.supernova.controller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.pueski.supernova.SuperNova;

public class LevelSelectController implements ScreenController{

	private Nifty nifty;
	
	
	public LevelSelectController() {
		
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {		
		this.nifty = nifty;
	}

	@Override
	public void onEndScreen() {		
	}

	@Override
	public void onStartScreen() {
		
	}

	public void startGame() {
		SuperNova.getInstance().startGame();
	}

}
