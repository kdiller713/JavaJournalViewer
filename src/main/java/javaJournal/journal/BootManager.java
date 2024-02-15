package javaJournal.journal;

import java.util.List;

import javaJournal.systemd.journal.object.EntryObject;

import javaJournal.utils.StringUtils;

public class BootManager {
    private List<EntryManager> entryObjects;
    
    private String bootId;
    
    public BootManager(List<EntryObject> eos){
        // Sort them based on time
        entryObjects = eos.stream()
                          .map(EntryManager::new)
                          .filter(EntryManager::isMessage)
                          .sorted((a,b) -> Long.compare(a.realTime(), b.realTime())).toList();
        
        bootId = StringUtils.toHexString(eos.get(0).bootId());
    }
    
    public long startTime(){ return entryObjects.get(0).realTime(); }
    public long endTime(){ return entryObjects.get(entryObjects.size() - 1).realTime(); }
    public List<EntryManager> entries(){ return entryObjects; }
    
    public String toString(){
        return "<html>" + entryObjects.get(0).date() + ":<br>&emsp;" + bootId + "</html>";
    }
}
