package util;

public class Constants {
	public static final int PANEL_WIDTH 	= 512;
	public static final int PANEL_HEIGHT 	= 864;
	
	public static class GameConstants {
		public static final int FPS_SET = 120;
		public static final int UPS_SET = 200;
	}
	
	public static class MenuConstants {
		public static final int TITLE_WIDTH 		= 256;
		public static final int TITLE_HEIGHT		= 108;
		
		public static final int TITLE_STARTING_X	= PANEL_WIDTH / 2 - TITLE_WIDTH / 2;
		public static final int TITLE_STARTING_Y	= PANEL_HEIGHT / 6;
	
		public static final int TITLE_SPEED			= PANEL_HEIGHT / 100;
	}
	
	// TODO: fix xOffset
	public static class MenuButtonSize {
		public static final int MENU_B_WIDTH_DEFAULT	= 24;
		public static final int MENU_B_HEIGHT_DEFAULT	= 24;
		
		public static final int MENU_B_WIDTH	= PANEL_WIDTH / 3;
		public static final int MENU_B_HEIGHT	= PANEL_HEIGHT / 10;
	
		public static final int MENU_B_X	= PANEL_WIDTH / 3;
		
		public static final int MENU_B_Y0	= PANEL_HEIGHT * 8/20;
		public static final int MENU_B_Y1	= PANEL_HEIGHT * 11/20;
		public static final int MENU_B_Y2	= PANEL_HEIGHT * 14/20;
	}
	
	public static class SettingsButtonSize {
		
	}
	
	public static class MatchConstants {
		public static final int BOARD_SIZE = 448;
		
		public static final int BOARD_X = 32;
		public static final int BOARD_Y	= 316;
		
		public static final int CELL_SIZE = BOARD_SIZE / 16;
		
		public static final int BOARD_CONTENT = 16;
	}
	
	public static class GameValues {
		public static final int VALUE_EMPTY = 0;
		public static final int VALUE_X = 1;
		public static final int VALUE_O = 2;
		
		public static final int DEFAULT = 0;
		public static final int HOVER	= 1;
		public static final int PRESSED	= 2;
	}
	
	public static class BotConstants {
		public static final int DEPTH = 5;
		
		public static final int DIR_X[] = {1, -1, -1, 1};
		public static final int DIR_Y[] = {1, 1, -1, -1};
	}
}
