package util.test;

import org.junit.jupiter.api.Test;
import util.ObjectSinkStream;
import util.ObjectSourceStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


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
        try {
            new ObjectSourceStream("No files here!").readObject();
            fail("NullPointerException should have been thrown!");
        } catch (NullPointerException ignored) {
        }
    }
}
