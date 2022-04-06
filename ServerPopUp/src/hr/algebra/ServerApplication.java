/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.implementation.MessageServiceImpl;
import hr.algebra.servie.MessagingService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HT-ICT
 */
public class ServerApplication {

    public static void main(String[] args) {
        startMessagingService();
    }

    private static void startMessagingService() {
        MessagingService server = new MessageServiceImpl();
        try {
            MessagingService stub = (MessagingService) UnicastRemoteObject
                    .exportObject((MessagingService) server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);

            System.out.println("RMI Registry created!");

            registry.rebind("MessengerService", stub);

            System.out.println("Service binded...");

        } catch (RemoteException ex) {
            Logger.getLogger(ServerApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
