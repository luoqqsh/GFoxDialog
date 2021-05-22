package com.xing.gfox.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class U_stream {
    /**
     * hl_sharedP中使用
     */
    public static String objectToStream(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        return new String(Base64.encode(os.toByteArray(), Base64.DEFAULT));
    }

    public static <T> T streamToObject(String str) throws IOException, ClassNotFoundException {
        byte[] base64 = Base64.decode(str.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream is = new ByteArrayInputStream(base64);
        ObjectInputStream bis = new ObjectInputStream(is);
        return (T) bis.readObject();
    }

    public static void streamCopy(FileInputStream inputStream, FileOutputStream outStream) throws IOException {
        int d = -1;
        while ((d = inputStream.read()) != -1) {
            outStream.write(d);
        }
        inputStream.close();
        outStream.close();
    }
}
