package scrabblebot;

import org.junit.Test;
import scrabblebot.core.Board;
import scrabblebot.core.Tile;
import scrabblebot.core.TileConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestHelper {

    public void p(Object... args){
        String s = "";
        for (Object o : args){
            s += o.toString();
        }
        System.out.println(s);
    }

    public Board parseBoardFixture(String path){
        Board b = new Board();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (int i = 0; i < 15; i++){
                String[] vals = lines.get(i).split(" ");
                if (vals.length < 15){
                    throw new IllegalArgumentException("yer board aint big enough");
                }
                for (int j = 0; j < 15; j++){
                    String v = vals[j];
                    if (v.equals("_"))
                        v = null;
                    b.getSpace(i,j).setValue(v);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return b;
    }

    public List<Tile> tilesFromString(String s){
        List<Tile> tiles = new ArrayList<>();
        String[] letters = s.split("");
        TileConfig tc = new TileConfig();
        for (String l : letters){
            int points = tc.getTilePoints(l);
            tiles.add(new Tile(l, points));
        }
        return tiles;
    }

    @Test public void testParseBoardFixture() {
        String empty = "src/test/resources/board_states/empty.txt";
        String fullOfA = "src/test/resources/board_states/full_of_a.txt";
        String oneA = "src/test/resources/board_states/one_a.txt";
        Board b = parseBoardFixture(empty);
        assertTrue(b.isEmpty());

        b = parseBoardFixture(fullOfA);
        assertFalse(b.isEmpty());
        assertTrue(
                b.getSpaces().stream()
                        .flatMap(r -> r.stream())
                        .allMatch(s -> s.getValue().equals("A"))
                );

        b = parseBoardFixture(oneA);
        assertFalse(b.isEmpty());
        assertTrue(b.getSpace(7,7).getValue().equals("A"));
    }

}
