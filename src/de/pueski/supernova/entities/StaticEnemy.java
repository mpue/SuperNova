package de.pueski.supernova.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.pueski.supernova.SuperNova;
import de.pueski.supernova.data.Game;
import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;

@XmlType(name = "StaticEnemy", propOrder = {"destroyedTexture", "destroyable"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Enemy.class)
public class StaticEnemy extends Enemy implements IExplodable {

	private static final Log log = LogFactory.getLog(StaticEnemy.class);

	@XmlTransient
	private Game game;

	private boolean destroyable = false;
	
	@XmlTransient
	private boolean destroyed = false;
	
	@XmlTransient
	private int destroyedTex;
	
	private String destroyedTexture;
	
	public StaticEnemy() {
		log.debug("created StaticEnemy");
	}

	public StaticEnemy(Game game) {
		this.setGame(game);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		StaticEnemy enemy = new StaticEnemy(getGame());
		enemy.index = System.currentTimeMillis();
		enemy.texId = TextureManager.getInstance().addTexture(texture);
		enemy.lastShotTime = System.currentTimeMillis();
		enemy.explosionTexId = TextureManager.getInstance().addTexture(getExplosionTex());
		enemy.destroyedTexture = destroyedTexture;
		enemy.destroyedTex = TextureManager.getInstance().addTexture(destroyedTexture);
		enemy.shootSound = SoundManager.getInstance().getSound("audio/zap.wav");
		enemy.explosionSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
		enemy.impactSource = SoundManager.getInstance().getSound("audio/explosion2.wav"); 
		enemy.shotInterval = shotInterval;
		enemy.explosionSize = explosionSize;
		enemy.speed = speed;
		enemy.hitPoints = hitPoints;
		enemy.texture = texture;
		enemy.rotationSpeed = rotationSpeed;
		enemy.bulletOffsetX = bulletOffsetX;
		enemy.bulletOffsetY = bulletOffsetY;
		enemy.width = width;
		enemy.height = height;
		enemy.scale = scale;
		enemy.xLoc = xLoc;
		enemy.yLoc = yLoc;
		enemy.impactSize = impactSize;
		enemy.drawImpacts = drawImpacts;
		enemy.destroyable = destroyable;
		return enemy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (obj.getClass() == getClass()) {
			StaticEnemy other = (StaticEnemy) obj;
			return (index == other.index);
		}

		return false;
	}

	@Override
	public Bullet shoot() {
		return null;
	}

	@Override
	public void fly() {
		yLoc -= SuperNova.getInstance().getVelocity();
	}

	@Override
	protected void drawImpact(Impact impact) {
		if (!destroyed) {
			super.drawImpact(impact);
		}			
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void setYLoc(float yLoc) {
	}

	public int getDestroyedTex() {
		return destroyedTex;
	}

	public void setDestroyedTex(int destroyedTex) {
		this.destroyedTex = destroyedTex;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {		
		if (destroyable) {
			this.destroyed = destroyed;
			if (destroyed) {
				texId = destroyedTex;
			}			
		}		
	}

	public String getDestroyedTexture() {
		return destroyedTexture;
	}

	public void setDestroyedTexture(String destroyedTexture) {
		this.destroyedTexture = destroyedTexture;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}
	
	
}
