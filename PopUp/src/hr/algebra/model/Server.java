/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;

/**
 *
 * @author HT-ICT
 */
public class Server implements Comparable,Serializable {

    private final int port; //4001
    private final int portGetWinnerThread; //4002
    private final int multicastPort; //1989 

    private final String name;

    public Server(int port, String name, int portGetWinnerThread, int multicastPort) {
        this.port = port;
        this.name = name;
        this.portGetWinnerThread = portGetWinnerThread;
        this.multicastPort = multicastPort;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    
    
    public int getPortGetWinnerThread() {
        return portGetWinnerThread;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return "Server{" + "port=" + port + ", portGetWinnerThread=" + portGetWinnerThread + ", multicastPort=" + multicastPort + ", name=" + name + '}';
    }

    

}
