package scrabblebot.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest {

    @Test
    public void testValidWord(){
        Dictionary d = Dictionary.INSTANCE;
        assert(d.validWord("hi"));
    }

}