package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.core.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        List<Move> moves = b.movesForAGivenWord(oneA, word, 7,7, Direction.ACROSS , tiles);
        assert(2 == moves.size());
    }

    @Test
    public void testGetBoardStringFromStartingTile(){
        Bot bot = new Bot();
        assertEquals("A", bot.getBoardStringFromStartingTile(oneA, 7, 7, Direction.ACROSS));
        assertEquals("A", bot.getBoardStringFromStartingTile(oneA, 7, 7, Direction.DOWN));
        assertEquals("SWEET", bot.getBoardStringFromStartingTile(twoWords, 7, 5, Direction.ACROSS));
    }

    @Test
    public void testAllTheMoves() {
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot();
        List<Move> moves = b.allMovesForAGivenTileRack(oneA,7,7,Direction.ACROSS, tiles);
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
        assert( 12 == (moves.get(0).getScore()));
    }

    @Test
    public void testIsPlayable(){
        Bot b = new Bot();
        assert(b.isPlayable(oneA,7,7));
        assertFalse(b.isPlayable(fiveWords,7,8));
        assert(b.isPlayable(fiveWords,7,7));
    }

    @Test
    public void testGetPlayableSquaresEmpty(){
        Bot b = new Bot();
        assert(b.getPlayableSquares(emptyBoard).isEmpty());
    }

    @Test
    public void testGetPlayableSquaresFullBoard(){
        Bot b = new Bot();
        assert(b.getPlayableSquares(fullOfA).isEmpty());
    }

    @Test
    public void testGetPlayableSquaresOneA(){
        Bot b = new Bot();
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(7,7,Direction.ACROSS, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX)
            ));
        assertEquals(expected, b.getPlayableSquares(oneA));
    }

    @Test
    public void testGetPlayableSquaresTwoWords(){
        Bot b = new Bot();
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(5,9,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(7,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(12,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,9,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,5,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.PREFIX)
        ));
        assertEquals(expected, b.getPlayableSquares(twoWords));
    }

    @Test
    public void testGetPlayableSquaresThreeWords(){
        Bot b = new Bot();
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(5,9,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(7,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(12,9,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,5,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.SUFFIX)
        ));
        assertEquals(expected, b.getPlayableSquares(threeWords));
    }

    @Test
    public void testGetPlayableSquaresFourWords(){
        Bot b = new Bot();
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(5,9,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(7,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(12,9,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(5,5,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,5,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,8,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.SUFFIX)
        ));
        assertEquals(expected, b.getPlayableSquares(fourWords));
    }

    @Test
    public void testGetPlayableSquaresFiveWords(){
        Bot b = new Bot();
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(5,9,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,8,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(6,8,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(7,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,8,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,8,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,8,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,8,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(10,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(11,9,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,9,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(12,9,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(5,5,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(5,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(5,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(6,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(6,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(8,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(8,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(9,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(9,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(10,5,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(10,5,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(7,5,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,6,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,10,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,11,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,12,Direction.DOWN,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.ACROSS,WordPosition.SUFFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.PREFIX),
                new PlayableSquare(12,13,Direction.DOWN,WordPosition.SUFFIX)
        ));
        assertEquals(expected, b.getPlayableSquares(fiveWords));
    }
}

