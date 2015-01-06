package scrabblebot.core;

public class BoardSpace {

    enum Type {
        DOUBLE_LETTER, DOUBLE_WORD, TRIPLE_LETTER, TRIPLE_WORD, PLAIN
    }

    final Type type;
    private String value = null;
    private boolean occupied = false;

    public BoardSpace(Type type){
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        if (value != null){
            this.occupied = true;
        }
    }

    public Type getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

}
