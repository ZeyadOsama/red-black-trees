package datastructures.redblacktree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RedBlackTree<T extends Comparable> {

    private int size;
    private Node<T> root;

    /**
     * constructor for creating a red-black tree
     */
    public RedBlackTree() {
        size = 0;
        root = null;
    }

    /**
     * Creates a new node
     * sets the data to the generic parameter passed in
     * defaults both left and right children to null
     *
     * @param <T> generic type being stored in the tree
     */
    private class Node<T> implements Comparable<Node<T>> {
        private T data;
        private Node<T> leftChild;
        private Node<T> rightChild;
        private Node<T> parent;
        private boolean isRed;

        private Node(T data) {
            this.data = data;
            leftChild = null;
            rightChild = null;
            isRed = true;
        }

        public int compareTo(@NotNull Node<T> node) {
            return (((Comparable<T>) data).compareTo(node.data));
        }
    }

    /**
     * Adds a new node to the tree at the correct null leaf node
     * re-balances the tree if adding the new node caused a violation
     *
     * @param data generic data being added to tree
     * @return true if object was added and false if not added
     */
    public boolean add(T data) {
        // tree is empty
        if (root == null) {
            root = new Node<>(data);
            size++;
            root.isRed = false;
            return true;
        }
        Node<T> tempNode = root;
        while (true) {
            // data is larger than tempNode.data
            if (((Comparable<T>) data).compareTo(tempNode.data) >= 0) {
                // reached a null subtree
                if (tempNode.rightChild == null) {
                    Node<T> newNode = new Node<>(data);
                    // set parent pointer
                    newNode.parent = tempNode;
                    tempNode.rightChild = newNode;
                    size++;
                    // consecutive red node violation
                    if (newNode.isRed && newNode.parent.isRed) {
                        balance(newNode, newNode.parent);
                    }
                    return true;
                }
                // traverse down tree
                else
                    tempNode = tempNode.rightChild;
            }
            // data is smaller than tempNode.data
            else {
                // reached a null subtree
                if (tempNode.leftChild == null) {
                    Node<T> newNode = new Node<>(data);
                    // set parent pointer
                    newNode.parent = tempNode;
                    tempNode.leftChild = newNode;
                    size++;
                    // consecutive red node violation
                    if (newNode.isRed && newNode.parent.isRed) {
                        balance(newNode, newNode.parent);
                    }
                    return true;
                } else {
                    // traverse down tree
                    tempNode = tempNode.leftChild;
                }
            }
        }
    }

    /**
     * recursion method to traverse down tree, starting at the root
     * checks to see if the tree contains a certain piece of data
     *
     * @param obj the data that is trying to be found
     * @return true if the data was found and false if the data was not found
     */
    public boolean contains(T obj) {
        return contains(root, obj);
    }

    /**
     * recursion method to traverse down tree if the data was not found
     *
     * @param current the current node being compared to
     * @param toFind  the data that is trying to be found
     * @return true if the data was found and false if the data was not found
     */
    private boolean contains(Node<T> current, T toFind) {
        // data not found
        if (current == null) {
            return false;
        }
        // data found
        if (((Comparable<T>) current.data).compareTo(toFind) == 0) {
            return true;
        }
        // data to contains is larger than current node data
        if (((Comparable<T>) current.data).compareTo(toFind) < 0) {
            // move to right subtree
            return contains(current.rightChild, toFind);
        }
        // data to contains is smaller than current node data, move to left subtree
        return contains(current.leftChild, toFind);
    }

    /**
     * re-balances the tree if a violation has occurred
     * performs proper rotation or color-flip based on the aunt of the node that caused the violation
     * re-calls it self the the rotation/color-flip causes a new violation
     *
     * @param node   the node that caused the violation
     * @param parent the parent of the node that caused the violation
     */
    private void balance(@NotNull Node<T> node, @NotNull Node<T> parent) {
        Node<T> newTop = null;
        Node<T> grandpa = parent.parent;
        Node<T> aunt = auntOf(node);
        // aunt node is black or null
        // rotate
        if (aunt == null || !aunt.isRed) {
            if (node == parent.rightChild) {
                // error is in the grandfathers right child's right child
                if (parent == grandpa.rightChild) {
                    newTop = leftRotation(grandpa);
                    if (grandpa != root) {
                        // sets the new parent pointer
                        newTop.parent = grandpa.parent;
                        if (grandpa.parent.rightChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.rightChild = newTop;
                        } else if (grandpa.parent.leftChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.leftChild = newTop;
                        }
                    }
                    // grandpa was the root node
                    else {
                        // reset root node
                        root = newTop;
                        root.parent = null;
                    }
                    // sets new parent pointer
                    newTop.leftChild.parent = newTop;
                    newTop.isRed = false;
                    newTop.leftChild.isRed = true;
                    newTop.rightChild.isRed = true;
                    root.isRed = false;
                }
                // error is in the grandfathers left child's right child
                else if (parent == grandpa.leftChild) {
                    leftRotation(parent);
                    grandpa.leftChild = node;
                    newTop = rightRotation(grandpa);
                    if (grandpa != root) {
                        // sets new parent pointer
                        newTop.parent = grandpa.parent;
                        if (grandpa.parent.rightChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.rightChild = newTop;
                        } else if (grandpa.parent.leftChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.leftChild = newTop;
                        }
                    }
                    // grandpa was root node
                    else {
                        // resets root node
                        root = newTop;
                        root.parent = null;
                    }
                    // sets new parent pointer
                    newTop.leftChild.parent = newTop;
                    // sets new parent pointer
                    newTop.rightChild.parent = newTop;
                    newTop.isRed = false;
                    newTop.leftChild.isRed = true;
                    newTop.rightChild.isRed = true;
                    root.isRed = false;
                }
            }
            // error is in the grandfathers right child's left child
            else if (node == parent.leftChild) {
                if (parent == parent.parent.rightChild) {
                    rightRotation(parent);
                    grandpa.rightChild = node;
                    newTop = leftRotation(grandpa);
                    if (grandpa != root) {
                        // sets new parent pointer
                        newTop.parent = grandpa.parent;
                        if (grandpa.parent.rightChild == parent.parent) {
                            // sets new child pointer
                            grandpa.parent.rightChild = newTop;
                        } else if (grandpa.parent.leftChild == parent.parent) {
                            // sets new child pointer
                            grandpa.parent.leftChild = newTop;
                        }
                    }
                    // grandpa was root node
                    else {
                        // resets root node
                        root = newTop;
                        root.parent = null;
                    }
                    // sets new parent pointer
                    newTop.leftChild.parent = newTop;
                    // sets new parent pointer
                    newTop.rightChild.parent = newTop;
                    newTop.isRed = false;
                    newTop.leftChild.isRed = true;
                    newTop.rightChild.isRed = true;
                    root.isRed = false;
                    // error is in the (grandfathers) left child's left child
                } else if (parent == grandpa.leftChild) {
                    newTop = rightRotation(grandpa);
                    if (grandpa != root) {
                        // sets new parent pointer
                        newTop.parent = grandpa.parent;
                        if (grandpa.parent.rightChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.rightChild = newTop;
                        } else if (grandpa.parent.leftChild == grandpa) {
                            // sets new child pointer
                            grandpa.parent.leftChild = newTop;
                        }
                    }
                    // grandpa was root node
                    else {
                        // resets the root node
                        root = newTop;
                        root.parent = null;
                    }
                    // sets new parent pointer
                    newTop.rightChild.parent = newTop;
                    newTop.isRed = false;
                    newTop.leftChild.isRed = true;
                    newTop.rightChild.isRed = true;
                    root.isRed = false;
                }
            }
        }
        // aunt node is red
        // color flip
        else {
            parent.isRed = false;
            aunt.isRed = false;
            grandpa.isRed = true;
            root.isRed = false;
            newTop = grandpa;
        }
        if (root != newTop)
            // rotation or color flip caused a new violation
            if (Objects.requireNonNull(newTop).isRed && newTop.parent.isRed)
                balance(newTop, newTop.parent);
    }

    /**
     * returns the current number of nodes/data stored in the tree
     *
     * @return the number of items stored in the tree
     */
    public int size() {
        return size;
    }

    /**
     * empties all the data from the tree
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * recursive method that counts the number of edges in
     * the longest path in the tree from root to leaf node
     * starting by root node
     *
     * @return the height of longest path in the tree
     */
    public int height() {
        return root == null ? 0 : height(root);
    }

    /**
     * recursive method that counts the number of edges in
     * the longest path in the tree from root to leaf node
     *
     * @param node current node being looked at
     * @return 1 more than the height to the leaf node
     */
    private int height(Node<T> node) {
        int leftHeight = -1;
        int rightHeight = -1;

        if (node.leftChild != null)
            leftHeight = height(node.leftChild);
        if (node.rightChild != null)
            rightHeight = height(node.rightChild);

        return (leftHeight > rightHeight) ? leftHeight + 1 : rightHeight + 1;
    }

    /**
     * counts the number of black nodes currently in the tree
     * starting at the root node
     *
     * @return the number of black nodes in the tree
     */
    public int countBlack() {
        return countBlack(root);
    }

    /**
     * recursively counts the number of
     * black nodes currently in the tree
     *
     * @param node current node being looked at
     * @return the number of black nodes in the tree
     */
    private int countBlack(Node<T> node) {
        if (node == null)
            return 0;

        int count = 0;
        if (node.leftChild != null)
            count += countBlack(node.leftChild);
        if (node.rightChild != null)
            count += countBlack(node.rightChild);

        if (!node.isRed)
            count++;
        return count;
    }

    /**
     * retrieves the aunt of the node being passed in
     *
     * @param node the node of which this method finds the aunt of
     * @return the aunt of the node that was passed in
     */
    @Nullable
    private Node<T> auntOf(@NotNull Node<T> node) {
        if (node.parent.parent.rightChild == node.parent) {
            return node.parent.parent.leftChild;
        }
        if (node.parent.parent.leftChild == node.parent) {
            return node.parent.parent.rightChild;
        }
        return null;
    }

    /**
     * rotates the node being passed in
     * to the left of its right child
     *
     * @param node the node being rotated
     * @return returns the new top node after the rotation
     */
    private Node<T> leftRotation(@NotNull Node<T> node) {
        Node<T> newTop = node.rightChild;
        node.rightChild = newTop.leftChild;
        if (newTop.leftChild != null) {
            // sets new parent pointer
            newTop.leftChild.parent = node;
        }
        newTop.leftChild = node;
        return newTop;
    }

    /**
     * rotates the node being passed in
     * to the right of its left child
     *
     * @param node the node being rotated
     * @return returns the new top node after the rotation
     */
    private Node<T> rightRotation(@NotNull Node<T> node) {
        Node<T> newTop = node.leftChild;
        node.leftChild = newTop.rightChild;
        if (newTop.rightChild != null) {
            // sets new parent pointer
            newTop.rightChild.parent = node;
        }
        newTop.rightChild = node;
        return newTop;
    }

    /**
     * test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root.rightChild == null;
    }

    /**
     * Removes a new node in the tree
     * re-balances the tree if adding the new node caused a violation
     *
     * @param data generic data being removed from tree
     * @return true if object was added and false if not added
     */
    public boolean remove(T data) {
        return (size-- != 0) ? remove(this.root, data) : false;
    }

    /**
     * Internal method to remove a node in a tree.
     *
     * @param node the node that roots the tree.
     * @param data generic data being removed from tree
     */
    private boolean remove(Node<T> node, T data) {
        // find the node containing data
        Node<T> z = null;
        Node<T> x;
        Node<T> y;
        while (node != null) {
            if (data.compareTo(node.data) == 0)
                z = node;
            if (data.compareTo(node.data) >= 0)
                node = node.rightChild;
            else
                node = node.leftChild;
        }
        if (z == null) {
            System.out.println(ConsoleColors.RED + "Error: Couldn't find key in tree" + ConsoleColors.RESET);
            return false;
        }
        y = z;
        boolean yOriginalColor = y.isRed;
        if (z.leftChild == null) {
            x = z.rightChild;
            colorTransplant(z, z.rightChild);
        } else if (z.rightChild == null) {
            x = z.leftChild;
            colorTransplant(z, z.leftChild);
        } else {
            y = findMinNode(z.rightChild);
            yOriginalColor = y.isRed;
            x = y.rightChild;
            if (y.parent == z) {
                x.parent = y;
            } else {
                colorTransplant(y, y.rightChild);
                y.rightChild = z.rightChild;
                y.rightChild.parent = y;
            }
            colorTransplant(z, y);
            y.leftChild = z.leftChild;
            y.leftChild.parent = y;
            y.isRed = z.isRed;
        }
        if (!yOriginalColor && x != null)
            fixRemove(x);

        return true;
    }

    /**
     * fix the tree modified by the remove operation
     *
     * @param node to be fixed
     */
    private void fixRemove(@NotNull Node<T> node) {
        Node<T> s;
        while (node != root && !node.isRed) {
            if (node == node.parent.leftChild) {
                s = node.parent.rightChild;
                if (s.isRed) {
                    // case 3.1
                    s.isRed = false;
                    node.parent.isRed = true;
                    leftRotation(node.parent);
                    s = node.parent.rightChild;
                }

                if (!s.leftChild.isRed && !s.rightChild.isRed) {
                    // case 3.2
                    s.isRed = true;
                    node = node.parent;
                } else {
                    if (!s.rightChild.isRed) {
                        // case 3.3
                        s.leftChild.isRed = false;
                        s.isRed = true;
                        rightRotation(s);
                        s = node.parent.rightChild;
                    }
                    // case 3.4
                    s.isRed = node.parent.isRed;
                    node.parent.isRed = false;
                    s.rightChild.isRed = false;
                    leftRotation(node.parent);
                    node = root;
                }
            } else {
                s = node.parent.leftChild;
                if (s.isRed) {
                    // case 3.1
                    s.isRed = false;
                    node.parent.isRed = true;
                    rightRotation(node.parent);
                    s = node.parent.leftChild;
                }

                if (!s.rightChild.isRed) {
                    // case 3.2
                    s.isRed = true;
                    node = node.parent;
                } else {
                    if (!s.leftChild.isRed) {
                        // case 3.3
                        s.rightChild.isRed = false;
                        s.isRed = true;
                        leftRotation(s);
                        s = node.parent.leftChild;
                    }

                    // case 3.4
                    s.isRed = node.parent.isRed;
                    node.parent.isRed = false;
                    s.leftChild.isRed = false;
                    rightRotation(node.parent);
                    node = root;
                }
            }
        }
        node.isRed = false;
    }

    private void colorTransplant(Node<T> firstNode, Node<T> secondNode) {
        if (firstNode.parent == null)
            root = secondNode;
        else if (firstNode == firstNode.parent.leftChild)
            firstNode.parent.leftChild = secondNode;
        else
            firstNode.parent.rightChild = secondNode;
        if (secondNode != null)
            secondNode.parent = firstNode.parent;
    }

    /**
     * Contains the node with the min key
     *
     * @param node to get minimum from
     * @return minimum node
     * @see Node
     */
    private Node<T> findMinNode(@NotNull Node<T> node) {
        while (node.leftChild != null)
            node = node.leftChild;
        return node.parent.parent;
    }

    /**
     * Contains the node with the min key
     *
     * @param node to get minimum from
     * @return minimum node
     * @see Node
     */
    private Node<T> findMaxNode(@NotNull Node<T> node) {
        while (node.rightChild != null)
            node = node.rightChild;
        return node.parent.parent;
    }

    /**
     * Find the smallest item  the tree.
     *
     * @return the smallest item or null if empty.
     */
    @Nullable
    public T findMin() {
        if (isEmpty())
            return null;

        Node<T> node = root.leftChild;
        while (node.leftChild != null)
            node = node.leftChild;
        return node.data;
    }

    /**
     * find the largest item in the tree.
     *
     * @return the largest item or null if empty.
     */
    @Nullable
    public T findMax() {
        if (isEmpty())
            return null;

        Node<T> node = root.rightChild;
        while (node.rightChild != null)
            node = node.rightChild;
        return node.data;
    }

    /**
     * default method which prints out all the
     * nodes in the tree in order starting at the root
     */
    public void printElements() {
        printElements(root);
    }

    /**
     * internal recursive method which prints out all the nodes
     * in the tree in order starting at a particular node
     *
     * @param node the current node being looked at
     */
    private void printElements(Node<T> node) {
        if (node != null) {
            printElements(node.leftChild);
            System.out.println(node.data);
            printElements(node.rightChild);
        }
    }

    /**
     * print all items in tree form.
     */
    public void printTree() {
        printTree(this.root, "", true);
    }


    /**
     * internal method to print a subtree in sorted order.
     *
     * @param node the node that roots the tree.
     */
    private void printTree(Node<T> node, String indent, boolean last) {
        if (node != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }
            System.out.println(node.data + (node.isRed ? "(RED)" : "(BLACK)"));
            printTree(node.leftChild, indent, false);
            printTree(node.rightChild, indent, true);
        }
    }

    /**
     * Utility class for console colors printing
     */
    private final class ConsoleColors {
        private static final String RESET = "\033[0m";
        private static final String RED = "\u001B[31m";
        private static final String GREEN = "\033[0;32m";
    }
}