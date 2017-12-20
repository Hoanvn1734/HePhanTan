/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_client;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.SwingUtilities;
import rmi_server.*;

/**
 *
 * @author Administrator
 */
public final class task_download extends javax.swing.JFrame {

    /**
     * Creates new form task_download
     */
    private Task task;
    private UpDownload upload;
    private AtomicBoolean pauseUpload;

    task_download(UpDownload upload) {
        this.upload = upload;
        this.pauseUpload = new AtomicBoolean(false);
        startDownload();
    }

    public void startDownload() {
        initComponents();
        task = new Task();
        task.start();
        upload.start();
        lblSize.setText(upload.tong /1024 / 1204 + "MB");
        lblNameFile.setText("" + upload.fileName);
        if(upload.getstate() == 1) {
            lblStatus.setText("UPLOAD");
        }else{
            lblStatus.setText("DOWNLOAD");
        }
    }

    public task_download() {

    }

    /*
    TODO: Thực hiện lấy % uploaded
    */
    private class Task extends Thread {

        public Task() {
        }

        boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(upload.uploaded == upload.tong) {
                            isRunning = false;
                            Client_interface.numberThread--;
                            btnStop.setText("Đã xong");
                        }
                        System.out.println("UPLOADED: " + upload.uploaded);
                        System.out.println("Total: " + upload.tong);
                        lblPhanTram.setText("Uploaded: " + (int) (upload.uploaded * 100 / upload.tong) + "%");
                        progressbarDownload.setValue((int) (upload.uploaded * 100 / upload.tong));
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
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

        progressbarDownload = new javax.swing.JProgressBar();
        lblStatus = new javax.swing.JLabel();
        lblPhanTram = new javax.swing.JLabel();
        btnStop = new javax.swing.JButton();
        lblNameFile = new javax.swing.JLabel();
        lblSize = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblStatus.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 51, 153));
        lblStatus.setText("Downloading File");

        lblPhanTram.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPhanTram.setForeground(new java.awt.Color(0, 0, 102));
        lblPhanTram.setText("50%");

        btnStop.setText("Pause");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        lblNameFile.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblNameFile.setText("Hinh anh");

        lblSize.setText("10 MB");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addGap(46, 46, 46)
                        .addComponent(lblNameFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                        .addComponent(lblSize))
                    .addComponent(progressbarDownload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblPhanTram)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(lblNameFile)
                    .addComponent(lblSize))
                .addGap(38, 38, 38)
                .addComponent(progressbarDownload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStop)
                    .addComponent(lblPhanTram, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // TODO add your handling code here:
        if (!pauseUpload.get()) {
            upload.pause();
            btnStop.setText("Resume");
            pauseUpload.set(true);
        } else {
            upload.resumeThread();
            btnStop.setText("Pause");
            pauseUpload.set(false);
        }
    }//GEN-LAST:event_btnStopActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(task_download.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(task_download.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(task_download.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(task_download.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new task_download().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStop;
    private javax.swing.JLabel lblNameFile;
    private javax.swing.JLabel lblPhanTram;
    private javax.swing.JLabel lblSize;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JProgressBar progressbarDownload;
    // End of variables declaration//GEN-END:variables
}
