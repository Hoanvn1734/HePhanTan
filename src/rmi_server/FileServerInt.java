 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import rmi_client.FileClientInt;

/**
 *
 * @author robot
 */
public interface FileServerInt extends Remote {
    // Hien thi trang thai ket noi
    public void connect(FileClientInt fileCI) throws RemoteException;
    
    // Hien thi cac trang thai
    public void showState(FileClientInt fileCI) throws RemoteException;
    
    // Khoi tao server
    public void start() throws Exception;
    
    // Ngat ket noi server
    public void stop() throws Exception;
    
    public File getServerFile() throws RemoteException;
//    public void setFile(File serverFie) throws RemoteException;
    public OutputStream getFileOutputStream(File f) throws Exception;
    public InputStream getFileInputStream(File f) throws Exception;
}
