package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.core.Move;
import scrabblebot.core.Tile;
import scrabblebot.data.RealList;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

public class ObliqueBotTest extends TestHelper {

    @Test
    public void testGetHighestScoringObliqueMove(){
        List<Tile> tiles = tilesFromString("TPIDEAW");
        ObliqueBot ob = new ObliqueBot(twoWords, tiles);
        //List<Move> moves = ob.getAllObliqueMoves();
        Move daWeiner = ob.getHighestScoringObliqueMove();
        p(daWeiner);
        p(daWeiner.getBoard());
    }

    @Test
    public void testGetObliqueMoves(){
        List<Tile> tiles = tilesFromString("TPIDEAW");
        ObliqueBot ob = new ObliqueBot(twoWords, tiles);
        List<Move> moves = ob.getAllObliqueMoves();
        p(moves.get(0));
    }

    @Test
    public void testIsInWordValidWord(){
        List<Tile> tiles = tilesFromString("ABCDEFG");
        ObliqueBot ob = new ObliqueBot(emptyBoard, tiles);
        RealList<Optional<Character>> row = ob.convertStringToList("_ _ _ _ _ B L A R E _ _ _ _ _");
        assertFalse(ob.isInWord(row, 4));
        assert(ob.isInWord(row, 5));
        assert(ob.isInWord(row, 6));
        assert(ob.isInWord(row, 7));
        assert(ob.isInWord(row, 8));
        assert(ob.isInWord(row, 9));
        assertFalse(ob.isInWord(row, 10));
        assertFalse(ob.isInWord(row, 11));
    }

    @Test
    public void testIsInWordInvalidWord(){
        List<Tile> tiles = tilesFromString("ABCDEFG");
        ObliqueBot ob = new ObliqueBot(emptyBoard, tiles);
        RealList<Optional<Character>> row = ob.convertStringToList("_ _ _ _ _ B L Z T F _ _ _ _ _");
        assertFalse(ob.isInWord(row, 4));
        assertFalse(ob.isInWord(row, 5));
        assertFalse(ob.isInWord(row, 6));
        assertFalse(ob.isInWord(row, 7));
        assertFalse(ob.isInWord(row, 8));
        assertFalse(ob.isInWord(row, 9));
        assertFalse(ob.isInWord(row, 10));
        assertFalse(ob.isInWord(row, 11));
    }

}