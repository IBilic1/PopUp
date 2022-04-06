/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.customControl;

import hr.algebra.model.Message;
import hr.algebra.model.MessageType;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author HT-ICT
 */
public class MessageControl extends GridPane {

    private Label messageText;
    private ImageView image;
    public Message message;
    public MediaPlayControl mpc;

    public MessageControl(Message sandable, int isSender) {
        this.message = sandable;
        this.getColumnConstraints().addAll(new ColumnConstraints(150), new ColumnConstraints(150));
        initGrid(isSender);
        this.setStyle("-fx-border-width:2px;-fx-border-color:lightGray;-fx-padding: 10 10 10 10; -fx-border-radius: 10.0");
    }

    private void initGrid(int isSender) {
        if (isSender == 1) {
            switch (message.getType()) {
                case MESSAGE:
                    initLabel(HPos.RIGHT, 1);
                    break;
                case PICTURE:
                    initImage(HPos.RIGHT, 1);
                    break;
                case VIDEO:
                    intiMedia(HPos.RIGHT, 1);
                    break;
            }
        } else {
            switch (message.getType()) {
                case MESSAGE:
                    initLabel(HPos.LEFT, 0);
                    break;
                case PICTURE:
                    initImage(HPos.LEFT, 0);
                    break;
                case VIDEO:
                    intiMedia(HPos.LEFT, 0);
                    break;
            }
        }
    }

    private void initLabel(HPos position, int column) {
        try {
            messageText = new Label();
            messageText.setText((String) MessageType.byteArrayToObject(message.getMessage(), MessageType.MESSAGE).get());
            this.setHalignment(messageText, position);
            messageText.setWrapText(true);
            this.add(messageText, column, 0);
        } catch (IOException ex) {
            Logger.getLogger(MessageControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initImage(HPos position, int column) {
        try {
            image = new ImageView((Image) MessageType.byteArrayToObject(message.getMessage(), MessageType.PICTURE).get());
            image.setFitHeight(150);
            image.setFitWidth(200);
            
            setHalignment(image, position);
            add(image, column, 0);
        } catch (IOException ex) {
            Logger.getLogger(MessageControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void intiMedia(HPos position, int column) {
        mpc = new MediaPlayControl(message);
        
        setHalignment(mpc, position);
        add(mpc, column, 0);
    }
    
    public void stopMediaPlayer(){
        mpc.mp.stop();
    }

}
