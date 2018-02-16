package de.pueski.supernova.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.input.Mouse;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.pueski.supernova.SuperNova;
import de.pueski.supernova.game.SoundManager;

public class SettingsController implements ScreenController{

	private static final Log log = LogFactory.getLog(SettingsController.class);
	
	private Nifty nifty;
	private Slider fxVolumeSlider;
	private Slider musicVolumeSlider;
	private CheckBox soundEnabledBox;
	
	private int blipSound;
	
	private Properties properties;
	
	public SettingsController() {
		blipSound = SoundManager.getInstance().getSound("audio/click.wav");
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		
		this.nifty = nifty;
		
		fxVolumeSlider = screen.findNiftyControl("fxVolume", Slider.class);
		musicVolumeSlider = screen.findNiftyControl("musicVolume", Slider.class);
		soundEnabledBox = screen.findNiftyControl("soundEnabled", CheckBox.class);
		
		try {
			File propertiesFile = new File("SuperNova.properties");
			log.info("loading properties from "+propertiesFile.getAbsolutePath());
			properties = new Properties();
			properties.load(new FileInputStream(propertiesFile));
		
			float fxvolume = Float.valueOf(properties.getProperty("fxvolume"));
			float musicvolume = Float.valueOf(properties.getProperty("musicvolume"));
			boolean soundEnabled = Boolean.valueOf(properties.getProperty("soundEnabled"));
			
			fxVolumeSlider.setValue(fxvolume);
			musicVolumeSlider.setValue(musicvolume);
			soundEnabledBox.setChecked(soundEnabled);
			
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}		
	}

	@Override
	public void onEndScreen() {
		SoundManager.getInstance().playEffect(blipSound);
	}

	@Override
	public void onStartScreen() {
		SoundManager.getInstance().playEffect(blipSound);
	}

	@NiftyEventSubscriber(id = "fxVolume")
	public void onFxVolumeChanged(final String id, final SliderChangedEvent event) {
		SoundManager.getInstance().adjustAllVolumes(fxVolumeSlider.getValue());		
	}
	
	@NiftyEventSubscriber(id = "musicVolume")
	public void onMusicVolumeChanged(final String id, final SliderChangedEvent event) {
		if (SuperNova.getInstance().getMusicPlayer() != null) {
			SuperNova.getInstance().getMusicPlayer().setVolume(musicVolumeSlider.getValue());			
		}
	}

	@NiftyEventSubscriber(id = "soundEnabled")
	public void onSoundEnabledChanged(final String id, final CheckBoxStateChangedEvent event){
		if (!soundEnabledBox.isChecked()) {
			SuperNova.getInstance().setSoundEnabled(false);
			SuperNova.getInstance().getMusicPlayer().close();				
		}
		else {
			try {
				SuperNova.getInstance().setSoundEnabled(true);
				SuperNova.getInstance().initSound();
				SuperNova.getInstance().getMusicPlayer().setVolume(musicVolumeSlider.getValue());
				SuperNova.getInstance().getMusicPlayer().nextSong();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}


	
	public void save() {		
		
		properties.put("fxvolume", String.valueOf(fxVolumeSlider.getValue()));
		properties.put("musicvolume", String.valueOf(musicVolumeSlider.getValue()));
		properties.put("soundEnabled", String.valueOf(soundEnabledBox.isChecked()));
		
		try {
			properties.store(new FileOutputStream(new File("SuperNova.properties")), "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Mouse.next();		
		nifty.gotoScreen("mainMenu");
		SuperNova.getInstance().setMenuIsShowing(false);
		
	}
	
}
