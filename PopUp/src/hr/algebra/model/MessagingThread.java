/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.controller.PopUpController;
import static hr.algebra.controller.PopUpController.friend;
import static hr.algebra.controller.PopUpController.user;
import hr.algebra.customControl.MessageControl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author HT-ICT
 */
public class MessagingThread extends Thread {

    private final ObservableList<MessageControl> messageViews;

    public MessagingThread(ObservableList<MessageControl> messageViews) {
        this.messageViews = messageViews;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message lastChatMessage = PopUpController.server.getLastChatMessage(user, friend);
                if (lastChatMessage != null && messageViews.size() > 0) {

                    if (!messageViews.get(messageViews.size() - 1).message.equals(lastChatMessage)) {
                        
                            MessageControl ms = new MessageControl(
                                    lastChatMessage,
                                    user.getIdUser() == lastChatMessage.getSender().getIdUser() ? 1 : 2);
                            messageViews.add(ms);

                    }
                }
                Thread.sleep(3000);
            } catch (Exception ex) {
                Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
