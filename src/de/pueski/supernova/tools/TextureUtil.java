package de.pueski.supernova.tools;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

public class TextureUtil {

	public static BufferedImage createImageFromFont(String s, Font f, Color c) {
		if (s == null)
			return null;
		BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) bi.getGraphics();
		g2d.setFont(f);
		g2d.setColor(Color.BLACK);
		g2d.drawString(s, 0, 0);
		FontMetrics fm = g2d.getFontMetrics();
		int height = fm.getHeight();
		int width = fm.stringWidth(s) + 10;

		if (width == 0 || height == 0)
			return null;

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		BufferedImage si = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D) si.getGraphics();
		g2d.setRenderingHints(rh);
		g2d.setFont(f);
		g2d.setColor(c);
		g2d.drawString(s, 5, height - fm.getDescent());
		return si;
	}

	public static ByteBuffer convertBufferedImageToByteBuffer(BufferedImage image) throws Exception {

		image = ImageUtils.flipHorizontal(image);

		int[] pixels = ImageUtils.getArrayFromImage(image, image.getWidth(), image.getHeight());

		ByteBuffer buf = ByteBuffer.allocateDirect(4 * pixels.length);
		for (int i = 0; i < pixels.length; i++) {
			buf.putInt(pixels[i]);
		}

		buf.rewind();

		return buf;
	}
	
	public static void drawTexturedSquare(float angle, float x, float y, float size, int texId) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		// GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		// GL11.glScalef(scale, scale, scale);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-size / 2, -size / 2);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(size / 2, -size / 2);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size / 2, size / 2);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-size / 2, size / 2);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void drawTexture(float angle, float x, float y, float size, int texId) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		// GL11.glScalef(scale, scale, scale);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_POLYGON);
		
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-size, -size);
		
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(size, -size);
		
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(size, size);
		
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-size, size);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public static void drawTexture(float angle, float x, float y, float width, float height, int texId) {
		
		glEnable(GL_TEXTURE_2D);
		
		width = width / 2;
		height = height / 2;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glBegin(GL11.GL_POLYGON);
		
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-width, -height);
		
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(width, -height);
		
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(width, height);
		
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-width, height);
		GL11.glEnd();
		GL11.glPopMatrix();
		
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void drawAnimationFrame(int frame, int gridSize, float scale, float rotation, float xLoc, float yLoc, float explosionSize, int explosionTexId) {
		
		int column = frame % gridSize;
		int row = frame / gridSize;

		float x_i = 1f / gridSize;
		float y_i = 1f / gridSize;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(xLoc, yLoc, 0);
		GL11.glScalef(scale, scale, 0.0f);
		GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, explosionTexId);
		
		GL11.glBegin(GL11.GL_POLYGON);
		// bottom left
		GL11.glTexCoord2f(0f + x_i * column, (1f / 8) + y_i * row);
		GL11.glVertex2f(-explosionSize * 2, -explosionSize * 2);

		// bottom right
		GL11.glTexCoord2f((1f / 8) + x_i * column, (1f / 8) + y_i * row);
		GL11.glVertex2f(explosionSize * 2, -explosionSize * 2);

		// top right
		GL11.glTexCoord2f((1f / 8) + x_i * column, 0f + y_i * row);
		GL11.glVertex2f(explosionSize * 2, explosionSize * 2);

		// top left
		GL11.glTexCoord2f(0f + x_i * column, 0f + y_i * row);
		GL11.glVertex2f(-explosionSize * 2, explosionSize * 2);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);		
	}

}
