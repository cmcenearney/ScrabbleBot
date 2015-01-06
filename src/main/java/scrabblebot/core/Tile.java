package scrabblebot.core;

import java.io.Serializable;

public class Tile implements Serializable {

    private final String character;
    private final int points;

    public Tile(String ch, int pts){
        this.character = ch;
        this.points = pts;
    }

    public String getCharacter() {
        return character;
    }

    public int getPoints() {
        return points;
    }

}