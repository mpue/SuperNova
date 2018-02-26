package de.pueski.supernova.controller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.pueski.supernova.SuperNova;

public class MenuController implements ScreenController{

	private Nifty nifty;
	
	
	public MenuController() {
		
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
		SuperNova.getInstance().setMenuIsShowing(true);
		nifty.gotoScreen("levelSelect");
		
		// SuperNova.getInstance().startGame();
	}
	
	public void showSettings() {
		SuperNova.getInstance().setMenuIsShowing(true);
		nifty.gotoScreen("menu");
	}
	
	public void exitGame() {
		SuperNova.getInstance().setFinished(true);
	}
	
}
