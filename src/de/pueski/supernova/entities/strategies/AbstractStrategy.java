package de.pueski.supernova.entities.strategies;

import com.eclipsesource.v8.V8Object;

import de.pueski.supernova.entities.Enemy;

public abstract class AbstractStrategy {

	private V8Object v8object;
	
	protected Enemy enemy;
	
	public AbstractStrategy(Enemy enemy) {
		this.enemy = enemy;
		
		v8object.registerJavaMethod(this, "supernova","fly",new Class<?>[]{Float.class});
		
	}
	
	public abstract void fly();
	
	
}
