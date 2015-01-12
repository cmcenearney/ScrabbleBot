package scrabblebot.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public enum Anagramsaurus {

    INSTANCE;

    Anagramsaurus(){
        initialize();
    }

    public List<String> getAnagrams(String s){
        return anagramMap.get(sortString(s.toUpperCase()));
    }

    private HashMap<String, List<String>> anagramMap = new HashMap<>();

    private void initialize(){
        File file = new File("src/main/resources/twl06.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() > 0) {
                    addToMap(line.toUpperCase());
                }
            }
        }
        catch (IOException e){
            System.out.println("problem reading dictionary file:" + e);
        }
    }

    private void addToMap(String s){
        String a = sortString(s);
        if(anagramMap.containsKey(a)){
            anagramMap.get(a).add(s);
        } else {
            List<String> list = new ArrayList<>();
            list.add(s);
            anagramMap.put(a,list);
        }
    }

    public static String sortString(String s){
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }

}
