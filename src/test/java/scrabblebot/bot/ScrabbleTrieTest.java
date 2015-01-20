package scrabblebot.bot;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Test;
import scrabblebot.TestHelper;
import scrabblebot.data.Trie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ScrabbleTrieTest extends TestHelper {

    @Test
    public void testScrabbleTrie(){
        Trie t = ScrabbleTrie.INSTANCE.getTrie();
        assert(t.containsPrefix("CRAT"));
        //p(t.listWords(t.getNode("SWEE"), new ArrayList<>()));
    }

    @Test
    public void testKryo() throws FileNotFoundException {
        Kryo kryo = new Kryo();
        // ...
        Output output = new Output(new FileOutputStream("src/main/resources/trie.bin"));
        Trie t = ScrabbleTrie.INSTANCE.getTrie();
        kryo.writeObject(output, t);
        output.close();
        // ...
    }

    @Test
    public void readFromKryo() throws FileNotFoundException {
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("src/main/resources/trie.bin"));
        Trie t = kryo.readObject(input, Trie.class);
        input.close();
    }


}