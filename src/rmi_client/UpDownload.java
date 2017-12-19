/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi_server.FileServerInt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class UpDownload extends Thread {

    private File clientFile;
    private File serverFile;
    private FileServerInt server;
    private FileClientInt client;
    private String Username = "";
    private int state; // 1 la upload, 2 la download
    private boolean check = false; // false la pause, true la tiep tuc down
    private boolean wait = true;
    private static int countTotalFile;
    private File source;
    private File destination;
    long tong = 0;
    long uploaded = 0;

    public UpDownload(FileClientInt client, FileServerInt server,
            int state, String userName, File source, File destination) {
        this.client = client;
        this.server = server;
        this.clientFile = clientFile;
        this.serverFile = serverFile;
        this.Username = userName;
        this.state = state;
        this.source = source;
        this.destination = destination;
        tong = source.length();
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String sts = sdf.format(source.lastModified());
        String dts = sdf.format(destination.lastModified());
        String time = sdf.format(destination.lastModified() - source.lastModified());
        System.out.println(sts);
        System.out.println(dts);
        System.out.println(time);
        destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
        if (destination.exists()) {
            destination.delete();
            try {
                client.setState("Người dùng " + this.Username + " : " + "file " + source.getName() + " đã được xóa");
                server.showState(client);
            } catch (RemoteException ex) {
                Logger.getLogger(UpDownload.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        synchronized (this) {
            splitFile(source, destination);
            mergeFile(destination);
            try {
                // Khi ghep file xong thi xoa phan tu trong ListThread
                server.removeElement(source.getName());
                
                // Them ten client vua upload vao Map 
                server.addUserName(source.getName(), this.Username);
            } catch (RemoteException ex) {
                Logger.getLogger(UpDownload.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void splitFile(File srcFile, File destFile) {
        try {
            if (state == 1) {
                client.setState("Người dùng " + this.Username + " : " + "file " + srcFile.getName() + " đang upload");
                server.showState(client);
            } else if (state == 2) {
                client.setState("Người dùng " + this.Username + " : " + "file " + srcFile.getName() + " đang download");
                server.showState(client);
            }
            FileInputStream fis;
            FileOutputStream fos;
            int sizeSrcFile = (int) srcFile.length();
            int sizeEachFile = 1 * 1024 * 128;
            int nChunks = 0, read = 0, readLength = sizeEachFile;
            byte[] byteChunkPart;
            Map map = new HashMap();
            fis = new FileInputStream(srcFile);
            while (sizeSrcFile > 0) {
//                    Thread.sleep(300);
                if (sizeSrcFile <= sizeEachFile) {
                    readLength = sizeSrcFile;
                }
                byteChunkPart = new byte[readLength];
                read = fis.read(byteChunkPart, 0, (int) readLength);
                sizeSrcFile -= read;
                uploaded += read;
                System.out.println("Read: " + read);
                System.out.println("Uploaded: " + uploaded);
                System.out.println("sizeSrcFile: " + sizeSrcFile);
                System.out.println("Uploaded " + uploaded * 100 / tong + "%");
                nChunks++;
                countTotalFile = nChunks;
                System.out.println(countTotalFile);
                fos = new FileOutputStream(new File(destFile.getAbsoluteFile() + ".part") + Integer.toString(nChunks));
                fos.write(byteChunkPart);
                fos.flush();
                fos.close();
                byteChunkPart = null;
                fos = null;
                synchronized (this) {
                    while (isCheck()) {
                        client.setState("Người dùng " + this.Username + " : " + "file " + srcFile.getName() + " đang được pause");
                        server.showState(client);
                        wait();
                    }
                }
            }
            fis.close();
        } catch (Exception ex) {
            Logger.getLogger(UpDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mergeFile(File srcFile) {
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
        List<File> list = new ArrayList<File>();
        System.out.println(countTotalFile);
        for (int i = 1; i <= countTotalFile; i++) {
            list.add(new File(srcFile.getAbsoluteFile() + ".part" + i));
        }
        try {
            fos = new FileOutputStream(srcFile, true);
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0, (int) file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
                file.delete();
            }
            fos.close();
            fos = null;
            if (state == 1) {
                client.setState("Người dùng " + this.Username + " : " + "file " + srcFile.getName() + " đã được upload thành công");
                server.showState(client);
            } else if (state == 2) {
                client.setState("Người dùng " + this.Username + " : " + "file " + srcFile.getName() + " đã được download thành công");
                server.showState(client);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean isCheck() {
        return check;
    }

    void pause() {
        check = true;
    }

    synchronized void resumeThread() {
        check = false;
        notify();
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getstate() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
