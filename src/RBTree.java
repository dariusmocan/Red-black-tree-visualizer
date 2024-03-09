public class RBTree {
    public static final int RED = 0;
    public static final int BLACK = 1;
    public static final int DOUBLEBLACK = 2;
    private Node root;

    Node insert(Node node, int x) {
        if (node.key <= x && node.right == null) {
            return node.right = new Node(x, node);
        }

        if (node.key > x && node.left == null) {
            return node.left = new Node(x, node);
        }

        if (node.key <= x)
            return insert(node.right, x);
        else
            return insert(node.left, x);
    }

    public RBTree() {
        this.root = null;
    }

    RBTree(Node node) {
        this.root = node;
    }

    public Node getRoot() {
        return root;
    }

    Node insert(int x) {
        if (root == null)
            return root = new Node(x);
        return insert(root, x);
    }

    boolean consecutiveRedNodes(Node node) {
        return node.parent != null && (node.parent.color == RED);
    }

    boolean isLeftChild(Node node) {
        if (node.parent == null)
            return false;
        return node.parent.left == node;
    }

    boolean uncleIsRed(Node node) {
        if (node.parent == null || node.parent.parent == null)
            return false;
        Node par = node.parent;
        if (isLeftChild(par))
            return (par.parent.right != null && (par.parent.right.color == RED) &&
                    par.parent.left.color == RED);
        else
            return par.parent.left != null && (par.parent.left.color == RED) &&
                    (par.parent.right.color == RED);
    }

    boolean isRoot(Node node) {
        return (node.parent == null);
    }

    void changeColorInsertion(Node node) {
        if (node.parent.parent.right != null)
            node.parent.parent.right.color = BLACK;

        if (node.parent.parent.left != null)
            node.parent.parent.left.color = BLACK;

        if (!isRoot(node.parent.parent)) node.parent.parent.color = RED;
    }

    void rightRotate(Node node) {
        Node temp = node.left;
        temp.parent = node.parent;
        if (isRoot(node))
            this.root = temp;
        else {
            if (isLeftChild(node)) node.parent.left = temp;
            else node.parent.right = temp;
        }
        node.parent = temp;
        if (temp.right != null) {
            node.left = temp.right;
            temp.right.parent = node;
        } else node.left = null;
        temp.right = node;
    }

    void LeftRotate(Node node) {
        Node temp = node.right;

        temp.parent = node.parent;
        if (isRoot(node))
            this.root = temp;
        else {
            if (isLeftChild(node)) node.parent.left = temp;
            else node.parent.right = temp;
        }

        node.parent = temp;
        if (temp.left != null) {
            node.right = temp.left;
            temp.left.parent = node;
        } else node.right = null;

        temp.left = node;
    }

    public void Rotation(Node node) {

        Node newParent = node.parent;

        if (isLeftChild(node.parent)) {
            if (!isLeftChild(node)) {
                newParent = node;
                node = node.parent;
                LeftRotate(node);
            }
            rightRotate(node.parent.parent);
        }
        else {
            if (isLeftChild(node)) {
                newParent = node;
                node = node.parent;
                rightRotate(node);
            }
            LeftRotate(node.parent.parent);
        }

        newParent.color = BLACK;

        if (newParent.right != null) newParent.right.color = RED;
        if (newParent.left != null) newParent.left.color = RED;

        if (isRoot(newParent))
            root = newParent;

        root.color = BLACK;
    }

    public void finalInsertion(int x) {
        Node node = insert(x);
        if (isRoot(node)) {
            node.color = BLACK;
            return;
        }
        while (node != null && consecutiveRedNodes(node)) {
            if (uncleIsRed(node)) {
                changeColorInsertion(node);
            } else {
                Rotation(node);
            }
            if (!isRoot(node))
                node = node.parent.parent;
        }
    }

    boolean isLeaf(Node node) {
        return (node.left == null && node.right == null);
    }

    Node getSuccessor(Node node) {
        Node curr = node.right;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    Node getPredecessor(Node node) {
        Node curr = node.left;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr;
    }

    Node Search(int x) {
        Node curr = root;
        while (curr.key != x) {
            if (curr.key <= x)
                curr = curr.right;
            else curr = curr.left;
        }
        return curr;
    }

    boolean IsBlack(Node node) {
        return (node == null || node.color == BLACK);
    }

    Node getSibling(Node node) {
        if (isRoot(node))
            return root;
        if (isLeftChild(node)) {
            return node.parent.right;
        }
        return node.parent.left;
    }

    Node swaping(Node node) {
        Node pre = null;
        while (!isLeaf(node)) {
            if (node.left != null && node.right != null) pre = getPredecessor(node);

            else if (node.left != null) {
                pre = node.left;
            }

            else if (node.right != null) {
                pre = node.right;
            }

            int x = node.key;
            node.key = pre.key;
            pre.key = x;


            node = pre;
        }
        return node;
    }

    void deleteNode(Node ToBeDeleted) {
        if (isLeftChild(ToBeDeleted))
            ToBeDeleted.parent.left = null;
        else ToBeDeleted.parent.right = null;
    }

    void FixDoubleBlack(Node ToBeDeleted) {

        if (ToBeDeleted == root || ToBeDeleted.color != DOUBLEBLACK) {
            root.color = BLACK;
            return;
        }

        Node sibling = getSibling(ToBeDeleted);
        if (IsBlack(sibling)) {
            if (IsBlack(sibling.left) && IsBlack(sibling.right)) {

                ToBeDeleted.color = BLACK;
                if (sibling != null)
                    sibling.color = RED;
                if (!IsBlack(ToBeDeleted.parent)) {
                    ToBeDeleted.parent.color = BLACK;
                }
                else {
                    ToBeDeleted.parent.color = DOUBLEBLACK;
                }
                FixDoubleBlack(ToBeDeleted.parent);
            } else {
                if (!isLeftChild(sibling)) {
                    if (!IsBlack(sibling.right)) {
                        sibling.right.color = sibling.color;
                        sibling.color = sibling.parent.color;
                        LeftRotate(ToBeDeleted.parent);
                    } else {
                        sibling.left.color = sibling.parent.color;
                        rightRotate(sibling);
                        LeftRotate(ToBeDeleted.parent);
                    }
                } else {
                    if (!IsBlack(sibling.left)) {
                        sibling.left.color = sibling.color;
                        sibling.color = sibling.parent.color;
                        rightRotate(ToBeDeleted.parent);

                    } else {
                        sibling.right.color = sibling.parent.color;
                        LeftRotate(sibling);
                        rightRotate(ToBeDeleted.parent);
                    }
                }
            }
            ToBeDeleted.parent.color = BLACK;
        } else {
            ToBeDeleted.parent.color = RED;
            sibling.color = BLACK;

            if (isLeftChild(ToBeDeleted)) {
                LeftRotate(ToBeDeleted.parent);
            }
            else rightRotate(ToBeDeleted.parent);

            FixDoubleBlack(ToBeDeleted);
        }
    }

    public void Delete(int x) {
        Node temp = Search(x);
        Node ToBeDeleted = swaping(temp);

        if (isRoot(ToBeDeleted)) {
            root = null;
            return;
        }
        if (ToBeDeleted.color == RED) {
            deleteNode(ToBeDeleted);
        } else {
            ToBeDeleted.color = DOUBLEBLACK;
            FixDoubleBlack(ToBeDeleted);
            deleteNode(ToBeDeleted);
        }
    }
    public void clear() {
        while (root != null) {
            Delete(root.key);
        }
    }
    public void reset(Node node) {
        if (node == null) return;
        node.nodeID = -1;
        reset(node.left);
        reset(node.right);
    }

    private Node Search(Node root, int key) {
        Node curr = root;

        while (curr != null) {
            if (key == curr.key) {
                return curr;
            } else if (key < curr.key) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        return null;
    }
    public boolean search(int key) {
        return search(root, key);
    }
    private boolean search(Node root, int key) {
        if (root == null) {
            return false;
        }

        if (key == root.key) {
            return true;
        } else if (key < root.key) {
            return search(root.left, key);
        } else {
            return search(root.right, key);
        }
    }



    private void inorderTraversal(Node node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print(node.key + " ");
            inorderTraversal(node.right);
        }
    }

    public void inorderTraversal() {
        System.out.print("Inorder Traversal: ");
        inorderTraversal(root);
        System.out.println();
    }

    private void preorderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preorderTraversal(node.left);
            preorderTraversal(node.right);
        }
    }

    public void preorderTraversal() {
        System.out.print("Preorder Traversal: ");
        preorderTraversal(root);
        System.out.println();
    }

    private void postorderTraversal(Node node) {
        if (node != null) {
            postorderTraversal(node.left);
            postorderTraversal(node.right);
            System.out.print(node.key + " ");
        }
    }

    public void postorderTraversal() {
        System.out.print("Postorder Traversal: ");
        postorderTraversal(root);
        System.out.println();
    }
}