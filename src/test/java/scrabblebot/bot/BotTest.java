package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.core.Board;
import scrabblebot.core.Move;
import scrabblebot.core.Tile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BotTest extends TestHelper {

    Board oneA = parseBoardFixture("src/test/resources/board_states/one_a.txt");
    Board twoWords = parseBoardFixture("src/test/resources/board_states/two_words.txt");
    Board fiveWords = parseBoardFixture("src/test/resources/board_states/five_words.txt");

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
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot();
        List<String> combos = b.allCombos(tiles);
        assert(127 == combos.size());
    }

    @Test
    public void testAllMakeableWords(){
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot();
        Set<String> words = b.allPossibleWords(tiles, "A");
        p(words);
        assert(205 == words.size());
    }

    @Test
    public void testMovesForAGivenWord(){
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot();
        String word = "RAMATE";
        List<Move> moves = b.movesForAGivenWord(oneA, word, 7,7, Move.Direction.ACROSS , tiles);
        assert(2 == moves.size());
    }

    @Test
    public void testGetBoardStringFromStartingTile(){
        Bot bot = new Bot();
        assertEquals("A", bot.getBoardStringFromStartingTile(oneA, 7, 7, Move.Direction.ACROSS));
        assertEquals("A", bot.getBoardStringFromStartingTile(oneA, 7, 7, Move.Direction.DOWN));
        assertEquals("SWEET", bot.getBoardStringFromStartingTile(twoWords, 7, 5, Move.Direction.ACROSS));
    }

    @Test
    public void testAllTheMoves() {
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot();
        List<Move> moves = b.allMovesForAGivenTileRack(oneA,7,7,Move.Direction.ACROSS, tiles);
        p(moves.size());
        moves.stream().filter(m -> m.checkMove());
        p(moves.size());
        moves.stream().forEach(m -> m.makeMove());
        moves = moves.stream()
                .sorted((m1, m2) -> {
                    m1.makeMove();
                    m2.makeMove();
                    return m2.getScore() - m1.getScore();
                })
                .collect(Collectors.toList());
        p(moves.get(0).getScore());
    }

    @Test
    public void testPlayableSquares(){
        Bot b = new Bot();
        assert(b.isPlayable(oneA,7,7));
        assertFalse(b.isPlayable(fiveWords,7,8));
        assert(b.isPlayable(fiveWords,7,7));
    }
}

