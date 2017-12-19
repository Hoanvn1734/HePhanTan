/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_client;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import jdk.nashorn.internal.ir.ContinueNode;
import rmi_server.FileServer;
import rmi_server.FileServerInt;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author robot
 */
public class Client_interface extends javax.swing.JFrame {

    /**
     * Creates new form Client_interface
     */
    private FileClientInt client;
    private FileServerInt server;
    private int state = -1; // 1 la upload, 2 la download
    private File server_path;
    private String client_path = "";
//    private UpDownload updownload;
    private String UserName = "";
    private AtomicBoolean pauseUpload;
    private AtomicBoolean pauseDownload;
    private UpDownload upload;
    private UpDownload download;

    public Client_interface(final String path, FileClientInt client, FileServerInt server, String userName) throws RemoteException, Exception {
        this.UserName = userName;
        this.client = client;
        this.server = server;
        this.server_path = server.getServerFile();
        this.client_path = path;
        this.state = -1;
        this.pauseUpload = new AtomicBoolean(false);
        this.pauseDownload = new AtomicBoolean(false);
        initComponents();
        initTime();
        start();
    }

    public void updateTable(String path, int x) throws RemoteException {
        Vector col = new Vector();
        col.add("Loại");
        col.add("Tên");
        col.add("Kích thước");
        col.add("Ngày cập nhật");
        if(x == 1) {
            col.add("Người cập nhật");
        }
        Vector data = new Vector();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        File f = new File(path);
        File[] allSubFiles = f.listFiles();
        if (allSubFiles == null) {
        } else {
            for (File file : allSubFiles) {
                Vector element = new Vector();
                element.addElement("file");
                element.addElement(file.getName());
                element.addElement(file.length());
                element.addElement(sdf.format(file.lastModified()));
                if (x == 1) {
                    element.addElement(server.getUserName(file.getName()));
                }
                data.add(element);
            }
            if (x == 0) {
                TB_client.setModel(new DefaultTableModel(data, col));
            } else {
                TB_server.setModel(new DefaultTableModel(data, col));
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BT_clientUpLoad = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TB_client = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TB_server = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        BT_serverDownLoad = new javax.swing.JButton();
        BT_PauseUpload = new javax.swing.JButton();
        BT_PauseDownload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BT_clientUpLoad.setText("Upload");
        BT_clientUpLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_clientUpLoadActionPerformed(evt);
            }
        });

        TB_client.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Loại", "Tên", "Ngày cập nhật", "Kích thước "
            }
        ));
        jScrollPane1.setViewportView(TB_client);

        jLabel1.setText("Client");

        TB_server.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Loại", "Tên", "Ngày cập nhật", "Kích thước", "Người Upload"
            }
        ));
        jScrollPane2.setViewportView(TB_server);

        jLabel2.setText("Server");

        BT_serverDownLoad.setText("Download");
        BT_serverDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_serverDownLoadActionPerformed(evt);
            }
        });

        BT_PauseUpload.setText("Pause");
        BT_PauseUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_PauseUploadActionPerformed(evt);
            }
        });

        BT_PauseDownload.setText("Pause");
        BT_PauseDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_PauseDownloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(layout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(241, 241, 241))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(BT_clientUpLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(BT_PauseUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(236, 236, 236)
                .addComponent(BT_serverDownLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(BT_PauseDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_serverDownLoad)
                    .addComponent(BT_clientUpLoad)
                    .addComponent(BT_PauseUpload)
                    .addComponent(BT_PauseDownload))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(271, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initTime() {
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateTable(server_path.getParent() + "\\" + server_path.getName(), 1);
                    updateTable(client_path, 0);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        timer.start();
    }

    private void BT_clientUpLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_clientUpLoadActionPerformed
        int row = TB_client.getSelectedRow();
        if (row > -1) {
            if (BT_clientUpLoad.getText().equalsIgnoreCase("Upload")) {
                try {
                    // Cho bat dau upload
                    state = 1;
                    File clientfile = new File(client_path + "/" + TB_client.getValueAt(row, 1));
                    File serverfile = server.getServerFile();
                    System.out.println(clientfile);
                    System.out.println(serverfile);
                    upload = new UpDownload(client, server, 1, UserName, clientfile, serverfile);
                    if (server.checkListThread(clientfile.getName())) {
                        JOptionPane.showMessageDialog(null, "File đang được upload bởi user khác");
                    } else{
                        server.addFileName(clientfile.getName());
                        upload.start();
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Upload");
        }
    }//GEN-LAST:event_BT_clientUpLoadActionPerformed

    private void BT_serverDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_serverDownLoadActionPerformed
        int row = TB_server.getSelectedRow();
        if (row > -1) {
            if (BT_serverDownLoad.getText().equalsIgnoreCase("Download")) {
                try {
                    state = 2;
                    File serverfile = new File(server_path + "/" + TB_server.getValueAt(row, 1));
                    File clientfile = new File(client_path);
                    download = new UpDownload(client, server, 2, UserName, serverfile, clientfile);
                    download.start();
                } catch (Exception ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Download");
        }
    }//GEN-LAST:event_BT_serverDownLoadActionPerformed

    private void BT_PauseDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_PauseDownloadActionPerformed
        // TODO add your handling code here:
        int row = TB_server.getSelectedRow();
        if (row > -1) {
            if (!pauseDownload.get()) {
                download.pause();
                BT_PauseDownload.setText("Resume");
                pauseDownload.set(true);
            } else {
                download.resumeThread();
                BT_PauseDownload.setText("Pause");
                pauseDownload.set(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Pause");
        }
    }//GEN-LAST:event_BT_PauseDownloadActionPerformed

    private void BT_PauseUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_PauseUploadActionPerformed
        // TODO add your handling code here:
        int row = TB_client.getSelectedRow();
        if (row > -1) {
            if (!pauseUpload.get()) {
                upload.pause();
                BT_PauseUpload.setText("Resume");
                pauseUpload.set(true);
            } else {
                upload.resumeThread();
                BT_PauseUpload.setText("Pause");
                pauseUpload.set(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Pause");
        }
    }//GEN-LAST:event_BT_PauseUploadActionPerformed

//    public String getPath(String currentpath) {
//        int endIndex = 0;
//        for (int i = currentpath.length() - 1; i >= 0; i--) {
//            if ((int) currentpath.charAt(i) == 92) {
//                endIndex = i;
//                break;
//            }
//        }
//        String checkpath = currentpath.substring(0, endIndex);
//        return checkpath;
//    }

    private void start() throws RemoteException, Exception {
        server.connect(client);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_PauseDownload;
    private javax.swing.JButton BT_PauseUpload;
    private javax.swing.JButton BT_clientUpLoad;
    private javax.swing.JButton BT_serverDownLoad;
    private javax.swing.JTable TB_client;
    private javax.swing.JTable TB_server;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
