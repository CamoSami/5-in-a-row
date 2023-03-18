package gamestates;

import static util.Constants.PANEL_HEIGHT;
import static util.Constants.PANEL_WIDTH;
import static util.Constants.GameValues.*;
import static util.Constants.MatchConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import board.Board;
import board.Bot;
import main.Game;

public class Playing extends State implements StateMethods{
	private ArrayList<Board> boards;
	private Board board;
	
	private Bot bot;
	
	private Random rnd = new Random();

	//	TODO: finish it + pass turn to bot + initRandomTurn if playerFirstTurn is false
	//		  if bot finished turn, set playerTurn to true again
	private boolean playerTurn = true;		//	TODO: update
	private boolean withBot;
	
	//	TODO: update selection + entire UI
	private int firstMove;
	private boolean playerFirst;
	
	private int setTimer;
	private int constantTimer;
	
	public Playing(Game game) {
		super(game);
		
		boards = new ArrayList<>();
		board = new Board();
		
		initTurnCycle();
		initBot();
		
		//	TODO: initTimer()
		
		//	TODO: if PreplaySettings -> Playing then new match
	}

	private void initTurnCycle() {
		if (firstMove == VALUE_EMPTY) {
			//	Initialize first turn
			if (game.getPreplayMenu().isRndFirstTurn()) {
				playerTurn = rnd.nextBoolean();	
			}
			else playerTurn = game.getPreplayMenu().isPlayerFirstTurn();
			
			playerFirst = playerTurn;
			
			//	Initialize values
			if (playerTurn) firstMove = game.getPreplayMenu().getPreferedValue();
			else {
				if (game.getPreplayMenu().getPreferedValue() == VALUE_O) firstMove = VALUE_X;
				else firstMove = VALUE_O;
			}
			
			announceFirstMove();
		}
		
		//	First move cycle upon winning a match
		else {
			if (firstMove == VALUE_O) firstMove = VALUE_X;
			else firstMove = VALUE_O;
			
			playerFirst = !playerFirst;
			playerTurn = playerFirst;
		}
		
		withBot = game.getPreplayMenu().isAgainstBot();
		
		board.initFirstTurn(firstMove);
		board.setPlayerTurn(playerTurn);
		board.setWithBot(withBot);
		
		if (withBot) initBot();
		
		//	TODO: if withBot is true and firstMove is first initialized, announce Player or Bot go first
	}
	
	private void initBot() {
		if (playerFirst) bot = new Bot(firstMove, firstMove == VALUE_O ? VALUE_X : VALUE_O);
		
		else bot = new Bot( firstMove == VALUE_O ? VALUE_X : VALUE_O, firstMove);
	}
	
	public void update() {
		if (withBot) updateTurnWithBot();
		
		else updateTurnWithPlayer();
		
		// 	TODO: [low priority] update timer + pass move
		
		//	TODO: update BG gradient values
		
		//	TODO: upon move: move() + cycleTurn() + resetTimer(); (perhaps a sound effect?)
		
		//	TODO: upon time runs out: cycleTurn() + resetTimer();
		
		if (board.isWon() != VALUE_EMPTY) updateIfWon();
	}

	private void updateTurnWithPlayer() {
		
	}

	private void updateTurnWithBot() {
			
//		System.out.println("playerTurn = " + playerTurn + 
//				" | board.playerTurn = " + board.isPlayerTurn() + 
//				" | playerFirst = " + playerFirst + 
//				" | botActive = " + bot.isBotActive() + 
//				" | bestMoveReady = " + bot.isBestMoveReady());
		
		if (!playerTurn) {
			if (!bot.isBotActive()) {
				
				bot.setCells(board.getCells());
				
				bot.setBotActive(true);
				bot.setBestMoveReady(false);
				
				System.out.println("BOT PASSED!");
			}
			
			else if (bot.isBestMoveReady()) {
				board.turnPlay(bot.getXBestMove(), bot.getYBestMove());
				
				board.setPlayerTurn(true);
				playerTurn = true;
				
				System.out.println("BOT PLAYED!");
				
				bot.valueProximityUpdate(board.getXLastMove(), board.getYLastMove());
			}
		}
		else {
			if (!board.isPlayerTurn()) {
				playerTurn = false;
				
				bot.valueProximityUpdate(board.getXLastMove(), board.getYLastMove());
			}
		}
	}
	
	private void updateIfWon() {
		//	TODO: [low priority] check score + new board
		
		//	TODO: announce winner
	}

	public void draw(Graphics g) {
		g.setColor(new Color(46, 129, 240));
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		board.draw(g);
	}
	
	private void announceFirstMove() {
		//	TODO: announce who go first
	}

	private void newBoard() {
		boards.add(board);
		board.initBoard();
		
		initBot();
		initTurnCycle();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (playerTurn) {
			board.mousePressed(e);
			
			board.update(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (playerTurn) {
			board.mouseReleased(e);
			
			board.update(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (playerTurn) board.update(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	public void setPlayerTurn(boolean turn) {
		playerTurn = turn;
	}
}
