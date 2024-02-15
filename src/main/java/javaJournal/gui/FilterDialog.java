package javaJournal.gui;

import javaJournal.journal.EntryManager;

import java.awt.Component;
import java.awt.BorderLayout;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javaJournal.journal.JournalManager;
import javaJournal.journal.EntryManager;

public class FilterDialog extends JPanel {
    private Component parent;
    
    private FilterPanel priorityPanel;
    private FilterPanel servicesPanel;
    private FilterPanel pidPanel;
    
    private JTextField searchBox;
    
    public FilterDialog(Component p){
        super(new BorderLayout());
        parent = p;
        
        priorityPanel = new FilterPanel("Message Level");
        this.add(BorderLayout.WEST, priorityPanel);
        
        servicesPanel = new FilterPanel("Service");
        this.add(BorderLayout.CENTER, servicesPanel);
        
        pidPanel = new FilterPanel("PID");
        this.add(BorderLayout.EAST, pidPanel);
        
        initializeSearch();
    }
    
    private void initializeSearch() {
        searchBox = new JTextField();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(BorderLayout.WEST, new JLabel("Search:"));
        panel.add(BorderLayout.CENTER, searchBox);
        
        this.add(BorderLayout.SOUTH, panel);
    }
    
    public void updateForJournal(JournalManager man){
        priorityPanel.clear();
        servicesPanel.clear();
        pidPanel.clear();
        
        for(PriorityLevel pl : PriorityLevel.values()){
            priorityPanel.addCheckBox(pl.toString());
        }
        
        List<EntryManager> entries = new ArrayList<EntryManager>();
        
        for(int i = 0; i < man.numBoots(); i++){
            entries.addAll(man.boot(i).entries());
        }
        
        entries.stream()
               .map(e -> e.service())
               .distinct()
               .sorted()
               .forEach(s -> servicesPanel.addCheckBox(s));
               
        entries.stream()
               .map(e -> e.pid())
               .distinct()
               .sorted()
               .forEach(p -> pidPanel.addCheckBox("" + p));
    }
    
    public void displayDialog() {
        JOptionPane.showOptionDialog(parent, this, "Filter Journal", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Filter"}, "Filter");
    }
    
    public boolean filterEntry(EntryManager e){
        return !priorityPanel.isChecked(PriorityLevel.values()[e.priority()].toString()) || !servicesPanel.isChecked(e.service()) || !pidPanel.isChecked("" + e.pid());
    }
    
    public String getSearchText() {
        return searchBox.getText().trim();
    }
}
