package de.pueski.supernova.tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;

public class TexCheck {

	public static void main(String[] args) throws Exception {
		
		
		final File inputDir = new File("C:\\Users\\mpue\\workspace\\SuperNova\\resources\\images\\mars");
		final File outputDir = new File("e:\\");
		
		
		BufferedImage[] tiles = new BufferedImage[15];
		
		File[] listFiles = inputDir.listFiles(); 
		
		if (listFiles == null) {
			System.out.println("No input files.");
			return;
		}
		
		ArrayList<File> inputFiles = new ArrayList<File>();
		
		for (File file : listFiles) {
			if (file.getName().endsWith("png") || file.getName().endsWith("PNG")) {
				inputFiles.add(file);
			}
		}
		
		// sort alphabetically
		Collections.sort(inputFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		
		for (File file : inputFiles) {
			System.out.println(file.getAbsolutePath());
		}
		
		int i = 0;
		
		for (File file : inputFiles) {
			tiles[i++] = ImageIO.read(file);
		}

		
		int tile = 0;
			
		
		for (int y = 0; y < tiles.length;y++){
			BufferedImage output = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = output.createGraphics();
			g.drawImage(tiles[tile++], 0, 0 , null);
			g.drawString(String.valueOf(y+1), 50, 50);
			ImageIO.write(output, "PNG", new File(outputDir,"mars_"+(y+1)+".png"));			
		}
	
		
	}
	
}
