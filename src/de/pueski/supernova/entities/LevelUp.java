package de.pueski.supernova.entities;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.TextureUtil;

public class LevelUp extends Entity {

	private static final float speed = 2.0f;
	
	private int texId;	
	private long index; 
	
	private float rot = 90.0f;
	private float rotationSpeed = 0.2f; 
	
	public LevelUp(float xLoc, float yLoc, int direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();	
		this.texId = TextureManager.getInstance().addTexture("images/items/levelup.png");
		this.width = 64.0f;
		this.height = 64.0f;
		this.scale = 1.0f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			LevelUp other = (LevelUp)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void fly() {
		yLoc -= speed;
	}
	
	public void draw() {
		GL11.glColor3f(1.0f,1.0f,1.0f);
		TextureUtil.drawTexture(rot, xLoc, yLoc, width * scale, height * scale, texId);
		rot += rotationSpeed;
	}
	
}
