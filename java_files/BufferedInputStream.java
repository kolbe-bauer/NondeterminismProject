// 
// Decompiled by Procyon v0.5.36
// 

package java11.io;

import clinitrewriter.Clinit;

public class BufferedInputStream extends FilterInputStream
{
    private static int defaultBufferSize;
    protected byte[] buf;
    protected int count;
    protected int pos;
    protected int markpos;
    protected int marklimit;
    
    static {
        XXXmyClinitXXX();
    }
    
    private void ensureOpen() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
    }
    
    public BufferedInputStream(final InputStream in) {
        this(in, BufferedInputStream.defaultBufferSize);
    }
    
    public BufferedInputStream(final InputStream in, final int size) {
        super(in);
        this.markpos = -1;
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }
    
    private void fill() throws IOException {
        if (this.markpos < 0) {
            this.pos = 0;
        }
        else if (this.pos >= this.buf.length) {
            if (this.markpos > 0) {
                final int sz = this.pos - this.markpos;
                System.arraycopy(this.buf, this.markpos, this.buf, 0, sz);
                this.pos = sz;
                this.markpos = 0;
            }
            else if (this.buf.length >= this.marklimit) {
                this.markpos = -1;
                this.pos = 0;
            }
            else {
                int nsz = this.pos * 2;
                if (nsz > this.marklimit) {
                    nsz = this.marklimit;
                }
                final byte[] nbuf = new byte[nsz];
                System.arraycopy(this.buf, 0, nbuf, 0, this.pos);
                this.buf = nbuf;
            }
        }
        this.count = this.pos;
        final int n = this.in.read(this.buf, this.pos, this.buf.length - this.pos);
        if (n > 0) {
            this.count = n + this.pos;
        }
    }
    
    public synchronized int read() throws IOException {
        this.ensureOpen();
        if (this.pos >= this.count) {
            this.fill();
            if (this.pos >= this.count) {
                return -1;
            }
        }
        return this.buf[this.pos++] & 0xFF;
    }
    
    private int read1(final byte[] b, final int off, final int len) throws IOException {
        int avail = this.count - this.pos;
        if (avail <= 0) {
            if (len >= this.buf.length && this.markpos < 0) {
                return this.in.read(b, off, len);
            }
            this.fill();
            avail = this.count - this.pos;
            if (avail <= 0) {
                return -1;
            }
        }
        final int cnt = (avail < len) ? avail : len;
        System.arraycopy(this.buf, this.pos, b, off, cnt);
        this.pos += cnt;
        return cnt;
    }
    
    public synchronized int read(final byte[] b, final int off, final int len) throws IOException {
        this.ensureOpen();
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int n = this.read1(b, off, len);
        if (n <= 0) {
            return n;
        }
        while (n < len && this.in.available() > 0) {
            final int n2 = this.read1(b, off + n, len - n);
            if (n2 <= 0) {
                break;
            }
            n += n2;
        }
        return n;
    }
    
    public synchronized long skip(final long n) throws IOException {
        this.ensureOpen();
        if (n <= 0L) {
            return 0L;
        }
        long avail = this.count - this.pos;
        if (avail <= 0L) {
            if (this.markpos < 0) {
                return this.in.skip(n);
            }
            this.fill();
            avail = this.count - this.pos;
            if (avail <= 0L) {
                return 0L;
            }
        }
        final long skipped = (avail < n) ? avail : n;
        this.pos += (int)skipped;
        return skipped;
    }
    
    public synchronized int available() throws IOException {
        this.ensureOpen();
        return this.count - this.pos + this.in.available();
    }
    
    public synchronized void mark(final int readlimit) {
        this.marklimit = readlimit;
        this.markpos = this.pos;
    }
    
    public synchronized void reset() throws IOException {
        this.ensureOpen();
        if (this.markpos < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.pos = this.markpos;
    }
    
    public boolean markSupported() {
        return true;
    }
    
    public void close() throws IOException {
        if (this.in == null) {
            return;
        }
        this.in.close();
        this.in = null;
        this.buf = null;
    }
    
    public static void XXXmyClinitXXX() {
        Clinit.logClinit("java11/io/BufferedInputStream");
        BufferedInputStream.defaultBufferSize = 0;
        BufferedInputStream.defaultBufferSize = 2048;
    }
}
