package de.pueski.supernova.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"xLoc","yLoc","width","height","energy", "explosionSize","visible" , "scale", "collisionBorder"}, name="Entity")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Entity implements IDrawable {

	public enum Direction {

		UP(1), DOWN(-1);

		private int value;

		private Direction(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	protected float xLoc;
	protected float yLoc;

	protected float width; 	
	protected float height;
	
	protected int energy;

	protected float collisionBorder = 0.0f;
	
	@XmlTransient
	protected Direction direction;

	protected float explosionSize;
	
	private boolean visible = true;
	
	protected float scale = 1.0f;
	
	/**
	 * @return the xLoc
	 */
	public float getXLoc() {
		return xLoc;
	}

	/**
	 * @param xLoc
	 *            the xLoc to set
	 */
	public void setXLoc(float xLoc) {
		this.xLoc = xLoc;
	}

	/**
	 * @return the yLoc
	 */
	public float getYLoc() {
		return yLoc;
	}

	/**
	 * @param yLoc
	 *            the yLoc to set
	 */
	public void setYLoc(float yLoc) {
		this.yLoc = yLoc;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy
	 *            the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public float getExplosionSize() {
		return explosionSize;
	}

	public void setExplosionSize(float size) {
		this.explosionSize = size;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public abstract void fly();
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getCollisionBorder() {
		return collisionBorder;
	}
	
	public void setCollisionBorder(float collisionBorder) {
		this.collisionBorder = collisionBorder;
	}

	public boolean collides(Entity entity) {
		float x = entity.getXLoc();
		float y = entity.getYLoc();
		return (x >= xLoc - (((width  - collisionBorder) / 2) * scale) && 
				x <= xLoc + (((width  - collisionBorder) / 2) * scale) && 
				y >= yLoc - (((height - collisionBorder) / 2) * scale) && 
				y <= yLoc + (((height - collisionBorder) / 2) * scale));
	}


}
