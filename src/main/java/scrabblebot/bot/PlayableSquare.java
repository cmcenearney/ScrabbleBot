package scrabblebot.bot;

import scrabblebot.core.Direction;
import scrabblebot.core.WordPosition;

public class PlayableSquare {


    final int row;
    final int column;
    final Direction direction;
    final WordPosition position;

    public PlayableSquare(int row, int column, Direction direction, WordPosition position) {
        this.row = row;
        this.column = column;
        this.direction = direction;
        this.position = position;
    }

    @Override
    public String toString() {
        return "PlayableSquare{" +
                "row=" + row +
                ", column=" + column +
                ", direction=" + direction +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayableSquare that = (PlayableSquare) o;

        if (column != that.column) return false;
        if (row != that.row) return false;
        if (direction != that.direction) return false;
        if (position != that.position) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        result = 31 * result + direction.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
