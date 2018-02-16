package de.pueski.supernova.data;

import java.util.ArrayList;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.pueski.supernova.entities.Boss;
import de.pueski.supernova.entities.Enemy;
import de.pueski.supernova.entities.StaticEnemy;

@XmlType(propOrder = {"images", "bosses", "enemies", "staticEnemies", "name", "duration", "tileSize"}, name="Stage")
@XmlAccessorType(XmlAccessType.FIELD)
public class Stage {
	
	private String name;
	private int tileSize;
	
	@XmlElements({
		@XmlElement(name="image"),
	})
	private ArrayList<String> images = new ArrayList<String>();

	@XmlTransient
	private final Vector<Integer> texIds = new Vector<Integer>();

	@XmlElements({
		@XmlElement(name="boss"),
	})
	private ArrayList<Boss> bosses = new ArrayList<Boss>();
	
	@XmlElements({
		@XmlElement(name="enemy"),
	})	
	
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	@XmlElements({
		@XmlElement(name="staticEnemy"),
	})	
	
	private ArrayList<StaticEnemy> staticEnemies = new ArrayList<StaticEnemy>();
	
	
	// the duration of the stage in seconds.
	// if 0 is provided the stage will play endlessly until the last boss
	// has been defeated	
	private int duration;
	
	@XmlTransient
	private int timeLeft;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	
	public void addTextureId(Integer id) {
		getTexIds().addElement(id);
	}

	public Vector<Integer> getTexIds() {
		return texIds;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public ArrayList<Boss> getBosses() {
		return bosses;
	}

	public void setBosses(ArrayList<Boss> bosses) {
		this.bosses = bosses;
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<StaticEnemy> getStaticEnemies() {
		return staticEnemies;
	}

	public void setStaticEnemies(ArrayList<StaticEnemy> staticEnemies) {
		this.staticEnemies = staticEnemies;
	}
	
}
