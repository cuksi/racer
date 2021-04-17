package com.codegym.games.racer;

import com.codegym.engine.cell.Game;
import com.codegym.engine.cell.*;
import com.codegym.games.racer.road.RoadManager;

public class RacerGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH/2;
    public static final int ROADSIDE_WIDTH = 14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private static final int RACE_GOAL_CARS_COUNT = 4;
    private ProgressBar progressBar;
    private int score;

    @Override
    public void initialize() {

        showGrid(false);
        setScreenSize(WIDTH,HEIGHT);

        createGame();
    }

    private void createGame() {

        roadMarking = new RoadMarking();
        player = new PlayerCar();
        
        score = 3500;

        isGameStopped = false;
        
        roadManager = new RoadManager();
        
        finishLine = new FinishLine();
        
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {

        drawField();
        roadManager.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
        
    }

    private void drawField() {

        for (int x=0; x <WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                if (x == CENTER_X) {
                    setCellColor(CENTER_X,y,Color.WHITE);
                }else if (x >= ROADSIDE_WIDTH && x < (WIDTH-ROADSIDE_WIDTH)) {
                    setCellColor(x,y,Color.DIMGREY);
                }else {
                    setCellColor(x,y,Color.GREEN);
                }
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (!(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT))
            super.setCellColor(x, y, color);
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        player.move();

        roadManager.move(player.speed);
        
        finishLine.move(player.speed);
        
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onTurn(int step) {
        
        score -=5;
        setScore(score);

        if (roadManager.checkCrash(player)) {
            gameOver();
            drawScene();
        } else {
            
            if(roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
                finishLine.show();
            }
            
            if(finishLine.isCrossed(player)) {
                win();
            } else{

            moveAll();

            roadManager.generateNewRoadObjects(this);
            
            }

            drawScene();
        }

    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        } else if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        }else if (key == Key.UP) {
            player.speed = 2;
        }else if (key == Key.SPACE && isGameStopped) {
            createGame();
        }
    }

    @Override
    public void onKeyReleased(Key key) {

        if (key == Key.RIGHT && player.getDirection()==Direction.RIGHT) {
            player.setDirection(Direction.NONE);
        }else if (key == Key.LEFT && player.getDirection()==Direction.LEFT) {
            player.setDirection(Direction.NONE);
        }else if (key == Key.UP) {
            player.speed = 1;
        }
    }

    private void gameOver() {

        isGameStopped = true;
        showMessageDialog(Color.CORAL,"GAME OVER",Color.BROWN,30);
        stopTurnTimer();
        player.stop();
    }
    
    
    private void win() {
        
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "BRAVO MAJSTORE! :) \n SCORE = " + score,Color.YELLOW, 30);
        
        stopTurnTimer();
        
    }
    
    
    
    
}
