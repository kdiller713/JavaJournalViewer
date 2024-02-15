// This original source is copied from https://formats.kaitai.io/systemd_journal/java.html
// and modified to be easier to use.
package javaJournal.systemd.journal;

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import javaJournal.systemd.journal.object.*;

/**
 * @see <a href="https://www.freedesktop.org/wiki/Software/systemd/journal-files/#objects">Source</a>
 */
public class JournalObject extends KaitaiStruct {
    public static JournalObject fromFile(String fileName) throws IOException {
        return new JournalObject(new ByteBufferKaitaiStream(fileName));
    }
    
    private Journal _root;
    private KaitaiStruct _parent;
    private byte[] _raw_payload;
    
    private byte[] padding;
    private ObjectType objectType;
    private int flags;
    private byte[] reserved;
    private long lenObject;
    private Object payload;

    public JournalObject(KaitaiStream _io) {
        this(_io, null, null);
    }

    public JournalObject(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public JournalObject(KaitaiStream _io, KaitaiStruct _parent, Journal _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    
    private void _read() {
        this.padding = this._io.readBytes(KaitaiStream.mod((8 - _io().pos()), 8));
        this.objectType = ObjectType.byId(this._io.readU1());
        this.flags = this._io.readU1();
        this.reserved = this._io.readBytes(6);
        this.lenObject = this._io.readU8le();
        
        this._raw_payload = this._io.readBytes((lenObject() - 16));
        if (objectType != null) {
            KaitaiStream _io__raw_payload = new ByteBufferKaitaiStream(_raw_payload);
            
            switch (objectType) {
            case DATA:
                this.payload = new DataObject(_io__raw_payload, this, _root);
                break;
            case FIELD:
                this.payload = new FieldObject(_io__raw_payload, this, _root);
                break;
            case ENTRY:
                this.payload = new EntryObject(_io__raw_payload, this, _root);
                break;
            default:
                this.payload = this._raw_payload;
                break;
            }
        } else {
            this.payload = this._raw_payload;
        }
    }
    
    public byte[] padding() { return padding; }
    public ObjectType objectType() { return objectType; }
    public int flags() { return flags; }
    public byte[] reserved() { return reserved; }
    public long lenObject() { return lenObject; }
    public Object payload() { return payload; }
    public Journal _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
    public byte[] _raw_payload() { return _raw_payload; }
}
