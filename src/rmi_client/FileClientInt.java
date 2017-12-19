/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_client;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author robot
 */
public interface FileClientInt extends Remote {
    // Lay dia chi cua may
    public InetAddress getAddress() throws RemoteException;
    
    // Thiet lap trang thai
    public void setState(String command) throws RemoteException;
    
    // Tra ve trang thai
    public String getState() throws RemoteException;

}
