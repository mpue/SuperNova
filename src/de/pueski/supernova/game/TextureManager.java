package de.pueski.supernova.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureManager {

	private static final Log log = LogFactory.getLog(TextureManager.class);
	
	private static TextureManager INSTANCE = null;

	private final HashMap<String, Integer> textureMap;
	
	private TextureManager() {
		textureMap = new HashMap<String, Integer>();
	}
	
	public static TextureManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextureManager();
		}
		return INSTANCE;
	}
	
	public int getTexture(String name) {
		return textureMap.get(name).intValue();
	}
	
	public int addTexture(String textureLocation) {
		if (!textureMap.containsKey(textureLocation)) {
			log.info("Loading texture from "+textureLocation);
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(textureLocation);		
			int id = loadTexture(is);
			textureMap.put(textureLocation,Integer.valueOf(id));
			return id;
		}
		else {
			return textureMap.get(textureLocation);
		}
	}
	
	private int loadTexture(InputStream is) {
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		try {

			PNGDecoder decoder = new PNGDecoder(is);
			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buf.flip();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

		}
		catch (IOException e) {
			System.out.println("Error decoding " + is);
			e.printStackTrace();
		}
		tmp.rewind();

		int texId = tmp.get(0);

		return texId;
	}
	
}
