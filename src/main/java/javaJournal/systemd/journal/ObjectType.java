// This original source is copied from https://formats.kaitai.io/systemd_journal/java.html
// and modified to be easier to use.
package javaJournal.systemd.journal;

import java.util.Map;
import java.util.HashMap;

public enum ObjectType {
    UNUSED(0),
    DATA(1),
    FIELD(2),
    ENTRY(3),
    DATA_HASH_TABLE(4),
    FIELD_HASH_TABLE(5),
    ENTRY_ARRAY(6),
    TAG(7);
    
    private static final Map<Long, ObjectType> byId = new HashMap<Long, ObjectType>(8);
    static {
        for (ObjectType e : ObjectType.values())
            byId.put(e.id(), e);
    }
    public static ObjectType byId(long id) { return byId.get(id); }

    private final long id;
    
    ObjectType(long id) { this.id = id; }
    
    public long id() { return id; }
}
