/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Message;
import java.util.List;

/**
 *
 * @author HT-ICT
 */
public interface MessageRepository {

    void createMessage(Message message) throws Exception;

    List<Message> get(int senderID, int receiverID) throws Exception;
}
