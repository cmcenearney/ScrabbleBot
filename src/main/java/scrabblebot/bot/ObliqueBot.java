package scrabblebot.bot;

import scrabblebot.core.*;
import scrabblebot.data.RealList;

import java.util.*;
import java.util.stream.Collectors;

public class ObliqueBot extends RowBot {

    final Board board;
    final RealList<Character> tiles;
    final List<Tile> tiletiles;

    public ObliqueBot(Board board, List<Tile> tiles) {
        this.board = board;
        this.tiletiles = tiles;
        this.tiles = tilesToChars(tiles);
    }

    public Move getHighestScoringObliqueMove(){
        List<Move> allMoves = getAllObliqueMoves();
        allMoves = allMoves.stream()
                .filter(m -> m.checkMove())
                .collect(Collectors.toList());
        allMoves.stream().forEach(m -> m.makeMove());
        allMoves = allMoves.stream()
                .sorted((m1, m2) -> m2.getScore () - m1.getScore())
                .collect(Collectors.toList());
        return allMoves.get(0);
    }

    public List<Move> getAllObliqueMoves(){
        List<Move> results = new ArrayList<>();
        for (BoardSpace s : getAllAnchorSquares()){
            results.addAll(getObliqueMovesForASpace(s));
        }
        return results;
    }

    public Set<Move> getObliqueMovesForASpace(BoardSpace sp){
        Set<Move> moves = new HashSet<>();
        if(board.getUpperNeighbor(sp).isPresent() && !board.getUpperNeighbor(sp).get().isOccupied()){
            RealList<Optional<Character>> boardRow = boardRowToRowBotRow(board.getColumn(sp.getCol()));
            for(int i = 0;i < tiles.size(); i++){
                Character c = tiles.get(i);
                RealList<Optional<Character>> sideWordRow = boardRow.replace(sp.getRow()-1, Optional.of(c));
                if (isInWord(sideWordRow, sp.getRow())){
                    RealList<Optional<Character>> obliqueWordRow = boardRowToRowBotRow(board.getRow(sp.getRow()-1)).replace(sp.getCol(), Optional.of(c));
                    Set<RowBotMove> rbmoves = getAllMovesForRow(obliqueWordRow, tiles.remove(i));
                    moves.addAll(rbmoves.stream()
                            .map(m -> new Move(board.clone(), sp.getRow()-1, m.start, Direction.ACROSS, m.word, tiletiles))
                            .collect(Collectors.toList()));
                }
            }
        }
        if(board.getLowerNeighbor(sp).isPresent() && !board.getLowerNeighbor(sp).get().isOccupied()){
            RealList<Optional<Character>> boardRow = boardRowToRowBotRow(board.getColumn(sp.getCol()));
            for(int i = 0;i < tiles.size(); i++){
                Character c = tiles.get(i);
                RealList<Optional<Character>> sideWordRow = boardRow.replace(sp.getRow()+1, Optional.of(c));
                if (isInWord(sideWordRow, sp.getRow())){
                    RealList<Optional<Character>> obliqueWordRow = boardRowToRowBotRow(board.getRow(sp.getRow()+1)).replace(sp.getCol(), Optional.of(c));
                    Set<RowBotMove> rbmoves = getAllMovesForRow(obliqueWordRow, tiles.remove(i));
                    moves.addAll(rbmoves.stream()
                            .map(m -> new Move(board.clone(), sp.getRow()+1, m.start, Direction.ACROSS, m.word, tiletiles))
                            .collect(Collectors.toList()));
                }
            }
        }
        if(board.getLeftNeighbor(sp).isPresent() && !board.getLeftNeighbor(sp).get().isOccupied()){
            RealList<Optional<Character>> boardRow = boardRowToRowBotRow(board.getRow(sp.getRow()));
            for(int i = 0;i < tiles.size(); i++){
                Character c = tiles.get(i);
                RealList<Optional<Character>> sideWordRow = boardRow.replace(sp.getCol()-1, Optional.of(c));
                if (isInWord(sideWordRow, sp.getCol())){
                    RealList<Optional<Character>> obliqueWordRow = boardRowToRowBotRow(board.getColumn(sp.getCol()-1)).replace(sp.getRow(), Optional.of(c));
                    Set<RowBotMove> rbmoves = getAllMovesForRow(obliqueWordRow, tiles.remove(i));
                    moves.addAll(rbmoves.stream()
                            .map(m -> new Move(board.clone(), m.start, sp.getCol()-1, Direction.DOWN, m.word, tiletiles))
                            .collect(Collectors.toList()));
                }
            }
        }
        if(board.getRightNeighbor(sp).isPresent() && !board.getRightNeighbor(sp).get().isOccupied()){
            RealList<Optional<Character>> boardRow = boardRowToRowBotRow(board.getRow(sp.getRow()));
            for(int i = 0;i < tiles.size(); i++){
                Character c = tiles.get(i);
                RealList<Optional<Character>> sideWordRow = boardRow.replace(sp.getCol()+1, Optional.of(c));
                if (isInWord(sideWordRow, sp.getCol())){
                    RealList<Optional<Character>> obliqueWordRow = boardRowToRowBotRow(board.getColumn(sp.getCol()+1)).replace(sp.getRow(), Optional.of(c));
                    Set<RowBotMove> rbmoves = getAllMovesForRow(obliqueWordRow, tiles.remove(i));
                    moves.addAll(rbmoves.stream()
                            .map(m -> new Move(board.clone(), m.start, sp.getCol()+1, Direction.DOWN, m.word, tiletiles))
                            .collect(Collectors.toList()));
                }
            }
        }
        return moves;
    }

    public List<BoardSpace> getAllAnchorSquares(){
        return board.getSpaces().stream()
                .flatMap(row -> row.stream())
                .filter(s -> isAnchorSquare(s))
                .collect(Collectors.toList());
    }

    boolean isAnchorSquare(BoardSpace sp){
        return
                ( sp.isOccupied() &&
                        (board.getNeighbors(sp).stream()
                                .anyMatch(s -> !s.isOccupied())
                        )
                );
    }

    boolean isInWord(RealList<Optional<Character>> row, int i){
        int j = i+1;
        String word = "";
        Optional<Character> current = row.get(i);
        if (!current.isPresent())
            return false;
        //prepend in the negative direction
        while (i >= 0 && current.isPresent()){
            word = current.get() + word;
            i--;
            current = row.get(i);
        }
        current = row.get(j);
        //append in the positive direction
        while (j < row.size() && current.isPresent()){
            word += current.get();
            j++;
            current = row.get(j);
        }
        return trie.containsWord(word);
    }




}
