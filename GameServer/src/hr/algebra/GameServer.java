package hr.algebra;

import hr.algebra.model.Server;
import hr.algebra.model.GameState;
import static hr.algebra.utils.JNDIUtils.search;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HT-ICT
 */
public class GameServer {

    public static final int CLIENT_PORT = Integer.valueOf(search("C:\\config", "configuration.properties", "server.port"));
    public static final String GROUP = search("C:\\config", "configuration.properties", "server.url");
    public static final int CLIENT_PORT_2 = Integer.valueOf(search("C:\\config", "configuration.properties", "server2.port"));
    public static final String GROUP_2 = search("C:\\config", "configuration.properties", "server2.url");
    public static final int PORT = 1989;
    public static final int MAIN_PORT = 1988;

    private static GameState state;
    public static GameState stateWithWinner = new GameState();
    public static HashSet<Server> servers = new HashSet<>();
    public static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {
        new Thread(() -> startMainServer()).start();
    }

    private static void startMainServer() {
        try (ServerSocket ss = new ServerSocket(MAIN_PORT)) {
            while (true) {
                Socket clientSocket = ss.accept();
                new Thread(() -> {
                    try {
                        proccesClient(clientSocket);
                    } catch (IOException ex) {
                        Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();

            }
        } catch (Exception e) {
        }
    }

    private static void proccesClient(Socket clientSocket) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

        String serverName = (String) ois.readObject();
        System.out.println(servers.size());
        Optional<Server> findFirst = servers.stream().filter(s
                -> {
            System.out.println(s.getName());
            System.out.println(serverName);
            return s.getName().equals(serverName);
        }).findFirst();
        if (findFirst.isPresent()) {
            
        } else {
            System.out.println(servers.size() + " :size");
            int port = getRandomPort();
            int port2 = getRandomPort();
            int port3 = getRandomPort();
            System.out.println(port+" "+port2+" "+port3);
            Server server = new Server(port, serverName, port2, port3);
            servers.add(server);
            oos.writeObject(server);
            ExecutorService executors = Executors.newFixedThreadPool(2);
            GetWinnerThread getWinnerThread = new GetWinnerThread(port2, port3);
            GameServerThread gameServerThread = new GameServerThread(state, port, getWinnerThread);
            gameServerThread.start();
            getWinnerThread.start();
            threads.addAll(Arrays.asList(getWinnerThread, gameServerThread));
        }
    }

    private static int getRandomPort() {
        String number = String.format("%04d", new Random().nextInt(10000));
        Integer portRandom = Integer.valueOf(number);
        servers.forEach(s -> {
            if (s.getMulticastPort() == portRandom
                    || s.getPort() == portRandom
                    || s.getPort() == portRandom) {
                getRandomPort();
            }
        });

        return portRandom;

    }

}
