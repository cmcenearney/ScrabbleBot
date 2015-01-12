package scrabblebot.data;

import java.util.HashMap;

public interface SuffixTreeNode {

    HashMap<String, SuffixTreeNode> getChildren();

    SuffixTreeNode getChild(String s);

}
