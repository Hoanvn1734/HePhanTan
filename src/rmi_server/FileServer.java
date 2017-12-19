/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_server;

import com.healthmarketscience.rmiio.SerializableInputStream;
import com.healthmarketscience.rmiio.SerializableOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import rmi_client.FileClientInt;

/**
 *
 * @author robot
 */
public class FileServer extends UnicastRemoteObject implements FileServerInt {

    public Registry rmiRegistry;
    public File serverFile;
    public boolean isStart = false;
    private ArrayList<cmdServer> information = new ArrayList<>();
    private int sizeQueue = 10;
    private ArrayList listThread;
    private Map listFile;

    public FileServer(File serverFile) throws RemoteException {
        super();
        this.serverFile = serverFile;
        listThread = new ArrayList();
        listFile = new HashMap<String, String>();
    }

    @Override
    public boolean checkListThread(String name) {
        for (int i = 0; i < listThread.size(); i++) {
            if (name.equals(listThread.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeElement(String name) {
        for (int i = 0; i < listThread.size(); i++) {
            if (name.equals(listThread.get(i))) {
                listThread.remove(i);
            }
        }
    }

    @Override
    public void addFileName(String name) {
        listThread.add(name);
    }

    public ArrayList getListThread() {
        return listThread;
    }

    public void setListThread(ArrayList listThread) {
        this.listThread = listThread;
    }

    @Override
    public void addUserName(String file, String name) throws RemoteException {
        listFile.put(file, name);
    }

    @Override
    public void updateUserName(String file, String name) throws RemoteException {
        listFile.put(file, name);
    }

    @Override
    public String getUserName(String file) throws RemoteException {
        return (String) listFile.get(file);
    }

    @Override
    public void start() throws Exception {
        isStart = true;
        // Dang ky cong cho server
        rmiRegistry = LocateRegistry.createRegistry(6969);
        // Dang ky ten cho server
        rmiRegistry.rebind("server", this);
        JOptionPane.showMessageDialog(null, "Server Start!");
    }

    @Override
    public void stop() throws Exception {
        isStart = false;
        rmiRegistry.unbind("server");
        unexportObject(this, true);
        unexportObject(rmiRegistry, true);
        JOptionPane.showMessageDialog(null, "Server Stop!");
    }

    @Override
    public File getServerFile() throws RemoteException {
        return serverFile;
    }

//    @Override
//    public void setFile(File serverFile) throws RemoteException{
//        this.serverFile = serverFile;
//    }
    @Override
    public OutputStream getFileOutputStream(File file) throws Exception {
        return new SerializableOutputStream(new FileOutputStream(file));
    }

    @Override
    public InputStream getFileInputStream(File file) throws Exception {
        return new SerializableInputStream(new FileInputStream(file));
    }

    @Override
    public void connect(FileClientInt fileCI) throws RemoteException {
        if (information.size() < sizeQueue) {
            information.add(new cmdServer(currentTime(), fileCI.getAddress() + " đang kết nối"));
        } else {
            information.remove(0);
            information.add(new cmdServer(currentTime(), fileCI.getAddress() + " đang kết nối"));
        }
    }

    @Override
    public void showState(FileClientInt fileCI) throws RemoteException {
        if (information.size() < sizeQueue) {
            information.add(new cmdServer(currentTime(), fileCI.getState()));
        } else {
            information.remove(0);
            information.add(new cmdServer(currentTime(), fileCI.getState()));
        }
    }

    public String currentTime() {
        String dateString = "";
        Date time = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM:dd:yyyy HH:mm:ss");
        dateString = formatTime.format(time.getTime());
        return dateString;
    }

    // Tra ve thong tin cua CMD
    public ArrayList<cmdServer> getInformation() {
        return information;
    }

}
