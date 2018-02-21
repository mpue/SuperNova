package de.pueski.supernova.entities.strategies;

import de.pueski.supernova.entities.Enemy;

public abstract class AbstractStrategy {

	protected Enemy enemy;
	
	public AbstractStrategy(Enemy enemy) {
		this.enemy = enemy;
	}
	
	public abstract void fly();
	
}
