package board;

import static util.Constants.GameValues.*;
import static util.Constants.MatchConstants.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import util.LoadSave;

public class Board {
	//	Value of the board
	private Cell[][] cells;
	
	//	Images
	private BufferedImage[][] valueImgs;
	
	//	Mouse state
	private int mouseAction = DEFAULT;
	private boolean mousePressed = false;
	
	//	Turn of the players
	private int currentTurn = VALUE_EMPTY;
	private boolean playerTurn;
	private boolean withBot;
	
	//	Locate the highlighted cell
	private int xMouse, yMouse;
	private int xLastMove = -1, yLastMove = -1;
	private boolean inBoard = false;
	
	//	Locate the connected 5 to draw
	private int x1Win, y1Win;
	private int x2Win, y2Win;
	private int dirX, dirY;
	private boolean active;
	
	//	Either X or O
	private int valueWon;
	
	public Board() {
		initBoard();
		
		initImages();
	}

	private void initImages() {
		valueImgs = new BufferedImage[3][3];
		
		valueImgs[VALUE_EMPTY][DEFAULT] = LoadSave.getSpriteAtLas(LoadSave.VALUE_NONE);
		valueImgs[VALUE_X][DEFAULT] 	= LoadSave.getSpriteAtLas(LoadSave.VALUE_X);
		valueImgs[VALUE_O][DEFAULT] 	= LoadSave.getSpriteAtLas(LoadSave.VALUE_O);
		
		valueImgs[VALUE_EMPTY][HOVER] 	= LoadSave.getSpriteAtLas(LoadSave.VALUE_NONE_HOVER);
		valueImgs[VALUE_X][HOVER] 		= LoadSave.getSpriteAtLas(LoadSave.VALUE_X_HOVER);
		valueImgs[VALUE_O][HOVER] 		= LoadSave.getSpriteAtLas(LoadSave.VALUE_O_HOVER);
		
		valueImgs[VALUE_EMPTY][PRESSED] = LoadSave.getSpriteAtLas(LoadSave.VALUE_NONE_PRESSED);
		valueImgs[VALUE_X][PRESSED] 	= LoadSave.getSpriteAtLas(LoadSave.VALUE_X_PRESSED);
		valueImgs[VALUE_O][PRESSED] 	= LoadSave.getSpriteAtLas(LoadSave.VALUE_O_PRESSED);
	}

	public void initBoard() {
		this.cells = new Cell[BOARD_CONTENT][BOARD_CONTENT];
		for (int i = 0; i < BOARD_CONTENT; i++) {
			for (int j = 0; j < BOARD_CONTENT; j++) {
				cells[i][j] = new Cell();
			}
		}
		
		valueWon = VALUE_EMPTY;
		
		x1Win = -1;
		x2Win = -1;
		y1Win = -1;
		y2Win = -1;
		
		dirX = 0;
		dirY = 0;
		
		active = true;
		
//		System.out.println("Board Initialized!");
	}
	
	public void update(MouseEvent e) {
		xMouse = (int)((e.getX() - BOARD_X) / CELL_SIZE);
		yMouse = (int)((e.getY() - BOARD_Y) / CELL_SIZE);
		
		if ((xMouse <= 15 && xMouse >= 0) && (yMouse <= 15 && yMouse >= 0)) {
			inBoard = true;
			
//			System.out.println(" x = " + xMouse + " | y = " + yMouse + " | mousePressed = " + mousePressed);
			
			if (mousePressed) {
				mouseAction = PRESSED;
			}
			else {
				mouseAction = HOVER;
			}
		}
		
		else inBoard = false;
		
//		System.out.println("x = " + xMouse + " | y = " + yMouse);
	}
	
	public void draw(Graphics g) {
		if (valueWon != VALUE_EMPTY) {
			for (int x = 0; x < BOARD_CONTENT; x++) {
				for (int y = 0; y < BOARD_CONTENT; y++) {
					drawCell(g, x, y, VALUE_EMPTY, PRESSED);
				}
			}
			
			for (int xTemp = x2Win + dirX, yTemp = y2Win + dirY; xTemp != x1Win || yTemp != y1Win; xTemp += dirX, yTemp += dirY) {
				drawCell(g, xTemp, yTemp, valueWon, DEFAULT);
				
//				System.out.println("xTemp = " + xTemp + " | yTemp = " + yTemp);
			}
			
//			System.out.println("WON!");
		}
		
		else {
			for (int x = 0; x < BOARD_CONTENT; x++) {
				for (int y = 0; y < BOARD_CONTENT; y++) {
					if (x == xLastMove && y == yLastMove) {
						
						drawCell(g, x, y, cells[x][y].getPlayerValue(), PRESSED);
						
//						System.out.println("xLastMove = " + xLastMove + " | yLastMove = " + yLastMove);
					}
					
					else if ((xMouse == x || yMouse == y) && inBoard) {
						
						drawCell(g, x, y, cells[x][y].getPlayerValue(), mouseAction);
						
					}
					else {
							
						drawCell(g, x, y, cells[x][y].getPlayerValue(), DEFAULT);
						
					}
//					g.setColor(new Color(3, 3, 200));
//					g.fillRect(BOARD_X + CELL_SIZE * x, BOARD_Y + CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
				}
			}
		}
	}
	
	private void drawCell(Graphics g, int x, int y, int value, int type) {
		g.drawImage(valueImgs[value][type], BOARD_X + CELL_SIZE * x, BOARD_Y + CELL_SIZE * y, CELL_SIZE, CELL_SIZE, null);
	
	}
	
	public void turnPlay(int x, int y) {
		//	TODO: finish
		
		cells[x][y].setPlayerValue(currentTurn);
		
		xLastMove = x;
		yLastMove = y;
		
		checkWin();
		turnPass();
	}
	
	public void turnPass() {
		System.out.println("Turn Passed!");
		
		if (currentTurn == VALUE_O) currentTurn = VALUE_X;
		else currentTurn = VALUE_O;
		
		if (withBot) playerTurn = !playerTurn;
		
		//	TODO: [low priority] reset background radient;
		
		//	TODO: [low priority] reset timer;
	}
	
	public void checkWin() {
		if (lineCheck(1, 1) || lineCheck(1, 0) || lineCheck(0, 1) || lineCheck(1, -1)) {
			valueWon = currentTurn;
			
			System.out.println("x1Win = " + x1Win + " | y1Win = " + y1Win + " | x2Win = " + x2Win + " | y2Win = " + y2Win);
		}
	}
	
	private boolean lineCheck(int i, int j) {
		int count = 1;
		
		int xTemp1 = xLastMove + i;
		int yTemp1 = yLastMove + j;
		int xTemp2 = xLastMove - i;
		int yTemp2 = yLastMove - j;
		
		while ((xTemp1 >= 0 && xTemp1 <= 15) && (yTemp1 >= 0 && yTemp1 <= 15)) {
			if (cells[xTemp1][yTemp1].getPlayerValue() == currentTurn) count++;
			else break;
			
			xTemp1 += i;
			yTemp1 += j;
		}
		
		while ((xTemp2 >= 0 && xTemp2 <= 15) && (yTemp2 >= 0 && yTemp2 <= 15)) {
			if (cells[xTemp2][yTemp2].getPlayerValue() == currentTurn) count++;
			else break;
			
			xTemp2 -= i;
			yTemp2 -= j;
		}
		
		if (count >= 5) {
			x1Win = xTemp1;
			y1Win = yTemp1;
			
			x2Win = xTemp2;
			y2Win = yTemp2;
			
			dirX = i;
			dirY = j;
			
			return true;
		}
		else return false;
	}

	public int isWon() {
		return valueWon;
	}

	public void mousePressed(MouseEvent e) {
		if (active) {
			mousePressed = true;
			
			if (cells[xMouse][yMouse].getPlayerValue() == VALUE_EMPTY) turnPlay(xMouse, yMouse);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (active) mousePressed = false;
	}
	
	public void initFirstTurn(int value) {
		currentTurn = value;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public void setPlayerTurn(boolean playerTurn) {
		this.playerTurn = playerTurn;
	}
	
	public boolean isPlayerTurn() {
		return playerTurn;
	}
	
	public void setWithBot(boolean withBot) {
		this.withBot = withBot;
	}
	
	public int getXLastMove() {
		return xLastMove;
	}
	
	public int getYLastMove() {
		return yLastMove;
	}
}
