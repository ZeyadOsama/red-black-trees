package main;

import datastructures.redblacktree.RedBlackTree;
import dictionary.Dictionary;

public class Main {

    public static void main(String[] args) {
        RedBlackTree<String> tree = Dictionary.load();
        System.out.println(tree.contains("house"));
        System.out.println(tree.size());
        System.out.println(tree.findMin());
        System.out.println(tree.findMax());
        System.out.println(tree.height());
    }
}


