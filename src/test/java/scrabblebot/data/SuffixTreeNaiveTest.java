package scrabblebot.data;

import org.junit.Test;
import scrabblebot.TestHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SuffixTreeNaiveTest extends TestHelper {

    @Test
    public void testSuffixTreeAllWords(){
        SuffixTreeNaive s = new SuffixTreeNaive();
        Trie t = new Trie();
        File file = new File("src/main/resources/twl06.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() > 0) {
                    //s.addString(line.toUpperCase());
                    t.addWord(line.toUpperCase());
                }
            }
        }
        catch (IOException e){
            System.out.println("problem reading dictionary file:" + e);
        }
        p(s.countNodes());

    }

}