package de.pueski.supernova.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.SuperNova;
import de.pueski.supernova.entities.Bullet.BulletColor;
import de.pueski.supernova.entities.strategies.AbstractStrategy;
import de.pueski.supernova.entities.strategies.MoveForward;
import de.pueski.supernova.entities.strategies.Oscillate;
import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.TextureUtil;

@XmlType(propOrder = {"speed", "hitPoints", "explosionTex","texture","shotInterval", "bulletOffsetX", "bulletOffsetY", "rotationSpeed" , "impactSize", "drawImpacts"}, name="Enemy")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Entity.class)
public class Enemy extends Entity implements IExplodable {

	protected float speed;
	
	@XmlTransient
	private static int POINTS = 1000;
	
	private String explosionTex;
	
	@XmlTransient
	protected int gridSize = 8;

	protected int hitPoints;

	@XmlTransient
	protected long index;

	@XmlTransient
	protected int texId;

	@XmlTransient
	protected float x_i = 1f / 8;
	@XmlTransient
	protected float y_i = 1f / 8;

	@XmlTransient
	protected long lastShotTime;

	protected long shotInterval;

	@XmlTransient
	protected float rot = 0.0f;
	
	@XmlTransient
	protected int explosionTexId;

	@XmlTransient
	protected int shootSound;
	
	@XmlTransient
	protected int explosionSource;

	@XmlTransient
	private int explosionIndex;

	protected String texture;

	protected float bulletOffsetX = 0;
	
	protected float bulletOffsetY = 0;
	
	protected float rotationSpeed = 0;
	
	protected float impactSize;
	
	protected boolean drawImpacts = false;
	
	@XmlTransient
	private final ArrayList<Impact> impacts = new ArrayList<Impact>();

	@XmlTransient
	private final Random random = new Random();

	@XmlTransient
	protected int impactSource;
	
	@XmlTransient
	protected long hitTime;
	
	@XmlTransient
	private List<AbstractStrategy> strategies;
	
	@XmlTransient
	private int currentStrategy = 0;
	
	@XmlTransient
	public static final int MAX_STRATEGIES = 2;
	
	public Enemy() {
		strategies = new ArrayList<>();
		strategies.add(new Oscillate(this));
		strategies.add(new MoveForward(this));
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {		
		Enemy enemy = new Enemy();
		enemy.index = System.currentTimeMillis();
		enemy.texId = TextureManager.getInstance().addTexture(texture);			
		enemy.lastShotTime = System.currentTimeMillis();
		enemy.explosionTexId = TextureManager.getInstance().addTexture(getExplosionTex());
		enemy.shootSound = SoundManager.getInstance().getSound("audio/zap.wav");
		enemy.explosionSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
		enemy.impactSource = SoundManager.getInstance().getSound("audio/explosion_short.wav"); 
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
		enemy.impactSize = impactSize;
		enemy.drawImpacts = drawImpacts;
		enemy.hitTime = System.currentTimeMillis();
		
		return enemy;		
	}
	
	/**
	 * @param bullet 
	 * @return the hitPoints
	 */
	public int hit(Bullet bullet) {
		if (drawImpacts) {
			
			// add some randomness to the impact
			
			float _x = random.nextFloat() * 25;
			float _y = random.nextFloat() * 25;
			
			impacts.add(new Impact(bullet.xLoc + _x, bullet.yLoc + _y));
			
			if (!SoundManager.getInstance().isPlayingSound()) {
				
				if (System.currentTimeMillis() - hitTime > 100) {
					SoundManager.getInstance().playEffect(impactSource);					
				}
				
			}

		}
		hitPoints--;
		
		hitTime = System.currentTimeMillis();
		
		return hitPoints;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (obj.getClass() == getClass()) {
			Enemy other = (Enemy) obj;
			return (index == other.index);
		}

		return false;
	}

	public void fly() {
		this.strategies.get(this.currentStrategy).fly();
	}

	public void draw() {
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		TextureUtil.drawTexture(rot, xLoc, yLoc, width * scale, height * scale, texId);

		rot += rotationSpeed;				
		
		for (Iterator<Impact> it = impacts.iterator();it.hasNext();) {		
			Impact impact = it.next();
			if (impact.getFrame() < 63) {
				drawImpact(impact);
				impact.nextFrame();
			}
			else {
				it.remove();
			}			
		}		
		
		rot += rotationSpeed;
	}

	protected void drawImpact(Impact impact) {
		TextureUtil.drawAnimationFrame(impact.getFrame(), gridSize, 1.5f, 0, impact.getX(), impact.getY(), impactSize, explosionTexId);		
	}
	
	public void drawExplosion(int frame) {
		TextureUtil.drawAnimationFrame(frame, gridSize, 1.5f, 0, xLoc, yLoc, explosionSize, explosionTexId);
	}

	public Bullet shoot() {
		if (System.currentTimeMillis() - getLastShotTime() > getShotInterval()) {
			if (SuperNova.getInstance().isSoundEnabled()) {
				if (!SoundManager.getInstance().isPlayingSound()) {
					SoundManager.getInstance().playEffect(shootSound);	
				}					
			}
			Bullet bullet = new Bullet(BulletColor.RED, getXLoc() + bulletOffsetX, getYLoc() + bulletOffsetY, Direction.DOWN);
			setLastShotTime(System.currentTimeMillis());
			return bullet;
		}
		return null;
	}
	
	public void explode() {
		if (!SoundManager.getInstance().isPlayingSound()) {
			SoundManager.getInstance().playEffect(explosionSource);
		}
	}
	

	public long getLastShotTime() {
		return lastShotTime;
	}

	public void setLastShotTime(long lastShotTime) {
		this.lastShotTime = lastShotTime;
	}

	public long getShotInterval() {
		return shotInterval;
	}

	public void setShotInterval(long shotInterval) {
		this.shotInterval = shotInterval;
	}

	public String getExplosionTex() {
		return explosionTex;
	}

	public void setExplosionTex(String explosionTex) {
		this.explosionTex = explosionTex;
	}

	@Override
	public int getExplosionIndex() {
		return explosionIndex;
	}

	@Override
	public void setExplosionIndex(int explosionIndex) {
		this.explosionIndex = explosionIndex;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public int getPoints() {
		return POINTS;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getBulletOffsetX() {
		return bulletOffsetX;
	}

	public void setBulletOffsetX(float bulletOffsetX) {
		this.bulletOffsetX = bulletOffsetX;
	}

	public float getBulletOffsetY() {
		return bulletOffsetY;
	}

	public void setBulletOffsetY(float bulletOffsetY) {
		this.bulletOffsetY = bulletOffsetY;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
	
	public float getImpactSize() {
		return impactSize;
	}

	public void setImpactSize(float impactSize) {
		this.impactSize = impactSize;
	}	
	
	public List<AbstractStrategy> getStrategies() {
		return strategies;
	}

	public void setStrategies(List<AbstractStrategy> strategies) {
		this.strategies = strategies;
	}

	public int getCurrentStrategy() {
		return currentStrategy;
	}

	public void setCurrentStrategy(int currentStrategy) {
		this.currentStrategy = currentStrategy;
	}
}
