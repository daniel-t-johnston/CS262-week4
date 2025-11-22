import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for CharacterCounter class
 */
class CharacterCounterTest {

    private CharacterCounter counter;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        counter = new CharacterCounter();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // Test wordCounter method
    @Test
    void testWordCounter_SingleWord() {
        assertEquals(1, counter.wordCounter("hello"));
    }

    @Test
    void testWordCounter_MultipleWords() {
        assertEquals(3, counter.wordCounter("hello world test"));
    }

    @Test
    void testWordCounter_EmptyString() {
        assertEquals(0, counter.wordCounter(""));
    }

    @Test
    void testWordCounter_MultipleSpaces() {
        assertEquals(3, counter.wordCounter("hello  world   test"));
    }

    @Test
    void testWordCounter_LeadingTrailingSpaces() {
        assertEquals(2, counter.wordCounter("  hello world  "));
    }

    @Test
    void testWordCounter_OnlySpaces() {
        assertEquals(0, counter.wordCounter("     "));
    }

    // Test readFile method with temporary files
    @Test
    void testReadFile_SimpleFile() throws IOException {
        Path tempFile = createTempFile("Hello world\nThis is a test\n");
        provideInput(tempFile.toString());

        int[] result = counter.readFile();

        assertEquals(2, result[0]); // lines
        assertEquals(6, result[1]); // words
        assertEquals(25, result[2]); // characters

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadFile_EmptyFile() throws IOException {
        Path tempFile = createTempFile("");
        provideInput(tempFile.toString());

        int[] result = counter.readFile();

        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadFile_SingleLine() throws IOException {
        Path tempFile = createTempFile("Single line text");
        provideInput(tempFile.toString());

        int[] result = counter.readFile();

        assertEquals(1, result[0]);
        assertEquals(3, result[1]);
        assertEquals(16, result[2]);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadFile_MultipleEmptyLines() throws IOException {
        Path tempFile = createTempFile("Line 1\n\n\nLine 2\n");
        provideInput(tempFile.toString());

        int[] result = counter.readFile();

        assertEquals(4, result[0]); // counts empty lines
        assertEquals(4, result[1]);
        assertEquals(12, result[2]); // only non-empty line characters

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadFile_FileNotFound() {
        provideInput("nonexistent_file.txt");

        IOException exception = assertThrows(IOException.class, () -> {
            counter.readFile();
        });

        assertTrue(exception.getMessage().contains("Unable to load file"));
    }

    // Test runCount method
    @Test
    void testRunCount_ValidFile() throws IOException {
        Path tempFile = createTempFile("Test line\n");
        provideInput(tempFile.toString());

        counter.runCount();

        String output = outContent.toString();
        assertTrue(output.contains("Lines: 1"));
        assertTrue(output.contains("Words: 2"));
        assertTrue(output.contains("Characters: 9"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testRunCount_InvalidFile() {
        provideInput("invalid_file.txt");

        counter.runCount();

        String output = outContent.toString();
        assertTrue(output.contains("Unable to load file"));
    }

    private Path createTempFile(String content) throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, content.getBytes());
        return tempFile;
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}