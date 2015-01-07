package scrabblebot.core;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int boardSize = 15;
    private List<ArrayList<BoardSpace>> spaces = new ArrayList<ArrayList<BoardSpace>>(boardSize);

    public Board(){
        BoardConfig config = new BoardConfig();
        for (int row = 0; row < boardSize; row++){
            spaces.add(row, new ArrayList<BoardSpace>(15));
            for (int col = 0; col < boardSize; col++){
                BoardSpace.Type type = config.scrabble_style.get(row).get(col);
                BoardSpace new_space = new BoardSpace(type);
                spaces.get(row).add(col, new_space);
            }
        }
    }

    public List<ArrayList<BoardSpace>> getSpaces(){
        return this.spaces;
    }

    public void setSpaces(ArrayList<ArrayList<BoardSpace>> s){
        spaces = s;
    }

    public BoardSpace getSpace(int row, int column){
        return this.spaces.get(row).get(column);
    }

    public ArrayList<BoardSpace> getRow(int row){
        return spaces.get(row);
    }

    public ArrayList<BoardSpace> getColumn(int col){
        ArrayList<BoardSpace> column = new ArrayList<BoardSpace>(15);
        for (int row=0; row < boardSize; row++) {
            BoardSpace space = this.getSpace(row,col);
            column.add(row, space);
        }
        return column;
    }

    public boolean isEmpty(){
        return spaces.stream()
                .flatMap(row -> row.stream())
                .allMatch(s -> !s.isOccupied());
    }

}
