/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.implementation;

import hr.algebra.model.Message;
import hr.algebra.model.Person;
import hr.algebra.servie.MessagingService;
import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author HT-ICT
 */
public class MessageServiceImpl implements MessagingService {

    public static String FILENAME = "serializableMessage.ser";
    public static Path FILE_PATH = new File(FILENAME).toPath();
    private boolean fileInUse;

    @Override
    public synchronized void sendMessage(Message message) throws RemoteException {
        try {
            while (fileInUse) {
                System.out.println("lock");
                wait();
                //ako netko cita iz file-a ne moze druga dretva doc i citati
            }
            fileInUse = true;
            if (new File(FILENAME).length() == 0) {
                List<Message> messages = new ArrayList<>();
                messages.add(message);
                SerializationUtils.write(messages, FILENAME);

            } else {
                List<Message> messages = (ArrayList<Message>) SerializationUtils.read(FILENAME);
                messages.add(message);
                SerializationUtils.write(messages, FILENAME);
            }
            fileInUse = false;
            System.out.println("unlock");
            notifyAll();
        } catch (Exception e) {
        }

    }

    @Override
    public List<Message> getChatHistory(Person sender, Person receiver) throws RemoteException {
        List<Message> messages = new ArrayList<>();
        try {
            if (!new File(FILENAME).exists()) {
                Files.createFile(FILE_PATH);
            }

            if (FILE_PATH.toFile().length() != 0) {
                if (SerializationUtils.read(FILENAME) instanceof ArrayList) {
                    messages = SerializationUtils.read(FILENAME);
                    List<Message> collect = messages.stream().filter(m -> {
                        if (checkMessage(m, sender, receiver)) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                    System.out.println(collect.size());
                    return collect;
                }
            }
        } catch (Exception e) {
        }
        return messages;
    }

    @Override
    public Message getLastChatMessage(Person sender, Person receiver) throws RemoteException {
        List<Message> chatHistory = getChatHistory(sender, receiver);
        if (chatHistory.size() > 0) {
            return chatHistory.get(chatHistory.size() - 1);
        }
        return null;
    }

    private boolean checkMessage(Message message, Person sender, Person receiver) {
        if ((message.getReceiver().getIdUser() == sender.getIdUser()
                && message.getSender().getIdUser() == receiver.getIdUser())
                || (message.getReceiver().getIdUser() == receiver.getIdUser()
                && message.getSender().getIdUser() == sender.getIdUser())) {
            return true;
        }
        return false;
    }

}
