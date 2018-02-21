package de.pueski.supernova.entities.strategies;

import javax.xml.bind.annotation.XmlTransient;

import de.pueski.supernova.entities.Enemy;

public class Oscillate extends AbstractStrategy {

	@XmlTransient
	protected float angle = 0.0f;
	
	
	public Oscillate(Enemy enemy) {
		super(enemy);
	}

	@Override
	public void fly() {
		enemy.setYLoc(enemy.getYLoc() - enemy.getSpeed());		
		enemy.setXLoc((float) (enemy.getXLoc() + (2 * Math.sin(Math.toRadians(angle)))));
		angle += 2.0f;
		
	}

}
