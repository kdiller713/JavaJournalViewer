package javaJournal;

import javaJournal.gui.MainFrame;

import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String startDirectory = null;
        
        if(args.length > 0){
            startDirectory = args[0];
        }
        
        final MainFrame frame = new MainFrame(startDirectory);
        
        SwingUtilities.invokeLater(() -> {
            try{
                frame.initialize();
                frame.setVisible(true);
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
