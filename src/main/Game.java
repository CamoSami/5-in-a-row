package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameStates;
import gamestates.Menu;
import gamestates.Options;
import gamestates.Playing;
import gamestates.PreplayMenu;

import static util.Constants.GameConstants.*;

public class Game implements Runnable{
	//	Essentials
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private AudioPlayer audioPlayer;

	//	Game States
	private Playing playing;
	private Menu menu;
	private PreplayMenu preplayMenu;
	private Options options;
	
	public Game() {
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		startGameLoop();
	}

	private void initClasses() {
		menu = new Menu(this);
		preplayMenu = new PreplayMenu(this);
		playing = new Playing(this);
		options = new Options(this);
		
		audioPlayer = new AudioPlayer();
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update() {
		switch (GameStates.state) {
		case MENU:
			menu.update();
			break;
			
		case PLAYING:
			playing.update();
			break;
			
		case PREPLAYMENU:
			preplayMenu.update();
			break;
			
		case OPTIONS:
			options.update();
			break;
			
		case QUIT:

		default:
			System.exit(0);
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (GameStates.state) {
		case MENU:
			menu.draw(g);
			break;
			
		case PLAYING:
			playing.draw(g);
			break;
			
		case PREPLAYMENU:
			preplayMenu.draw(g);
			break;
			
		case OPTIONS:
			options.draw(g);
			break;
			
		case QUIT:
			break;
			
		default:
			break;
		}
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000 / FPS_SET;
		double timePerUpdate = 1000000000 / UPS_SET;
		
		long previousTime = System.nanoTime();
		
		int frames = 0, updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		while (true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				
//				System.out.println("FPS = " + frames + " | UPS = " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public PreplayMenu getPreplayMenu() {
		return preplayMenu;
	}
	
	public Options getOptions() {
		return options;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
