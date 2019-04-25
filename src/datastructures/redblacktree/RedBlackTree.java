package datastructures.redblacktree;

public class RedBlackTree<T> {

    private int currentSize;
    private Node<T> root;

    /**
     * constructor for creating a red-black tree
     */
    public RedBlackTree() {
        currentSize = 0;
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
        T data;
        Node<T> leftChild;
        Node<T> rightChild;
        Node<T> parent;
        boolean isRed;

        private Node(T data) {
            this.data = data;
            leftChild = null;
            rightChild = null;
            isRed = true;
        }

        public int compareTo(Node<T> node) {
            return (((Comparable<T>) data).compareTo(node.data));
        }
    }

    /**
     * adds a new node to the tree at the correct null leaf node
     * re-balances the tree if adding the new node caused a violation
     *
     * @param data generic data being added to tree
     * @return true if object was added and false if not added
     */
    public boolean add(T data) {
        //tree is empty
        if (root == null) {
            root = new Node<>(data);
            currentSize++;
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
                    currentSize++;
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
                    currentSize++;
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
    public boolean find(T obj) {
        return find(root, obj);
    }

    /**
     * recursion method to traverse down tree if the data was not found
     *
     * @param current the current node being compared to
     * @param toFind  the data that is trying to be found
     * @return true if the data was found and false if the data was not found
     */
    public boolean find(Node<T> current, T toFind) {
        // data not found
        if (current == null) {
            return false;
        }
        // data found
        if (((Comparable<T>) current.data).compareTo(toFind) == 0) {
            return true;
        }
        // data to find is larger than current node data
        if (((Comparable<T>) current.data).compareTo(toFind) < 0) {
            // move to right subtree
            return find(current.rightChild, toFind);
        }
        // data to find is smaller than current node data, move to left subtree
        return find(current.leftChild, toFind);
    }

    /**
     * re-balances the tree if a violation has occurred
     * performs proper rotation or color-flip based on the aunt of the node that caused the violation
     * re-calls it self the the rotation/color-flip causes a new violation
     *
     * @param node   the node that caused the violation
     * @param parent the parent of the node that caused the violation
     */
    public void balance(Node<T> node, Node<T> parent) {
        Node<T> newTop = null;
        Node<T> grandpa = parent.parent;
        Node<T> aunt = auntOf(node);
        // aunt node is black or null... rotate
        if (aunt == null || !aunt.isRed) {
            if (node == parent.rightChild) {
                // error is in the grandfathers right child's right child
                if (parent == grandpa.rightChild) {
                    newTop = leftRotation(grandpa);
                    if (grandpa != root) {
                        // sets the new parent pointer
                        newTop.parent = grandpa.parent;
                        if (grandpa.parent.rightChild == grandpa) {
                            //sets new child pointer
                            grandpa.parent.rightChild = newTop;
                        } else if (grandpa.parent.leftChild == grandpa) {
                            //sets new child pointer
                            grandpa.parent.leftChild = newTop;
                        }
                        // grandpa was the root node
                    } else {
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
                    // error is in the grandfathers left child's right child
                } else if (parent == grandpa.leftChild) {
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
            } else if (node == parent.leftChild) {
                // error is in the grandfathers right child's left child
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
        // aunt node is red, color flip
        else {
            parent.isRed = false;
            aunt.isRed = false;
            grandpa.isRed = true;
            root.isRed = false;
            newTop = grandpa;
        }
        if (root != newTop) {
            // rotate/color flip caused a new violation
            if (newTop.isRed && newTop.parent.isRed) {
                balance(newTop, newTop.parent);
            }
        }
    }

    /**
     * default in-order method which prints out all the
     * nodes in the tree in order starting at the root
     */
    public void inOrder() {
        inOrder(root);
    }

    /**
     * recursive method which prints out all the nodes
     * in the tree in order starting at a particular node
     *
     * @param node the current node being looked at
     */
    public void inOrder(Node<T> node) {
        if (node != null) {
            inOrder(node.leftChild);
            System.out.println(node.data);
            inOrder(node.rightChild);
        }
    }

    /**
     * returns the current number of nodes/data stored in the tree
     *
     * @return the number of items stored in the tree
     */
    public int size() {
        return currentSize;
    }

    /**
     * empties all the data from the tree
     */
    public void clear() {
        root = null;
        currentSize = 0;
    }

    /**
     * recursive method that counts the number of edges in
     * the longest path in the tree from root to leaf node
     *
     * @return the height of longest path in the tree
     */
    public int height() {
        if (root == null) {
            return 0;
        } else {
            return height(root);
        }
    }

    /**
     * recursive method that counts the number of edges in
     * the longest path in the tree from root to leaf node
     *
     * @param node current node being looked at
     * @return 1 more than the height to the leaf node
     */
    public int height(Node<T> node) {
        int leftHeight = -1;
        int rightHeight = -1;
        if (node.leftChild != null) {
            leftHeight = height(node.leftChild);
        }
        if (node.rightChild != null) {
            rightHeight = height(node.rightChild);
        }
        if (leftHeight > rightHeight) {
            return leftHeight + 1;
        } else {
            return rightHeight + 1;
        }
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
     * @param node
     * @return the number of black nodes in the tree
     */
    public int countBlack(Node<T> node) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        if (node.leftChild != null) {
            count += countBlack(node.leftChild);
        }
        if (node.rightChild != null) {
            count += countBlack(node.rightChild);
        }
        if (!node.isRed) {
            count++;
        }
        return count;
    }

    /**
     * retrieves the aunt of the node being passed in
     *
     * @param node the node of which this method finds the aunt of
     * @return the aunt of the node that was passed in
     */
    public Node<T> auntOf(Node<T> node) {
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
    public Node<T> leftRotation(Node<T> node) {
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
    public Node<T> rightRotation(Node<T> node) {
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
     * Find the smallest item  the tree.
     *
     * @return the smallest item or null if empty.
     */
    public T findMin() {
        if (isEmpty())
            return null;

        Node<T> itr = root.leftChild;
        while (itr.leftChild != null)
            itr = itr.leftChild;

        return itr.data;
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item or null if empty.
     */
    public T findMax() {
        if (isEmpty())
            return null;

        Node<T> itr = root.rightChild;
        while (itr.rightChild != null)
            itr = itr.rightChild;

        return itr.data;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root.rightChild == null;
    }

    /**
     * Print all items.
     */
    public void printElements() {
        printElements(root.rightChild);
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param node the node that roots the tree.
     */
    private void printElements(Node<T> node) {
        if (node != null) {
            printElements(node.leftChild);
            System.out.println(node.data);
            printElements(node.rightChild);
        }
    }


    /**
     * Print all items in tree form.
     */
    public void printTree() {
        printTree(this.root, "", true);
    }


    /**
     * Internal method to print a subtree in sorted order.
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
            String color = node.isRed ? "RED" : "BLACK";
            System.out.println(node.data + "(" + color + ")");
            printTree(node.leftChild, indent, false);
            printTree(node.rightChild, indent, true);
        }
    }
}