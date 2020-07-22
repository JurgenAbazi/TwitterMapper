package util;

import java.io.*;

/**
 * Write objects to a file.
 */
public class ObjectSinkStream {
    private ObjectOutputStream objectOutputStream;

    public ObjectSinkStream(String filename) {
        try {
            File file = new File(filename);
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeObject(Object o) {
        try {
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
