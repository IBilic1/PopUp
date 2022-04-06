/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.PopUpApplication;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.User;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.HashUtil;
import hr.algebra.utils.MessageUtils;
import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class LogInController implements Initializable {

    private static String FILENAME = "serializationUser.ser";

    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfEmail;
    @FXML
    private CheckBox cbRemeberMe;

    @FXML
    private void logInUser(ActionEvent event) throws Exception {
        if (!formValid()) {
            return;
        }

        if (!RepositoryFactory.REPOSITORY.getRepository().login(tfEmail.getText(), HashUtil.encrypt(pfPassword.getText()))) {
            MessageUtils.showAlert("ERROR", "Login error", "There isn't user with email " + tfEmail.getText() + "and password " + pfPassword.getText(), Alert.AlertType.ERROR);
            return;
        }

        loadToApp(tfEmail.getText(), HashUtil.encrypt(pfPassword.getText()));

    }

    private boolean checkIfUserExists(String email, String password) throws Exception {
        return RepositoryFactory.REPOSITORY.getRepository().login(email, password);
    }

    private boolean formValid() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (tfEmail.getText().trim().isEmpty()) {
            sb.append("You need to write your email").append("\n");
            valid = false;
        }
        if (pfPassword.getText().trim().isEmpty()) {
            sb.append("You need to write your password").append("\n");
            valid = false;
        }
        if (pfPassword.getText().length() < 6) {
            sb.append("Your password needs to be 6 charcters long.").append("\n");
            valid = false;
        }
        if (!sb.toString().isEmpty()) {
            MessageUtils.showAlert("Error", "Data missing", sb.toString(), Alert.AlertType.CONFIRMATION);
        }
        return valid;
    }

    @FXML
    private void remeberMe(ActionEvent event) throws Exception {
        if (!formValid()) {
            MessageUtils.showAlert("Error", "Data missing", "Form is not valid!", Alert.AlertType.ERROR);
            return;
        }
        
        if (cbRemeberMe.isSelected()) {
            User user;
            if ((user = RepositoryFactory.REPOSITORY.getRepository().get(tfEmail.getText(), HashUtil.encrypt(pfPassword.getText())).get()) != null) {
                SerializationUtils.write(user, FILENAME);
            }
        } else {
            FileUtils.clearFile(FILENAME);
            tfEmail.clear();
            pfPassword.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadUserFromFile();
        } catch (Exception ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadUserFromFile() throws Exception {
        File file = new File(FILENAME);
        if (file.exists() && file.length() != 0) {
            User user = SerializationUtils.read(FILENAME);
            if (RepositoryFactory.REPOSITORY.getRepository().login(user.getEmail(), user.getPassword())) {
                tfEmail.setText(user.getEmail());
                pfPassword.setText(HashUtil.decrypt(user.getPassword()));
                pfPassword.setFocusTraversable(false);
                cbRemeberMe.setSelected(true);
            }
        }
    }

    private void loadToApp(String email, String password) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/view/PopUp.fxml"));
        Parent parent = loader.load();
        PopUpController popUpController = loader.getController();
        popUpController.initData(RepositoryFactory.REPOSITORY.getRepository().get(email, password).get());
        PopUpApplication.getStage().setScene(new Scene(parent));

    }


}
