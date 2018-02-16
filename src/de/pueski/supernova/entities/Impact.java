package de.pueski.supernova.entities;

public class Impact {

	private float x;
	private float y;
	private int frame = 0;

	public Impact(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}

	public void nextFrame() {
		frame++;
	}
	
}
