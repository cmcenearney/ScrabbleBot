package scrabblebot.core;

import org.junit.Test;

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