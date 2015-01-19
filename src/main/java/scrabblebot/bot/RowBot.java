package scrabblebot.bot;

import scrabblebot.core.*;
import scrabblebot.data.RealList;
import scrabblebot.data.TrieNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RowBot {

    ScrabbleTrie trie = ScrabbleTrie.INSTANCE;

//    final Board b;
//    final RealList<Tile> tiles;
//    final RealList<Character> tileChars;
//
//    public RowBot(Board b, List<Tile> tiles) {
//        this.b = b;
//        this.tiles = new RealList<>(tiles);
//        this.tileChars = tilesToChars(tiles);
//    }


    public Move highestScoringMove(Board b, List<Tile> tiles){
        List<Move> rowMoves = getAllRowMoves(b, tiles);
        List<Move> colMoves = getAllColumnMoves(b, tiles);
        List<Move> allMoves = new ArrayList<>();
        allMoves.addAll(colMoves);
        allMoves.addAll(rowMoves);
        allMoves = allMoves.stream()
                .filter(m -> m.checkMove())
                .collect(Collectors.toList());
        allMoves.stream().forEach(m -> m.makeMove());
        allMoves = allMoves.stream()
                .sorted((m1, m2) -> m2.getScore () - m1.getScore())
                .collect(Collectors.toList());
        return allMoves.get(0);
    }

    /*
        a square is startable if
            - it is empty and has an occupied square 7 or less to the right
            - it is occupied and
                - left-most or the square to the left is empty
                - and there is at least one empty square somewhere to the right
    */
    List<Integer> startableIndices(String s){
        return startableIndices(convertStringToList(s));
    }

    List<Integer> startableIndices(RealList<Optional<Character>> row ){
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < row.size()-1;i++){
            if (row.get(i).isPresent()){
                if (i == 0 || !row.get(i-1).isPresent()){
                    indices.add(i);
                } else {
                    boolean add = IntStream.range(i, row.size() - 1)
                            .mapToObj(j -> row.get(j))
                            .anyMatch(o -> !o.isPresent());
                    if (add)
                        indices.add(i);
                }
            } else {
                if (i == 0 || !row.get(i-1).isPresent() ) {
                    boolean add = IntStream.range(i, i + 8) // 7 + 1 because range is exclusive
                            .filter(j -> j < row.size())
                            .mapToObj(j -> row.get(j))
                            .anyMatch(o -> o.isPresent());
                    if (add)
                        indices.add(i);
                }
            }
        }
        return indices;
    }

     RealList<Optional<Character>> convertStringToList(String s){
        List<Optional<Character>> chars = new ArrayList<>();
        for (char c : s.toCharArray()){
            if (c == ' '){
                continue;
            }
            else if (c == '_'){
                chars.add(Optional.empty());
            } else {
                chars.add(Optional.of(c));
            }
        }
        return new RealList<>(chars);
    }

    RealList<Optional<Character>> boardRowToRowBotRow(List<BoardSpace> spaces){
        List<Optional<Character>> results = new ArrayList<>();
        for (BoardSpace s : spaces){
            if (s.isOccupied()){
                results.add(Optional.of(s.getValue()));
            } else {
                results.add(Optional.empty());
            }
        }
        return new RealList<>(results);
    }

    List<Move> getAllRowMoves(Board b, List<Tile> tiles){
        RealList<Character> tilesFinal = tilesToChars(tiles);
        List<Move> results = new ArrayList<>();
        for (int i = 0; i < Board.boardSize; i++){
            final int j = i;
            RealList<Optional<Character>> row = boardRowToRowBotRow(b.getRow(j));
            Set<RowBotMove> moves = getAllMovesForRow(row, tilesFinal);
            results.addAll(moves.stream()
                    .map(m -> new Move(b.clone(), j, m.start, Direction.ACROSS, m.word, tiles))
                    .collect(Collectors.toList()));
        }
        return results;
    }

    List<Move> getAllColumnMoves(Board b, List<Tile> tiles){
        RealList<Character> tilesFinal = tilesToChars(tiles);
        List<Move> results = new ArrayList<>();
        for (int i = 0; i < Board.boardSize; i++){
            final int j = i;
            RealList<Optional<Character>> row = boardRowToRowBotRow(b.getColumn(j));
            Set<RowBotMove> moves = getAllMovesForRow(row, tilesFinal);
            results.addAll( moves.stream()
                    .map(m -> new Move(b.clone(),m.start,j,Direction.DOWN,m.word,tiles))
                    .collect(Collectors.toList()));
        }
        return results;
    }

    Set<RowBotMove> getAllMovesForRow(RealList<Optional<Character>> row, RealList<Character> tiles){
        //RealList<Character> tilesFinal = new RealList<>(tiles);
        List<Integer> inds = startableIndices(row);
        Set<RowBotMove> results = new HashSet<>();
        for (Integer i : inds){
            recursiveCalc(results, row, tiles, i, i, trie.getRoot(), "", false);
        }
        return results;
    }

    void recursiveCalc(Set<RowBotMove> moves, RealList<Optional<Character>> row, RealList<Character> tiles,
                       Integer start, Integer i,  TrieNode node, String partial, boolean tileTouched){
        if (node.isWord() && tileTouched && (i == row.size()-1 || !row.get(i).isPresent()))
            moves.add(new RowBotMove(start, partial));
        if (tiles.isEmpty() || i == Board.boardSize-1)
            return;
        if (row.get(i).isPresent()){
            tileTouched = true;
            Character c = row.get(i).get();
            partial += c;
            i++;
            if (node.containsChildValue(c)){
                node = node.getChild(c);
            } else {
                return;
            }
        }
        for(int j = 0; j < tiles.size();j++){
            Character c = tiles.get(j);
            if (node.containsChildValue(c)){
                //ImmutableList<Character> t = tiles.subList(j,tiles.size());
                String p = partial + c;
                RealList<Character> t = tiles.remove(j);
                recursiveCalc(moves, row, t, start, i+1, node.getChild(c), p, tileTouched);
            }
        }
        return;
    }

    RealList<Character> tilesToChars(List<Tile> tiles){
        List<Character> c = tiles.stream()
                .map(t -> t.getCharacter())
                .collect(Collectors.toList());
        return new RealList<>(c);
    }

    class RowBotMove {
        int start;
        String word;

        RowBotMove(int start, String word) {
            this.start = start;
            this.word = word;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RowBotMove that = (RowBotMove) o;

            if (start != that.start) return false;
            if (!word.equals(that.word)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + word.hashCode();
            return result;
        }
    }


}
