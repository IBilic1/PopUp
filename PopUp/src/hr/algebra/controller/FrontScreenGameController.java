/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.PopUpApplication;
import hr.algebra.model.Server;
import hr.algebra.model.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class FrontScreenGameController implements Initializable {

    @FXML
    private TextField tfServer;
    public static User user;
    public Scene scene;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void resolveScene(Scene scene, User user) {
        FrontScreenGameController.user = user;
        this.scene = scene;

    }

    @FXML
    private void createServer(ActionEvent event) {
        new Thread(() -> {
            try (Socket clientSocket = new Socket("localhost", 1988)) {

                sendRequest(clientSocket);

            } catch (Exception e) {
            }
        }).start();
    }


    @FXML
    private void play(ActionEvent event) throws InterruptedException {
        System.out.println(server);
        Thread.sleep(3000);
        try {
            FXMLLoader loader = new FXMLLoader(PopUpApplication.class
                    .getResource("/hr/algebra/view/Game.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400, Color.WHITESMOKE);
            PopUpApplication.getStage().setScene(scene);
            GameController controller = loader.getController();
            controller.resolveScene(scene, FrontScreenGameController.user, server, false);

        } catch (IOException ex) {
            Logger.getLogger(FrontScreenGameController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void replay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(PopUpApplication.class
                    .getResource("/hr/algebra/view/Game.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400, Color.WHITESMOKE);
            PopUpApplication.getStage().setScene(scene);
            GameController controller = loader.getController();
            controller.resolveScene(scene, FrontScreenGameController.user, server, true);

        } catch (IOException ex) {
            Logger.getLogger(FrontScreenGameController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    Server server;

    private void sendRequest(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        oos.writeObject(tfServer.getText());
        server = (Server) ois.readObject();
        System.out.println(server);

    }

}
