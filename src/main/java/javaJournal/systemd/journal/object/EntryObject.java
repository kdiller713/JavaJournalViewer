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
 * @see <a href="https://www.freedesktop.org/wiki/Software/systemd/journal-files/#entryobjects">Source</a>
 */
public class EntryObject extends KaitaiStruct {
    public static EntryObject fromFile(String fileName) throws IOException {
        return new EntryObject(new ByteBufferKaitaiStream(fileName));
    }
    
    private record EntryItem(long offset, long hash) {};
    
    private Journal _root;
    private JournalObject _parent;
    
    private long seqNum;
    private long realTime;
    private long monotonic;
    private byte[] bootId;
    private long xorHash;
    
    private EntryItem[] entryItemOffsets;
    private JournalObject[] entryItems;

    public EntryObject(KaitaiStream _io) {
        this(_io, null, null);
    }

    public EntryObject(KaitaiStream _io, JournalObject _parent) {
        this(_io, _parent, null);
    }

    public EntryObject(KaitaiStream _io, JournalObject _parent, Journal _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    
    private void _read() {
        this.seqNum = this._io.readU8le();
        this.realTime = this._io.readU8le();
        this.monotonic = this._io.readU8le();
        this.bootId = this._io.readBytes(16);
        this.xorHash = this._io.readU8le();
        
        int numItems = (int)((this._io.size() - this._io.pos()) / 16);
        entryItemOffsets = new EntryItem[numItems];
        entryItems = new JournalObject[numItems];
        
        for(int i = 0; i < numItems; i++){
            entryItemOffsets[i] = new EntryItem(this._io.readU8le(), this._io.readU8le());
        }
    }
    
    public JournalObject entryItem(int i) {
        if(entryItems[i] == null){
            KaitaiStream io = _root()._io();
            long _pos = io.pos();
            io.seek(entryItemOffsets[i].offset());
            this.entryItems[i] = new JournalObject(io, this, _root);
            io.seek(_pos);
        }
        
        return this.entryItems[i];
    }
    
    public long seqNum() { return seqNum; }
    public long realTime() { return realTime; }
    public long monotonic() { return monotonic; }
    public byte[] bootId() { return bootId; }
    public long xorHash() { return xorHash; }
    public int numItems() { return entryItems.length; }
    
    public Journal _root() { return _root; }
    public JournalObject _parent() { return _parent; }
}
