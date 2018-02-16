package de.pueski.supernova.ui;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.entities.IDrawable;

public class GLGauge implements IDrawable {

	private int value;
	
	private boolean visible = true;

	private float xloc;
	private float yloc;
	private float angle = 0.0f;
	
	private GLColor3f color;
	
	private float size = 10f;

	private float opacity = 1.0f;
	
	public GLGauge(int value,float angle, float xloc, float yloc) {
		super();
		this.value = value;
		this.xloc = xloc;
		this.yloc = yloc;
		this.angle = angle;
	}

	public float getXloc() {
		return xloc;
	}

	public void setXloc(float xloc) {
		this.xloc = xloc;
	}

	public float getYloc() {
		return yloc;
	}

	public void setYloc(float yloc) {
		this.yloc = yloc;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void draw() {
		
		if (!visible) {
			return;
		}		

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(xloc, yloc, 0.0f);
		GL11.glRotatef(angle, 0, 0, 1.0f);
		GL11.glScalef(size, size, 0.0f);
		
		for (float i = 0; i < 360;i+=6) {
			GL11.glPushMatrix();

			GL11.glRotatef(i, 0, 0, 1.0f);
			GL11.glTranslatef(-4, 0.0f, 0.0f);
			
			if (i/6 < value) {
				GL11.glColor4f(color.getRed(),color.getGreen(), color.getBlue(), getOpacity() );
			}
			else {
				GL11.glColor4f(color.getRed(),color.getGreen(), color.getBlue(), getOpacity()  / 2);		
			}
			
			GL11.glBegin(GL11.GL_POLYGON);
			GL11.glVertex2f(-0.5f, -0.18f);
			GL11.glVertex2f(0.5f, -0.08f);
			GL11.glVertex2f(0.5f, 0.08f);
			GL11.glVertex2f(-0.5f, 0.18f);
			GL11.glEnd();
			
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public GLColor3f getColor() {
		return color;
	}

	public void setColor(GLColor3f color) {
		this.color = color;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
}
