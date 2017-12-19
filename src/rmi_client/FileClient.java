/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_client;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author robot
 */
public class FileClient extends UnicastRemoteObject implements FileClientInt{

    public InetAddress clientAddress;
    public String state;


    public FileClient(InetAddress clientAddress) throws RemoteException {
        super();
        this.clientAddress = clientAddress;
    }



    
    @Override
    public String getState() {
        return state;
    }

    @Override
    public InetAddress getAddress() throws RemoteException {
        return clientAddress;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    @Override
    public void setState(String command) throws RemoteException {
        state = command + " từ " + getAddress();
    }

    
}
