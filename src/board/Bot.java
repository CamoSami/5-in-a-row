package board;

import static util.Constants.GameValues.*;
import static util.Constants.MatchConstants.*;
import static util.Constants.BotConstants.*;

import java.util.Random;

public class Bot implements Runnable{	
	private Cell[][] cells;
	
	private boolean botActive = false;
	private boolean bestMoveReady = true;
	
	private int botValue;
	private int playerValue;
	
	private Thread botThread;
	
//	private int timeLimit;
//	private int timeLeft;
	
	private int xBestMove = 1, yBestMove = 1;
	private Random rnd;
	
	public Bot(int botValue, int player1Value) {
		//	TODO: Try using thread to avoid lagging
		
		this.botValue = botValue;
		this.playerValue = player1Value;
		
		rnd = new Random();
		
		botThread = new Thread(this);
		botThread.start();
	}

	@Override
	public void run() {
		while (true) {
//			System.out.print("");
			
			lookForBestMove();
		}
	}
	
	private void lookForBestMove() {
		if (botActive) {
			//	TODO: find best move
			
			getBestMove();
			
			System.out.println("x = " + xBestMove + " | y = " + yBestMove);
			
			bestMoveReady = true;
			botActive = false;
		}
	}
    
    public boolean getBestMove(){
        int bestValue = Integer.MIN_VALUE;
        
        for (int i = 0; i < BOARD_CONTENT; i++) {
            for (int j = 0; j < BOARD_CONTENT; j++) {
                Cell cell = cells[i][j];
                
                if(cell.getPlayerValue() == VALUE_EMPTY && cell.getMinmaxValue() != -1){
                    cell.setPlayerValue(botValue);

                    int moveValue = minimax(DEPTH, i, j);
                 
//                    System.out.println(moveValue + " " + bestValue);
                    
                    cell.setPlayerValue(VALUE_EMPTY);
                    
                    if(moveValue > bestValue){
                        xBestMove = i;
                        yBestMove = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return true;
    }
    
    public int minimax(int depth, int i, int j){
        return alphabeta(depth, i, j, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }
    
    public int alphabeta(int depth, int i1, int j1, int a, int b, int isMax){

        int u = bfs(i1, j1, isMax);
        
        /* Debug */
//        if(depth == 1){
//            System.out.println(u);
//            for(int i2 = 0; i2 < M; i2++){
//                for(int j2 = 0; j2 < N; j2++){
//                    Cell cell = matrix[i2][j2];
//                    if(cell.getValue().equals(Cell.EMPTY_VALUE)){
//                        System.out.print(" + ");
//                    }else{
//                        System.out.print(" " + cell.getValue() + " ");
//                    }
//                }
//                System.out.println("");
//            }
//            System.out.println("");
//            System.out.println("");
//
//        }
        
        
        if(Math.abs(u) == 5 || depth == 0){
            return u;
        }
        
        if(isMax == 1){
            int highestVal = Integer.MIN_VALUE;
            
            for (int i = 0; i < BOARD_CONTENT; i++){
                for (int j = 0; j < BOARD_CONTENT; j++){
                    Cell cell = cells[i][j];
                    if(cell.getPlayerValue() == VALUE_EMPTY && cell.getMinmaxValue() != -1){
                        cell.setPlayerValue(botValue);
                        
                        highestVal = Math.max(highestVal, alphabeta(depth - 1, i, j, a, b, 0));
                        
                        a = Math.max(a, highestVal);
                        
                        cell.setPlayerValue(VALUE_EMPTY);
                        
                        if (a >= b){
                            return highestVal;
                        }
                    }
                }
            }
            return highestVal;
        }
        else {
            int lowestVal = Integer.MAX_VALUE;
            
            for (int i = 0; i < BOARD_CONTENT; i++){
                for (int j = 0; j < BOARD_CONTENT; j++){
                    Cell cell = cells[i][j];
                    if (cell.getPlayerValue() == VALUE_EMPTY && cell.getMinmaxValue() != -1){
                        cell.setPlayerValue(playerValue);
                        
                        lowestVal = Math.min(lowestVal, alphabeta(depth - 1, i, j, a, b, 1));
                        
                        b = Math.min(b, lowestVal);
                        
                        cell.setPlayerValue(VALUE_EMPTY);
                        
                        if (b <= a) {
                            return lowestVal;
                        }
                    }
                }
            }
            return lowestVal;
        }
    }
	
	public int bfs(int row, int col, int isMax){
        int win = 5;
        
        int player1 = (isMax == 1) ? playerValue : botValue;
        int player12 = (isMax == 0) ? playerValue : botValue;

        int count1 = 0, count2 = 0, count3 = 0, count4 = 0;
        int countx = 0, kc = 0;
        int diem = (isMax == 1) ? -1 : 1;
        int flag = 1;

        for (int j = 0; j < BOARD_CONTENT; j++){
            if (flag == 0) {
                break;
            }
            for (int k = j; k < BOARD_CONTENT; k++){
                Cell cell = cells[row][k];
                
                if (cell.getPlayerValue() == player1){
                    countx++;
                }
                if (countx == win){
                    return diem * countx;
                }
                if (cell.getPlayerValue() == VALUE_EMPTY){
                    kc++;
                }
                count1 = Math.max(countx, count1);

                if (kc >= 2 || cell.getPlayerValue() == player12){
                    countx = kc = 0;
                    break;
                }
                if (k == BOARD_CONTENT - 1){
                    flag = 0;
                    break;
                }
            }
        }
        
        
        flag = 1;
        countx = kc = 0;
        for (int i = 0; i < BOARD_CONTENT; i++){
            if (flag == 0){
                break;
            }
            for (int k = i; k < BOARD_CONTENT; k++){
                Cell cell = cells[k][col];
                
                if (cell.getPlayerValue() == player1){
                    countx++;
                }
                if (countx == win){
                    return diem * countx;
                }
                if (cell.getPlayerValue() == VALUE_EMPTY){
                    kc++;
                }
                count1 = Math.max(countx, count1);

                if (kc >= 2 || cell.getPlayerValue() == player12){
                    countx = kc = 0;
                    break;
                }
                if (k == BOARD_CONTENT - 1){
                    flag = 0;
                    break;
                }
            }
        }
        
//        System.out.println("2: " + count2 + " player1: " + player1);

        // Cheo trai
        flag = 1;
        countx = kc = 0;
        int min = Math.min(row, col);
        int TopI = row - min;
        int TopJ = col - min;

        for (;TopI < BOARD_CONTENT && TopJ < BOARD_CONTENT; TopI++, TopJ++){
            if (flag == 0){
                break;
            }
            for (int k1 = TopI, k2 = TopJ; k1 < BOARD_CONTENT && k2 < BOARD_CONTENT; k1++, k2++){
                Cell cell = cells[k1][k2];
                
                if(cell.getPlayerValue() == player1){
                    countx++;
                }
                if(countx == win){
                    return diem * countx;
                }
                if(cell.getPlayerValue() == VALUE_EMPTY){
                    kc++;
                }
                count1 = Math.max(countx, count1);

                if(kc >= 2 || cell.getPlayerValue() == player12){
                    countx = kc = 0;
                    break;
                }
                if(k1 == BOARD_CONTENT - 1 || k2 == BOARD_CONTENT - 1){
                    flag = 0;
                    break;
                }
            }
        }
        
//        System.out.println("3: " + count3 + " player1: " + player1);
      
        for(int i2 = 0; i2 < BOARD_CONTENT; i2++){
            TopI = row - i2 * DIR_X[0];
            TopJ = i2 * DIR_Y[0] + col;
            if(TopI == 0 || TopJ == BOARD_CONTENT - 1){
                break;
            }
        }   
        
//        System.out.println(TopI + " " + TopJ);
        flag = 1;
        countx = kc = 0;

        for(; TopI < BOARD_CONTENT && TopJ >= 0; TopI++, TopJ--){
            if(flag == 0){
                break;
            }
            for(int k1 = TopI, k2 = TopJ; k1 < BOARD_CONTENT && k2 >= 0; k1++, k2--){
                Cell cell = cells[k1][k2];
                if(cell.getPlayerValue() == player1){
                    countx++;
                }
                if(countx == win){
                    return diem * countx;
                }
                if(cell.getPlayerValue() == VALUE_EMPTY){
                    kc++;
                }
                count4 = Math.max(countx, count4);
                if(kc >= 2 || cell.getPlayerValue() == player12){
                    countx = kc = 0;
                    break;
                }
                if(k1 == BOARD_CONTENT - 1 || k2 == 0){
                    flag = 0;
                    break;
                }
            }
        }

        return diem * Math.max(count1, Math.max(count2, Math.max(count3, count4)));
    }
	
	public void valueProximityUpdate(int xLocate, int yLocate) {
		for (int i = xLocate - 3; i <= xLocate + 3; i++) {
			for (int j = yLocate - 3; j <= yLocate + 3; j++) {
				if ((i >= 0 && i <= 15) && (j >= 0 && j <= 15)) {
					if (cells[i][j].getMinmaxValue() == -1) cells[i][j].setMinmaxValue(0);
				}
			}
		}
	}
	
	public void switchBotTurn() {
		int temp = botValue;
		botValue = playerValue;
		playerValue = botValue;
	}
	
	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}
	
	public void setBotActive(boolean value) {
		botActive = value;
	}
	
	public boolean isBotActive() {
		return botActive;
	}
	
	public void setBestMoveReady(boolean value) {
		bestMoveReady = value;
	}
	
	public boolean isBestMoveReady() {
		return bestMoveReady;
	}
	
	public int getXBestMove() {
		return xBestMove;
	}
	
	public int getYBestMove() {
		return yBestMove;
	}
}
