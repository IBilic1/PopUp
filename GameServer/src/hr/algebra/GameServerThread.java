/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import static hr.algebra.GameServer.GROUP;
import hr.algebra.model.GameState;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author HT-ICT
 */
public class GameServerThread extends Thread {

    private GameState state;
    private GetWinnerThread thread;
    private final int port;

    public GameServerThread(GameState state, int port, GetWinnerThread thread) {
        this.state = state;
        this.port = port;
        this.thread = thread;
        setDaemon(true);
    }

    @Override
    public void run() {
        state = new GameState();
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            while (true) {
                state.setPositionX((int) (Math.random() * 600));
                state.setPositionY(5);
                
                if (thread.getGameState() != null) {
                    state.setUser(thread.getGameState().getUser());
                } else {
                    state.setUser(null);
                }

                byte[] arr = null;
                InetAddress groupAddres = InetAddress.getByName(GROUP);

                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);) {
                    oos.writeObject(state);
                    oos.flush();
                    arr = bos.toByteArray();
                }
                if (thread.getGameState()!=null) {
                 System.out.println(thread.getGameState().getUser()+"WINNER");   
                }
                System.out.println(port+"WINNER");
                DatagramPacket dp = new DatagramPacket(arr, arr.length, groupAddres, port);
                serverSocket.send(dp);
                Thread.sleep(3000);
            }
        } catch (Exception e) {
        }
    }

    public int getPort() {
        return port;
    }

}
