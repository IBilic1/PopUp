/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.model.CatchPresentThread;
import hr.algebra.model.Game;
import hr.algebra.model.GameState;
import hr.algebra.model.Person;
import hr.algebra.model.PresentThread;
import hr.algebra.model.RemovePresentThread;
import hr.algebra.model.Server;
import hr.algebra.model.Sprite;
import hr.algebra.model.User;
import hr.algebra.utils.DOMUtils;
import static hr.algebra.utils.JNDIUtils.search;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class GameController {

    public static final int CLIENT_PORT = Integer.valueOf(search("C:\\Users\\HT-ICT\\Desktop\\RMI\\PopUp", "configuration.properties", "server.port"));
    public static final String GROUP = search("C:\\Users\\HT-ICT\\Desktop\\RMI\\PopUp", "configuration.properties", "server.url");
    public static final int CLIENT_PORT_2 = Integer.valueOf(search("C:\\Users\\HT-ICT\\Desktop\\RMI\\PopUp", "configuration.properties", "server2.port"));
    public static final String GROUP_2 = search("C:\\Users\\HT-ICT\\Desktop\\RMI\\PopUp", "configuration.properties", "server2.url");

    public static List<GameState> games = new ArrayList<>();
    public static List<GameState> poitions = new ArrayList<>();

    public Game game;

    private static Person user;

    public static GameState gameState;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ImageView ivSantaClouse;

    @FXML
    private Label lblLives;

    @FXML
    private Group board;

    public int modifier = 150, villianCounter = modifier - 1;

    public static boolean east, west, up, down, enter;
    private static Server server;

    public void resolveScene(Scene scene, User user, Server server, boolean replay) {
        GameController.user = user;
        GameController.server = server;
        menuBar.prefWidthProperty().bind(board.getScene().getWindow().widthProperty());
        scene.setOnKeyPressed(e -> keyPressed(e, true));
        scene.setOnKeyReleased(e -> keyPressed(e, false));
        startGame(replay);
    }

    public void startGame(boolean replay) {
        initGame();
        if (replay) {
            startReplay();
        } else {
            startAnimationTimer();
        }
        setUpListeners();
    }

    private int i = 0;
    private int n = 0;

    private void startAnimationTimer() {
        new RemovePresentThread(game.getBallCharacters(), server.getMulticastPort()).start();
        PresentThread presentThread2 = new PresentThread(game, board, server.getPort());
        presentThread2.start();
        ivSantaClouse.setLayoutY(300);
        Sprite.LIVES = 0;

        AnimationTimer animationTimer;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    int deltaForX = 0, deltaForY = 0;
                    if (west) {
                        deltaForX -= 3;
                    }
                    if (east) {
                        deltaForX += 3;
                    }
                    if (up) {
                        deltaForY -= 3;
                    }
                    if (down) {
                        deltaForY += 3;
                    }

                    poitions.add(new GameState((int) ivSantaClouse.getLayoutX(), (int) ivSantaClouse.getLayoutY()));

                    if (villianCounter++ % modifier == 0) {
                        if (modifier > 20) {
                            modifier--;
                        }
                        ImageView opponent = new ImageView(game.getOpponentCharacter().getImageView().getImage());
                        presentThread2.setGift(opponent);
                    }

                    moveHeroTo(ivSantaClouse.getLayoutX() + deltaForX, ivSantaClouse.getLayoutY() + deltaForY);
                    moveGift();
                    moveBalls();
                } catch (IOException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        animationTimer.start();
    }
    List<GameState> loadedPositions =new ArrayList<>();
    ImageView opponent;
    GameState get;

    public void startReplay() {
        loadedPositions = DOMUtils.loadPOSITIONS();
        Sprite.LIVES = 0;
        modifier = 150;
        villianCounter = modifier - 1;
        AnimationTimer animationTimer;
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (n < loadedPositions.size()) {
                    int detltaForX = 0;
                    int l = n;
                    if (l > 0 && loadedPositions.get(--l).getPositionX() > loadedPositions.get(n).getPositionX()) {
                        detltaForX += 3;
                    } else {
                        detltaForX -= 3;
                    }
                    GameState GET = loadedPositions.get(n);
                    moveHeroTo(GET.getPositionX() + detltaForX, GET.getPositionY());
                    ++n;
                }

                if (villianCounter++ % modifier == 0) {
                    if (modifier > 20) {
                        modifier--;
                    }
                    opponent = new ImageView(game.getOpponentCharacter().getImageView().getImage());
                    List<GameState> r = DOMUtils.loadGameStatues();
                    new Timeline(new KeyFrame(Duration.millis(3000),
                            e -> {
                                if (i < r.size()) {
                                    get = r.get(i);
                                    opponent.setLayoutX(get.getPositionX());
                                    opponent.setLayoutY(get.getPositionY());
                                    game.addOpponent(opponent);
                                    board.getChildren().add(opponent);
                                    ++i;
                                }

                            })).play();

                }

                try {
                    moveGift();
                } catch (IOException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            private int countDeltaForX(int deltaForX) {
                if (west) {
                    deltaForX -= 3;
                }
                if (east) {
                    deltaForX += 3;
                }
                return deltaForX;
            }

            private int countDeltaForY(int deltaForY) {
                if (up) {
                    deltaForY -= 3;
                }
                if (down) {
                    deltaForY += 3;
                }
                return deltaForY;
            }

        };
        animationTimer.start();
    }

    private void moveBalls() throws IOException {
        for (int i = 0; i < game.getBallCharacters().size(); i++) {
            Node ball = game.getBallCharacters().get(i);
            if (ball.getLayoutY() > 0) {
                game.getBallCharacters().get(i).relocate(ball.getLayoutX(), ball.getLayoutY() - Sprite.DELTA_FOR_SPRITE);
                for (int j = 0; j < game.getOpponentCharacters().size(); j++) {
                    Node gift = game.getOpponentCharacters().get(j);
                    if (ball.getBoundsInParent().intersects(gift.getBoundsInParent())) {
                        handleCatchPresent();
                    }
                }
            } else {
                game.getBallCharacters().remove(ball);
            }

        }
    }

    private void replayift(Node node, double x, double y) {
        //if (x >= 0 && x <= widht - ivSantaClouse.getBoundsInLocal().getWidth() && y >= 0 && y <= height) {

        node.relocate(x, y);

    }

    private void moveHeroTo(double x, double y) {
        //if (x >= 0 && x <= widht - ivSantaClouse.getBoundsInLocal().getWidth() && y >= 0 && y <= height) {

        if (x > board.getScene().getWidth()) {
            ivSantaClouse.relocate(x - board.getScene().getWidth(), y);
        }
        if (x < 0) {
            ivSantaClouse.relocate(board.getScene().getWidth(), y);
        } else {
            ivSantaClouse.relocate(x, y);

        }

    }

    private void moveGift() throws IOException {
        for (int i = 0; i < game.getOpponentCharacters().size(); i++) {
            Node gift = game.getOpponentCharacters().get(i);
            gift.relocate(gift.getLayoutX(), gift.getLayoutY() + Sprite.DELTA_FOR_SPRITE);
            if (gift.getBoundsInParent().intersects(ivSantaClouse.getBoundsInParent())) {
                if (server == null) {
                    handleCatchPresentReplay();
                } else {
                    handleCatchPresent();
                }
            }
        }

    }

    private void handleCatchPresentReplay() throws IOException {
        board.getChildren().remove(game.getOpponentCharacters().get(game.getOpponentCharacters().size() - 1));
        game.getOpponentCharacters().remove(game.getOpponentCharacters().get(game.getOpponentCharacters().size() - 1));
        updateLives();
    }

    private void handleCatchPresent() throws IOException {
        board.getChildren().remove(game.getOpponentCharacters().get(game.getOpponentCharacters().size() - 1));
        game.getOpponentCharacters().remove(game.getOpponentCharacters().get(game.getOpponentCharacters().size() - 1));
        updateLives();
        new CatchPresentThread(Sprite.LIVES, user, server.getPortGetWinnerThread()).start();
    }

    private void updateLives() {
        AtomicInteger integer = new AtomicInteger(Sprite.LIVES);
        Sprite.LIVES = integer.incrementAndGet();
        lblLives.setText(String.valueOf(Sprite.LIVES));
    }

    private void setUpListeners() {
        game.getBallCharacters().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(gift -> {
                            game.getBallCharacters().remove(gift);
                            board.getChildren().remove(gift);
                        });
                    } else if (change.wasRemoved()) {
                        change.getAddedSubList().forEach(gift -> {
                            board.getChildren().add(gift);
                        });
                    }
                }
            }
        });
    }

    private void refreshServer() {
        new CatchPresentThread(0, null, server.getPortGetWinnerThread()).start();
    }
    Image laBronJamesLeft = new Image(new File("src/assets/lbLeft.png").toURI().toString(), 40, 40, true, true);
    Image laBronJamesRight = new Image(new File("src/assets/labron.png").toURI().toString(), 40, 40, true, true);
    Image laBronJamesCenter = new Image(new File("src/assets/lb.png").toURI().toString(), 40, 40, true, true);

    private void keyPressed(KeyEvent e, boolean isPressed) {
        switch (e.getCode()) {
            case LEFT:
                ivSantaClouse.setImage(laBronJamesLeft);
                west = isPressed;
                break;
            case UP:
                up = isPressed;
                break;
            case RIGHT:
                ivSantaClouse.setImage(laBronJamesRight);
                east = isPressed;
                break;
            case DOWN:
                down = isPressed;
                break;
            case ENTER:
                enter = isPressed;
                if (enter) {
                    ivSantaClouse.setImage(laBronJamesCenter);
                    ImageView a = new ImageView(game.getBall().getImageView().getImage());
                    a.setLayoutX(ivSantaClouse.getLayoutX());
                    a.setLayoutY(ivSantaClouse.getLayoutY());
                    game.addBallCharacters(a);
                    board.getChildren().add(a);
                }
                break;
        }

    }

    private void initGame() {
        game = new Game(new Sprite(ivSantaClouse));
        System.out.println(game.getBallCharacters().size());
        game.setOpponentCharacter(new Sprite(new ImageView(new Image(new File("src/assets/bulls.png").toURI().toString(), 50, 50, true, true))));
        game.setBall(new Sprite(new ImageView(new Image(new File("src/assets/la.png").toURI().toString(), 30, 30, true, true))));
        lblLives.setText(String.valueOf(Sprite.LIVES));
    }

}
