package de.pueski.supernova.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.TextureUtil;

@XmlType(propOrder = {"energy","texture","shadowTexture","shieldTexture", "explosionTexture", "ammo"}, name="Ship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Entity.class)
public class Ship extends Entity implements IExplodable, IDrawable {
	
	@XmlTransient
	public static final int MAX_AMMO = 1000;
	
	private int energy;
	
	@XmlTransient
	private boolean shielded = false;
	
	@XmlTransient
	private int texId;
	@XmlTransient
	private int shieldTexId;
	@XmlTransient
	private int shadowTexId;
	@XmlTransient
	private long index; 
	@XmlTransient
	private int explosionTexId;
	@XmlTransient
	private int gridSize = 8;
	@XmlTransient
	private int explosionIndex;
	@XmlTransient
	private boolean visible;
	@XmlTransient
	float x_i = 1f / 8;
	@XmlTransient
	float y_i = 1f / 8;
	@XmlTransient
	float shieldAlpha = 1.0f;
	@XmlTransient
	private Fade fade = Fade.IN;
	@XmlTransient
	private int shootSound;
	@XmlTransient
	private int explosionSource;

	private String texture;	
	private String shadowTexture;	
	private String shieldTexture;	
	private String explosionTexture;
	
	private int ammo = MAX_AMMO;

	private enum Fade {
		IN,
		OUT
	}
	
	public Ship() {		
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Ship ship = new Ship();
		ship.xLoc = xLoc;
		ship.yLoc = yLoc;
		ship.energy = 100;				
		ship.texId = TextureManager.getInstance().addTexture(texture);						
		ship.shadowTexId = TextureManager.getInstance().addTexture(shadowTexture);
		ship.shieldTexId = TextureManager.getInstance().addTexture(shieldTexture);		
		ship.explosionTexId = TextureManager.getInstance().addTexture(explosionTexture);
		ship.visible = true;
		ship.explosionSize = explosionSize;
		ship.shootSound = SoundManager.getInstance().getSound("audio/laser.wav");
		ship.explosionSource = SoundManager.getInstance().getSound("audio/explosion2.wav");
		ship.height = height;
		ship.width = width;
		ship.scale = scale;
		return ship;		
	}
	
	public boolean hit() {
		if (shielded) {
			return true;
		}
		
		if (energy >= 10) {
			energy -= 10;
			return true;
		}
		return false;		
	}
	
	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		if (shielded) {
			return;
		}
		this.energy = energy;
	}
	
	public int getExplosionIndex() {
		return explosionIndex;
	}

	public void setExplosionIndex(int explosionIndex) {
		this.explosionIndex = explosionIndex;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		
		if (obj.getClass() == getClass()) {
			Ship other = (Ship)obj;
			return (index == other.index);
		}
		
		return false;
	}
	
	public void draw() {
		
		if (!visible)
			return;
		
		if (shielded) {
		
			if (fade.equals(Fade.IN)) {
				
				if (shieldAlpha < 1.0f) {
					shieldAlpha += 0.005f;
				}
				else {
					shieldAlpha = 1.0f;
					fade = Fade.OUT;
				}
				
			}
			else {
				if (shieldAlpha > 0) {
					shieldAlpha -= 0.005f;
				}
				else {
					shieldAlpha = 0.0f;
					fade = Fade.IN;
				}				
			}
			
		}
		else {
			shieldAlpha = 0.0f;
		}
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

		// shadow
		
		TextureUtil.drawTexture(0f, (float) (xLoc - ((400 - xLoc) * 0.1)), yLoc - 40, width * scale, height * scale, shadowTexId);
		
		// shield

		GL11.glColor4f(1.0f, 1.0f, 1.0f, shieldAlpha);
		
		TextureUtil.drawTexture(0f, xLoc, yLoc, width * scale, height * scale, shieldTexId);
		
		// ship
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		TextureUtil.drawTexture(0f, xLoc, yLoc, width * scale, height * scale, texId);

	}
	
	public void drawExplosion(int frame) {
		TextureUtil.drawAnimationFrame(frame, gridSize, 1.5f, 0, xLoc, yLoc, explosionSize, explosionTexId);		
	}

	@Override
	public void fly() {	
	}

	public void shoot() {
		if (!SoundManager.getInstance().isPlayingSound()) {
			SoundManager.getInstance().playEffect(shootSound);	
		}		
	};
	
	public void explode() {
		if (!SoundManager.getInstance().isPlayingSound()) {
			SoundManager.getInstance().playEffect(explosionSource);
		}
	}
	
	public void addEnergy(Energy e) {
		
		energy += e.getAmount();
		
		if (energy > 100) {
			energy = 100;
		}
		
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void resetAmmo() {
		ammo = MAX_AMMO;
	}

	public void addAmmo(int amount) {
		ammo += amount;
	}

	public void decreaseAmmo() {
		//ammo--;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public String getShadowTexture() {
		return shadowTexture;
	}

	public void setShadowTexture(String shadowTexture) {
		this.shadowTexture = shadowTexture;
	}

	public String getShieldTexture() {
		return shieldTexture;
	}

	public void setShieldTexture(String shieldTexture) {
		this.shieldTexture = shieldTexture;
	}

	public String getExplosionTexture() {
		return explosionTexture;
	}

	public void setExplosionTexture(String explosionTexture) {
		this.explosionTexture = explosionTexture;
	}
	
}
