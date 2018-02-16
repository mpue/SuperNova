package de.pueski.supernova.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.pueski.supernova.entities.Ship;

@XmlRootElement
@XmlType(propOrder = {"textures", "stages", "ships", "defaultShip", "defaultStage"}, name="Game")
@XmlAccessorType(XmlAccessType.FIELD)
public class Game {

	@XmlTransient
	public static final int MAX_SEQUENCES = 2;
	
	@XmlElements({
		@XmlElement(name="stage"),
	})
	private ArrayList<Stage> stages = new ArrayList<Stage>();
	
	@XmlElements({
		@XmlElement(name="texture"),
	})
	private ArrayList<String> textures = new ArrayList<String>();
	
	@XmlElements({
		@XmlElement(name="ship"),
	})
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	
	private int defaultStage = 0;

	private int defaultShip;
	
	@XmlTransient
	private int score = 0;
	
	@XmlTransient
	private int enemiesShot = 0;

	@XmlTransient
	private int sequence = 0;
	
	@XmlTransient
	private float yOffset = 0;
	
	public ArrayList<Stage> getStages() {
		return stages;
	}

	public void setStages(ArrayList<Stage> stages) {
		this.stages = stages;
	}

	public int getDefaultStage() {
		return defaultStage;
	}

	public void setDefaultStage(int defaultStage) {
		this.defaultStage = defaultStage;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getEnemiesShot() {
		return enemiesShot;
	}

	public void setEnemiesShot(int enemiesShot) {
		this.enemiesShot = enemiesShot;
	}

	public void enemyKilled() {
		enemiesShot++;		
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public void nextSequence() {
		if (sequence < MAX_SEQUENCES) {
			sequence++;
		}
		else {
			sequence = 0;
		}
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public ArrayList<String> getTextures() {
		return textures;
	}

	public void setTextures(ArrayList<String> textures) {
		this.textures = textures;
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	public void setShips(ArrayList<Ship> ships) {
		this.ships = ships;
	}

	public int getDefaultShip() {
		return defaultShip;
	}

	public void setDefaultShip(int defaultShip) {
		this.defaultShip = defaultShip;
	}
	
}
