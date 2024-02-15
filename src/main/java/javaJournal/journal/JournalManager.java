package javaJournal.journal;

import java.io.File;
import java.io.IOException;

import javaJournal.systemd.journal.Journal;
import javaJournal.systemd.journal.JournalObject;
import javaJournal.systemd.journal.ObjectType;
import javaJournal.systemd.journal.object.EntryObject;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JournalManager {
    public static JournalManager parseJournalFolder(String journalFolder) throws IOException {
        File[] journalFiles = new File(journalFolder).listFiles((dir, name) -> name.endsWith(".journal"));
        
        if(journalFiles == null || journalFiles.length == 0){
            throw new IOException("Failed to find any journal files");
        }
        
        ArrayList<JournalObject> journalObjects = new ArrayList<JournalObject>();
        
        for(File journalFile : journalFiles) {
            journalObjects.addAll(Journal.fromFile(journalFile.getAbsolutePath()).objects());
        }
        
        Map<String, List<EntryObject>> journalObjectsGrouped = journalObjects.stream()
            .filter(jo -> jo.objectType() == ObjectType.ENTRY)
            .map(jo -> (EntryObject)jo.payload())
            .collect(Collectors.groupingBy(jo -> Arrays.toString(jo.bootId())));
            
        return new JournalManager(journalObjectsGrouped.values().stream().map(BootManager::new).toList());
    }
    
    private List<BootManager> boots;
    
    private JournalManager(List<BootManager> bs){
        this.boots = bs.stream().sorted((a,b) -> Long.compare(a.startTime(), b.startTime())).toList();
    }
    
    public int numBoots(){ return boots.size(); }
    public BootManager boot(int i){ return boots.get(i); }
    
    public List<String> listBoots() {
        return boots.stream()
                    .map(BootManager::toString)
                    .toList();
    }
}
