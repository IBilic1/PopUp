/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author HT-ICT
 */
public class PopUpApplication extends Application {

    public PopUpApplication() {
    }

    private static Stage mainSatge;

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    public static Stage getStage() {
        return mainSatge;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainSatge = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(PopUpApplication.class.getResource("view/LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("PopUp App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
