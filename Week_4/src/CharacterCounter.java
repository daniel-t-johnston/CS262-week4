import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Character Counter - counts lines, words, and characters in a file
 */
final class CharacterCounter {
    public static void main(String[] args) {
        CharacterCounter characterCounter = new CharacterCounter();  
        characterCounter.runCount(); 
    }

    /**
     * Main counter method that orchestrates file reading and output
     *
     *
     */
    public void runCount() {
        int[] counts = new int[3];
        try {
            counts = readFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Lines: " + counts[0] + ", Words: " + counts[1] + ", Characters: " + counts[2]);
    }

    /**
     * Reads file and counts lines, words, and characters
     */
    public int[] readFile() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Character Counter");
        System.out.println("Please enter file name: ");
        System.out.println("(Week_4/src/hw4p1test.txt)"); //file name from homework
        String fileName = in.nextLine();
        in.close();

        int lineCount = 0;
        int wordCount = 0;
        int characterCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                characterCount += line.length();
                wordCount += wordCounter(line);
            }
        } catch (IOException e) {
            throw new IOException("Unable to load file: " + fileName);
        }

        return new int[]{lineCount, wordCount, characterCount};
    }

    /**
     * Counts non-empty words in a line
     */
    public int wordCounter(String line) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(line.split(" ")));
        int i = 0;
        while(i < list.size()) {
            if (list.get(i).isEmpty()) {
                list.remove(i);
            } else {
                ++i;
            }
        }
        return list.size();
    }
}