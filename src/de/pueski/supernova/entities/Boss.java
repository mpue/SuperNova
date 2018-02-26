package de.pueski.supernova.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.lwjgl.opengl.Display;

import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;

@XmlType(propOrder = {"appereanceTime","finalBoss"}, name="Boss")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Enemy.class)

public class Boss extends Enemy implements IExplodable {
	
	private int appereanceTime;

	private boolean finalBoss;
	
	public Boss() {
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {		
		Boss boss = new Boss();
		boss.index = System.currentTimeMillis();
		boss.texId = TextureManager.getInstance().addTexture(texture);			
		boss.lastShotTime = System.currentTimeMillis();
		boss.explosionTexId = TextureManager.getInstance().addTexture(getExplosionTex());
		boss.shootSound = SoundManager.getInstance().getSound("audio/zap.wav");
		boss.explosionSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
		boss.impactSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
		boss.shotInterval = shotInterval;
		boss.explosionSize = explosionSize;		
		boss.setSpeed(speed);
		boss.hitPoints = hitPoints;
		boss.setTexture(texture);
		boss.setFinalBoss(finalBoss);
		boss.setRotationSpeed(rotationSpeed);
		boss.setBulletOffsetX(bulletOffsetX);
		boss.setBulletOffsetY(bulletOffsetY);
		boss.width = width;
		boss.height = height;
		boss.scale = scale;
		boss.impactSize = impactSize;
		boss.drawImpacts = drawImpacts;
		return boss;		
	}

	
	@Override
	public void fly() {		
		if (yLoc > Display.getHeight() - height / 2 )		
			yLoc -= speed;	
		
		// super.fly();

	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (obj.getClass() == getClass()) {
			Boss other = (Boss) obj;
			return (index == other.index);
		}

		return false;
	}

	public int getAppereanceTime() {
		return appereanceTime;
	}

	public void setAppereanceTime(int appereanceTime) {
		this.appereanceTime = appereanceTime;
	}

	public boolean isFinalBoss() {
		return finalBoss;
	}

	public void setFinalBoss(boolean finalBoss) {
		this.finalBoss = finalBoss;
	}

	
}
