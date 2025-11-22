import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.NoSuchElementException;

public class DataAnalyzerTest {

    private String getResourcePath(String filename) {
        // Get the path where the class files are located
        String classPath = this.getClass().getResource("/" + filename).getPath();
        // Remove leading slash on Windows if present
        if (classPath.startsWith("/") && classPath.contains(":")) {
            classPath = classPath.substring(1);
        }
        return classPath;
    }

    @Test
    public void testInput2_ValidData() throws IOException {
        double[] result = DataAnalyzer.readFile(getResourcePath("input2.txt"));

        assertEquals(4, result.length);
        assertEquals(1.0, result[0], 0.001);
        assertEquals(2.0, result[1], 0.001);
        assertEquals(3.0, result[2], 0.001);
        assertEquals(4.0, result[3], 0.001);

        double sum = 0;
        for (double d : result) { sum += d; }
        assertEquals(10.0, sum, 0.001);
    }

    @Test
    public void testInput1_InvalidData() {
        assertThrows(NoSuchElementException.class, () -> {
            DataAnalyzer.readFile(getResourcePath("input1.txt"));
        });
    }

    @Test
    public void testInput3_TooLittleData() {
        assertThrows(NoSuchElementException.class, () -> {
            DataAnalyzer.readFile(getResourcePath("input3.txt"));
        });
    }
}
