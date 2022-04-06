/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;

/**
 *
 * @author HT-ICT
 */
public class GameState implements Serializable {

    private int positionX;
    private int positionY;
    private boolean isHited;
    private Person user;

    public GameState() {
    }

    public GameState(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    

    public GameState(Person user) {
        this.user = user;
        this.positionX = 0;
        this.positionY = 0;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public boolean isIsHited() {
        return isHited;
    }

    public void setIsHited(boolean isHited) {
        this.isHited = isHited;
    }

}
