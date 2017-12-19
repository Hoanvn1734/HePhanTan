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
    // Kiem tra xem file upload co dang duoc upload boi client khac hay khong
    public boolean checkListThread(String name) throws RemoteException;
    
    // Xoa phan tu file trong ListThread
    public void removeElement(String name) throws RemoteException;
    
    // Them mot phan tu file vao ListThread
    public void addFileName(String name) throws RemoteException;
    
    // Lay ten user vua upload file
    public String getUserName (String file) throws RemoteException;
    
    // Them mot user vao Map (key la ten file, value la ten user)
    public void addUserName (String file, String name) throws RemoteException;
    
    // Hien thi trang thai ket noi
    public void connect(FileClientInt fileCI) throws RemoteException;
    
    // Hien thi cac trang thai
    public void showState(FileClientInt fileCI) throws RemoteException;
    
    // Khoi tao server
    public void start() throws Exception;
    
    // Ngat ket noi server
    public void stop() throws Exception;
    
    public File getServerFile() throws RemoteException;
}
