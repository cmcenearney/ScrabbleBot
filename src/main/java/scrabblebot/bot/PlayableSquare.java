package scrabblebot.bot;

public class PlayableSquare {

    enum Direction {
        ACROSS,DOWN
    }
    enum Position {
        SUFFIX,PREFIX
    }

    final int row;
    final int column;
    final Direction direction;
    final Position position;

    public PlayableSquare(int row, int column, Direction direction, Position position) {
        this.row = row;
        this.column = column;
        this.direction = direction;
        this.position = position;
    }

}
