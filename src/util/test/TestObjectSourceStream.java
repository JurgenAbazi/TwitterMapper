package util.test;

import org.junit.jupiter.api.Test;
import util.ObjectSourceStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ObjectSource stream..
 */
public class TestObjectSourceStream {
    @Test
    public void testReadObjectWithCorrectFilePath() {
        ObjectSourceStream objectSource = new ObjectSourceStream("data/TwitterCapture.jobj");
        assertNotNull(objectSource.readObject());
        objectSource.close();
    }

    @Test
    public void testReadObjectWithIncorrectFilePath() {
        Object object = new ObjectSourceStream("No files here!").readObject();
        assertNull(object);
    }

}
