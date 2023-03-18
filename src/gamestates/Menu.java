package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameStates;
import main.Game;
import ui.MenuButton;
import util.LoadSave;

import static util.Constants.MenuButtonSize.*;
import static util.Constants.MenuConstants.*;
import static util.Constants.PANEL_HEIGHT;
import static util.Constants.PANEL_WIDTH;

public class Menu extends State implements StateMethods{
	private MenuButton[] buttons = new MenuButton[3];
	
	private BufferedImage backgroundImg;
	private BufferedImage title;
	
	private int titleX = TITLE_STARTING_X;
	private int titleY = TITLE_STARTING_Y;
	private boolean revSpeed = false;
	private int titleSpeed = 0;
	private int titleUpdate = 15;
	private int titleTick = 0;
	
	public Menu(Game game) {
		super(game);
		
		loadButtons();
		loadMenu();
	}

	private void loadMenu() {
		backgroundImg = LoadSave.getSpriteAtLas(LoadSave.MENU_BACKGROUND_IMG);
		
		title = LoadSave.getSpriteAtLas(LoadSave.MENU_TITLE);
	}

	private void loadButtons() {
		// TODO: functional buttons
		
		buttons[0] = new MenuButton(MENU_B_X, MENU_B_Y0, 0, GameStates.PREPLAYMENU);
		buttons[1] = new MenuButton(MENU_B_X, MENU_B_Y1, 1, GameStates.OPTIONS);
		buttons[2] = new MenuButton(MENU_B_X, MENU_B_Y2, 2, GameStates.QUIT);
	}

	@Override
	public void update() {
		updateTitle();
		
		for (MenuButton mb : buttons) {
			 mb.update();
		 }
	}

	@Override
	public void draw(Graphics g) {
//		g.drawImage(backgroundImg, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
		g.setColor(new Color(46, 129, 240));
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
//		g.drawImage(title, titleX, titleY, TITLE_WIDTH, TITLE_HEIGHT, null);
		g.setColor(new Color(249, 250, 77));
		g.fillRect(titleX, titleY, TITLE_WIDTH, TITLE_HEIGHT);
		
		for (MenuButton mb : buttons) {
			 mb.draw(g);
		}
	}

	private void updateTitle() {
		titleTick++;
		
		if (titleTick >= titleUpdate) {
			if (revSpeed) titleSpeed++;
			else titleSpeed--;
			
			titleY += titleSpeed;
			
			if (titleSpeed >= TITLE_SPEED || titleSpeed <= -TITLE_SPEED) {
				revSpeed = !revSpeed;
			}
			
			titleTick = 0;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			 if (isIn(e, mb)) {
				 mb.setMousePressed(true);
				 break;
			 }
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			 if (isIn(e, mb)) {
				 if (mb.isMousePressed()) {
					 mb.applyGameState();
				 }
				 if (mb.getState() == GameStates.PLAYING) {
					 //
				 }
				 break;
			 }
		 }
		
		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) {
			 mb.setMouseOver(false);
		}
		
		for (MenuButton mb : buttons) {
			 if (isIn(e, mb)) {
				 mb.setMouseOver(true);
				 
				 break;
			 }
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			 GameStates.state = GameStates.PLAYING;
		 }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

	private void resetButtons() {
		for (MenuButton mb : buttons) {
			 mb.resetBools();
		 }
	}
	
}
