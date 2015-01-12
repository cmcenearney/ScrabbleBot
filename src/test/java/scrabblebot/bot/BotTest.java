package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.core.Direction;
import scrabblebot.core.Move;
import scrabblebot.core.Tile;
import scrabblebot.core.WordPosition;

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
        Bot b = new Bot(emptyBoard);
        List<String> combos = b.allCombos(tiles);
        assert(127 == combos.size());
    }

    @Test
    public void testAllMakeableWords(){
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot(emptyBoard);
        Set<String> words = b.allPossibleWords(tiles, "A");
        p(words);
        assert(205 == words.size());
    }

    @Test
    public void testMovesForAGivenWord(){
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot(oneA);
        String word = "RAMATE";
        List<Move> moves = b.movesForAGivenWord(word, 7,7, Direction.ACROSS , tiles);
        assert(2 == moves.size());
    }

    @Test
    public void testGetBoardStringFromStartingTile(){
        Bot bot = new Bot(oneA);
        PlayableSquare aAcrossPre = new PlayableSquare(7,7,Direction.ACROSS,WordPosition.PREFIX);
        PlayableSquare aDownPre = new PlayableSquare(7,7,Direction.DOWN,WordPosition.PREFIX);
        assertEquals("A", bot.getBoardStringFromPlayableSquare(aAcrossPre));
        assertEquals("A", bot.getBoardStringFromPlayableSquare(aDownPre));
        Bot twoWordsBot = new  Bot(twoWords);
        PlayableSquare sweetAcrossPre = new PlayableSquare(7,5,Direction.ACROSS,WordPosition.PREFIX);
        assertEquals("SWEET", twoWordsBot.getBoardStringFromPlayableSquare(sweetAcrossPre));
    }

    @Test
    public void testAllTheMoves() {
        List<Tile> tiles = tilesFromString("HAMSTER");
        Bot b = new Bot(oneA);
        List<Move> moves = b.allMoves(tiles);
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
        assert( 12 == b.highestScoringMove(tiles).getScore());
    }

    @Test
    public void testHighestScoringMove(){
        Bot b = new Bot(fiveWords);
        Move theOne = b.highestScoringMove(tilesFromString("AXERODS"));
        p(theOne.getScore());
    }

    @Test
    public void testIsPlayable(){
        Bot oneABot = new Bot(oneA);
        assert(oneABot.isPlayable(7,7));
        Bot fiveWordsBot = new Bot(fiveWords);
        assertFalse(fiveWordsBot.isPlayable(7, 8));
        assert(fiveWordsBot.isPlayable(7,7));
    }

    @Test
    public void testGetPlayableSquaresEmpty(){
        Bot bot = new Bot(emptyBoard);
        assert(bot.getPlayableSquares().isEmpty());
    }

    @Test
    public void testGetPlayableSquaresFullBoard(){
        Bot bot = new Bot(fullOfA);
        assert(bot.getPlayableSquares().isEmpty());
    }

    @Test
    public void testGetPlayableSquaresOneA(){
        Bot bot = new Bot(oneA);
        Set<PlayableSquare> expected = new HashSet<PlayableSquare>(Arrays.asList(
                new PlayableSquare(7,7,Direction.ACROSS, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.ACROSS,WordPosition.PREFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.SUFFIX),
                new PlayableSquare(7,7,Direction.DOWN, WordPosition.PREFIX)
            ));
        assertEquals(expected, bot.getPlayableSquares());
    }

    @Test
    public void testGetPlayableSquaresTwoWords(){
        Bot bot = new Bot(twoWords);
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
        assertEquals(expected, bot.getPlayableSquares());
    }

    @Test
    public void testGetPlayableSquaresThreeWords(){
        Bot bot = new Bot(threeWords);
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
        assertEquals(expected, bot.getPlayableSquares());
    }

    @Test
    public void testGetPlayableSquaresFourWords(){
        Bot bot = new Bot(fourWords);
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
        assertEquals(expected, bot.getPlayableSquares());
    }

    @Test
    public void testGetPlayableSquaresFiveWords(){
        Bot bot = new Bot(fiveWords);
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
        assertEquals(expected, bot.getPlayableSquares());
    }
}

