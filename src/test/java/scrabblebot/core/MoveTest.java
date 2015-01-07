package scrabblebot.core;

import org.junit.Test;
import scrabblebot.TestHelper;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class MoveTest extends TestHelper {

    Board emptyBoard = parseBoardFixture("src/test/resources/board_states/empty.txt");
    Board oneA = parseBoardFixture("src/test/resources/board_states/one_a.txt");
    Board twoWords = parseBoardFixture("src/test/resources/board_states/two_words.txt");
    Board threeWords = parseBoardFixture("src/test/resources/board_states/three_words.txt");

    @Test
    public void testFirstMoveMustTouchCenterTile(){
        List<Tile> tiles = tilesFromString("AHEMBCD") ;
        Move m = new Move(emptyBoard,2,2, Move.Direction.ACROSS, "AHEM", tiles);
        assertFalse(m.checkMove());
        Move m2 = new Move(emptyBoard,7,7, Move.Direction.ACROSS, "AHEM", tiles);
        assert(m2.checkMove());
    }

    @Test
    public void testMovesThatContradictExistingTilesAreInvalid(){
        List<Tile> tiles = tilesFromString("AHEMBCD") ;
        Move m = new Move(oneA,7,6, Move.Direction.ACROSS, "HEM", tiles); //clashing with center tile "A"
        assertFalse(m.checkMove());
        List<Tile> tiles2 = tilesFromString("WRONGRA") ;
        Move m2 = new Move(oneA,6,7, Move.Direction.DOWN, "WRONG", tiles); //clashing with center tile "A"
        assertFalse(m2.checkMove());
    }

    @Test
    public void testMovesThatGoOffTheBoardAreInvalid(){
        List<Tile> tiles = tilesFromString("AHEMBCD") ;
        Move m = new Move(threeWords,12,13, Move.Direction.DOWN, "READ", tiles);
        assertFalse(m.checkMove());
        m = new Move(threeWords,12,13, Move.Direction.DOWN, "RED", tiles);
        assert (m.checkMove());
    }

    @Test
    public void testMovesThatGoOffTheBoardAreInvalid2(){
        List<Tile> tiles = tilesFromString("ARENTS") ;
        Move m = new Move(threeWords,5,9, Move.Direction.ACROSS, "PARENTS", tiles);
        assertFalse(m.checkMove());
        m = new Move(threeWords,5,9, Move.Direction.ACROSS, "PARENT", tiles);
        assert (m.checkMove());
    }

    @Test
    public void testSideWordsBasic(){
        List<Tile> tiles = tilesFromString("AHEMBCD");
        Move m = new Move(twoWords, 6,8,Move.Direction.ACROSS, "MODE", tiles);
        assert(m.checkMove());
        List<Tile> tiles2 = tilesFromString("TPIDAAA");
        Move m2 = new Move(twoWords, 6,8,Move.Direction.DOWN, "TEPID", tiles2);
        assert(m2.checkMove());
        m2.makeMove();
        p(m2.getScore());
    }

    @Test
    public void testScoringBasic(){
        List<Tile> tiles = tilesFromString("AHEMBCD") ;
        Move m2 = new Move(emptyBoard,7,7, Move.Direction.ACROSS, "AHEM", tiles);
        m2.makeMove();
        assert(18 == m2.getScore());
    }

    @Test
    public void testScoringSideWordsBasic() {
        List<Tile> tiles = tilesFromString("AHEMBCD");
        Move m = new Move(twoWords, 6, 8, Move.Direction.ACROSS, "MODE", tiles);
        assert (m.checkMove());
        m.makeMove();
        assert(17 == m.getScore());
    }

    @Test
    public void testScoringSideWordsBasic2() {
        List<Tile> tiles = tilesFromString("TPIDAAA");
        Move m = new Move(twoWords, 6,8,Move.Direction.DOWN, "TEPID", tiles);
        assert(m.checkMove());
        m.makeMove();
        assert(27 == m.getScore());
    }

}