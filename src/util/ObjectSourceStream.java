package util;

import java.io.*;

/**
 * Read objects from a file.
 */
public class ObjectSourceStream {
    private ObjectInputStream objectInputStream;

    public ObjectSourceStream(String filename) {
        try {
            File file = new File(filename);
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject() {
        try {
            return objectInputStream.readObject();
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
