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
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.MessageUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class SignInController implements Initializable {

    @FXML
    private ImageView iwImage;

    @FXML
    private TextField tfLastName, tfEmail, tfFirstName;

    @FXML
    private DatePicker dpBirthDate;

    private static String extension;

    private static final String DEFAULT_PICTURE = "assets/defaultProfile.jpg";

    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            iwImage.setImage((new Image(DEFAULT_PICTURE)));
        } catch (Exception ex) {
            Logger.getLogger(SignInController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void changePicture(ActionEvent event) throws IOException, Exception {
        File file = FileUtils.uploadFile(iwImage.getScene().getWindow(), "jpeg", "jpg", "png");
        if (file != null) {
            Image image = ImageUtils.createImage(file);
            iwImage.setImage(image);
            extension = file.getName().substring(file.getName()
                    .lastIndexOf('.') + 1, file.getName().length());
        }

    }

    @FXML
    private void registerUser(ActionEvent event) throws Exception {
        if (formValid()) {
            User user = new User(
                    tfEmail.getText().trim(),
                    HashUtil.encrypt(pfPassword.getText().trim()),
                    ImageUtils.BufferedImageToByteArray(
                            iwImage.getImage(),
                            extension == null ? "jpg" : extension),
                    tfFirstName.getText().trim(),
                    tfLastName.getText().trim(),
                    dpBirthDate.getValue());
            if (!RepositoryFactory.REPOSITORY.getRepository().register(user)) {
                MessageUtils.showAlert(
                        "Error",
                        "Error",
                        "User with email ${user.getEmail()}",
                        Alert.AlertType.ERROR);
            } else {
                clearForm();
                FXMLLoader loader = new FXMLLoader(PopUpApplication.class.getResource("view/LogIn.fxml"));
                Scene scene = new Scene(loader.load());
                PopUpApplication.getStage().setScene(scene);
            }
        }

    }

    private void clearForm() throws IOException {
        Stream.of(tfEmail, tfFirstName, tfEmail, pfPassword, tfLastName).forEach(text -> text.setText(""));
        iwImage.setImage(new Image(DEFAULT_PICTURE));
        dpBirthDate.setValue(null);
    }

    private boolean formValid() {
        boolean valid = true;
        StringBuilder sb = new StringBuilder();
        if (tfFirstName.getText().trim().isEmpty()) {
            sb.append("You did not write yout name").append("\n");
            valid = false;
        }
        if (tfLastName.getText().trim().isEmpty()) {
            sb.append("You did not write yout last name").append("\n");
            valid = false;
        }
        if (tfEmail.getText().trim().isEmpty()) {
            sb.append("You did not write your email.").append("\n");
            valid = false;
        }
        if (!tfEmail.getText().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            sb.append("Your email is in wrong format."
                    + "Write format is ivanaBilic@email.com").append("\n");
            valid = false;
        }
        if (dpBirthDate.getValue() == null) {
            sb.append("You did not choose birth date").append("\n");
            valid = false;
        }
        if (dpBirthDate.getValue() != null
                && dpBirthDate.getValue().isAfter(LocalDate.now().minusYears(10))) {
            sb.append("You are too young.").append("\n");
            valid = false;
        }
        if (dpBirthDate.getValue() != null
                && dpBirthDate.getValue().isBefore(LocalDate.now().minusYears(100))) {
            sb.append("You are too old.").append("\n");
            valid = false;
        }
        if (pfPassword.getText().trim().isEmpty()) {
            sb.append("You did not enter your password").append("\n");
            valid = false;
        }
        if (pfPassword.getText().length() < 6) {
            sb.append("Your password needs to be 6 charachters long.").append("\n");
            valid = false;
        }
        if (!sb.toString().isEmpty()) {
            hr.algebra.utils.MessageUtils.showAlert("Error", "Something went wrong...", sb.toString(), Alert.AlertType.ERROR);
        }
        return valid;
    }

}
