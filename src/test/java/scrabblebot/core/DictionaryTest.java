package scrabblebot.core;

import org.junit.Test;
import scrabblebot.data.Dictionary;

public class DictionaryTest {

    @Test
    public void testValidWord(){
        Dictionary d = Dictionary.INSTANCE;
        assert(d.validWord("hi"));
        assert(d.validWord("howdy"));
        assert(d.validWord("me"));
        assert(!d.validWord("hiawatha"));
        assert(!d.validWord("sparklepants"));
    }

}