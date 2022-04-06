/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.PopUpApplication;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class FirstMenuController {

    @FXML
    private void showLogIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(PopUpApplication.class.getResource("view/LogIn.fxml"));
        Scene scene = new Scene(loader.load());
        PopUpApplication.getStage().setScene(scene);
    }

    @FXML
    private void showSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(PopUpApplication.class.getResource("view/SignIn.fxml"));
        Scene scene = new Scene(loader.load());
        PopUpApplication.getStage().setScene(scene);
    }

}
