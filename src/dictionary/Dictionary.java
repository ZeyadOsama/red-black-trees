package dictionary;

import datastructures.redblacktree.RedBlackTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class Dictionary {

    public static RedBlackTree<String> load() {
        RedBlackTree<String> tree = new RedBlackTree<>();
        BufferedReader reader;
        URL path = Dictionary.class.getResource("words.txt");
        File file = new File(path.getFile());
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                tree.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tree;
    }
}
