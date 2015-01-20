package scrabblebot.bot;

import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.core.Move;
import scrabblebot.core.Tile;
import scrabblebot.data.RealList;
import scrabblebot.data.Trie;
import scrabblebot.data.TrieNode;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class RowBotTest extends TestHelper {

    Trie trie = ScrabbleTrie.INSTANCE.getTrie();

    public void testStartableIndicesEmpty(){
        RowBot rb = new RowBot();
        assert(rb.startableIndices("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _").isEmpty());
    }

    /*
    _ _ _ _ _ _ _ _ A _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
     */
    @Test
    public void testStartableIndices1(){
        RowBot rb = new RowBot();
        List<Integer> expected = new ArrayList<>(Arrays.asList(
                1,2,3,4,5,6,7,8
        ));
        assertEquals(expected, rb.startableIndices("_ _ _ _ _ _ _ _ A _ _ _ _ _ _"));
    }

    /*
    _ _ A _ _ _ _ _ _ _ _ _ B _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    */
    @Test
    public void testStartableIndices2(){
        RowBot rb = new RowBot();
        List<Integer> expected = new ArrayList<>(Arrays.asList(
                0,1,2,5,6,7,8,9,10,11,12
        ));
        assertEquals(expected, rb.startableIndices("_ _ A _ _ _ _ _ _ _ _ _ B _ _"));
    }

    /*
    A _ _ _ _ _ _ _ _ _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    */
    @Test
    public void testStartableIndices3(){
        RowBot rb = new RowBot();
        List<Integer> expected = new ArrayList<>(Arrays.asList(
                0
        ));
        assertEquals(expected, rb.startableIndices("A _ _ _ _ _ _ _ _ _ _ _ _ _ _"));
    }


    /*
    _ _ _ _ _ _ _ _ _ _ _ _ _ _ A
    0 1 2 3 4 5 6 7 8 9 a b c d e
    */
    @Test
    public void testStartableIndices4(){
        RowBot rb = new RowBot();
        List<Integer> expected = new ArrayList<>(Arrays.asList(
                7,8,9,10,11,12,13
        ));
        assertEquals(expected, rb.startableIndices("_ _ _ _ _ _ _ _ _ _ _ _ _ _ A"));
    }


    /*
    _ _ _ _ _ B _ _ A _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    */
    @Test
    public void testStartableIndices5(){
        RowBot rb = new RowBot();
        List<Integer> expected = new ArrayList<>(Arrays.asList(
                0,1,2,3,4,5,7,8
        ));
        assertEquals(expected, rb.startableIndices("_ _ _ _ _ B _ _ A _ _ _ _ _ _"));
    }


    /*
    row:
    _ _ _ _ _ _ _ _ A _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    tiles:
    'C','R','T','E','G','Z','Q'
    starting at position 6, expected moves:
    [CRATE, CRAG, CRAZE, CZAR, REACT, TRACE, TEAR, TZAR, GRACE, GRAT, GRATE, GRAZE, GEAR]
     */
    @Test
    public void testRecursiveMoves(){
        Set<RowBot.RowBotMove> moves = new HashSet<>();
        RowBot rb = new RowBot();
        RealList<Character> tiles = new RealList<Character>(Arrays.asList(
                'C','R','T','E','G','Z','Q'
        ));
        TrieNode node = trie.getRoot();
        RealList<Optional<Character>> row = rb.convertStringToList("_ _ _ _ _ _ _ _ A _ _ _ _ _ _");
        rb.recursiveCalc(moves, row, tiles, 6, 6, node, "", false);
        p(moves.size());
        p(moves.stream().map(m -> m.word.toString()).collect(Collectors.toList()));
    }

    /*
    row:
    _ _ _ _ _ B _ _ A _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    tiles:
    'N','R','T','E','G','Z','M'
    starting at position 5, expected moves:
    [BREAM, BE, BEGAN, BEGAT, BEZANT]
     */
    @Test
    public void testRecursiveMoves2(){
        Set<RowBot.RowBotMove> moves = new HashSet<>();
        RowBot rb = new RowBot();
        RealList<Character> tiles = new RealList<Character>(Arrays.asList(
                'N','R','T','E','G','Z','M'
        ));
        TrieNode node = trie.getRoot();
        RealList<Optional<Character>> row = rb.convertStringToList("_ _ _ _ _ B _ _ A _ _ _ _ _ _");
        rb.recursiveCalc(moves, row, tiles, 5, 5, node, "", false);
        p(moves.size());
        p(moves.stream().map(m -> m.word).collect(Collectors.toList()));
    }

    /*
    row:
    _ _ _ _ _ B _ _ A _ _ _ _ _ _
    0 1 2 3 4 5 6 7 8 9 a b c d e
    tiles:
    'N','R','T','E','G','Z','M'
    starting at position 5, expected moves:
    [BREAM, BE, BEGAN, BEGAT, BEZANT]
    */
    @Test
    public void testGetAllMovesForRow(){
        RowBot rb = new RowBot();
        RealList<Character> tiles = new RealList<Character>(Arrays.asList(
                'N','R','T','E','G','Z','M'
        ));
        TrieNode node = trie.getRoot();
        RealList<Optional<Character>> row = rb.convertStringToList("_ _ _ _ _ B _ _ A _ _ _ _ _ _");
        //rb.recursiveCalc(moves, row, tiles, 5, 5, node, "", false);
        Set<RowBot.RowBotMove> moves = rb.getAllMovesForRow(row,tiles);
        p(moves.size());
        p(moves.stream().map(m -> m.word).collect(Collectors.toList()));
    }

    @Test
    public void testOpeningMoves(){
        RowBot rb = new RowBot();
        List<Tile> tiles = tilesFromString("AACEIOV");
        Move m = rb.highestScoringMove(oneA, tiles);
        p(m);
    }

//    @Test
//    public void testBoard(){
//        List<Tile> tiles = tilesFromString("TPIDEAW");
//        RowBot rb = new RowBot();
//        List<Move> rowMoves = rb.getAllRowMoves(twoWords, tiles);
//        p(rowMoves.size());
//        List<Move> colMoves = rb.getAllColumnMoves(twoWords, tiles);
//        p(colMoves.size());
//        List<Move> allMoves = new ArrayList<>();
//        allMoves.addAll(colMoves);
//        allMoves.addAll(rowMoves);
//        // allMoves.stream().filter(m -> m.checkMove());
//        p(allMoves.size());
//        //allMoves.stream().forEach(m -> m.makeMove());
//        allMoves = allMoves.stream()
//                .filter(m -> m.checkMove())
//                //.sorted((m1, m2) -> m1.getScore () - m2.getScore())
//                .collect(Collectors.toList());
//        allMoves.stream().forEach(m -> m.makeMove());
//        allMoves = allMoves.stream()
//                .sorted((m1, m2) -> m2.getScore () - m1.getScore())
//                .collect(Collectors.toList());
//        p(allMoves.size());
//        p(allMoves.get(0));
//        p(allMoves.get(0).getBoard());
//        p(allMoves.get(1));
//        p(allMoves.get(1).getBoard());
//        p(allMoves.get(2));
//        p(allMoves.get(2).getBoard());
//        p(allMoves.get(3));
//        p(allMoves.get(3).getBoard());
//        p(allMoves.get(4));
//        p(allMoves.get(4).getBoard());
//
//    }

}