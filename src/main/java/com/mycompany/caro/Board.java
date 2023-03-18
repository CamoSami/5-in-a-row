/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
    + CT tính chéo trái: top x = x0 - Min(x0, y0), top y = y0 - Min(x0, y0)
    + CT tính cháo phải: top x = x0 - Min(x0, y0) , top y = y0 + Min(x0, y0)
    !! Nếu y >= biên => dư = y - (biên - 1)
    => x = x + dư, y = biên - 1 !! i++, j--
*/  
package com.mycompany.caro;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;

/**
 *
 * @author admin
 */
public class Board extends JPanel{
    private static final int N = 6;
    private static final int M = 6;
    private static final int DEPTH = 5;
    
    public static final int ST_DRAW = 0;
    public static final int ST_WIN = 1;
    public static final int ST_NORMAL = 2;


    private int manOrMachine = -1;
    private int status = -1;
    private EndGameListener endGameListener;
    private Image imgX;
    private Image imgO;
    private Cell[][] matrix = new Cell[N][M];
    private String currentPlayer = Cell.EMPTY_VALUE;   
    private String currentPlayer1 = Cell.EMPTY_VALUE;
    private String machine = Cell.EMPTY_VALUE;
    private String Player = Cell.EMPTY_VALUE;


    
    public Board(String Player) throws IOException{
        this();
        this.currentPlayer = Player;
    }
    
    public Board() throws IOException{
        // Khoi tao matrix
        this.initMatrix();
        
        addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {                
                super.mousePressed(e); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                int x = e.getX();
                int y = e.getY();
                
//                System.out.println(manOrMachine + " " + status);

                
                if(currentPlayer.equals(Cell.EMPTY_VALUE)){
                    return;
                }
                if(manOrMachine == -1){
                    return;
                }
                
                // Phát ra âm thanh
                soundClick();
                
                
                if(manOrMachine == 0){
                    for(int i = 0; i < N; i++){
                        for(int j = 0; j < M; j++){
                            Cell cell = matrix[i][j];

                            int cXStart = cell.getX();
                            int cYStart = cell.getY();

                            int cXEnd = cXStart + cell.getW();
                            int cYEnd = cYStart + cell.getH();

                            if(x >= cXStart && x <= cXEnd && y >= cYStart && y <=  cYEnd){
                                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                                    cell.setValue(currentPlayer);
                                    repaint();
                                    int result = checkWin(currentPlayer, i, j);
                                    if(endGameListener != null){
                                        endGameListener.end(currentPlayer, result);
                                    }

                                    if(result == ST_NORMAL){
                                        currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
                                    }
                                    
                                }
                            }
                        }
                    }
                }else{
                    for(int i = 0; i < N; i++){
                        for(int j = 0; j < M; j++){
//                            System.out.println(status);
                            if(status == 0){
                                Cell cell = matrix[i][j];

                                int cXStart = cell.getX();
                                int cYStart = cell.getY();

                                int cXEnd = cXStart + cell.getW();
                                int cYEnd = cYStart + cell.getH();

                                if(x >= cXStart && x <= cXEnd && y >= cYStart && y <=  cYEnd){
                                    if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                                        cell.setValue(currentPlayer);
                                        repaint();
                                        int result = checkWin(currentPlayer, i, j);
//                                        System.out.println(result);
                                        if(endGameListener != null){
                                            endGameListener.end(currentPlayer, result);
                                        }
                                        if(result == ST_NORMAL){
                                            status = 1;
                                        }
                                    }
                                }
                            }else if(status == 1){
                                int[] best = getBestMove();
                                Cell cell = matrix[best[0]][best[1]];
//                                System.out.println(best[0] + " " + best[1]);
                                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
//                                    machine = (currentPlayer1.equals(Cell.X_VALUE)) ? Cell.O_VALUE : Cell.X_VALUE;

                                    cell.setValue(Player);
                                    repaint();
                                    System.out.println(Player);
                                    int result = checkWin(Player, best[0], best[1]);
//                                    System.out.println(result);
                                    if(endGameListener != null){
                                        endGameListener.end(Player, result);
                                    }
                                    if(result == ST_NORMAL){
                                        status = 0;
                                    }
                                }
                            }else{
                                return;
                            }
                        }
    //                    System.out.println(best[0] + " " + best[1]);
                    }
                    
//                    System.out.println(cell.getValue());
                }
            }
//Tinh toan xem x, y roi vao o nao trong board, sau do ve x, o tuy y
        });
        
        try{
            imgX = ImageIO.read(getClass().getResource("x.png"));
            imgO = ImageIO.read(getClass().getResource("o.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private synchronized void soundClick(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("Click1.wav"));
                    clip.open(audioInputStream);
                    clip.start();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    
    private void initMatrix(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                Cell cell = new Cell();
                matrix[i][j] = cell;
            }
        }
    }
    
    public void reset(){
        this.initMatrix();
        this.setCurrentPlayer(Cell.EMPTY_VALUE);
        this.setCurrentPlayer1(Cell.EMPTY_VALUE);
        this.setMachine(Cell.EMPTY_VALUE);
        this.setManOrMachine(-1);
        this.setStatus(-1);
        this.setPlayer(Cell.EMPTY_VALUE);
        repaint();
    }
    
    public void setMachine(String machine){
        this.machine = machine;
    }
    
    public void setManOrMachine(int manOrMachine){
        this.manOrMachine = manOrMachine;
    }
    
    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
    }
      
    public void setCurrentPlayer1(String currentPlayer1){
        this.currentPlayer1 = currentPlayer1;
    }
    
    public void setEndGameListener(EndGameListener endGameListener){
        this.endGameListener = endGameListener;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public void setPlayer(String Player){
        this.Player = Player;
    }
    
    public String getCurrentPlayer(){
        return currentPlayer;
    }
    
    //0: het nuoc 0 ai thang ca, 1: player thang, 2: player chua thang
    public int checkWin(String player, int i, int j){
        int count = 0;
        int min;
        int TopI, TopJ;
        // Chieu ngang
        for(int col = 0; col < M; col++){
            Cell cell = matrix[i][col];
            if(cell.getValue().equals(player)){
                count++;
                if(count >= 5){
                    System.out.println("Ngang");
                    return ST_WIN;
                }
            }else{
                count = 0;
            }
        }
        
        // Chieu doc
        count = 0;
        for(int row = 0; row < N; row++){
            Cell cell = matrix[row][j];
            if(cell.getValue().equals(player)){
                count++;
                if(count >= 5){
                    System.out.println("Doc");
                    return ST_WIN;
                }
            }else{
                count = 0;
            }
        }
        
        // Cheo trai
        count = 0;
        min = Math.min(i, j);
        TopI = i - min;
        TopJ = j - min;
        
        for(;TopI < N && TopJ < M; TopI++, TopJ++){
            Cell cell = matrix[TopI][TopJ];
            if(cell.getValue().equals(player)){
                count++;
                if(count >= 5){
                    System.out.println("Cheo trai");
                    return ST_WIN;
                }
            }else{
                count = 0;
            }
        }
        
        // Cheo phai
        count = 0;
        min = Math.min(i, j);
        TopI = i - min;
        TopJ = j + min;
        if(TopJ >= M){
            int du = TopJ - (M - 1);
            TopI += du;
            TopJ = M - 1;
        }
        
        for(; TopI < N && TopJ >= 0; TopI++, TopJ--){
            Cell cell = matrix[TopI][TopJ];
            if(cell.getValue().equals(player)){
                count++;
                if(count >= 5){
                    System.out.println("Cheo phai");
                    return ST_WIN;
                }
            }else{
                count = 0;
            }
        }
        
        return this.isFull() ? ST_DRAW : ST_NORMAL;
    }
    
    private boolean isFull(){
        int number  = N * N;
        
        int k = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                Cell cell = matrix[i][j];
                if(!cell.getValue().equals(Cell.EMPTY_VALUE)){
                    k++;
                }
            }
        }
        return k == number;
    }
    
    public int[] getBestMove(){
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;
        
        for(int row = 0; row < N; row++){
            for(int col = 0; col < M; col++){
                Cell cell = matrix[row][col];
                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                    cell.setValue(Player);

                    int moveValue = minimax(DEPTH,row, col);
                    cell.setValue(Cell.EMPTY_VALUE);
                    System.out.println(moveValue + " " + bestValue);
                    if(moveValue > bestValue){
                        bestMove[0] = row;
                        bestMove[1] = col;
//                        System.out.println(bestMove[0] + " " + bestMove[1]);
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }
    
    
    public int check(int i, int j){
        int xWin = 5;
        int oWin = -5;
        
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int countx = 0;
//        String Player = (currentPlayer == Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;

        
        for(int col = 0; col < M; col++){
            
            
            Cell cell = matrix[i][col];
            
            if(cell.getValue().equals(Player)){
                countx++;
                if(countx == xWin){
                    return countx;
                }
            }
            else if(cell.getValue().equals(currentPlayer)){
                countx--;
                if(countx == oWin){
                    return countx;
                }
            }
            else{
                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                    count1 = countx;
                }else{
                    countx = 0;
                }
            }
        }

        countx = 0;
                // Chieu doc
        for(int row = 0; row < N; row++){
            Cell cell = matrix[row][j];
            if(cell.getValue().equals(Player)){
                countx++;
                if(countx == xWin){
                    return countx;
                }
            }
            else if(cell.getValue().equals(currentPlayer)){
                countx--;
                if(countx == oWin){
                    return countx;
                }
            }
            else{
                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                    count2 = countx;
                }else{
                    countx = 0;
                }
            }
        }
        
        countx = 0;
        // Cheo trai
        int min = Math.min(i, j);
        int TopI = i - min;
        int TopJ = j - min;

        for(;TopI < N && TopJ < M; TopI++, TopJ++){
            Cell cell = matrix[TopI][TopJ];
            if(cell.getValue().equals(Player)){
                countx++;
                if(countx == xWin){
                    return countx;
                }
            }
            else if(cell.getValue().equals(currentPlayer)){
                countx--;
                if(countx == oWin){
                    return countx;
                }
            }
            else{
                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                    count3 = countx;
                }else{
                    countx = 0;
                }
            }
        }
        
        countx = 0;
        // Cheo phai
        min = Math.min(i, j);
        TopI = i - min;
        TopJ = j + min;
        if(TopJ >= M){
            int du = TopJ - (M - 1);
            TopI += du;
            TopJ = M - 1;
        }

        for(; TopI < N && TopJ >= 0; TopI++, TopJ--){
            Cell cell = matrix[TopI][TopJ];
            if(cell.getValue().equals(Player)){
                countx++;
                if(countx == xWin){
                    return countx;
                }
            }
            else if(cell.getValue().equals(currentPlayer)){
                countx--;
                if(countx == oWin){
                    return countx;
                }
            }
            else{
                if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                    count4 = countx;
                }else{
                    countx = 0;
                }
            }
        }
        int Max = Math.max(count1, Math.max(count2, Math.max(count3, count4)));
        int Min = Math.min(count1, Math.min(count2, Math.min(count3, count4)));
//        System.out.println(Max + "minimax" + Min);
        return (Max > Math.abs(Min)) ? Max : Min;
        
    }
    
    
    
    public int minimax(int depth, int i, int j){
        return alphabeta(depth, i, j, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }
    
    public int alphabeta(int depth, int i1, int j1, int a, int b, int isMax){
        int u = check(i1, j1);
        
        if(Math.abs(u) == 5 || depth == 0){
            return u;
        }
        
        if(isMax == 1){
            int highestVal = Integer.MIN_VALUE;
            for(int i = 0; i < M; i++){
                for(int j = 0; j < N; j++){
                    Cell cell = matrix[i][j];
                    if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                        cell.setValue(currentPlayer);
                        highestVal = Math.max(highestVal, alphabeta(depth - 1, i, j, a, b, 0));
                        a = Math.max(a, highestVal);
                        cell.setValue(Cell.EMPTY_VALUE);
                        if(a >= b){
                            return highestVal;
                        }
                    }
                }
            }
            return highestVal;
        }else{
            int lowestVal = Integer.MAX_VALUE;
            for(int i = 0; i < M; i++){
                for(int j = 0; j < N; j++){
                    Cell cell = matrix[i][j];
                    if(cell.getValue().equals(Cell.EMPTY_VALUE)){
                        cell.setValue(Player);
                        lowestVal = Math.min(lowestVal, alphabeta(depth - 1, i, j, a, b, 1));
                        b = Math.min(b, lowestVal);
                        cell.setValue(Cell.EMPTY_VALUE);
                        if(b <= a){
                            return lowestVal;
                        }
                    }
                }
            }
            return lowestVal;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        int w = getWidth() / M;
        int h = getHeight() / N;
        Graphics2D graphic2d = (Graphics2D) g;
        
        for(int i = 0; i < N; i++){
            int k = i;
            for(int j = 0; j < M; j++){
                int x = j * w;
                int y = i * h;
                
                // Cap nhat lai ma tran
                Cell cell = matrix[i][j];
                cell.setX(x);
                cell.setY(y);
                cell.setW(w);
                cell.setH(h);
                
                Color color = k % 2 == 0 ? Color.white : Color.gray;      
                graphic2d.setColor(color);
                graphic2d.fillRect(x, y, w, h);
                if(cell.getValue().equals(Cell.X_VALUE)){
                    Image img = imgX;
                    graphic2d.drawImage(img, x, y, w, h, this);
                }else if(cell.getValue().equals(Cell.O_VALUE)){
                    Image img = imgO;
                    graphic2d.drawImage(img, x, y, w, h, this);
                }
                k++;
            }
        }
    }  

}
