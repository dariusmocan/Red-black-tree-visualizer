public class Node {
    public static final int RED = 0;
    public static final int BLACK = 1;
    public static final int DOUBLEBLACK = 2;
    Node left, right, parent;
    int key;
    int color;
    int initx = 0, nodeID = -1;


    Node() {
        left = right = parent = null;
        this.color = RED;
    }
    Node(int key) {
        left = right = parent = null;
        this.key = key;
        this.color = RED;
    }
    Node(int key, Node parent) {
        left = right = null;
        this.parent = parent;
        this.key = key;
        this.color = RED;
    }

    public int getKey() {
        return key;
    }

    public int getColor() {
        return color;
    }

    public int getNodeID() {
        return nodeID;
    }

    public int getInitx() {
        return initx;
    }

    public Node getLeft() {
        return left;
    }

    public Node getParent() {
        return parent;
    }

    public Node getRight() {
        return right;
    }

    public void setInitx(int initx) {
        this.initx = initx;
    }
}