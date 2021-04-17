package com.codegym.games.racer.road;

import com.codegym.games.racer.PlayerCar;
import com.codegym.games.racer.RacerGame;
import com.codegym.engine.cell.Game;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RoadManager {

    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;
    
    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        
        if (type == RoadObjectType.SPIKE) {
            return new Spike(x,y);
        }else if(type == RoadObjectType.DRUNK_CAR) {
            
            return new MovingCar(x,y);
        
        }else {
            return new Car(type, x, y);
        }
    }
    
    private void addRoadObject(RoadObjectType type, Game game) {
        
        int x = game.getRandomNumber(FIRST_LANE_POSITION,FOURTH_LANE_POSITION);
        int y = -1*RoadObject.getHeight(type);
        
        
        RoadObject object = createRoadObject(type, x, y);
        
        boolean addObjectOrNot = isRoadSpaceFree(object);
        
        if(addObjectOrNot) {
        
        if (object != null) {
            items.add(object);
        }
        }
    }
    
    public void draw(Game game) {
        
        for(int i=0; i < items.size(); i++) {
            items.get(i).draw(game);
        }
    }
    
    public void move(int boost) {
        
        for(RoadObject object : items) {
            object.move(boost + object.speed, items);
        }

        deletePassedItems();
        
    }

    private boolean spikeExists() {
        for (RoadObject object : items) {
            if (object.type == RoadObjectType.SPIKE) {
                return true;
            }
        }
        return false;
    }

    private void generateSpike(Game game) {

        int randomNumber = game.getRandomNumber(100);
        boolean spikeCheck = spikeExists();

        if (randomNumber<10 && !spikeCheck) {
            addRoadObject(RoadObjectType.SPIKE, game);
        }

    }

    public void generateNewRoadObjects(Game game) {

        generateSpike(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {

        List<RoadObject> copy = new ArrayList<>(items);

        for (RoadObject object : copy) {

            if (object.y >= RacerGame.HEIGHT ) {
                
                if(object.type != RoadObjectType.SPIKE) {
                    passedCarsCount++;
                }
                items.remove(object);
            }
        }
    }

    public boolean checkCrash(PlayerCar playerCar) {

        for (RoadObject objects : items) {
            if (objects.isCollision(playerCar)) {
                return true;
            }
        }

        return false;
    }
    
    private void generateRegularCar(Game game) {
        
        int randomNumber = game.getRandomNumber(100);
        
        int carTypeNumber = game.getRandomNumber(4);
        
        if(randomNumber < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
        
    }
    
    private boolean isRoadSpaceFree(RoadObject object) {
        
        for(RoadObject test : items) {
        
        boolean freeSpace =  test.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE);
        
        if(freeSpace) {
            return false;
        }
        
        
        }
        
        return true;
        
    }
    
    
    private boolean movingCarExists() {
        
        for (RoadObject object : items) {
            if(object.type == RoadObjectType.DRUNK_CAR) {
                return true;
            }
        }
        
        return false;
        
    }
    
    private void generateMovingCar(Game game) {
        
        int randomNumber = game.getRandomNumber(100);
        
        if(randomNumber < 10 && !movingCarExists()) {
            
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
        
    }
    
    public int getPassedCarsCount() {
        
        return this.passedCarsCount;
    }
    
    
    
    

}
