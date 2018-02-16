package de.pueski.supernova.entities;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.TextureUtil;

public class Bullet extends Entity {

	public static enum BulletColor {
		RED,
		GREEN,
		VIOLET,
		BLUE
	}
	
	private static final HashMap<BulletColor, String> imageMap = new HashMap<Bullet.BulletColor, String>();
	
	static {
		imageMap.put(BulletColor.RED, "images/weapons/laser.png");
		imageMap.put(BulletColor.BLUE, "images/weapons/laser_blue.png");
		imageMap.put(BulletColor.GREEN, "images/weapons/laser_green.png");
		imageMap.put(BulletColor.VIOLET, "images/weapons/laser_violet.png");
	}
	
	private static final float speed = 10.0f;
	
	private int texId;
	
	private long index; 

	
	public Bullet(BulletColor color,float xLoc, float yLoc, Direction direction) {
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.index = System.currentTimeMillis();
		this.texId = TextureManager.getInstance().getTexture(imageMap.get(color));	
		this.direction = direction;
		this.explosionSize = 5.0f;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			Bullet other = (Bullet)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void fly() {
		yLoc += speed*direction.getValue();
	}
	
	public void draw() {
		GL11.glColor3f(1.0f,1.0f,1.0f);
		TextureUtil.drawTexture(0, xLoc, yLoc, explosionSize * 2, explosionSize * 2, texId);
	}
	
}
