// This original source is copied from https://formats.kaitai.io/systemd_journal/java.html
// and modified to be easier to use.
package javaJournal.systemd.journal;

// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

import io.kaitai.struct.ByteBufferKaitaiStream;
import io.kaitai.struct.KaitaiStruct;
import io.kaitai.struct.KaitaiStream;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * systemd, a popular user-space system/service management suite on Linux,
 * offers logging functionality, storing incoming logs in a binary journal
 * format.
 * 
 * On live Linux system running systemd, these journals are typically located at:
 * 
 * * /run/log/journal/machine-id/*.journal (volatile, lost after reboot)
 * * /var/log/journal/machine-id/*.journal (persistent, but disabled by default on Debian / Ubuntu)
 * @see <a href="https://www.freedesktop.org/wiki/Software/systemd/journal-files/">Source</a>
 */
public class Journal extends KaitaiStruct {
    public static Journal fromFile(String fileName) throws IOException {
        return new Journal(new ByteBufferKaitaiStream(Files.readAllBytes(Paths.get(fileName))));
    }
    
    private Long lenHeader;
    private byte[] dataHashTable;
    private byte[] fieldHashTable;
    private Header header;
    private ArrayList<JournalObject> objects;
    private Journal _root;
    private KaitaiStruct _parent;
    private byte[] _raw_header;

    public Journal(KaitaiStream _io) {
        this(_io, null, null);
    }

    public Journal(KaitaiStream _io, KaitaiStruct _parent) {
        this(_io, _parent, null);
    }

    public Journal(KaitaiStream _io, KaitaiStruct _parent, Journal _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root == null ? this : _root;
        _read();
    }
    private void _read() {
        this._raw_header = this._io.readBytes(lenHeader());
        KaitaiStream _io__raw_header = new ByteBufferKaitaiStream(_raw_header);
        this.header = new Header(_io__raw_header, this, _root);
        this.objects = new ArrayList<JournalObject>();
        for (int i = 0; i < header().numObjects(); i++) {
            this.objects.add(new JournalObject(this._io, this, _root));
        }
    }

    /**
     * Header length is used to set substream size, as it thus required
     * prior to declaration of header.
     */
    public Long lenHeader() {
        if (this.lenHeader != null)
            return this.lenHeader;
        long _pos = this._io.pos();
        this._io.seek(88);
        this.lenHeader = this._io.readU8le();
        this._io.seek(_pos);
        return this.lenHeader;
    }
    
    public byte[] dataHashTable() {
        if (this.dataHashTable != null)
            return this.dataHashTable;
        long _pos = this._io.pos();
        this._io.seek(header().ofsDataHashTable());
        this.dataHashTable = this._io.readBytes(header().lenDataHashTable());
        this._io.seek(_pos);
        return this.dataHashTable;
    }
    
    public byte[] fieldHashTable() {
        if (this.fieldHashTable != null)
            return this.fieldHashTable;
        long _pos = this._io.pos();
        this._io.seek(header().ofsFieldHashTable());
        this.fieldHashTable = this._io.readBytes(header().lenFieldHashTable());
        this._io.seek(_pos);
        return this.fieldHashTable;
    }
    
    public Header header() { return header; }
    public ArrayList<JournalObject> objects() { return objects; }
    public Journal _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
    public byte[] _raw_header() { return _raw_header; }
}
