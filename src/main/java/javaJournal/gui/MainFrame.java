package javaJournal.gui;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.io.IOException;

import java.util.regex.Pattern;

import javaJournal.journal.JournalManager;
import javaJournal.journal.EntryManager;

public class MainFrame extends JFrame {
    private static final String TITLE_PREFIX = "Journal Viewer";
    
    private MainFrame owner = this;

    private String journalDirectory;
    private JournalManager journalManager;
    
    private JFileChooser fileChooser;
    
    private JList<String> bootList;
    private ListSelectionListener bootSelectionListner;
    
    private JEditorPane journalDisplay;
    
    private JButton openButton;
    private JButton filterButton;
    
    private FilterDialog filterDialog;
    
    private LoadingDialog loadingDialog;
    
    public MainFrame(String jd){
        journalDirectory = jd;
    }
    
    public void initialize() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initializeComponents();
        initializeButtons();
        initializeBootList();
        initializeJournalDisplay();
        
        loadingDialog = new LoadingDialog(this);
        
        this.pack();
        this.setMinimumSize(this.getSize());
        
        setDirectory(journalDirectory);
    }
    
    private void initializeComponents() {
        fileChooser = new JFileChooser(journalDirectory == null ? "." : journalDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        filterDialog = new FilterDialog(this);
    }
    
    private void initializeButtons() {
        openButton = new JButton("Open");
        openButton.addActionListener(e -> {
            int choice = fileChooser.showOpenDialog(owner);
            
            if(choice == JFileChooser.APPROVE_OPTION){
                loadingDialog.showDialogAndRun(() -> {
                    setDirectory(fileChooser.getSelectedFile().getAbsolutePath());
                });
            }
        });
        
        filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            filterDialog.displayDialog();
            
            loadingDialog.showDialogAndRun(() -> {
                updateSelectedBoot();
            });
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(openButton);
        buttonPanel.add(filterButton);
        
        this.add(BorderLayout.NORTH, buttonPanel);
    }
    
    private void initializeBootList() {
        bootList = new JList<String>();
        bootList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        bootSelectionListner = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                loadingDialog.showDialogAndRun(() -> {
                    updateSelectedBoot();
                });
            }
        };
        
        JScrollPane scrollPane = new JScrollPane(bootList);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        scrollPane.setMinimumSize(new Dimension(250, 500));
        
        this.add(BorderLayout.WEST, scrollPane);
    }
    
    private void initializeJournalDisplay() {
        journalDisplay = new JEditorPane("text/html", "");
        journalDisplay.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(journalDisplay);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(750, 500));
        scrollPane.setMinimumSize(new Dimension(750, 500));
        
        this.add(BorderLayout.CENTER, scrollPane);
    }
    
    private void setDirectory(String directory){
        filterButton.setEnabled(false);
        
        // Removing the list selection listener here to prevent double triggering the loading dialog
        // It is added back after the list has been setup
        bootList.removeListSelectionListener(bootSelectionListner);
        bootList.setEnabled(false);
        bootList.setListData(new String[]{});
    
        if(directory == null){
            return;
        }
        
        try{
            journalManager = JournalManager.parseJournalFolder(directory);
            
            filterDialog.updateForJournal(journalManager);
            
            bootList.setListData(journalManager.listBoots().toArray(new String[] {}));
            bootList.setSelectedIndex(journalManager.numBoots() - 1);
            bootList.addListSelectionListener(bootSelectionListner);
            
            filterButton.setEnabled(true);
            bootList.setEnabled(true);
            
            journalDirectory = directory;
            this.setTitle(TITLE_PREFIX + ": " + journalDirectory);
            
            updateSelectedBoot();
        }catch(IOException e){
            JOptionPane.showMessageDialog(owner, "Failed to find any journal logs", "Error Parsing Journal Directory", JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void updateSelectedBoot(){
        String html = journalManager.boot(bootList.getSelectedIndex())
                                    .entries()
                                    .parallelStream()
                                    .filter(e -> !filterDialog.filterEntry(e))
                                    .map(e -> convertEntryToHtml(e))
                                    .reduce("", (ans, v) -> ans + v);
        
        journalDisplay.setText("<html><table>" + html + "</table></html>");
        journalDisplay.setCaretPosition(0);
    }
    
    private String convertEntryToHtml(EntryManager e) {
        String messageStyle = PriorityLevel.values()[e.priority()].messageStyle;
        
        String message = e.message().replaceAll("\n", "<br>");
        String searchString = filterDialog.getSearchText().toLowerCase();
        
        if(!searchString.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            String lowerMessage = message.toLowerCase();
            
            int lastIndex = 0;
            int nextIndex = -1;
            
            while((nextIndex = lowerMessage.indexOf(searchString, lastIndex)) > 0){
                messageBuilder.append(message.substring(lastIndex, nextIndex));
                messageBuilder.append("<span style=\"background-color:#FFFF00\">");
                messageBuilder.append(message.substring(nextIndex, nextIndex + searchString.length()));
                messageBuilder.append("</span>");
                
                lastIndex = nextIndex + searchString.length();
            }
            
            messageBuilder.append(message.substring(lastIndex));
            message = messageBuilder.toString();
        }
        
        return "<tr>" +
               "<td><nobr>" + e.date() + "</td>" +
               "<td><nobr>" + e.exe() + "[" + e.pid() + "," + e.priority() + "," + e.service() + "]</td>" +
               "<td style=\"" + messageStyle + "\"><nobr>" + message + "</td></tr>";
    }
}
