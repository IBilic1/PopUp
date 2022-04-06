/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import static hr.algebra.GameServer.GROUP_2;
import hr.algebra.model.GameState;
import hr.algebra.model.Person;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HT-ICT
 */
public class GetWinnerThread extends Thread {

    private static int multicastPort;
    private static int clientPort;
    private static Person best;
    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public int getClientPort() {
        return clientPort;
    }

    public static Person getBest() {
        return best;
    }

    public GetWinnerThread(int multicastPort, int clientPort) {
        GetWinnerThread.multicastPort = multicastPort;
        GetWinnerThread.clientPort = clientPort;
        setDaemon(true);
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(GetWinnerThread.multicastPort)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());

                new Thread(() -> {
                    try {
                        process(clientSocket);
                    } catch (IOException ex) {
                        Logger.getLogger(GetWinnerThread.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GetWinnerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();

            }
            // while (true) {
            //   System.out.println();
            // Socket clientSocket = serverSocket.accept();
            //  process(clientSocket);
            // }
        } catch (Exception e) {
        }
    }

    private void process(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        //GameServer.stateWithWinner = (GameState) ois.readObject();
        GameState stateWithWinner = (GameState) ois.readObject();
        if (stateWithWinner != null) {
            System.out.println(stateWithWinner.getUser());
        }
        gameState = stateWithWinner;
// ovjde saznajem da je netko dohvatio poklon i da je mozda netko pobjednik
        // ako je pobjednik onda moram svih preko socketa za lokaciju obavijestiti

        try (DatagramSocket serverSocket = new DatagramSocket()) {
            byte[] arr = null;
            InetAddress groupAddres = InetAddress.getByName(GROUP_2);
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(bos);) {
                dos.writeBoolean(true); //ovdje vracam da je netko stv zadnji poklon skupio
                dos.flush();
                arr = bos.toByteArray();
            }

            //DatagramPacket dp = new DatagramPacket(arr, arr.length, groupAddres, CLIENT_PORT_2)
            DatagramPacket dp = new DatagramPacket(arr, arr.length, groupAddres, GetWinnerThread.clientPort); //1982
            serverSocket.send(dp);
        }
    }

}
