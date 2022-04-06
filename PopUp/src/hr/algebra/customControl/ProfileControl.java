/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.customControl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author HT-ICT
 */
public class ProfileControl extends GridPane {

    @FXML
    private ImageView iwProfile = new ImageView();
    @FXML
    private Label lblFirstName = new Label();

    public ProfileControl(Image image, String firstName, String lastName) {
        this.getColumnConstraints().addAll(new ColumnConstraints(150), new ColumnConstraints(150));
        iwProfile.setImage(image);
        iwProfile.setFitHeight(50);
        iwProfile.setFitWidth(50);
        lblFirstName.setText(firstName + " " + lastName);
        lblFirstName.setWrapText(true);
        this.add(iwProfile, 0, 0);
        this.add(lblFirstName, 1, 0);
    }
}
