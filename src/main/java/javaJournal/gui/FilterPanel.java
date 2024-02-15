package javaJournal.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;

public class FilterPanel extends JPanel {
    private JPanel checkboxPanel;
    private List<JCheckBox> checkboxes;
    
    public FilterPanel(String title){
        super(new BorderLayout());
        
        checkboxes = new ArrayList<JCheckBox>();
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        scrollPane.setMinimumSize(new Dimension(200, 250));
        
        JButton selectAll = new JButton("Select All");
        selectAll.addActionListener(e -> {
            checkboxes.stream().forEach(c -> c.setSelected(true));
        });
        
        JButton deselectAll = new JButton("Deselect All");
        deselectAll.addActionListener(e -> {
            checkboxes.stream().forEach(c -> c.setSelected(false));
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectAll);
        buttonPanel.add(deselectAll);
        
        this.add(BorderLayout.NORTH, new JLabel(title));
        this.add(BorderLayout.CENTER, buttonPanel);
        this.add(BorderLayout.SOUTH, scrollPane);
    }
    
    public void clear() {
        checkboxes.clear();
        checkboxPanel.removeAll();
    }
    
    public void addCheckBox(String text) {
        JCheckBox checkbox = new JCheckBox(text, true);
        checkboxPanel.add(checkbox);
        checkboxes.add(checkbox);
    }
    
    public boolean isChecked(String value) {
        return checkboxes.stream()
                         .filter(c -> c.isSelected())
                         .map(c -> c.getText())
                         .toList()
                         .contains(value);
    }
}
