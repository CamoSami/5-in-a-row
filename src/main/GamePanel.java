package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;

import inputs.MouseInputs;

import static util.Constants.PANEL_HEIGHT;
import static util.Constants.PANEL_WIDTH;

public class GamePanel extends JPanel{
	private MouseInputs mausInputs;
	private Game game;
	
	public GamePanel(Game game) {
		mausInputs = new MouseInputs(this);
		
		this.game = game;
		
		setPanelSize();
		
		addMouseListener(mausInputs);
		addMouseMotionListener(mausInputs);
	}

	private void setPanelSize() {
		Dimension screenSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		setPreferredSize(screenSize);
		
//		System.out.println("size = " + (int)screenSize.getWidth() + " / " + (int)screenSize.getHeight());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
