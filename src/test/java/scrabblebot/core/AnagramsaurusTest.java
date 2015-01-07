package scrabblebot.core;

import org.junit.Test;
import scrabblebot.TestHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AnagramsaurusTest extends TestHelper{

    @Test
    public void testSortString(){
        String s = Anagramsaurus.sortString("MLKJI");
        assertEquals("IJKLM",s);
    }

    @Test
    public void testAnagramsMap(){
        Anagramsaurus a = Anagramsaurus.INSTANCE;
        Set<String> expected = new HashSet<>(Arrays.asList(
        "MATRES", "MASTER", "RAMETS", "MATERS", "STREAM", "TAMERS", "ARMETS"));
        Set<String> actual = new HashSet<>(a.getAnagrams("stream"));
        assertEquals(expected, actual);
    }


}