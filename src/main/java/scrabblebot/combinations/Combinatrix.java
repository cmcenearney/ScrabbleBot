package scrabblebot.combinations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Combinatrix<T> {

    private final int n;
    private final int k;
    private final List<T> list;
    private BinomialTreeCombinations tree;

    public Combinatrix(int n, int k, List<T> list) {
        this.n = n;
        this.k = k;
        this.list = list;
        tree = new BinomialTreeCombinations(n);
    }

    public Set<List<T>> hurtMe(){
        return tree.getKIndices(k).stream()
                .map(set -> set.stream()
                        .map(i -> list.get(i))
                        .collect(Collectors.toList()))
                .collect(Collectors.toSet());
    }

    public Set<List<Integer>> safetyWord(){
        return tree.getKIndices(k);
    }

}