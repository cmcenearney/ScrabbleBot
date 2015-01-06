package scrabblebot;

import org.junit.Test;
import scrabblebot.Library;
import scrabblebot.core.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


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
                if (vals.length != 15){
                    throw new IllegalArgumentException("");
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

    @Test public void testParseBoardFixture() {
        String empty = "src/test/resources/board_states/empty.txt";
        String fullOfA = "src/test/resources/board_states/full_of_a.txt";
        Board b = parseBoardFixture(empty);
        assertTrue(b.isEmpty());
        b = parseBoardFixture(fullOfA);
        assertFalse(b.isEmpty());
        assertTrue(
                b.getSpaces().stream()
                        .flatMap(r -> r.stream())
                        .allMatch(s -> s.getValue().equals("A"))
                );
    }

}
