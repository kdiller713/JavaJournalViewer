package javaJournal.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

public class LoadingDialog extends JDialog {

    public LoadingDialog(Frame own){
        super(own);
        
        JLabel label = new JLabel("Loading...");
        label.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        
        this.add(label);
        this.setUndecorated(true); // Note: This needs to be before pack()
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(this.getParent());
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
    
    public void showDialogAndRun(Runnable run){
        new Thread(() -> {
            try{
                Thread.sleep(1000);
            }catch(Exception e){}
            
            SwingUtilities.invokeLater(() -> {
                try{
                    run.run();
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                setVisible(false);
            });
        }).start();
        
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
}
