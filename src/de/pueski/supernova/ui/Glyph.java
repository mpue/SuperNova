package de.pueski.supernova.ui;

import java.util.HashMap;

public final class Glyph {

	static final HashMap<String, Glyph> letterLocations = new HashMap<String, Glyph>();

	static {
		letterLocations.put("!", new Glyph(0, 0, 1.0f));
		letterLocations.put("\"", new Glyph(1, 0, 1.0f));
		letterLocations.put("#", new Glyph(2, 0, 1.0f));
		letterLocations.put("$", new Glyph(3, 0, 1.0f));
		letterLocations.put("%", new Glyph(4, 0, 1.0f));
		letterLocations.put("&", new Glyph(5, 0, 1.0f));
		letterLocations.put("'", new Glyph(6, 0, 1.0f));
		letterLocations.put("(", new Glyph(7, 0, 1.0f));
		letterLocations.put(")", new Glyph(8, 0, 1.0f));
		letterLocations.put("*", new Glyph(9, 0, 1.0f));
		letterLocations.put("+", new Glyph(10, 0, 1.0f));
		letterLocations.put(",", new Glyph(11, 0, 0.4f));
		letterLocations.put("-", new Glyph(0, 1, 1.0f));
		letterLocations.put(".", new Glyph(1, 1, 0.4f));
		letterLocations.put("/", new Glyph(2, 1, 1.0f));
		letterLocations.put("0", new Glyph(3, 1, 1.0f));
		letterLocations.put("1", new Glyph(4, 1, 1.0f));
		letterLocations.put("2", new Glyph(5, 1, 1.0f));
		letterLocations.put("3", new Glyph(6, 1, 1.0f));
		letterLocations.put("4", new Glyph(7, 1, 1.0f));
		letterLocations.put("5", new Glyph(8, 1, 1.0f));
		letterLocations.put("6", new Glyph(9, 1, 1.0f));
		letterLocations.put("7", new Glyph(10, 1, 1.0f));
		letterLocations.put("8", new Glyph(11, 1, 1.0f));
		letterLocations.put("9", new Glyph(0, 2, 1.0f));
		letterLocations.put(":", new Glyph(1, 2, 0.5f));
		letterLocations.put(";", new Glyph(2, 2, 0.5f));
		letterLocations.put("<", new Glyph(3, 2, 1.0f));
		letterLocations.put("=", new Glyph(4, 2, 1.0f));
		letterLocations.put(">", new Glyph(5, 2, 1.0f));
		letterLocations.put("?", new Glyph(6, 2, 1.0f));
		letterLocations.put("@", new Glyph(7, 2, 1.0f));
		letterLocations.put("A", new Glyph(8, 2, 1.2f));
		letterLocations.put("B", new Glyph(9, 2, 1.2f));
		letterLocations.put("C", new Glyph(10, 2, 1.2f));
		letterLocations.put("D", new Glyph(11, 2, 1.2f));
		letterLocations.put("E", new Glyph(0, 3, 1.2f));
		letterLocations.put("F", new Glyph(1, 3, 1.2f));
		letterLocations.put("G", new Glyph(2, 3, 1.2f));
		letterLocations.put("H", new Glyph(3, 3, 1.2f));
		letterLocations.put("I", new Glyph(4, 3, 0.6f));
		letterLocations.put("J", new Glyph(5, 3, 1.2f));
		letterLocations.put("K", new Glyph(6, 3, 1.1f));
		letterLocations.put("L", new Glyph(7, 3, 1.0f));
		letterLocations.put("M", new Glyph(8, 3, 1.3f));
		letterLocations.put("N", new Glyph(9, 3, 1.2f));
		letterLocations.put("O", new Glyph(10, 3, 1.2f));
		letterLocations.put("P", new Glyph(11, 3, 1.2f));
		letterLocations.put("Q", new Glyph(0, 4, 1.2f));
		letterLocations.put("R", new Glyph(1, 4, 1.2f));
		letterLocations.put("S", new Glyph(2, 4, 1.0f));
		letterLocations.put("T", new Glyph(3, 4, 1.2f));
		letterLocations.put("U", new Glyph(4, 4, 1.2f));
		letterLocations.put("V", new Glyph(5, 4, 1.4f));
		letterLocations.put("W", new Glyph(6, 4, 1.4f));
		letterLocations.put("X", new Glyph(7, 4, 1.2f));
		letterLocations.put("Y", new Glyph(8, 4, 1.2f));
		letterLocations.put("Z", new Glyph(9, 4, 1.2f));
		letterLocations.put("[", new Glyph(10, 4, 1.0f));
		letterLocations.put("\\", new Glyph(11, 4, 1.0f));
		letterLocations.put("]", new Glyph(0, 5, 1.0f));
		letterLocations.put("^", new Glyph(1, 5, 1.0f));
		letterLocations.put("_", new Glyph(2, 5, 1.0f));
		letterLocations.put("`", new Glyph(3, 5, 1.0f));
		letterLocations.put("a", new Glyph(4, 5, 0.95f));
		letterLocations.put("b", new Glyph(5, 5, 0.90f));
		letterLocations.put("c", new Glyph(6, 5, 0.9f));
		letterLocations.put("d", new Glyph(7, 5, 0.95f));
		letterLocations.put("e", new Glyph(8, 5, 0.8f));
		letterLocations.put("f", new Glyph(9, 5, 0.70f));
		letterLocations.put("g", new Glyph(10, 5, 0.9f));
		letterLocations.put("h", new Glyph(11, 5, 0.8f));
		letterLocations.put("i", new Glyph(0, 6, 0.5f));
		letterLocations.put("j", new Glyph(1, 6, 0.65f));
		letterLocations.put("k", new Glyph(2, 6, 0.95f));
		letterLocations.put("l", new Glyph(3, 6, 0.6f));
		letterLocations.put("m", new Glyph(4, 6, 1.3f));
		letterLocations.put("n", new Glyph(5, 6, 0.9f));
		letterLocations.put("o", new Glyph(6, 6, 0.9f));
		letterLocations.put("p", new Glyph(7, 6, 0.96f));
		letterLocations.put("q", new Glyph(8, 6, 1.0f));
		letterLocations.put("r", new Glyph(9, 6, 0.6f));
		letterLocations.put("s", new Glyph(10, 6, 0.85f));
		letterLocations.put("t", new Glyph(11, 6, 0.70f));
		letterLocations.put("u", new Glyph(0, 7, 0.90f));
		letterLocations.put("v", new Glyph(1, 7, 1.0f));
		letterLocations.put("w", new Glyph(2, 7, 1.0f));
		letterLocations.put("x", new Glyph(3, 7, 1.0f));
		letterLocations.put("y", new Glyph(4, 7, 1.0f));
		letterLocations.put("z", new Glyph(5, 7, 1.0f));
		letterLocations.put("{", new Glyph(6, 7, 1.0f));
		letterLocations.put("|", new Glyph(7, 7, 1.0f));
		letterLocations.put("}", new Glyph(8, 7, 1.0f));
		letterLocations.put("~", new Glyph(9, 7, 1.0f));
		letterLocations.put(" ", new Glyph(11, 11, 0.6f));

	}

	int x;
	int y;
	float width;

	public Glyph(int x, int y, float width) {
		this.x = x;
		this.y = y;
		this.width = width;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the stretch
	 */
	public float getWidth() {
		return width;
	}

}
