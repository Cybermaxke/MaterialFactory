package me.cybermaxke.materialapi.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static BufferedImage getResizedImage(BufferedImage image) {
		if (image == null) {
			return null;
		}
		
		if (image.getWidth() != image.getHeight()) {
			BufferedImage nImage = new BufferedImage(Math.max(image.getWidth(), image.getHeight()), Math.max(image.getWidth(), image.getHeight()), 2);
		  	nImage.getGraphics().drawImage(image, nImage.getWidth() / 2 - image.getWidth() / 2, nImage.getHeight() / 2 - image.getHeight() / 2, null);
		  	image = nImage;
		}
		
		BufferedImage img = new BufferedImage(128, 128, 2);
		img.getGraphics().drawImage(image.getScaledInstance(128, 128, 1), 0, 0, null);
		
		return img;	
	}
	
	public static BufferedImage getImage(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}
	
	public static BufferedImage getResizedImage(File file) {
		return getResizedImage(getImage(file));
	}
}