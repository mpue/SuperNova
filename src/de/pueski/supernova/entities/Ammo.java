package de.pueski.supernova.entities;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.TextureUtil;

public class Ammo extends Entity {

	private static final float speed = 2.0f;
	
	private int texId;	
	private long index; 

	private int amount = 200;

	private float rot = 90.0f;
	private float rotationSpeed = 0.2f; 
	
	public Ammo(float xLoc, float yLoc, int direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();
		this.texId = TextureManager.getInstance().addTexture("images/items/ammo.png");
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
			Ammo other = (Ammo)obj;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
