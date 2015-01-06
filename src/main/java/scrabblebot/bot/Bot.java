package scrabblebot.bot;

import scrabblebot.combinations.Combinatrix;
import scrabblebot.core.Anagramsaurus;

import java.util.*;
import java.util.stream.Collectors;

public class Bot {

    Anagramsaurus a = Anagramsaurus.INSTANCE;
    public Bot(){}

    String joinList(List<String> list){
        String r = "";
        for (String s : list)
            r += s;
        return r;
    }

    List<String> allCombos(List<String> tiles){
        List<String> combos = new ArrayList<>();
        for (int k = 1; k <= tiles.size(); k++){
            Combinatrix<String> trix = new Combinatrix<>(7,k,tiles);
            List<String> ks = trix.hurtMe().stream()
                    .map(list -> joinList(list))
                    .collect(Collectors.toList());
            combos.addAll(ks);
        }
        return combos;
    }

    Set<String> makeableWords(List<String> tiles, String wordOnBoard){
        Set<String> words = new HashSet<>();
        List<String> combos = allCombos(tiles);
        for (String w : combos){
            List<String> ws = a.getAnagrams(w + wordOnBoard);
            if (ws != null)
                words.addAll(ws);
        }
        return words;
    }

}
