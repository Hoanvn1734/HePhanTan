/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_upload_client;

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
import org.joda.time.DateTimeUtils;
import rmi_download_server.FileServerInt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class UpDownload {

    private File clientFile;
    private File serverFile;
    private FileServerInt server;
    private FileClientInt client;
    private String Username = "";
    private int state; // 1 la upload, 2 la download
    private boolean check = true; // false la pause, true la tiep tuc down
    private boolean wait = true;
    private Thread myThread;
    private boolean isBegin = true;
    private CopyFile2 copy;

    public UpDownload() {

    }

    public UpDownload(FileClientInt client, FileServerInt server,
            int state, String userName) {
        this.client = client;
        this.server = server;
        this.clientFile = clientFile;
        this.serverFile = serverFile;
        this.Username = userName;
        this.state = state;
    }

    public boolean upDownLoad(File source, File destination) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String sts = sdf.format(source.lastModified());
        String dts = sdf.format(destination.lastModified());
        System.out.println(sts);
        System.out.println(dts);
        destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
        copyFile(source, destination);
        return false;
    }

    private void splitFile(File srcFile, File desFile) {
        if (isBegin) {
            // Create thread
            copy = new CopyFile2(srcFile, desFile, state, server);
            myThread = new Thread(copy);
            myThread.start();
            isBegin = false;
        } else {
            // Resume
            synchronized (myThread) {
                myThread.notify();
            }
        }
//        copy.stop();
    }

    private void copyFile(File srcFile, File destFile) throws Exception {
        if (srcFile.isFile()) {
            if (state == 1) {
                splitFile(srcFile, destFile);
                System.out.println("Da upload thanh cong");
//                copy.stop();
            } else if (state == 2) {
                splitFile(srcFile, destFile);
                System.out.println("Da download thanh cong");
            }
        }
        if (srcFile.isDirectory()) {
            // Neu thu muc dich khong ton tai thi tao ra thu muc moi
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            File[] listFile = srcFile.listFiles();
            for (File f : listFile) {
//                    System.out.println(f.getAbsolutePath());
//                    System.out.println(destFile);
                copyFile(new File(f.getAbsolutePath()), new File(destFile + "\\" + f.getName()));
            }
        }
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//            if (os != null) {
//                os.close();
//            }
//        }

//        boolean successTimestampOp = destFile.setLastModified(srcFile.lastModified());
//        if (!successTimestampOp) {
//            JOptionPane.showMessageDialog(null, "Lỗi trong quá trình sửa đổi ngày cập nhật file :"
//                    + destFile);
//            client.setSyncState("Người dùng " + this.Username + " : " + "Lỗi trong quá trình sửa đổi ngày cập nhật file : " + destFile);
//            server.showSyncState(client);
//        }
    }

    public void stopSync() throws RemoteException {
        DateTimeUtils.setCurrentMillisSystem();
        if (state == 0) {
            client.setSyncState("Người dùng " + this.Username + " : đã dừng Upload");
        }
        server.showSyncState(client);
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getPaused() {
        return copy.getPaused();
    }

    public void setPaused(boolean paused) {
        copy.setPaused(paused);
    }
    
    public boolean getSuccess() {
        return copy.isSuccess();
    }

    public void setSuccess(boolean success) {
        copy.setSuccess(success);
    }

}

class CopyFile2 implements Runnable {

    private boolean paused;
    private boolean success;
    private Thread myThread;
    private int state;
    private File srcFile;
    private File desFile;
    private FileServerInt server;
    private volatile boolean running = true;
    public static int countTotalFile;

    public CopyFile2(File sourceFile, File desFile, int state, FileServerInt server) {
        this.srcFile = sourceFile;
        this.desFile = desFile;
        this.state = state;
        this.server = server;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void run() {
        FileInputStream fis;
        FileOutputStream fos;
        int sizeSrcFile = (int) srcFile.length();
        int sizeEachFile = 16 * 512 * 1024;
        int nChunks = 0, read = 0, readLength = sizeEachFile;
        byte[] byteChunkPart;
        while(running) {
            try {
                fis = new FileInputStream(srcFile);
                while (sizeSrcFile > 0) {
                    if (paused) {
                        synchronized (myThread) {
                            // Pause
                            try {
                                myThread.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }

                    if (sizeSrcFile <= sizeEachFile) {
                        readLength = sizeSrcFile;
                    }
                    byteChunkPart = new byte[readLength];
                    read = fis.read(byteChunkPart, 0, (int) readLength);
                    sizeSrcFile -= read;
                    nChunks++;
                    countTotalFile = nChunks;
                    System.out.println(countTotalFile);
                    fos = new FileOutputStream(new File(desFile.getAbsoluteFile() + ".part") + Integer.toString(nChunks));
                    fos.write(byteChunkPart);
                    fos.flush();
                    fos.close();
                    byteChunkPart = null;
                    fos = null;
                    if(sizeSrcFile == 0) {
                        success = true;
                        mergeFile(desFile);
                    }
                    success = false;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

//        System.out.println("Da tai thanh cong");
    }

    public void stop() {
        running = false;
    }
    
    public void mergeFile(File srcFile) {
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
        List<File> list = new ArrayList<File>();
//        System.out.println(CopyFile2.getCountTotalFile());
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
