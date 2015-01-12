package scrabblebot.bot;

import scrabblebot.combinations.Combinatrix;
import scrabblebot.core.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Bot {

    Anagramsaurus a = Anagramsaurus.INSTANCE;

    public Bot() {}

    String joinList(List<Tile> list) {
        String r = "";
        for (Tile t : list)
            r += t.getCharacter();
        return r;
    }

    List<String> allCombos(List<Tile> tiles) {
        List<String> combos = new ArrayList<>();
        for (int k = 1; k <= tiles.size(); k++) {
            Combinatrix<Tile> trix = new Combinatrix<Tile>(7, k, tiles);
            List<String> ks = trix.hurtMe().stream()
                    .map(list -> joinList(list))
                    .collect(Collectors.toList());
            combos.addAll(ks);
        }
        return combos;
    }

    Set<String> allPossibleWords(List<Tile> tiles, String wordOnBoard) {
        Set<String> words = new HashSet<>();
        List<String> combos = allCombos(tiles);
        for (String w : combos) {
            List<String> ws = a.getAnagrams(w + wordOnBoard);
            if (ws != null)
                words.addAll(ws);
        }
        return words;
    }

    /*
     take an occupied boardspace, a word that will intersect with it,
     and return all Moves that can be made
    */
    List<Move> movesForAGivenWord(Board b, String word, int row, int col, Direction dir, List<Tile> tiles) {
        List<Move> moves = new ArrayList<>();
        List<Integer> offsets = new ArrayList<>();
        String c = b.getSpace(row, col).getValue();
        for (int i = 0; i < word.length(); i++) {
            if (Character.toString(word.charAt(i)).equals(c)) {
                offsets.add(i);
            }
        }
        for (Integer offset : offsets) {
            Move m;
            if (dir == Direction.ACROSS) {
                m = new Move(b.clone(), row, col - offset, dir, word, tiles);
            } else {
                m = new Move(b.clone(), row - offset, col, dir, word, tiles);
            }
            moves.add(m);
        }
        return moves;
    }

    List<Move> allMovesForAGivenTileRack(Board b, int row, int col, Direction dir, List<Tile> tiles) {
        List<Move> moves = new ArrayList<>();
        String boardString = getBoardStringFromStartingTile(b, row, col, dir);
        for (String word : allPossibleWords(tiles, boardString)) {
            moves.addAll(movesForAGivenWord(b, word, row, col, dir, tiles));
        }
        return moves;
    }

    String getBoardStringFromStartingTile(Board b, int row, int col,Direction dir) {
        BoardSpace s = b.getSpace(row, col);
        String str = "";
        while (s.isOccupied()) {
            str += s.getValue();
            if (dir == Direction.DOWN) {
                row++;
            } else {
                col++;
            }
            s = b.getSpace(row, col);
        }
        return str;
    }

    Set<PlayableSquare> getPlayableSquares(Board b){
        Set<PlayableSquare> sqrs = new HashSet<>();
        List<ArrayList<BoardSpace>> spaces = b.getSpaces();
        for (int i = 0; i < Board.boardSize; i++){
            for (int j = 0; j < Board.boardSize; j++){
                //BoardSpace space = spaces.get(i).get(j);
                if(isPlayable(b,i,j)){
                    //if leftNeighborEmpty then ACROSS and PREFIX
                    if (leftNeighborEmpty(b,i,j))
                        sqrs.add(new PlayableSquare(i,j,Direction.ACROSS, WordPosition.PREFIX));
                    //if rightNeighborEmpty then ACROSS abd SUFFIX
                    if (rightNeighborEmpty(b,i,j))
                        sqrs.add(new PlayableSquare(i,j, Direction.ACROSS, WordPosition.SUFFIX));
                    //if upperNeighborEmpty then DOWN and PREFIX
                    if (upperNeighborEmpty(b,i,j))
                        sqrs.add(new PlayableSquare(i,j,Direction.DOWN, WordPosition.PREFIX));
                    //if lowerNeighborEmpty then DOWN and SUFFIX
                    if (lowerNeighborEmpty(b,i,j))
                        sqrs.add(new PlayableSquare(i,j,Direction.DOWN, WordPosition.SUFFIX));
                }
            }
        }
        return sqrs;
    }

    /*
    identify board squares that words can be joined to
    - space is occupied and at least one neighbor is not occupied
     */
    boolean isPlayable(Board b, int row, int col) {
        List<ArrayList<BoardSpace>> spaces = b.getSpaces();
        List<Coordinate> neighbors = allNeighbors(row, col);
        return (spaces.get(row).get(col).isOccupied()  &&
                neighbors.stream()
                .map(m -> spaces.get(m.row).get(m.col))
                .anyMatch(s -> !s.isOccupied()));
    }

    /*
    at most four neighbors (no diagonals)
        row-1, col
        row+1, col
        row, col-1
        row, col+1
    */
    private List<Coordinate> allNeighbors(int row, int col) {
        List<Coordinate> coords = new ArrayList<>();
        if (row > 0) {
            coords.add(new Coordinate(row - 1, col));
        }
        if (row < 14) {
            coords.add(new Coordinate(row + 1, col));
        }
        if (col > 0) {
            coords.add(new Coordinate(row, col - 1));
        }
        if (col < 14) {
            coords.add(new Coordinate(row, col + 1));
        }
        return coords;
    }

    private boolean leftNeighborEmpty(Board b, int row, int col){
        if(col == 0)
            return false;
        return !b.getSpace(row, col-1).isOccupied();
    }

    private boolean rightNeighborEmpty(Board b, int row, int col){
        if(col == Board.boardSize-1)
            return false;
        return !b.getSpace(row, col+1).isOccupied();
    }

    private boolean upperNeighborEmpty(Board b, int row, int col){
        if(row == 0)
            return false;
        return !b.getSpace(row-1, col).isOccupied();
    }

    private boolean lowerNeighborEmpty(Board b, int row, int col){
        if(row == Board.boardSize-1)
            return false;
        return !b.getSpace(row+1, col).isOccupied();
    }

}
