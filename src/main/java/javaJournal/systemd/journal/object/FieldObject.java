// This original source is copied from https://formats.kaitai.io/systemd_journal/java.html
// and modified to be easier to use.
package javaJournal.systemd.journal.object;

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import javaJournal.systemd.journal.Journal;
import javaJournal.systemd.journal.JournalObject;

/**
 * @see <a href="https://www.freedesktop.org/wiki/Software/systemd/journal-files/#fieldobjects">Source</a>
 */
public class FieldObject extends KaitaiStruct {
    public static FieldObject fromFile(String fileName) throws IOException {
        return new FieldObject(new ByteBufferKaitaiStream(fileName));
    }
    
    private Journal _root;
    private JournalObject _parent;
    
    private long hash;
    private long ofsNextHash;
    private long ofsHeadField;
    private byte[] payload;
    
    private JournalObject nextHash;
    private JournalObject headField;

    public FieldObject(KaitaiStream _io) {
        this(_io, null, null);
    }

    public FieldObject(KaitaiStream _io, JournalObject _parent) {
        this(_io, _parent, null);
    }

    public FieldObject(KaitaiStream _io, JournalObject _parent, Journal _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    
    private void _read() {
        this.hash = this._io.readU8le();
        this.ofsNextHash = this._io.readU8le();
        this.ofsHeadField = this._io.readU8le();
        this.payload = this._io.readBytesFull();
    }
    
    public JournalObject nextHash() {
        if (this.nextHash != null)
            return this.nextHash;
        if (ofsNextHash() != 0) {
            KaitaiStream io = _root()._io();
            long _pos = io.pos();
            io.seek(ofsNextHash());
            this.nextHash = new JournalObject(io, this, _root);
            io.seek(_pos);
        }
        return this.nextHash;
    }
    
    public JournalObject headField() {
        if (this.headField != null)
            return this.headField;
        if (ofsHeadField() != 0) {
            KaitaiStream io = _root()._io();
            long _pos = io.pos();
            io.seek(ofsHeadField());
            this.headField = new JournalObject(io, this, _root);
            io.seek(_pos);
        }
        return this.headField;
    }
    
    public long hash() { return hash; }
    public long ofsNextHash() { return ofsNextHash; }
    public long ofsHeadField() { return ofsHeadField; }
    public byte[] payload() { return payload; }
    public Journal _root() { return _root; }
    public JournalObject _parent() { return _parent; }
}
