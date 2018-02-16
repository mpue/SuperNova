package de.pueski.supernova.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import org.lwjgl.opengl.DisplayMode;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ConfigPanel extends JPanel {
	private JComboBox<DisplayMode> displayModeCombo;
	private JCheckBox fullscreenCheckBox;
	private JButton launchButton;
	private JCheckBox vSyncCheckBox;
	private JButton exitButton;
	private JLabel label;

	/**
	 * Create the panel.
	 */
	public ConfigPanel() {
		setBackground(Color.BLACK);
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		label = new JLabel("");
		label.setIcon(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/logo/supernova_logo_small.png")));
		add(label, "2, 2, 9, 3, left, default");
		
		JLabel lblDisplayMode = new JLabel("Display Mode");
		lblDisplayMode.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDisplayMode.setForeground(Color.WHITE);
		add(lblDisplayMode, "4, 6, right, default");
		
		displayModeCombo = new JComboBox();
		add(displayModeCombo, "6, 6, 5, 1, fill, default");
		
		JLabel lblFullscreen = new JLabel("Fullscreen");
		lblFullscreen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFullscreen.setForeground(Color.WHITE);
		add(lblFullscreen, "4, 8");
		
		fullscreenCheckBox = new JCheckBox("");
		fullscreenCheckBox.setBorder(new CompoundBorder());
		fullscreenCheckBox.setBackground(Color.BLACK);
		add(fullscreenCheckBox, "6, 8");
		
		JLabel lblVsync = new JLabel("VSync enabled");
		lblVsync.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVsync.setForeground(Color.WHITE);
		add(lblVsync, "8, 8");
		
		vSyncCheckBox = new JCheckBox("");
		vSyncCheckBox.setBackground(Color.BLACK);
		add(vSyncCheckBox, "10, 8");
		
		Dimension buttonSize = new Dimension(100,30);
		
		launchButton = new JButton("Launch");
		launchButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		launchButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		launchButton.setBackground(Color.BLACK);
		launchButton.setPreferredSize(buttonSize);
		add(launchButton, "4, 10, 3, 1");
		
		exitButton = new JButton("Exit");
		exitButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		exitButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		exitButton.setBackground(Color.BLACK);
		exitButton.setPreferredSize(buttonSize);
		add(exitButton, "8, 10, 3, 1");

	}

	public JComboBox<DisplayMode> getDisplayModeCombo() {
		return displayModeCombo;
	}
	public JCheckBox getFullscreenCheckBox() {
		return fullscreenCheckBox;
	}
	public JButton getLaunchButton() {
		return launchButton;
	}
	public JCheckBox getVSyncCheckBox() {
		return vSyncCheckBox;
	}
	public JButton getExitButton() {
		return exitButton;
	}
}
