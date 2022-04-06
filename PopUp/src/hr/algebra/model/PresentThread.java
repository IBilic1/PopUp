/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.controller.GameController;
import static hr.algebra.controller.GameController.GROUP;
import static hr.algebra.controller.GameController.gameState;
import hr.algebra.utils.DOMUtils;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

/**
 *
 * @author HT-ICT
 */
public class PresentThread extends Thread {

    private final Game game;
    private ImageView gift;
    private Group board;
    private boolean finished;

    private static int port;

    public PresentThread(Game game, Group board, int port) {
        this.game = game;
        this.board = board;
        this.port = port;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("moj posrt je:" + port);
            try (MulticastSocket ms = new MulticastSocket(port)) {
                InetAddress groupAdd = InetAddress.getByName(GROUP);
                ms.joinGroup(groupAdd);

                byte[] arr = new byte[1024];
                DatagramPacket dp = new DatagramPacket(arr, arr.length);
                ms.receive(dp);
                byte[] data = dp.getData();
                try (ObjectInputStream oos = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));) {
                    gameState = (GameState) oos.readObject();
                    
                    if (gameState.getUser() != null) {
                        System.out.println("done");
                        finishGame();
                    }

                    Platform.runLater(() -> {
                        this.gift.relocate(gameState.getPositionX(), gameState.getPositionY());
                        ImageView iv = new ImageView(gift.getImage());
                        iv.setLayoutX(gameState.getPositionX());
                        iv.setLayoutY(gameState.getPositionY());
                        GameController.games.add(new GameState(gameState.getPositionX(), gameState.getPositionY()));
                        game.addOpponent(iv);
                        board.getChildren().add(iv);

                    });

                }
                Thread.sleep(2000);
            } catch (Exception e) {

            }

        }

    }

    public void setGift(ImageView gift) {
        this.gift = gift;
    }

    public void setBoard(Group board) {
        this.board = board;
    }

    //ovo je kriticki odsijecak jer ak se jednom igrica zavrsi vise se nesmije opet zavrsit
    // prebrzo dode novi krug whilea i opet mi isto napravi
    private synchronized void finishGame() throws InterruptedException {
        while (finished == true) {
            wait(); //salje sve niti u spavanje
        }
        finished = true;
        Platform.runLater(() -> {
            DOMUtils.saveGameState(GameController.games);
            DOMUtils.saveGPOSITIONS(GameController.poitions);
            hr.algebra.utils.MessageUtils.show("Game finised", gameState.getUser().getFirstName() + " is winner", "Great game!!", Alert.AlertType.INFORMATION).show();
        });
    }

}
