package scrabblebot.combinations;

import java.util.*;

public class BinomialTreeCombinations {

    private final BTree root;
    private final int n;

    public class BTree {
        HashMap<Integer, BTree> children;
        BTree (){this.children = new HashMap<>();}
    }

    public BinomialTreeCombinations(int n){
        this.n = n;
        root = new BTree();
        root.children.put(0, new BTree());
        add(n - 1);
    }

    public Set<List<Integer>> getKIndices(int k){
        return targetedDLS(k);
    }

    public Set<List<Integer>> getAllPaths(){
        return dfs(root);
    }

    private void add(int n){
        for(int i = 1; i <= n; i++){
            if (root.children.containsKey(i))
                continue;
            root.children.put(i, new BTree());
            BTree node = root.children.get(i);
            for (int j = 0; j < i; j++){
                node.children.put(j, root.children.get(j));
            }
        }
    }

    private Set<List<Integer>> dfs(BTree node){
        Set<List<Integer>> results = new HashSet<>();
        List<Integer> path = new ArrayList<>();
        dfs(node, path, results);
        return results;
    }

    private void  dfs(BTree node, List<Integer> path, Set<List<Integer>> results){
        if (node.children.isEmpty()) {
            results.add(path);
            return;
        }
        for (Integer i : node.children.keySet()) {
            BTree child = node.children.get(i);
            ArrayList<Integer> pathC = new ArrayList<>(path);
            pathC.add(i);
            dfs(child, pathC, results);
        }
    }

    private Set<List<Integer>> targetedDLS(int limit){
        Set<List<Integer>> results = new HashSet<>();
        for (int i = n-1; i >= limit - 1; i--){
            BTree node = root.children.get(i);
            List<Integer> path = new ArrayList<>();
            path.add(i);
            targetedDLS(node, path, results, limit-1);
        }
        return results;
    }

    private void targetedDLS(BTree node, List<Integer> path, Set<List<Integer>> results, int limit){
        if (limit == 0) {
            results.add(path);
            return;
        }
        for (Integer i : node.children.keySet()) {
            BTree child = node.children.get(i);
            ArrayList<Integer> pathC = new ArrayList<>(path);
            pathC.add(0,i);
            targetedDLS(child, pathC, results, limit - 1);
        }
    }

    private Set<List<Integer>> dls(int limit){
        Set<List<Integer>> results = new HashSet<>();
        List<Integer> path = new ArrayList<>();
        dls(root, path, results, limit);
        return results;
    }

    private void  dls(BTree node, List<Integer> path, Set<List<Integer>> results, int limit){
        if (limit == 0) {
            results.add(path);
            return;
        }
        for (Integer i : node.children.keySet()) {
            BTree child = node.children.get(i);
            ArrayList<Integer> pathC = new ArrayList<>(path);
            pathC.add(0,i);
            dls(child, pathC, results, limit - 1);
        }
    }

}
