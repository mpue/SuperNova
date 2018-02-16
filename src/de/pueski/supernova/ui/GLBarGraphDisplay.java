package de.pueski.supernova.ui;

import org.lwjgl.opengl.GL11;

import de.pueski.supernova.entities.IDrawable;

public class GLBarGraphDisplay implements IDrawable {

	public static enum Orientation {
		VERTICAL,
		HORIZONTAL
	}
	
	private int value;
	
	private boolean visible = true;
	
	private float xloc;
	private float yloc;
	
	private long time;
	private long blinkInterval = 1000;
	
	private boolean blinking = false;	
	private boolean showing = true;
	
	private float spacing = 1.4f;
	
	boolean showHighLight = false;
	
	private float borderWidth = 1.0f;	
	private boolean border;
	private GLColor3f borderColor = new GLColor3f(1.0f,1.0f,1.0f);
	private GLColor3f barColor    = new GLColor3f(0.0f,1.0f,0.0f);
	
	private boolean colored = true;
	
	private float stepsize;
	private float step;

	private float height = 101.0f;
	private float width = 12.0f;
	
	private float scale = 2.0f;
	private float angle = 0.0f; 

	public GLBarGraphDisplay(int value, float stepsize, float xloc, float yloc, boolean border) {
		this(value, stepsize, xloc, yloc, border, false);			
	}

	public GLBarGraphDisplay(int value, float stepsize, float xloc, float yloc, boolean border, boolean colored) {
		this.value = value;
		this.stepsize = stepsize;
		this.step = (float) 100 / (float) stepsize;
		this.xloc = xloc;
		this.yloc = yloc;
		this.border = border;
		this.colored = colored;
		time = System.currentTimeMillis();
	}

	@Override
	public void draw() {
		
		if (!visible) {
			return;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();

		GL11.glTranslatef(xloc, yloc, 0.0f);
		GL11.glRotatef(getAngle(), 0, 0, 1.0f);
		GL11.glScalef(1.0f, 2.0f, 0.0f);
		
		// draw border if desired
		
		if (border) {
			GL11.glColor4f(borderColor.getRed(),borderColor.getGreen(),borderColor.getBlue(), 1.0f);
			GL11.glLineWidth(borderWidth);
			GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex2f(0.0f, 0.0f);
				GL11.glVertex2f(width, 0.0f);
				GL11.glVertex2f(width, height);
				GL11.glVertex2f(0.0f, height);
			GL11.glEnd();
			GL11.glLineWidth(1.0f);
		}

		// draw energy bar

		GL11.glColor4f(barColor.getRed(),barColor.getGreen(), barColor.getBlue(), 1.0f);
		
		if (blinking) {
			if (System.currentTimeMillis() - time > blinkInterval) {
				time = System.currentTimeMillis();
				showing = !showing;
			}			
		}

		if (showing) {
			for (int y = 0; y < value; y += stepsize) {

				if (colored) {
					if (y < 50 && y >= 20) {
						GL11.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
					} else if (y < 20) {
						GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
					} else {
						GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
					}
				}
				else {
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				}
				GL11.glBegin(GL11.GL_POLYGON);
					
				if (showHighLight){
					GL11.glVertex2f(0.0f  + spacing / 2, 0.0f + y + spacing / 2);
					GL11.glColor3f(0.0f, 1.0f, 1.0f);
					GL11.glVertex2f(width/2 - spacing / 2, 0.0f + y + spacing / 2);
					GL11.glColor3f(barColor.getRed(),barColor.getGreen(), barColor.getBlue());
					GL11.glVertex2f(width - spacing / 2, 0.0f + y + spacing / 2);
					
					GL11.glVertex2f(width - spacing / 2, height / step + y - spacing / 2);				
					GL11.glColor3f(0.9f, 0.9f, 0.9f);
					GL11.glVertex2f(width/2 - spacing / 2, height / step + y - spacing / 2);
					GL11.glColor3f(barColor.getRed(),barColor.getGreen(), barColor.getBlue());
					GL11.glVertex2f(0.0f  + spacing / 2, height / step + y - spacing / 2);				
				}
				else {
					GL11.glVertex2f(0.0f  + spacing / 2, 0.0f + y + spacing / 2);
					GL11.glVertex2f(width - spacing / 2, 0.0f + y + spacing / 2);
					GL11.glVertex2f(width - spacing / 2, height / step + y - spacing / 2);
					GL11.glVertex2f(0.0f  + spacing / 2, height / step + y - spacing / 2);				
				}

				GL11.glEnd();

			}
	
		}
		
		GL11.glPopMatrix();	
		
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the xloc
	 */
	public float getXloc() {
		return xloc;
	}

	/**
	 * @param xloc
	 *            the xloc to set
	 */
	public void setXloc(float xloc) {
		this.xloc = xloc;
	}

	/**
	 * @return the yloc
	 */
	public float getYloc() {
		return yloc;
	}

	/**
	 * @param yloc
	 *            the yloc to set
	 */
	public void setYloc(float yloc) {
		this.yloc = yloc;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	/**
	 * @param borderWidth the borderWidth to set
	 */
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	/**
	 * @param borderColor the borderColor to set
	 */
	public void setBorderColor(GLColor3f borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return the barColor
	 */
	public GLColor3f getBarColor() {
		return barColor;
	}

	/**
	 * @param barColor the barColor to set
	 */
	public void setBarColor(GLColor3f barColor) {
		this.barColor = barColor;
	}

	/**
	 * @param colored the colored to set
	 */
	public void setColored(boolean colored) {
		this.colored = colored;
	}

	/**
	 * @param showHighLight the showHighLight to set
	 */
	public void setShowHighLight(boolean showHighLight) {
		this.showHighLight = showHighLight;
	}

	/**
	 * @param blinkInterval the blinkInterval to set
	 */
	public void setBlinkInterval(long blinkInterval) {
		this.blinkInterval = blinkInterval;
	}

	/**
	 * @param blinking the blinking to set
	 */
	public void setBlinking(boolean blinking) {
		this.blinking = blinking;
		if (!blinking) {
			this.showing = true;
		}
	}

	/**
	 * @param border the border to set
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	
}
