/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @author HT-ICT
 */
public class Game {

    private Sprite mainCharacter;
    private Sprite winnerCharacter;
    private Sprite opponentCharacter;
    private Sprite ball;
    private boolean gameIsFished;
    private ObservableList<Node> opponentCharacters = FXCollections.observableArrayList();
    private ObservableList<Node> ballCharacters = FXCollections.observableArrayList();

    public Game(Sprite mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    public ObservableList<Node> getOpponentCharacters() {
        return opponentCharacters;
    }

    public void addOpponent(Node opponentCharacters) {
        this.opponentCharacters.add(opponentCharacters);
    }

    public ObservableList<Node> getBallCharacters() {
        return ballCharacters;
    }

    public void addBallCharacters(Node ballCharacters) {
        this.ballCharacters.add(ballCharacters);
    }

    public boolean isGameIsFished() {
        return gameIsFished;
    }

    public void setGameIsFished(boolean gameIsFished) {
        this.gameIsFished = gameIsFished;
    }

    @Override
    public String toString() {
        return winnerCharacter + " is winner of the game.";
    }

    public Sprite getMainCharacter() {
        return mainCharacter;
    }

    public Sprite getWinnerCharacter() {
        return winnerCharacter;
    }

    public Sprite getBall() {
        return ball;
    }

    public void setMainCharacter(Sprite mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    public void setBall(Sprite ball) {
        this.ball = ball;
    }

    public Sprite getOpponentCharacter() {
        return opponentCharacter;
    }

    public void setOpponentCharacter(Sprite opponentCharacter) {
        this.opponentCharacter = opponentCharacter;
    }


}
