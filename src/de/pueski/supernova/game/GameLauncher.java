package de.pueski.supernova.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GameLauncher {
	
	private static final Log log = LogFactory.getLog(GameLauncher.class);

	private static final Preferences prefs = Preferences.userRoot().node("SuperNova");
	
	/**
	 * Runs a launch window with resolution selector at the beginning
	 * 
	 * @throws Exception
	 */

	public static void runLauncher(final IStartupCallback callback) throws Exception {

		try {			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
			log.info("failed to set look and feel.");
		}
		
		final JFrame frame = new JFrame("SuperNova launcher");

		frame.setSize(new Dimension(390, 240));
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setUndecorated(true);

		final ConfigPanel configPanel = new ConfigPanel();
		frame.add(configPanel, BorderLayout.CENTER);

		configPanel.getFullscreenCheckBox().setSelected(prefs.getBoolean("fullscreen", false));
		configPanel.getVSyncCheckBox().setSelected(prefs.getBoolean("vsync", false));
		configPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		configPanel.getLaunchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();

				prefs.putBoolean("fullscreen", configPanel.getFullscreenCheckBox().isSelected());
				prefs.putBoolean("vsync", configPanel.getVSyncCheckBox().isSelected());
				prefs.putInt("mode", configPanel.getDisplayModeCombo().getSelectedIndex());

				DisplayMode mode = (DisplayMode) configPanel.getDisplayModeCombo().getSelectedItem();						
				try {
					callback.startup(mode, configPanel.getFullscreenCheckBox().isSelected(), configPanel.getVSyncCheckBox().isSelected());
				}
				catch (Exception e1) {
					return;
				}

			}
		});

		frame.getRootPane().setDefaultButton(configPanel.getLaunchButton());

		BufferedImage icon;
		try {
			icon = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/ui/SuperNova_icon.png"));
			frame.setIconImage(icon);
		}
		catch (IOException e1) {
			System.out.println("Could not load icon.");
		}

		configPanel.getExitButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			ArrayList<DisplayMode> displayModes = new ArrayList<DisplayMode>();

			for (DisplayMode mode : modes) {
				displayModes.add(mode);
			}

			Collections.sort(displayModes, new Comparator<DisplayMode>() {

				@Override
				public int compare(DisplayMode o1, DisplayMode o2) {

					if (o1.getWidth() > o2.getWidth()) {
						return 1;
					}
					else if (o1.getWidth() < o2.getWidth()) {
						return -1;
					}
					else
						return 0;

				}
			});

			for (DisplayMode mode : displayModes) {
				configPanel.getDisplayModeCombo().addItem(mode);
			}

			configPanel.getDisplayModeCombo().setSelectedIndex(prefs.getInt("mode", 0));
		}
		catch (LWJGLException e) {
			throw new RuntimeException(e);
		}

		frame.setVisible(true);

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				configPanel.getLaunchButton().requestFocus();
			}
		});

	}
	
}
