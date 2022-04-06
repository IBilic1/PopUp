/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import static hr.algebra.controller.GameController.GROUP_2;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 *
 * @author HT-ICT
 */
public class RemovePresentThread extends Thread {

    private final List<Node> gifts;

    private static int port;
    public RemovePresentThread(List<Node> gifts,int port) {
        this.gifts = gifts;
        this.setDaemon(true);
        RemovePresentThread.port=port;
    }

    @Override
    public void run() {
        System.out.println("Thisssssssssssssssssssss->"+port);
        try (MulticastSocket clientSocket = new MulticastSocket(RemovePresentThread.port)) {
            InetAddress groupAdd = InetAddress.getByName(GROUP_2);
            clientSocket.joinGroup(groupAdd);
            
            while (true) {

                byte[] arr = new byte[1];
                DatagramPacket dp = new DatagramPacket(arr, arr.length);
                clientSocket.receive(dp);
                boolean b = arr[0]==1;
                
                Platform.runLater(() -> {
                    if (gifts.size() > 0) {
                        gifts.remove(gifts.size() - 1);
                    }
                });
            }
            //clientSocket.leaveGroup(groupAdd);
        } catch (Exception e) {
        }

    }

}
