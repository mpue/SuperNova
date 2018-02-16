package de.pueski.supernova.game;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.pueski.supernova.entities.IDrawable;
import de.pueski.supernova.tools.FontUtils.Alignment;
import de.pueski.supernova.tools.TextureUtil;
import de.pueski.supernova.ui.GLBarGraphDisplay;
import de.pueski.supernova.ui.GLColor3f;
import de.pueski.supernova.ui.GLGauge;
import de.pueski.supernova.ui.Text;

/**
 * <p>
 * This is the heads up display (HUD) for the game.
 * </p>
 * <p>
 * It currently displays 
 * <ul>
 * <li>The score</li>
 * <li>The time left</li>
 * <li>The energy</li>
 * <li>The ammo</li>
 * </ul>
 * 
 * @author Matthias Püski
 *
 */
public class HUD implements IDrawable{

	private boolean visible = true;

	private GLBarGraphDisplay energyDisplay;
	private GLBarGraphDisplay ammoDisplay;

	private Text scoreText;
	private Text timeLeftDisplay;
	private Text fpsDisplay;	
	private GLGauge shieldDisplay;
	
	private int miniHudId;	
	
	private float fps;
	
	private DisplayMode displayMode;

	private boolean fpsVisible = true;

	private long lastFrameTime;
	
	public HUD(DisplayMode mode) {
		this.displayMode = mode;
		energyDisplay = new GLBarGraphDisplay(100, 1.5f, 10, 60, true, true);
		energyDisplay.setBlinkInterval(250);
		energyDisplay.setBorderColor(new GLColor3f(0.003f, 0.32f, 0.59f));

		ammoDisplay = new GLBarGraphDisplay(100, 1.5f, 60, 20, true, true);
		ammoDisplay.setAngle(-90.0f);
		ammoDisplay.setBorderColor(new GLColor3f(0.003f, 0.32f, 0.59f));
		
		scoreText = new Text(100, 98, String.valueOf(0), 10);
		scoreText.setAlignment(Alignment.RIGHT);
		timeLeftDisplay = new Text(69, 74, "0:00", 10);
		
		miniHudId = TextureManager.getInstance().addTexture("images/hud/mini_hud.png");
		
		shieldDisplay = new GLGauge(0, 0, 80, 80);
		shieldDisplay.setColor(new GLColor3f(0.003f, 0.65f, 1f));
		shieldDisplay.setOpacity(0.7f);
		
		fpsDisplay = new Text(680 , displayMode.getHeight() - 20, "FPS : " + fps, 10);
		lastFrameTime = System.currentTimeMillis();
	}
	

	@Override
	public void draw() {
		
		if (!visible) {
			return;
		}
		
		energyDisplay.draw();
		ammoDisplay.draw();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		TextureUtil.drawTexture(0, 80, 80, 34, miniHudId);

		scoreText.draw();
		timeLeftDisplay.draw();
		shieldDisplay.draw();
		
		if (fpsVisible) {
			
			if (System.currentTimeMillis() - lastFrameTime > 1000) {
				fpsDisplay.setText("FPS : "+fps);
				lastFrameTime = System.currentTimeMillis();
			}
			
			fpsDisplay.draw();				
		}
		
		
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setEnergyBlinking(boolean blinking) {
		energyDisplay.setBlinking(blinking);
	}
	
	public void setEnergy(int energy) {
		energyDisplay.setValue(energy);
	}
	
	public void setAmmo(int ammo) {
		ammoDisplay.setValue(ammo);
	}
	
	public void setScore(String score) {
		scoreText.setText(score);		
	}
	
	public void setTimeLeft(String time) {
		timeLeftDisplay.setText(time);
	}
	
	public void setShieldTimeLeFt(int seconds) {
		shieldDisplay.setValue(seconds);
	}
	
	public void setFps(float fps) {
		this.fps = fps;
	}
}
