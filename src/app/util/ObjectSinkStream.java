package app.util;

import java.io.*;

/**
 * Write objects to a file.
 */
public class ObjectSinkStream {
    /**
     * The output stream object.
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructor.
     *
     * @param filename The file that we will write on.
     */
    public ObjectSinkStream(String filename) {
        try {
            File file = new File(filename);
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores the object to a file.
     *
     * @param o The object that will be written to a file.
     */
    public void storeObject(Object o) {
        try {
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the output stream.
     */
    public void close() {
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
