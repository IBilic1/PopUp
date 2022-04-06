/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.controller.PopUpController;
import static hr.algebra.controller.PopUpController.user;
import hr.algebra.customControl.ProfileControl;
import hr.algebra.dal.RepositoryFactory;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ListView;

/**
 *
 * @author HT-ICT
 */
public class FrindsThread extends Thread {

    private final ListView<ProfileControl> friends;
    private Consumer<Optional<String>> callback;

    public FrindsThread(ListView<ProfileControl> friends, Consumer<Optional<String>> callback) {
        this.friends = friends;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (friends.getItems().size() != RepositoryFactory.REPOSITORY.getRepository().getFriends(user.getIdUser()).size()) {
                    
                    Platform.runLater(() -> {
                        friends.getItems().clear();
                        try {
                            callback.accept(Optional.of("done"));
                        } catch (Exception ex) {
                            Logger.getLogger(FrindsThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    Thread.sleep(3000);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
