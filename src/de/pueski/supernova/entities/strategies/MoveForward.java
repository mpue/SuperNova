package de.pueski.supernova.entities.strategies;

import de.pueski.supernova.entities.Enemy;

public class MoveForward extends AbstractStrategy {

	public MoveForward(Enemy enemy) {
		super(enemy);
	}

	@Override
	public void fly() {
		enemy.setYLoc(enemy.getYLoc() - enemy.getSpeed());	
	}

}
