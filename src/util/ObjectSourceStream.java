package util;

import java.io.*;

/**
 * Read objects from a file.
 */
public class ObjectSourceStream {
    /**
     * The input stream object.
     */
    private ObjectInputStream objectInputStream;

    /**
     * Constructor.
     *
     * @param filename The file that will be read.
     */
    public ObjectSourceStream(String filename) {
        try {
            File file = new File(filename);
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the object from the input stream.
     *
     * @return The object that is read.
     */
    public Object readObject() {
        try {
            return objectInputStream.readObject();
        } catch (EOFException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the input stream.
     */
    public void close() {
        try {
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
