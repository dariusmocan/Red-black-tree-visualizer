import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class TreeAnim {
    private final int latime = 1920;

    private int nr = -1;

    private void setNodeID(Node node) {
        if (node.nodeID == -1) node.nodeID = ++nr;
    }

    public void bfs(Node node, Graph frame) {

        Queue<Node> queue = new LinkedList();
        queue.add(node);

        Map<Node, Boolean> Drawn = new HashMap();

        int lvl = 100, prev = latime / 3;
        int relative = latime / 6;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            while (sz > 0) {
                Node currNode = queue.poll();
                if (!Drawn.containsKey(currNode)) {
                    currNode.initx = prev;
                    frame.addNode(currNode.key, currNode.color, prev, lvl);
                    Drawn.put(currNode, true);
                }
                setNodeID(currNode);

                Node child = currNode.left;
                if (child != null) {
                    frame.addNode(child.key, child.color, child.parent.initx - relative, lvl + 100);
                    child.initx = child.parent.initx - relative;
                    Drawn.put(child, true);
                    setNodeID(child);
                    queue.add(child);
                    frame.addEdge(child.nodeID, child.parent.nodeID);
                }
                child = currNode.right;
                if (child != null) {
                    setNodeID(child);
                    frame.addNode(child.key, child.color, child.parent.initx + relative, lvl + 100);
                    child.initx = child.parent.initx + relative;
                    Drawn.put(child, true);
                    queue.add(child);
                    frame.addEdge(child.nodeID, child.parent.nodeID);
                }
                sz--;
            }
            relative /= 2;
            lvl += 100;
        }
    }
}