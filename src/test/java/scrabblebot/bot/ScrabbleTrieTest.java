package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.data.Trie;

public class ScrabbleTrieTest extends TestHelper {

    @Test
    public void testScrabbleTrie(){
        Trie t = ScrabbleTrie.INSTANCE.getTrie();
        assert(t.containsPrefix("CRAT"));
        //p(t.listWords(t.getNode("SWEE"), new ArrayList<>()));
    }


}