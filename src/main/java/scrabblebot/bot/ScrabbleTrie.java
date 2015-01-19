package scrabblebot.bot;

import scrabblebot.data.TrieNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public enum ScrabbleTrie {

    INSTANCE;

    public TrieNode getRoot() {
        return root;
    }

    private TrieNode root = new TrieNode("");

    private void initialize(){
        File file = new File("src/main/resources/twl06.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() > 0) {
                    this.addWord(line.toUpperCase());
                }
            }
        }
        catch (IOException e){
            System.out.println("problem reading dictionary file:" + e);
        }
    }

    ScrabbleTrie (){initialize();}

    public void addWord(String word){
        TrieNode currentNode = root;
        char chars[] = word.toCharArray();
        for (int i = 0; i < chars.length; i++){
            if (!currentNode.containsChildValue(chars[i])){
                currentNode.addChild(chars[i], new TrieNode(currentNode.getValue() + chars[i]));
            }
            currentNode = currentNode.getChild(chars[i]);
        }
        currentNode.setIsWord(true);
    }

    public TrieNode getNode(String word){
        TrieNode currentNode = root;
        char chars[] = word.toCharArray();
        for (int i = 0; i < chars.length; i++){
            if (currentNode.containsChildValue(chars[i])){
                currentNode = currentNode.getChild(chars[i]);
            }
            else {
                return null;
            }
        }
        return currentNode;
    }

    public boolean containsPrefix(String word) {
        return (getNode(word) != null && !getNode(word).isWord());
    }

    public boolean containsWord(String word) {
        return (getNode(word) != null && getNode(word).isWord());
    }

    public ArrayList<String> listWords(){
        ArrayList<String> words = new ArrayList<String>();
        return listWords(root, words);
    }
    
    public ArrayList<String> listWords(TrieNode node, ArrayList<String> words){
        TrieNode currentNode = node;
        if (currentNode.isWord()) {
            words.add(currentNode.getValue());
        }
        if (currentNode.hasChildren()){
            for (TrieNode n : currentNode.getChildren() ) {
                listWords(n, words);
            }
        }
        return words;
    }

    List<TrieNode> breadthFirstTraversal(TrieNode node){
        List<TrieNode> results = new LinkedList<TrieNode>();
        Queue<TrieNode> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            TrieNode n = queue.remove();
            n.getChildren().stream().forEach(i -> queue.add(i));
            results.add(n);
        }
        return results;
    }

}
