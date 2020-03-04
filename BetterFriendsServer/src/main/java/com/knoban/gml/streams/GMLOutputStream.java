package com.knoban.gml.streams;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class was pulled from:
 * https://github.com/Chukobyte/Java_GML_Server/blob/master/GML_Java_Server/src/com/chukobyte/gmljavaserver/main/GMLOutputStream.java
 *
 * It has been modified slightly to improve writeString performance.
 */
public class GMLOutputStream {

    private OutputStream out;

    private ByteBuffer byteBuffer;
    private DataOutput byteWriter;

    public GMLOutputStream(OutputStream out) {
        this.out = out;
        this.byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteWriter = new DataOutputStream(out);
    }

    private void write(int bytes) throws IOException {
        byteBuffer.rewind();
        for(int i=0; i<bytes; i++) {
            byteWriter.writeByte(byteBuffer.get());
        }
        byteBuffer.clear();
    }

    public void writeS8(byte S8) throws IOException {
        byteWriter.writeByte(S8);
    }

    public void writeS16(short S16) throws IOException {
        byteBuffer.putShort(S16);
        write(2);
    }

    public void writeS32(int S32) throws IOException {
        byteBuffer.putInt(S32);
        write(4);
    }

    public void writeF32(float F32) throws IOException {
        byteBuffer.putFloat(F32);
        write(4);
    }
    public void writeF64(double F64) throws IOException {
        byteBuffer.putDouble(F64);
        write(8);
    }
    public void writeString(String string) throws IOException {
        for(int i=0; i<string.length(); i++){
            char c = string.charAt(i);
            byteWriter.writeByte((byte) c);
        }
        byteWriter.writeByte((byte) 0);
    }

    public void flush() throws IOException {
        ((DataOutputStream) byteWriter).flush();
    }

    public void setByteWriter(DataOutput byteWriter) {
        this.byteWriter = byteWriter;
    }
}