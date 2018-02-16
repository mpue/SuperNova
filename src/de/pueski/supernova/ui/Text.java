package de.pueski.supernova.ui;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import de.pueski.supernova.entities.IDrawable;
import de.pueski.supernova.entities.IFadeable;
import de.pueski.supernova.tools.FontUtils.Alignment;

public class Text implements IFadeable, IDrawable {

	// Member variables

	private float xLoc;
	private float yLoc;
	private String text;
	private float opacity = 1.0f;
	private Fade mode;
	private boolean visible = true;

	private UnicodeFont font;

	private Color color;

	private Alignment alignment;

	public Text(float xLoc, float yLoc, String text, Integer fontSize) {

		font = new UnicodeFont(new Font("Lucida Sans", Font.BOLD, fontSize));

		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

		try {
			font.loadGlyphs();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}

		color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.text = text;
	}

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

	public void draw() {

		color.a = opacity;

		glPushMatrix();
		glTranslatef(xLoc, yLoc, 0.0f);
		glScalef(1.0f, -1.0f, 1.0f);
		if (text != null) {

			int width = font.getWidth(text);

			if (null != alignment && alignment.equals(Alignment.CENTER)) {
				font.drawString(-width / 2, 0, text, color);
			}
			else if (null != alignment && alignment.equals(Alignment.RIGHT)) {
				font.drawString(-width, 0, text, color);
			}
			else {
				font.drawString(0, 0, text, color);
			}

		}

		glPopMatrix();

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	@Override
	public float getOpacity() {
		return opacity;
	}

	@Override
	public Fade getMode() {
		return mode;
	}

	@Override
	public void setMode(Fade mode) {
		this.mode = mode;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
}
