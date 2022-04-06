/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.servie;

import hr.algebra.model.Message;
import hr.algebra.model.Person;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author HT-ICT
 */
public interface MessagingService extends Remote {

    void sendMessage(Message mesage) throws RemoteException;

    List<Message> getChatHistory(Person sender,Person receiver) throws RemoteException;

    Message getLastChatMessage(Person sender,Person receiver) throws RemoteException;
}
