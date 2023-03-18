package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {
	//	Menu
	public static final String MENU_BACKGROUND_IMG 	= "bg_test.png";
	public static final String MENU_TITLE			= "refr.png";
	
	//	Menu Button
	public static final String MENU_BUTTON_0		= "439063847966343178.png";
	public static final String MENU_BUTTON_1		= "764167547439939604.png";
	public static final String MENU_BUTTON_2		= "876496032446439464.png";
	
	//	Value
	public static final String VALUE_NONE			= "VALUE_NONE.png";
	public static final String VALUE_NONE_HOVER		= "VALUE_NONE_HOVER.png";
	public static final String VALUE_NONE_PRESSED	= "VALUE_NONE_PRESSED.png";
	
	public static final String VALUE_X				= "VALUE_X.png";
	public static final String VALUE_X_HOVER		= "VALUE_X_HOVER.png";
	public static final String VALUE_X_PRESSED		= "VALUE_X_PRESSED.png";
	
	public static final String VALUE_O				= "VALUE_O.png";
	public static final String VALUE_O_HOVER		= "VALUE_O_HOVER.png";
	public static final String VALUE_O_PRESSED		= "VALUE_O_PRESSED.png";
	
	public static BufferedImage getSpriteAtLas(String fileName) {
		BufferedImage img = null;
		
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return img;
	}
}
