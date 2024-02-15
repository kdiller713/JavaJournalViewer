package javaJournal.journal;

import java.util.Date;

import javaJournal.systemd.journal.object.EntryObject;
import javaJournal.systemd.journal.object.DataObject;

import javaJournal.gui.PriorityLevel;

public class EntryManager {
    
    // Stored as microseconds since the epoch
    private long realTime;
    
    private String hostname = "";
    private String message;
    private String service = "kernel";
    private int priority = PriorityLevel.values().length - 1;
    private int pid;
    
    public EntryManager(EntryObject eo){
        realTime = eo.realTime();
        
        for(int i = 0; i < eo.numItems(); i++){
            String payload = ((DataObject)eo.entryItem(i).payload()).stringPayload();
            
            if(payload.startsWith("_HOSTNAME=")) {
                hostname = payload.split("=", 2)[1];
            }else if(payload.startsWith("_COMM=")) {
                service = payload.split("=", 2)[1];
            }else if(payload.startsWith("MESSAGE=")) {
                message = payload.split("=", 2)[1];
            }else if(payload.startsWith("_PID=")) {
                pid = Integer.parseInt(payload.split("=", 2)[1]);
            }else if(payload.startsWith("PRIORITY=")) {
                priority = Integer.parseInt(payload.split("=", 2)[1]);
            }
        }
    }
    
    public long realTime() { return realTime; }
    public Date date() { return new Date(realTime / 1000); }
    public String message() { return message; }
    public String hostname() { return hostname; }
    public String service() { return service; }
    public int pid() { return pid; }
    public int priority() { return priority; }
    
    public boolean isMessage() { return message != null; }
    
    public String toString() {
        return date() + " [" + priority + "] " + hostname + " " + service + "[" + pid + "]: " + message;
    }
}
