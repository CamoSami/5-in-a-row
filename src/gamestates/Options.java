package gamestates;

import static util.Constants.PANEL_HEIGHT;
import static util.Constants.PANEL_WIDTH;
import static util.Constants.MenuButtonSize.*;
import static util.Constants.MenuConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.MenuButton;
import util.LoadSave;

public class Options extends State implements StateMethods{
	private MenuButton[] buttons = new MenuButton[3];
	
	private BufferedImage backgroundImg;
	
	public Options(Game game) {
		super(game);

		// TODO: update
		loadButtons();
		loadMenu();
	}

	private void loadMenu() {
		backgroundImg = LoadSave.getSpriteAtLas(LoadSave.MENU_BACKGROUND_IMG);
	}

	private void loadButtons() {
		// TODO: functional buttons
		
		buttons[0] = new MenuButton(MENU_B_X, MENU_B_Y0, 0, GameStates.MENU);
		buttons[1] = new MenuButton(MENU_B_X, MENU_B_Y1, 1, GameStates.MENU);
		buttons[2] = new MenuButton(MENU_B_X, MENU_B_Y2, 2, GameStates.MENU);
	}

	@Override
	public void update() {
		//TODO: update whatever
		
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
		g.setColor(new Color(249, 149, 77));
		g.fillRect(TITLE_STARTING_X - TITLE_WIDTH / 2, TITLE_STARTING_Y * 2, TITLE_WIDTH * 2, TITLE_HEIGHT * 3);
		
		for (MenuButton mb : buttons) {
			 mb.draw(g);
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
					 break;
				 }
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
