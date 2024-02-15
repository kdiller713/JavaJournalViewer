// This original source is copied from https://formats.kaitai.io/systemd_journal/java.html
// and modified to be easier to use.
package javaJournal.systemd.journal;

import java.util.Map;
import java.util.HashMap;

public enum State {
    OFFLINE(0),
    ONLINE(1),
    ARCHIVED(2);
    
    private static final Map<Long, State> byId = new HashMap<Long, State>(3);
    static {
        for (State e : State.values())
            byId.put(e.id(), e);
    }
    public static State byId(long id) { return byId.get(id); }

    private final long id;
    State(long id) { this.id = id; }
    public long id() { return id; }
}
