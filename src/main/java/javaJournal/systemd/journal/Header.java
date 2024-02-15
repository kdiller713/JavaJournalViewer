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

public class Header extends KaitaiStruct {
    public static Header fromFile(String fileName) throws IOException {
        return new Header(new ByteBufferKaitaiStream(fileName));
    }
    
    private byte[] signature;
    private long compatibleFlags;
    private long incompatibleFlags;
    private State state;
    private byte[] reserved;
    private byte[] fileId;
    private byte[] machineId;
    private byte[] bootId;
    private byte[] seqnumId;
    private long lenHeader;
    private long lenArena;
    private long ofsDataHashTable;
    private long lenDataHashTable;
    private long ofsFieldHashTable;
    private long lenFieldHashTable;
    private long ofsTailObject;
    private long numObjects;
    private long numEntries;
    private long tailEntrySeqnum;
    private long headEntrySeqnum;
    private long ofsEntryArray;
    private long headEntryRealtime;
    private long tailEntryRealtime;
    private long tailEntryMonotonic;
    private Long numData;
    private Long numFields;
    private Long numTags;
    private Long numEntryArrays;
    private Journal _root;
    private Journal _parent;

    public Header(KaitaiStream _io) {
        this(_io, null, null);
    }

    public Header(KaitaiStream _io, Journal _parent) {
        this(_io, _parent, null);
    }

    public Header(KaitaiStream _io, Journal _parent, Journal _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    private void _read() {
        this.signature = this._io.readBytes(8);
        if (!(Arrays.equals(signature(), new byte[] { 76, 80, 75, 83, 72, 72, 82, 72 }))) {
            throw new KaitaiStream.ValidationNotEqualError(new byte[] { 76, 80, 75, 83, 72, 72, 82, 72 }, signature(), _io(), "/types/header/seq/0");
        }
        this.compatibleFlags = this._io.readU4le();
        this.incompatibleFlags = this._io.readU4le();
        this.state = State.byId(this._io.readU1());
        this.reserved = this._io.readBytes(7);
        this.fileId = this._io.readBytes(16);
        this.machineId = this._io.readBytes(16);
        this.bootId = this._io.readBytes(16);
        this.seqnumId = this._io.readBytes(16);
        this.lenHeader = this._io.readU8le();
        this.lenArena = this._io.readU8le();
        this.ofsDataHashTable = this._io.readU8le();
        this.lenDataHashTable = this._io.readU8le();
        this.ofsFieldHashTable = this._io.readU8le();
        this.lenFieldHashTable = this._io.readU8le();
        this.ofsTailObject = this._io.readU8le();
        this.numObjects = this._io.readU8le();
        this.numEntries = this._io.readU8le();
        this.tailEntrySeqnum = this._io.readU8le();
        this.headEntrySeqnum = this._io.readU8le();
        this.ofsEntryArray = this._io.readU8le();
        this.headEntryRealtime = this._io.readU8le();
        this.tailEntryRealtime = this._io.readU8le();
        this.tailEntryMonotonic = this._io.readU8le();
        if (!(_io().isEof())) {
            this.numData = this._io.readU8le();
        }
        if (!(_io().isEof())) {
            this.numFields = this._io.readU8le();
        }
        if (!(_io().isEof())) {
            this.numTags = this._io.readU8le();
        }
        if (!(_io().isEof())) {
            this.numEntryArrays = this._io.readU8le();
        }
    }
    
    public byte[] signature() { return signature; }
    public long compatibleFlags() { return compatibleFlags; }
    public long incompatibleFlags() { return incompatibleFlags; }
    public State state() { return state; }
    public byte[] reserved() { return reserved; }
    public byte[] fileId() { return fileId; }
    public byte[] machineId() { return machineId; }
    public byte[] bootId() { return bootId; }
    public byte[] seqnumId() { return seqnumId; }
    public long lenHeader() { return lenHeader; }
    public long lenArena() { return lenArena; }
    public long ofsDataHashTable() { return ofsDataHashTable; }
    public long lenDataHashTable() { return lenDataHashTable; }
    public long ofsFieldHashTable() { return ofsFieldHashTable; }
    public long lenFieldHashTable() { return lenFieldHashTable; }
    public long ofsTailObject() { return ofsTailObject; }
    public long numObjects() { return numObjects; }
    public long numEntries() { return numEntries; }
    public long tailEntrySeqnum() { return tailEntrySeqnum; }
    public long headEntrySeqnum() { return headEntrySeqnum; }
    public long ofsEntryArray() { return ofsEntryArray; }
    public long headEntryRealtime() { return headEntryRealtime; }
    public long tailEntryRealtime() { return tailEntryRealtime; }
    public long tailEntryMonotonic() { return tailEntryMonotonic; }
    public Long numData() { return numData; }
    public Long numFields() { return numFields; }
    public Long numTags() { return numTags; }
    public Long numEntryArrays() { return numEntryArrays; }
    public Journal _root() { return _root; }
    public Journal _parent() { return _parent; }
}
