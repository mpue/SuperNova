package de.pueski.supernova.data;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import de.pueski.supernova.entities.Boss;
import de.pueski.supernova.entities.Enemy;
import de.pueski.supernova.entities.StaticEnemy;

public class DataTest {

	public static void main(String[] args) throws Exception {
		
		
		Game game = new Game();

		Stage mars = new Stage();

		Boss boss = new Boss();
		boss.setAppereanceTime(60);
		boss.setExplosionSize(256);
		boss.setVisible(true);
		boss.setXLoc(0);
		boss.setYLoc(0);
		boss.setSpeed(2.0f);
		boss.setHitPoints(1);
		boss.setExplosionTex("images/explosion0.png");
		boss.setTexture("images/boss1.png");
		boss.setShotInterval(0);

		mars.getBosses().add(boss);
		
		mars.setTileSize(600);
		mars.getImages().add("mars_01.png");
		mars.getImages().add("mars_02.png");
		mars.getImages().add("mars_03.png");
		mars.getImages().add("mars_04.png");
		mars.getImages().add("mars_05.png");
		mars.getImages().add("mars_06.png");
		mars.setName("Mars");

		game.getStages().add(mars);
		
		Enemy e = new Enemy();
		e.setEnergy(100);
		e.setExplosionTex("images/explosion0.png");
		e.setShotInterval(500);
		e.setExplosionSize(20);
		e.setTexture("images/alien3.png");
		e.setHitPoints(1);

		mars.getEnemies().add(e);
		
		StaticEnemy s = new StaticEnemy(game);
		s.setEnergy(100);
		s.setExplosionTex("images/explosion0.png");
		s.setShotInterval(500);
		s.setExplosionSize(20);
		s.setTexture("images/mainbase.png");
		s.setHitPoints(1);
		s.setWidth(208);
		s.setHeight(1);
		
		mars.getEnemies().add(s);
		
		
//		Ship ship = new Ship(0,0);
//		
//		game.getShips().add(ship);
		
		
		Marshaller m = (Marshaller)JAXBContext.newInstance(Game.class).createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(game, new FileOutputStream(new File("game_generated.xml")));
		
		
	}
	
}
