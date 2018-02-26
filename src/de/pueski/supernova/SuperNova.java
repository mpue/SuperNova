package de.pueski.supernova;

import static org.lwjgl.opengl.GL11.GL_ALL_ATTRIB_BITS;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.eclipsesource.v8.V8;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;
import de.pueski.supernova.data.Game;
import de.pueski.supernova.data.Stage;
import de.pueski.supernova.entities.Ammo;
import de.pueski.supernova.entities.Boss;
import de.pueski.supernova.entities.Bullet;
import de.pueski.supernova.entities.Bullet.BulletColor;
import de.pueski.supernova.entities.Enemy;
import de.pueski.supernova.entities.Energy;
import de.pueski.supernova.entities.Entity;
import de.pueski.supernova.entities.Entity.Direction;
import de.pueski.supernova.entities.IDrawable;
import de.pueski.supernova.entities.IExplodable;
import de.pueski.supernova.entities.IFadeable;
import de.pueski.supernova.entities.IFadeable.Fade;
import de.pueski.supernova.entities.LevelUp;
import de.pueski.supernova.entities.Shield;
import de.pueski.supernova.entities.Ship;
import de.pueski.supernova.entities.StaticEnemy;
import de.pueski.supernova.game.GameLauncher;
import de.pueski.supernova.game.HUD;
import de.pueski.supernova.game.IProgressCallback;
import de.pueski.supernova.game.IStartupCallback;
import de.pueski.supernova.game.MusicPlayer;
import de.pueski.supernova.game.SoundManager;
import de.pueski.supernova.game.TextureManager;
import de.pueski.supernova.tools.FontUtils.Alignment;
import de.pueski.supernova.tools.GLUtils;
import de.pueski.supernova.tools.TextureUtil;
import de.pueski.supernova.ui.GLBarGraphDisplay;
import de.pueski.supernova.ui.GameState;
import de.pueski.supernova.ui.Text;

@SuppressWarnings("deprecation")
public class SuperNova {

	private static final Log log = LogFactory.getLog(SuperNova.class);

	private static SuperNova INSTANCE = null;

	public static SuperNova getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SuperNova();
		}
		return INSTANCE;
	}

	private V8 runtime;
	
	///////////////////////////////////////////////////////////////////////
	// Constants
	///////////////////////////////////////////////////////////////////////

	private static final String GAME_TITLE = "SuperNova";
	private static final boolean DEBUG = false;

	private static final int SCREEN_WIDTH = 1024;
	private static final int SCREEN_HEIGHT = 768;

	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	private static final int MAX_FRAMERATE = 100;
	private static final int MAX_AMMO = 1000;
	private static final boolean FULLSCREEN_ENABLED = false;
	private static final int INITIAL_SHIP_Y = 70;

	private boolean finished;
	private boolean soundEnabled = true;

	///////////////////////////////////////////////////////////////////////
	// Velocity, offset and positioning
	///////////////////////////////////////////////////////////////////////

	private float velocity = 1.2f;
	private float starfieldVelocity = 0.25f;
	private float starfieldYOffset = 0.0f;

	private float xPos = 0;
	private float yPos = 0;

	private float deltaX;
	private float deltaY;

	private float shootX;
	private float shootY;

	private float fps;

	///////////////////////////////////////////////////////////////////////	
	// Texture Ids
	///////////////////////////////////////////////////////////////////////

	private int backgroundTexId;
	private int starfieldTexId;
	private int logoTexId;
	private int hudMaskId;
	private int imgTexId;

	///////////////////////////////////////////////////////////////////////
	// Time measurement
	///////////////////////////////////////////////////////////////////////

	private long lastShotTime;
	private long lastEnemyTime;
	private long frameTime;
	private long startTime;
	private long bossDefeatTime = 0;
	private long levelEndDelay = 2000;
	

	///////////////////////////////////////////////////////////////////////
	// Game stuff
	///////////////////////////////////////////////////////////////////////

	private DisplayMode displayMode;
	private Controller defaultController;
	private Ship ship;
	private Game game;
	private Stage currentStage;
	private int stageIndex = 0;
	private GameState gameState = GameState.LOADING;
	private Random random;

	///////////////////////////////////////////////////////////////////////	
	// Entities
	///////////////////////////////////////////////////////////////////////

	private final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private final ArrayList<Entity> enemies = new ArrayList<Entity>();
	private final ArrayList<Entity> entities = new ArrayList<Entity>();
	private final ArrayList<IExplodable> explodables = new ArrayList<IExplodable>();
	private final ArrayList<IDrawable> drawables = new ArrayList<IDrawable>();
	private final ArrayList<IFadeable> fadeables = new ArrayList<IFadeable>();
	private final ArrayList<Enemy> availableEnemies = new ArrayList<Enemy>();

	///////////////////////////////////////////////////////////////////////
	// Timers
	///////////////////////////////////////////////////////////////////////

	private final Timer defaultTimer = new Timer();
	private Timer stageTimer;

	///////////////////////////////////////////////////////////////////////
	// Sound sources
	///////////////////////////////////////////////////////////////////////

	private int energySource;
	private int reloadSource;
	private int shieldSource;
	private int gameOverSource;
	private int shipHitSource;
	private int impactSound;
	
	///////////////////////////////////////////////////////////////////////	
	// On screen display
	///////////////////////////////////////////////////////////////////////

	private Text gameOverText;
	private Text summaryText;
	private Text nowPlaying;
	private Text songName;
	private Text pauseText;
	private Text levelName;
	private Text pickup;
	private Text scoreText;
	private Text scoreValueText;
	private GLBarGraphDisplay loadingProgress;
	private HUD hud;

	///////////////////////////////////////////////////////////////////////
	// Sound and music
	/////////////////////////////////////////////////////////////////////

	private MusicPlayer musicPlayer;
	private SoundManager soundManager;
	private Unmarshaller unmarshaller;

	///////////////////////////////////////////////////////////////////////
	// Misc
	///////////////////////////////////////////////////////////////////////

	private boolean defeatingBoss = false;

	///////////////////////////////////////////////////////////////////////
	// Nifty GUI
	///////////////////////////////////////////////////////////////////////
	
	private Nifty nifty;
	private LwjglInputSystem lwjglInputSystem;
	private boolean menuIsShowing = false;

	private float musicVolume;
	private float novaRot = 0.0f;
	private float fxvolume;

	///////////////////////////////////////////////////////////////////////	
	// Implementation
	///////////////////////////////////////////////////////////////////////

	private SuperNova() {
		
		runtime = V8.createV8Runtime();
		
		try {
			unmarshaller = (Unmarshaller) JAXBContext.newInstance(Game.class).createUnmarshaller();
		}
		catch (JAXBException e) {
			log.error("Unable to create unmarshaller instance.");
			e.printStackTrace();
		}
		if (DEBUG) {
			displayMode = new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT);
			startup(displayMode, FULLSCREEN_ENABLED, false);
		}
		else {
			try {
				GameLauncher.runLauncher(new IStartupCallback() {
					@Override
					public void startup(DisplayMode mode, boolean fullscreen, boolean vsync) throws Exception {
						SuperNova.getInstance().startup(mode, fullscreen, vsync);
					}
				});
			}
			catch (Exception e) {
				System.exit(-1);
			}
		}

	}

	public static void main(String args[]) throws Exception {
		SuperNova.getInstance();
	}

	private void startup(DisplayMode mode, boolean fullscreen, boolean vsync) {

		displayMode = mode;

		try {
			init(mode, fullscreen, vsync);
		}
		catch (Exception e1) {
			log.error("Error initializing display mode.");
			e1.printStackTrace();
			System.exit(0);
		}

		run();
		cleanup();
		System.exit(0);

	}

	/**
	 * <p>
	 * Initializes the game.
	 * </p>
	 * <p>
	 * This method preloads the texture, displays a loading screen and does the basic game initialization
	 * </p>
	 *  
	 * @param mode the display mode to use for the game
	 * @param fullscreen flag that indicates fullscreen use
	 * @param vsync use vsync?
	 * @throws Exception
	 */
	private void init(final DisplayMode mode, boolean fullscreen, boolean vsync) throws Exception {

		Display.setTitle(GAME_TITLE);
		GLUtils.setDisplayMode(mode.getWidth(), mode.getHeight(), fullscreen, vsync);
		Display.create();

		// load the game contents from the game.xml
		game = (Game) unmarshaller.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("game.xml"));
		
		resetStage();

		// init keyboard, mouse and controller input system
		initInput();
		initGL();

		// preload textures with loading screem
		preloadTextures(mode);

		gameState = GameState.MENU;
		Mouse.setGrabbed(false);

		lastShotTime = System.currentTimeMillis();
		lastEnemyTime = System.currentTimeMillis();
		frameTime = System.currentTimeMillis();

		random = new Random();

		// create and select ship
		if (game.getShips().size() > 0) {
			ship = (Ship) game.getShips().get(game.getDefaultShip()).clone();
			ship.setXLoc(WIDTH / 2);
			ship.setYLoc(INITIAL_SHIP_Y);
		}
		else {
			throw new RuntimeException("You need to define at least one ship in game.xml");
		}

		xPos = ship.getXLoc();
		yPos = ship.getYLoc();

		// load properties from file and adjust volumes
		
		fxvolume = 0.4f;
		musicVolume = 0.0f;

		try {
			File propertiesFile = new File("SuperNova.properties");
			log.info("loading properties from "+propertiesFile.getAbsolutePath());
			Properties p = new Properties();
			p.load(new FileInputStream(propertiesFile));
			fxvolume = Float.valueOf(p.getProperty("fxvolume"));
			musicVolume = Float.valueOf(p.getProperty("musicvolume"));
			soundEnabled = Boolean.valueOf(p.getProperty("soundEnabled"));			
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		initSound();

		initHUD();
		drawBackground();

		defaultTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				game.nextSequence();
			}

		}, 0, 10000);

		lwjglInputSystem = new LwjglInputSystem();

		nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(), lwjglInputSystem, new TimeProvider());
		nifty.fromXml("menu.xml", "mainMenu");
	}

	/**
	 * Preloads the textures while displaying a loading screen
	 * 
	 * @param mode
	 */
	private void preloadTextures(final DisplayMode mode) {
		final float x = mode.getWidth() / 2;
		final float y = mode.getHeight() / 2 - 150;

		final Text loadingText = new Text(x, y + 40, "Sucking data from the disk ...", 30);
		loadingText.setAlignment(Alignment.CENTER);

		loadingProgress = new GLBarGraphDisplay(0, 1.5f, x - 100, y - 20, true, false);
		loadingProgress.setAngle(-90.0f);

		final int logoTex = TextureManager.getInstance().addTexture("images/logo/supernova_logo.png");

		loadTextures(new IProgressCallback() {

			@Override
			public void updateProgress(float progress) {
				Display.update();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
				TextureUtil.drawTexture(0, displayMode.getWidth() / 2, displayMode.getHeight() / 2, WIDTH, HEIGHT, logoTex);
				loadingProgress.setValue((int) progress);
				loadingProgress.draw();
				loadingText.draw();
				glPushMatrix();
				glTranslatef(x, y + 40, 0.0f);
				glScalef(1.0f, -1.0f, 1.0f);
				glPopMatrix();

			}
		});
	}

	/**
	 * Initialize the OpenGL viewport
	 */
	private void initGL() {
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// TODO : Currently the origin is at the bottom left, this leads to problems while using slick and nifty
		// because they both assume that the origin is at the top left. Because of that we need to mirror the slick and nifty 
		// display. 
		
		glOrtho(0.0d, Display.getDisplayMode().getWidth(), 0.0d, Display.getDisplayMode().getHeight(), -1.0d, 1.0d);
		// glOrtho(0.0d, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0.0d, -1.0d, 1.0d);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
	}

	/**
	 * Initialize the input system
	 * 
	 * @throws LWJGLException
	 */
	private void initInput() throws LWJGLException {
		Keyboard.create();
		Mouse.create();
		Mouse.setGrabbed(false);

		Mouse.setCursorPosition(WIDTH / 2, INITIAL_SHIP_Y);

		Controllers.create();
		Controllers.poll();

		// select the first found XBOX controller as default controller
		// TODO : make input controller configurable
		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			Controller controller = Controllers.getController(i);
			System.out.println(controller.getName());
			if (controller.getName().contains("XINPUT")) {
				defaultController = controller;
			}
		}
	}

	/**
	 * Initialize the sound subsystem
	 * 
	 * @throws Exception
	 */
	public void initSound() throws Exception {

		soundManager = SoundManager.getInstance();
		soundManager.setSoundEnabled(soundEnabled);

		if (!soundEnabled) {
			return;
		}

		// TODO : We should move that to game.xml too
		energySource = soundManager.getSound("audio/energy2.wav");
		reloadSource = soundManager.getSound("audio/reload.wav");
		shieldSource = soundManager.getSound("audio/shield.wav");
		gameOverSource = soundManager.getSound("audio/gameover.wav");
		shipHitSource = soundManager.getSound("audio/hit.wav");
		impactSound = soundManager.getSound("audio/cinematic_impact.wav");

		soundManager.adjustAllVolumes(fxvolume);

		if (musicPlayer != null) {
			musicPlayer.close();
			musicPlayer = null;
		}
		
		musicPlayer = new MusicPlayer();
				
	}

	/**
	 * Build heads up display
	 */
	private void initHUD() {

		gameOverText = new Text(WIDTH / 2, displayMode.getHeight() / 2, "Game over!", 30);
		gameOverText.setAlignment(Alignment.CENTER);
		scoreText = new Text(WIDTH / 2, displayMode.getHeight() / 2 - 50, "Your Score", 30);
		scoreText.setAlignment(Alignment.CENTER);
		scoreValueText = new Text(WIDTH / 2, displayMode.getHeight() / 2 - 100, String.valueOf(game.getScore()), 50);
		scoreValueText.setAlignment(Alignment.CENTER);

		summaryText = new Text(WIDTH / 2, displayMode.getHeight() / 2, "Your Score is " + game.getScore() + ". Press SPACE to continue.", 18);
		summaryText.setAlignment(Alignment.CENTER);
		nowPlaying = new Text(10, displayMode.getHeight() - 20, "Now playing", 18);
		songName = new Text(10, displayMode.getHeight() - 40, "", 18);
		pauseText = new Text(WIDTH / 2, displayMode.getHeight() / 2, "Pause", 40);
		pauseText.setAlignment(Alignment.CENTER);
		levelName = new Text(WIDTH / 2, displayMode.getHeight() / 2, currentStage.getName(), 80);
		levelName.setAlignment(Alignment.CENTER);
		pickup = new Text(WIDTH / 2, displayMode.getHeight() / 2, currentStage.getName(), 50);
		pickup.setAlignment(Alignment.CENTER);

		hud = new HUD(displayMode);

		nowPlaying.setOpacity(0.0f);
		songName.setOpacity(0.0f);
		levelName.setOpacity(0.0f);
		pickup.setOpacity(0.0f);
		drawables.add(hud);

	}

	/**
	 * Draw game background for the current stage
	 */
	private void drawBackground() {
		backgroundTexId = glGenLists(1);
		glNewList(backgroundTexId, GL_COMPILE);
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);

		int tileSize = currentStage.getTileSize();

		Collections.reverse(currentStage.getImages());

		for (int i = 0; i < currentStage.getImages().size(); i++) {
			TextureUtil.drawTexturedSquare(0, tileSize / 2, tileSize / 2 + i * tileSize, tileSize, currentStage.getTexIds().get(i));
		}

		TextureUtil.drawTexturedSquare(0, tileSize / 2, tileSize / 2 + currentStage.getImages().size() * tileSize, tileSize, currentStage.getTexIds().get(0));
		TextureUtil.drawTexturedSquare(0, tileSize / 2, tileSize / 2 + (currentStage.getImages().size() + 1) * tileSize, tileSize, currentStage.getTexIds().get(1));

		glPopMatrix();
		glEndList();
		glDisable(GL_TEXTURE_2D);
	}

	/**
	 * Load textures defined in the game.xml
	 * 
	 * @param progress The progress callback for the loading screen
	 */
	
	private void loadTextures(IProgressCallback progress) {

		int numTextures = game.getTextures().size();

		for (Stage stage : game.getStages()) {
			numTextures += stage.getImages().size();
		}

		numTextures += currentStage.getEnemies().size() * 2;

		log.info("Loading " + numTextures + " textures.");

		float onePercent = (float) numTextures / 100;

		numTextures = 0;

		for (String texture : game.getTextures()) {
			TextureManager.getInstance().addTexture(texture);
			progress.updateProgress(numTextures / onePercent);
			numTextures++;
		}

		for (Stage stage : game.getStages()) {
			for (String imageLocation : stage.getImages()) {
				stage.addTextureId(TextureManager.getInstance().addTexture("images/" + imageLocation));
				progress.updateProgress(numTextures / onePercent);
				numTextures++;
			}
		}

		for (Enemy enemy : currentStage.getEnemies()) {
			TextureManager.getInstance().addTexture(enemy.getExplosionTex());
			TextureManager.getInstance().addTexture(enemy.getTexture());
			numTextures += 2;
			progress.updateProgress(numTextures / onePercent);
		}

		log.info("Loaded " + numTextures + " textures.");

		logoTexId = TextureManager.getInstance().getTexture("images/logo/supernova_logo.png");
		imgTexId = TextureManager.getInstance().getTexture("images/logo/supernova_img.png");
		starfieldTexId = TextureManager.getInstance().getTexture("images/logo/starfield.png");
		hudMaskId = TextureManager.getInstance().getTexture("images/ui/mask.png");

	}

	/**
	 * Start the main game loop
	 */
	private void run() {

		if (soundEnabled) {
			musicPlayer.start();
			musicPlayer.setVolume(musicVolume);
			songName.setText(musicPlayer.getCurrentSongName());
		}
		do {
			if (finished)
				break;
			Display.update();
			if (Display.isCloseRequested()) {
				finished = true;
			}
			else if (Display.isActive()) {
				logic();
				render();
				Display.sync(MAX_FRAMERATE);
				updateFPS();
			}
			else {
				try {
					Thread.sleep(100L);
				}
				catch (InterruptedException e) {
				}
				logic();
				if (Display.isVisible() || Display.isDirty()) {
					render();
				}
			}

		}
		while (true);
	}

	private void updateFPS() {
		float sec = (float) ((System.currentTimeMillis() - frameTime)) / 1000;
		fps = (1 / sec);
		hud.setFps(fps);
		frameTime = System.currentTimeMillis();
	}

	private void cleanup() {
		if (soundEnabled) {
			soundManager.destroy();
		}
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
		Controllers.destroy();
	}

	/**
	 * Logic selector, processes game logic depending on game state
	 */	
	private void logic() {

		switch (gameState) {

			case RUNNING:
				processRunningStateLogic();
				break;
			case PAUSE:
				processPauseLogic();
				break;
			case GAME_OVER:
				processGameOverLogic();
				break;
			case MENU:
				processMenuLogic();
				break;
			case LEVEL_PREPARE_END:
				checkPendingEvents();
				break;
			case LEVEL_END:
				processLevelEndLogic();
				break;
			default:
				break;
		}

	}

	private void checkPendingEvents() {

		if (explodables.isEmpty() && System.currentTimeMillis() - bossDefeatTime >= levelEndDelay) {
			gameState = GameState.LEVEL_END; 
		}
		
		processRunningStateLogic();
	}

	/**
	 * Logic in pause mode
	 */
	private void processPauseLogic() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (Keyboard.getEventKeyState()) {
				}
				else {
					gameState = GameState.RUNNING;
				}
			}
		}
	}

	/**
	 * Logic to be executed if level has been ended
	 */
	private void processLevelEndLogic() {

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Keyboard.poll();
			if (soundEnabled) {
				musicPlayer.nextSong();
			}
			enemies.clear();
			bullets.clear();
			ship.setEnergy(100);
			ship.resetAmmo();
			hud.setEnergy(ship.getEnergy());
			gameState = GameState.RUNNING;
			nextStage();
		}

		game.setyOffset(game.getyOffset() + velocity);

		int maxSize = currentStage.getTileSize() * currentStage.getImages().size();

		if (game.getyOffset() >= maxSize) {
			game.setyOffset(0);
		}

	}

	/**
	 * Select the next stage
	 */
	private void nextStage() {

		if (stageIndex < game.getStages().size() - 1) {
			stageIndex++;
		}
		else {
			stageIndex = 0;
		}
		prepareCurrentStage();
	}

	/**
	 * prepare the current stage. Thus reset the entities, load according enemies
	 * and different stage relevant stuff
	 */
	private void prepareCurrentStage() {
		currentStage = game.getStages().get(stageIndex);
		availableEnemies.clear();
		availableEnemies.addAll(currentStage.getEnemies());

		currentStage.setTimeLeft(currentStage.getDuration());

		if (soundEnabled) {
			musicPlayer.nextSong();
		}

		Keyboard.enableRepeatEvents(true);
		levelName.setMode(Fade.IN);
		levelName.setText(currentStage.getName());
		fadeables.add(levelName);
		hud.setEnergyBlinking(false);
		hud.setEnergy(ship.getEnergy());
		drawBackground();
		lastEnemyTime = System.currentTimeMillis();
		lastShotTime = System.currentTimeMillis();
		startTime = System.currentTimeMillis();
		enemies.clear();
		bullets.clear();
		ship.setEnergy(100);
		ship.setShielded(false);
		ship.resetAmmo();
		hud.setScore(String.valueOf(game.getScore()));
		game.setyOffset(0);
		Mouse.setCursorPosition(WIDTH / 2, INITIAL_SHIP_Y);

		for (StaticEnemy enemy : currentStage.getStaticEnemies()) {
			try {
				enemy = (StaticEnemy) enemy.clone();
			}
			catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

			enemy.setGame(game);
			enemies.add(enemy);
		}

		reloadStageTimer();
	}

	/**
	 * reloads the stage time with the value given from game.xml
	 */
	private void reloadStageTimer() {
		if (stageTimer != null) {
			stageTimer.cancel();
			stageTimer = null;
		}
		stageTimer = new Timer();
		stageTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (currentStage.getDuration() > 0) {
					currentStage.setTimeLeft(currentStage.getTimeLeft() - 1);
				}
			}
		}, 0, 1000);
	}

	/**
	 * Logic to be executed when game is over
	 */
	private void processGameOverLogic() {

		checkEnemyState();
		enemiesShoot();
		moveBullets();
		moveEnemies();
		moveEntities();

		Keyboard.enableRepeatEvents(false);

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_SPACE || Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				if (Keyboard.getEventKeyState()) {
				}
				else {
					if (soundEnabled) {
						musicPlayer.nextSong();
					}
					enemies.clear();
					bullets.clear();
					ship.setEnergy(100);
					ship.resetAmmo();
					scoreValueText.setText(String.valueOf(game.getScore()));
					game.setScore(0);
					game.setEnemiesShot(0);
					hud.setEnergy(ship.getEnergy());
					gameState = GameState.MENU;
					resetStage();
					Mouse.setGrabbed(false);
				}
			}
		}

		game.setyOffset(game.getyOffset() + velocity);

		int maxSize = currentStage.getTileSize() * currentStage.getImages().size();

		if (game.getyOffset() >= maxSize) {
			game.setyOffset(0);
		}

	}

	private void enemiesShoot() {
		for (Entity entity : enemies) {
			if (entity instanceof Enemy) {
				Enemy enemy = (Enemy) entity;
				Bullet bullet = enemy.shoot();
				if (bullet != null) {
					bullets.add(bullet);
				}
			}
		}
	}

	private void checkEnemyState() {

		for (Boss boss : currentStage.getBosses()) {
			if (!defeatingBoss && currentStage.getDuration() - currentStage.getTimeLeft() == boss.getAppereanceTime()) {

				Boss newBoss;

				try {
					newBoss = (Boss) boss.clone();
					newBoss.setXLoc(WIDTH / 2);
					newBoss.setYLoc(displayMode.getHeight() + boss.getExplosionSize() * 2);

					if (newBoss.getShotInterval() == 0) {
						long t = random.nextInt(500);
						newBoss.setShotInterval(500 + t);
					}

					enemies.add(newBoss);
					defeatingBoss = true;
				}
				catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}

			}
		}

		if (defeatingBoss) {
			return;
		}

		if (availableEnemies.size() < 1) {
			return;
		}

		int enemyClass = random.nextInt(currentStage.getEnemies().size());
		
		
		switch (game.getSequence()) {
			case 0:
				// totally random
				if (System.currentTimeMillis() - lastEnemyTime > 500 && System.currentTimeMillis() - startTime > 5000) {
					float f = random.nextFloat();
					Enemy enemy;
					try {
						enemy = (Enemy) availableEnemies.get(enemyClass).clone();

						if (enemy instanceof StaticEnemy) {
							return;
						}

						enemy.setXLoc(WIDTH * f);
						enemy.setYLoc(displayMode.getHeight());
						enemy.setCurrentStrategy(new Random().nextInt(enemy.getStrategies().size()));
						
						if (enemy.getShotInterval() == 0) {
							long t = random.nextInt(500);
							enemy.setShotInterval(500 + t);
						}

						enemies.add(enemy);
					}
					catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					lastEnemyTime = System.currentTimeMillis();
				}
				break;
			case 1:
				// one line horizontal
				if (System.currentTimeMillis() - lastEnemyTime > 4000 && System.currentTimeMillis() - startTime > 2) {
					
					int strategy = new Random().nextInt(Enemy.MAX_STRATEGIES);
					
					for (int i = 1; i < 6; i++) {

						Enemy enemy;
						try {
							enemy = (Enemy) availableEnemies.get(enemyClass).clone();

							if (enemy instanceof StaticEnemy) {
								return;
							}

							enemy.setXLoc(i * 100);
							enemy.setYLoc(displayMode.getHeight());
							enemy.setCurrentStrategy(strategy);
							
							if (enemy.getShotInterval() == 0) {
								long t = random.nextInt(500);
								enemy.setShotInterval(500 + t);
							}
							enemies.add(enemy);

						}
						catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					lastEnemyTime = System.currentTimeMillis();
				}
				break;
			case 2:
				// one line 45 degrees
				if (System.currentTimeMillis() - lastEnemyTime > 4000 && System.currentTimeMillis() - startTime > 2) {
					
					int strategy = new Random().nextInt(Enemy.MAX_STRATEGIES);
					
					for (int i = 1; i < 6; i++) {
						Enemy enemy;
						try {
							enemy = (Enemy) availableEnemies.get(enemyClass).clone();

							if (enemy instanceof StaticEnemy) {
								return;
							}

							enemy.setXLoc(i * 100);
							enemy.setYLoc(displayMode.getHeight() + i * 50);
							enemy.setCurrentStrategy(strategy);
							
							if (enemy.getShotInterval() == 0) {
								long t = random.nextInt(500);
								enemy.setShotInterval(500 + t);
							}
							enemies.add(enemy);
						}
						catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					lastEnemyTime = System.currentTimeMillis();
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Logic to be executed while game is running
	 */
	private void processRunningStateLogic() {

		while (Keyboard.next()) {

			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (Keyboard.getEventKeyState()) {
				}
				else {
					gameState = GameState.PAUSE;
				}
			}

			else if (Keyboard.getEventKey() == Keyboard.KEY_X) {
				if (Keyboard.getEventKeyState()) {
				}
				else {
					nextStage();
					levelName.setMode(Fade.IN);
					levelName.setText(currentStage.getName());
					fadeables.add(levelName);
				}
			}

		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			ship.increaseLevel();			
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Keyboard.enableRepeatEvents(false);
			Mouse.setGrabbed(false);
			gameState = GameState.MENU;
			Mouse.setGrabbed(false);
			enemies.clear();
			bullets.clear();
			if (soundEnabled) {
				musicPlayer.nextSong();
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			yPos += 2.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			yPos -= 2.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			xPos -= 5.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			xPos += 5.0f;
		}
		if (soundEnabled) {
			while (Mouse.next()) {
				if (Mouse.getEventButton() == 1) {
					if (!Mouse.getEventButtonState()) {
						musicPlayer.nextSong();
						songName.setText(musicPlayer.getCurrentSongName());
						nowPlaying.setMode(Fade.IN);
						songName.setMode(Fade.IN);
						fadeables.add(nowPlaying);
						fadeables.add(songName);
					}
				}
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_N) || ((defaultController != null) && defaultController.isButtonPressed(4))) {
				musicPlayer.previousSong(musicVolume);
				musicPlayer.setVolume(musicVolume);
				nowPlaying.setMode(Fade.IN);
				songName.setMode(Fade.IN);
				fadeables.add(nowPlaying);
				fadeables.add(songName);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_M) || ((defaultController != null) && defaultController.isButtonPressed(5))) {
				musicPlayer.nextSong();
				songName.setText(musicPlayer.getCurrentSongName());
				nowPlaying.setMode(Fade.IN);
				songName.setMode(Fade.IN);
				fadeables.add(nowPlaying);
				fadeables.add(songName);

			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			if (!explodables.contains(ship)) {
				explodables.add(ship);
				ship.setVisible(false);
				if (soundEnabled) {
					ship.explode();
				}
			}
		}

		if (defaultController != null) {

			if (Math.abs(defaultController.getXAxisValue()) > 0.3) {
				deltaX = defaultController.getXAxisValue() * 50;
			}
			else {
				deltaX = 0;
			}
			if (Math.abs(defaultController.getYAxisValue()) > 0.) {
				deltaY = -defaultController.getYAxisValue() * 50;
			}
			else {
				deltaY = 0;
			}

			float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			if (distance > 1) {
				xPos += deltaX * 0.1;
				yPos += deltaY * 0.1;
			}

		}
		else {
			deltaX = Mouse.getX() - ship.getXLoc();
			deltaY = Mouse.getY() - ship.getYLoc();
			float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			if (distance > 1) {
				xPos += deltaX * 0.1;
				yPos += deltaY * 0.1;
			}
		}

		clampShipToScreen();

		shootX = xPos;
		shootY = yPos;
		
		if (gameState.equals(GameState.RUNNING)) {			
			checkEnemyState();
			enemiesShoot();
		}

		moveBullets();
		moveEnemies();
		moveEntities();

		shoot();

		checkBulletColission();

		// Am I hit by an enemy ?

		checkEnemyCollision();

		// Any entity hit??

		checkEntityCollision();

		game.setyOffset(game.getyOffset() + velocity);

		int maxSize = currentStage.getTileSize() * currentStage.getImages().size();

		if (game.getyOffset() >= maxSize) {
			game.setyOffset(0);
		}

		ship.setXLoc(xPos);
		ship.setYLoc(yPos);

		hud.setEnergyBlinking(ship.getEnergy() <= 20);

		if (currentStage.getDuration() > 0 && currentStage.getTimeLeft() == 0) {
			summaryText.setText("Your Score is " + game.getScore() + ". Press SPACE to continue.");
			gameState = GameState.LEVEL_END;
		}

	}

	private void clampShipToScreen() {
		if (xPos < ship.getWidth() / 2 * ship.getScale()) {
			xPos = ship.getWidth() / 2 * ship.getScale();
		}
		if (xPos > WIDTH - ship.getWidth() / 2 * ship.getScale()) {
			xPos = WIDTH - ship.getWidth() / 2 * ship.getScale();
		}

		if (yPos < ship.getWidth() / 2 * ship.getScale()) {
			yPos = ship.getWidth() / 2 * ship.getScale();
		}
		if (yPos > displayMode.getHeight() - ship.getHeight() / 2 * ship.getScale()) {
			yPos = displayMode.getHeight() - ship.getHeight() / 2 * ship.getScale();
		}
	}

	private void checkEntityCollision() {
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {

			Entity entity = it.next();

			if (entity.collides(ship)) {

				pickup.setMode(Fade.IN);
				pickup.setText(entity.getClass().getSimpleName());
				fadeables.add(pickup);
				it.remove();

				if (entity instanceof Ammo) {

					Ammo a = (Ammo) entity;
					ship.addAmmo(a.getAmount());
					if (ship.getAmmo() > Ship.MAX_AMMO) {
						ship.resetAmmo();
					}

					hud.setAmmo(calculateAmmoInPercent(ship.getAmmo()));

					if (soundEnabled) {
						if (!soundManager.isPlayingSound()) {
							soundManager.playEffect(reloadSource);
						}
					}

				}
				else if (entity instanceof Energy) {
					Energy e = (Energy) entity;
					ship.addEnergy(e);
					hud.setEnergy(ship.getEnergy());
					if (soundEnabled) {
						if (!soundManager.isPlayingSound()) {
							soundManager.playEffect(energySource);
						}
					}
				}
				else if (entity instanceof LevelUp) {
					LevelUp e = (LevelUp) entity;
					ship.increaseLevel();
					if (soundEnabled) {
						if (!soundManager.isPlayingSound()) {
							soundManager.playEffect(energySource);
						}
					}
				}
				else if (entity instanceof Shield) {
					if (soundEnabled) {
						if (!soundManager.isPlayingSound()) {
							soundManager.playEffect(shieldSource);
						}
					}

					if (ship.isShielded()) {
						return;
					}

					ship.setShielded(true);
					// turn of the shield after 30 seconds
					defaultTimer.schedule(new TimerTask() {

						private int timeleft = 30;

						@Override
						public void run() {

							hud.setShieldTimeLeFt(--timeleft);

							if (timeleft < 1) {
								ship.setShielded(false);
								cancel();
							}
						}

					}, 0, 1000);
				}

			}

		}
	}

	private void checkEnemyCollision() {
		for (Iterator<Entity> it = enemies.iterator(); it.hasNext();) {

			Entity entity = it.next();

			if (entity instanceof Enemy) {

				Enemy enemy = (Enemy) entity;
				
				if (ship.collides(enemy)) {

					if (enemy instanceof StaticEnemy) {
						StaticEnemy s = (StaticEnemy)enemy;
						if (!s.isDestroyed()) {
							s.setDestroyed(true);
						}
						else {
							continue;
						}
					}
					
					if (!ship.hit()) {
						if (!explodables.contains(ship)) {
							if (soundEnabled) {
								ship.explode();
								soundManager.playEffect(gameOverSource);
							}
							explodables.add(ship);
							scoreValueText.setText(String.valueOf(game.getScore()));
							game.setScore(0);
							hud.setScore(String.valueOf(game.getScore()));
							ship.setEnergy(100);
							ship.setShielded(false);
							hud.setEnergy(100);
							gameState = GameState.GAME_OVER;
						}
					}
					else {
						if (!(enemy instanceof StaticEnemy)) {
							it.remove();																
						}
						else {
							StaticEnemy s = (StaticEnemy)entity;
							s.setDestroyed(true);
						}
						explodables.add(enemy);
						if (soundEnabled) {
							enemy.explode();
						}
						game.enemyKilled();

						hud.setEnergy(ship.getEnergy());
						dropLoot(enemy);
					}
				}

			}

		}
	}

	private void checkBulletColission() {
		for (Iterator<Bullet> bulletIt = bullets.iterator(); bulletIt.hasNext();) {

			Bullet bullet = bulletIt.next();

			// Any enemy hit ?
			for (Iterator<Entity> it = enemies.iterator(); it.hasNext();) {
				Entity entity = it.next();
				if (entity instanceof Enemy) {
					Enemy enemy = (Enemy) entity;
					if (bullet.getDirection().equals(Direction.UP) && enemy.collides(bullet)) {
						if (enemy.hit(bullet) == 0) {
							
							if (!(enemy instanceof StaticEnemy)) {
								it.remove();																
							}
							else {
								StaticEnemy s = (StaticEnemy)entity;
								s.setDestroyed(true);
							}
							
							explodables.add(enemy);
							if (soundEnabled) {
								enemy.explode();
							}
							dropLoot(enemy);
							game.enemyKilled();
							game.setScore(game.getEnemiesShot() * enemy.getPoints());
							hud.setScore(String.valueOf(game.getScore()));

							if (enemy instanceof Boss) {
								Boss boss = (Boss) enemy;
								defeatingBoss = false;
								if (boss.isFinalBoss()) {
									summaryText.setText("Your Score is " + game.getScore() + ". Press SPACE to continue.");
									bossDefeatTime = System.currentTimeMillis();
									gameState = GameState.LEVEL_PREPARE_END;
									return;
								}
							}

						}
						try {
							bulletIt.remove();
						}
						catch (Exception e) {
							// ignore
						}
					}
				}

			}
			// Am I hit by a bullet
			if (bullet.getDirection().equals(Direction.DOWN) && ship.collides(bullet)) {
				if (!ship.hit()) {
					if (ship.getEnergy() == 0) {
						if (!explodables.contains(ship)) {
							if (soundEnabled) {
								ship.explode();
							}
							explodables.add(ship);
							scoreValueText.setText(String.valueOf(game.getScore()));
							game.setScore(0);
							ship.setEnergy(100);
							hud.setEnergy(100);
							hud.setScore(String.valueOf(game.getScore()));
							gameState = GameState.GAME_OVER;
						}
					}
				}
				else {
					bulletIt.remove();
					hud.setEnergy(ship.getEnergy());
					// if (SOUND_ENABLED) {
					// if (!sm.isPlayingSound()) {
					// sm.playSound(shipHitSource);
					// }
					// }
					/*
					 * if (ship.getEnergy() == 20) { if (SOUND_ENABLED) {
					 * sm.playEffect(energyWarningSource); } }
					 */
				}
			}
		}
	}

	private void dropLoot(Enemy enemy) {
		if (game.getEnemiesShot() % 7 == 0 && game.getEnemiesShot() > 0) {
			entities.add(new Ammo(enemy.getXLoc(), enemy.getYLoc(), 0));
		}
		if (game.getEnemiesShot() % 23 == 0 && game.getEnemiesShot() > 0) {
			entities.add(new Energy(enemy.getXLoc(), enemy.getYLoc(), 0));
		}
		if (game.getEnemiesShot() % 10 == 0 && game.getEnemiesShot() > 0) {
			entities.add(new Shield(enemy.getXLoc(), enemy.getYLoc(), 0));
		}
		if (game.getEnemiesShot() % 15 == 0 && game.getEnemiesShot() > 0) {
			entities.add(new LevelUp(enemy.getXLoc(), enemy.getYLoc(), 0));
		}
	}
	
	private void moveEnemies() {
		for (Iterator<Entity> it = enemies.iterator(); it.hasNext();) {

			Entity entity = it.next();

			if (entity.getYLoc() < -50) {
				it.remove();
			}
			else {
				entity.fly();
			}

		}
	}

	private void moveBullets() {
		for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {

			Bullet bullet = it.next();

			if (bullet.getYLoc() > currentStage.getTileSize() && bullet.getYLoc() < 0) {
				it.remove();
			}
			else {
				bullet.fly();
			}

		}
	}

	private void moveEntities() {
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {

			Entity entity = it.next();

			if (entity.getYLoc() > currentStage.getTileSize() && entity.getYLoc() < 0) {
				it.remove();
			}
			else {
				entity.fly();
			}
		}
	}

	private void shoot() {

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.isButtonDown(0) || ((defaultController != null) && defaultController.isButtonPressed(0))) {

			if (ship.getAmmo() > 0) {
				if (System.currentTimeMillis() - lastShotTime > 200) {
					bullets.add(new Bullet(BulletColor.GREEN, shootX + 8, shootY + 10 +random.nextFloat(), Direction.UP));
					bullets.add(new Bullet(BulletColor.GREEN, shootX + -8, shootY + 10 + random.nextFloat(), Direction.UP));
					bullets.add(new Bullet(BulletColor.GREEN, shootX + 43, shootY + random.nextFloat() - 20, Direction.UP));
					bullets.add(new Bullet(BulletColor.GREEN, shootX + -43, shootY + random.nextFloat() - 20, Direction.UP));
					
					if (ship.getLevel() >= 1) {
						bullets.add(new Bullet(BulletColor.GREEN, shootX +  63, shootY + random.nextFloat() - 20, Direction.UP));
						bullets.add(new Bullet(BulletColor.GREEN, shootX + -63, shootY + random.nextFloat() - 20, Direction.UP));
					}
					if (ship.getLevel() >= 2) {
						bullets.add(new Bullet(BulletColor.GREEN, shootX +  83, shootY + random.nextFloat() - 20, Direction.UP));
						bullets.add(new Bullet(BulletColor.GREEN, shootX + -83, shootY + random.nextFloat() - 20, Direction.UP));
					}
					
					
					lastShotTime = System.currentTimeMillis();
					if (soundEnabled) {
						if (!soundManager.isPlayingSound()) {
							ship.shoot();
						}
					}
				}

				ship.decreaseAmmo();
				hud.setAmmo(calculateAmmoInPercent(ship.getAmmo()));

			}
		}
	}

	private int calculateAmmoInPercent(int ammo) {

		float onePercent = MAX_AMMO / 100;
		float ammoInPercent = ammo / onePercent;

		return (int) ammoInPercent;

	}

	private void render() {

		glPushMatrix();
		glTranslatef(displayMode.getWidth() / 2 - WIDTH / 2, 0, 0);

		switch (gameState) {

			case RUNNING:
			case LEVEL_PREPARE_END:
				processRunningStateRender();
				break;
			case PAUSE:
				processPauseRender();
				break;
			case GAME_OVER:
				processGameOverRender();
				break;
			case MENU:
				processMenuRender();
				break;
			case LEVEL_END:
				processLevelEndRender();
				break;
			default:
				break;

		}

		glPopMatrix();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		TextureUtil.drawTexture(0, displayMode.getWidth() / 2, displayMode.getHeight() / 2, 1920, 1200, hudMaskId);

	}

	private void processPauseRender() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 1.0f);

		glPushMatrix();
		glTranslatef(0, -game.getyOffset(), 0);
		glCallList(backgroundTexId);
		glPopMatrix();

		for (Bullet bullet : bullets) {
			bullet.draw();
		}
		for (Entity entity : enemies) {
			entity.draw();
		}
		for (Entity entity : entities) {
			entity.draw();
		}

		ship.draw();
		pauseText.draw();

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

	}

	private void processLevelEndRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 1.0f);
		glPushMatrix();
		glTranslatef(0, -game.getyOffset(), 0);
		glCallList(backgroundTexId);
		glPopMatrix();

		for (Entity entity : enemies) {
			entity.draw();
		}

		for (Iterator<IExplodable> it = explodables.iterator(); it.hasNext();) {
			IExplodable explodable = it.next();

			if (explodable.getExplosionIndex() < 63) {
				explodable.setExplosionIndex(explodable.getExplosionIndex() + 1);
				explodable.drawExplosion(explodable.getExplosionIndex());
			}
			else {
				it.remove();
			}

		}

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

		summaryText.draw();

	}

	private void processMenuLogic() {

		if (!menuIsShowing) {

			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || ((defaultController != null) && defaultController.isButtonPressed(0))) {
				startGame();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F10)) {
				finished = true;
			}

		}
		if (menuIsShowing) {
			while (Keyboard.next()) {
				nifty.getScreen("menu").keyEvent(new KeyboardInputEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState(), false, false));
			}
		}
		while (Keyboard.next()) {

			if (Keyboard.getEventKey() == Keyboard.KEY_F12) {
				if (Keyboard.getEventKeyState()) {
				}
				else {
					if (!menuIsShowing) {
						menuIsShowing = true;
						Mouse.setGrabbed(false);
						nifty.gotoScreen("menu");
					}
					else {
						nifty.gotoScreen("mainMenu");
					}
				}
			}
		}

		starfieldYOffset += starfieldVelocity;

		if (starfieldYOffset >= HEIGHT)
			starfieldYOffset = 0;

		nifty.update();

		novaRot += 0.1f;

	}

	public void startGame() {
		Mouse.setGrabbed(true);
		resetStage();
		game.setScore(0);
		game.setEnemiesShot(0);
		gameState = GameState.RUNNING;
		hud.setScore("Score " + game.getScore());
		prepareCurrentStage();
	}

	private void resetStage() {
		if (game.getStages().size() > 0) {
			currentStage = game.getStages().get(game.getDefaultStage());
			currentStage.setTimeLeft(currentStage.getDuration());
			stageIndex = game.getDefaultStage();
		}
		else {
			throw new RuntimeException("Could not init primary stage.");
		}
		availableEnemies.clear();
		availableEnemies.addAll(currentStage.getEnemies());
		defeatingBoss = false;
	}

	private void processMenuRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 1.0f);

		TextureUtil.drawTexture(0, 400, starfieldYOffset + 1200, 400, starfieldTexId);
		TextureUtil.drawTexture(0, 400, starfieldYOffset + 400, 400, starfieldTexId);
		TextureUtil.drawTexture(0, 400, starfieldYOffset - 400, 400, starfieldTexId);
		if (!isMenuIsShowing()) {
			TextureUtil.drawTexture(novaRot, 400, displayMode.getHeight() / 2, 400, imgTexId);
			TextureUtil.drawTexture(0, 400, displayMode.getHeight() / 2, 400, logoTexId);
		}	
		glDisable(GL_TEXTURE_2D);

		// glPushMatrix();
		// glOrtho(0.0d, Display.getDisplayMode().getWidth(),
		// Display.getDisplayMode().getHeight(), 0.0d, -1.0d, 1.0d);
		// nifty.render(false);
		// glPopMatrix();

		glPushMatrix();
		glPushAttrib(GL_ALL_ATTRIB_BITS);

		// set up GL state for Nifty rendering
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0d, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0.0d, -1.0d, 1.0d);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);

		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_NOTEQUAL, 0);

		glDisable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);

		// render Nifty (this will change OpenGL state)
		nifty.render(false);

		// restore your OpenGL state
		glPopAttrib();
		glPopMatrix();

		glPushMatrix();
		glPushAttrib(GL_ALL_ATTRIB_BITS);

		// set up GL state for Nifty rendering
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0d, Display.getDisplayMode().getWidth(), 0.0d, Display.getDisplayMode().getHeight(), -1.0d, 1.0d);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		// restore your OpenGL state
		glPopAttrib();
		glPopMatrix();

	}

	private void processGameOverRender() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 1.0f);
		glPushMatrix();
		glTranslatef(0, -game.getyOffset(), 0);
		glCallList(backgroundTexId);
		glPopMatrix();

		for (Entity entity : enemies) {
			entity.draw();
		}

		for (Iterator<IExplodable> it = explodables.iterator(); it.hasNext();) {
			IExplodable explodable = it.next();

			if (explodable.getExplosionIndex() < 63) {
				explodable.setExplosionIndex(explodable.getExplosionIndex() + 1);
				explodable.drawExplosion(explodable.getExplosionIndex());
			}
			else {
				it.remove();
			}

		}

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

		gameOverText.draw();
		scoreText.draw();
		scoreValueText.draw();

	}

	private void processRunningStateRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1.0f, 1.0f, 1.0f);
		glPushMatrix();
		glTranslatef(0, -game.getyOffset(), 0);
		glCallList(backgroundTexId);
		glPopMatrix();

		for (Bullet bullet : bullets) {
			bullet.draw();
		}
		for (Entity enemy : enemies) {
			enemy.draw();
		}
		for (Entity entity : entities) {
			entity.draw();
		}

		ship.draw();

		for (Iterator<IExplodable> it = explodables.iterator(); it.hasNext();) {
			IExplodable explodable = it.next();

			if (explodable.getExplosionIndex() < 63) {
				explodable.setExplosionIndex(explodable.getExplosionIndex() + 1);
				explodable.drawExplosion(explodable.getExplosionIndex());
			}
			else {
				if (explodable instanceof Ship) {
					gameState = GameState.GAME_OVER;
					explodables.remove(ship);
				}
				it.remove();
			}

		}

		float fadeInSpeed = 0.01f;
		float fadeOutSpeed = 0.005f;

		for (Iterator<IFadeable> it = fadeables.iterator(); it.hasNext();) {

			IFadeable fadeable = it.next();

			if (fadeable.getMode() == Fade.IN) {

				if (fadeable.getOpacity() < 1.0f) {
					fadeable.setOpacity(fadeable.getOpacity() + fadeInSpeed);
				}
				else {
					fadeable.setMode(Fade.OUT);
				}

			}
			else {
				if (fadeable.getOpacity() > 0.0f) {
					fadeable.setOpacity(fadeable.getOpacity() - fadeOutSpeed);
				}
				else {
					it.remove();
				}

			}

		}

		if (soundEnabled) {
			nowPlaying.draw();
			songName.draw();
		}

		levelName.draw();
		pickup.draw();

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

		displayTimeLeft();

	}

	private void displayTimeLeft() {

		if (currentStage.getDuration() > 0) {

			int timeLeft = currentStage.getTimeLeft();

			StringBuffer time = new StringBuffer();

			int minutes = timeLeft / 60;
			int seconds = timeLeft - minutes * 60;

			time.append(minutes);
			time.append(":");

			if (seconds < 10) {
				time.append("0");
			}
			time.append(seconds);

			hud.setTimeLeft(time.toString());

		}

	}

	public Ship getShip() {
		return ship;
	}

	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}

	public float getVelocity() {
		return velocity;
	}

	public boolean isMenuIsShowing() {
		return menuIsShowing;
	}

	public void setMenuIsShowing(boolean menuIsShowing) {
		this.menuIsShowing = menuIsShowing;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}
}
