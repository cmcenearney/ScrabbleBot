package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.combinations.Combinatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BotTest extends TestHelper {

    /**
     int k = 1 to 7
     int n = 7

     get all possible combinations of tiles:

     n k count
     ---------
     7 1 7
     7 2 21
     7 3 35
     7 4 35
     7 5 21
     7 6 7
     7 7 1

     total = 127 = 7 + 21 + 35 + 35 + 21 + 7 + 1
     */
    @Test
    public void testAllCombos(){
        List<String> tiles = new ArrayList<>(Arrays.asList("H","A","M","S","T","E","R"));
        Bot b = new Bot();
        List<String> combos = b.allCombos(tiles);
        assert(127 == combos.size());
    }

    @Test
    public void testAllMakeableWords(){
        List<String> tiles = new ArrayList<>(Arrays.asList("H","A","M","S","T","E","R"));
        Bot b = new Bot();
        Set<String> words = b.makeableWords(tiles, "A");
        p(words);
        assert(236 == words.size());
    }
}

