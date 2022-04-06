/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.customControl;

import hr.algebra.model.Message;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author HT-ICT
 */
public class MediaPlayControl extends GridPane {

    private MediaView mediaView;
    public MediaPlayer mp;
    public Message sandable;
    boolean toggle;

    public MediaPlayControl(Message sandable) {
        this.sandable = sandable;
        intiMedia();
        setUpListeners();
    }

    private void intiMedia() {
        try {
            Media media = new Media(hr.algebra.utils.MediaUtils.byteArrayToFile(sandable.getMessage()).toURI().toURL().toString());
            
            mediaView = new MediaView();
            mp = new MediaPlayer(media);
            
            mediaView.setMediaPlayer(mp);
            mediaView.setFitHeight(250);
            mediaView.setFitWidth(200);
            this.add(mediaView,0,0);
        } catch (MalformedURLException ex) {
            Logger.getLogger(MessageControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUpListeners() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toggle = !toggle;
                if (toggle) {
                    mp.play();
                } else {
                    mp.stop();
                }
            }
        });
    }
}
