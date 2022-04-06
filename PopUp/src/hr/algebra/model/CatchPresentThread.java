/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author HT-ICT
 */
public class CatchPresentThread extends Thread {

    private static int presents;
    private static Person person;
    private static int port;

    public CatchPresentThread(int presents, Person person, int port) {
        CatchPresentThread.presents = presents;
        CatchPresentThread.person = person == null ? null : new Person(person.getFirstName(), person.getLastName(), person.getBirthDate());
        CatchPresentThread.port = port;
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket("localhost", CatchPresentThread.port)) {
            System.err.println("Client is connecting to: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            sendSerializableRequest(clientSocket);
        } catch (IOException ex) {
        }
    }

    private static void sendSerializableRequest(Socket clientSocket) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

        if (presents == 3) {
            oos.writeObject(new GameState(person));
        } else {
            oos.writeObject(null);
        }

    }

}
