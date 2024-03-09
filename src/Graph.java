import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

class Graph extends JFrame {
    int latime;
    int inaltime;

    public ArrayList<GUINode> GUINodes;
    public ArrayList<edge> edges;
    private volatile boolean isAnimating = false;
    RBTree redBlackTree;

    public Graph() {
        super();
        this.setTitle("Red Black Tree");
        this.setLocationRelativeTo(null);

        this.setSize(1280, 1024);

        this.getContentPane().setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GUINodes = new ArrayList<GUINode>();
        edges = new ArrayList<edge>();
        latime = 40;
        inaltime = 40;
        setLayout(new BorderLayout());
        Font font = new Font("Helvetica", Font.BOLD, 30);

        redBlackTree = new RBTree();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JTextField insertText = new JTextField(10);
        insertText.setFont(font);
        bottomPanel.add(insertText);


        JButton insert = new JButton("Insert");

        insert.addActionListener(itemEvent -> {
            if (!isAnimating) {
                isAnimating = true;
                animateInsertion(insertText, 0);
            }
        });

        insert.setBackground(new Color(29, 117, 209));
        insert.setForeground(Color.white);
        insert.setFont(font);
        this.getRootPane().setDefaultButton(insert);

        insert.addActionListener(itemEvent -> {
            Listenr(insertText, 0);
        });

        bottomPanel.add(insert);

        JTextField deleteText = new JTextField(10);
        deleteText.setFont(font);

        bottomPanel.add(deleteText);
        JButton delete = new JButton("Delete");
        delete.setBackground(new Color(255, 0, 0));

        delete.setFont(font);
        delete.setForeground(Color.white);
        bottomPanel.add(delete);



        delete.addActionListener(itemEvent -> {
            Listenr(deleteText, 1);
        });

        JButton clear = new JButton("Clear");
        clear.setBackground(new Color(169, 169, 169));
        clear.setFont(font);
        clear.setForeground(Color.white);//font color

        bottomPanel.add(clear);

        clear.addActionListener(itemEvent -> {
            redBlackTree.clear();
            this.edges.clear();
            this.GUINodes.clear();
            this.repaint();
        });

        add(bottomPanel, BorderLayout.SOUTH);
    }

    static class GUINode {
        int x, y, data, color;

        public GUINode(int data, int color, int myX, int myY) {
            x = myX;
            y = myY;
            this.color = color;
            this.data = data;
        }
    }

    static class edge {
        int i, j;

        public edge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    public void addNode(int data, int color, int x, int y) {
        GUINodes.add(new GUINode(data, color, x, y));
        this.repaint();
    }

    public void addEdge(int i, int j) {
        edges.add(new edge(i, j));
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Font font = new Font("Helvetica", Font.BOLD, 18);
        g.setFont(font);
        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(inaltime, f.getHeight())*2;
        for (edge e : edges) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.draw(new Line2D.Float(GUINodes.get(e.i).x, GUINodes.get(e.i).y, GUINodes.get(e.j).x, GUINodes.get(e.j).y));
        }

        for (GUINode n : GUINodes) {
            String Data = String.valueOf(n.data);
            int nodeWidth = Math.max(latime, f.stringWidth(Data) + latime / 2)*2;
            if (n.color == 0) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);
            g.setColor(Color.white);
            g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2, nodeWidth, nodeHeight);
            g.drawString(Data, n.x - f.stringWidth(Data) / 2, n.y + f.getHeight() / 2);
        }
    }

    void Listenr(JTextField textField, int operation) {
        String input = textField.getText().trim();
        textField.setText("");
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int x = Integer.parseInt(input);
            textField.setText("");

            this.edges.clear();
            this.GUINodes.clear();

            if (operation == 0) {
                redBlackTree.finalInsertion(x);
            } else {
                if (!redBlackTree.search(x)) {
                    JOptionPane.showMessageDialog(this, "No node with value " + x + " in the Red-Black Tree.", "Node Not Found", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                redBlackTree.Delete(x);
            }
            new TreeAnim().bfs(redBlackTree.getRoot(), this);

            Node node = redBlackTree.getRoot();
            redBlackTree.reset(node);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void animateInsertion(JTextField textField, int operation) {
        String input = textField.getText().trim();
        textField.setText("");

        if (input.isEmpty()) {
            //JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int x = Integer.parseInt(input);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    redBlackTree.finalInsertion(x);

                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publish();
                    }
                    isAnimating = false;
                    return null;
                }

                @Override
                protected void process(java.util.List<Void> chunks) {
                    edges.clear();
                    GUINodes.clear();
                    new TreeAnim().bfs(redBlackTree.getRoot(), Graph.this);
                    repaint();
                }
            };

            worker.execute();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void animateDeletion(JTextField textField) {
        String input = textField.getText().trim();
        textField.setText("");

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int x = Integer.parseInt(input);
            if (!redBlackTree.search(x)) {
                JOptionPane.showMessageDialog(this, "No node with value " + x + " in the Red-Black Tree.",
                        "Node Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    redBlackTree.Delete(x);

                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publish();
                    }
                    isAnimating = false;
                    return null;
                }

                @Override
                protected void process(java.util.List<Void> chunks) {
                    edges.clear();
                    GUINodes.clear();
                    new TreeAnim().bfs(redBlackTree.getRoot(), Graph.this);
                    repaint();
                }
            };

            worker.execute();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

}