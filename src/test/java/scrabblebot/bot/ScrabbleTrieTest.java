package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;

public class ScrabbleTrieTest extends TestHelper {

    @Test
    public void testScrabbleTrie(){
        ScrabbleTrie t = ScrabbleTrie.INSTANCE;
        assert(t.containsPrefix("CRAT"));
        //p(t.listWords(t.getNode("SWEE"), new ArrayList<>()));
    }


}