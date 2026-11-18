import javax.swing.*;
import java.util.HashMap;

public abstract class Graph {
    protected HashMap<String, Node> nodes;
    private final String size;

    public Graph(String ccrr) {
        this.nodes = new HashMap<>();
        this.size = ccrr;
    }

    public void addNode(String id){
        if (!nodes.containsKey(id)) {
            nodes.put(id, new Node(id));
        } else {
            System.out.println("Element already inside the graph.");
        }
    }

    public void addNode(int row, int col) {
        if(row <= this.getRows() && col <= this.getCols()) {

            StringBuilder id = new StringBuilder("" + (col * 100 + row));

            while(id.length() < 4){
                id.insert(0, "0");
            }

            if (!nodes.containsKey(id.toString())) {
                nodes.put(id.toString(), new Node(id.toString()));
            } else {
                System.out.println("Element already inside the graph.");
            }

        } else if (row > this.getRows()) {
            IO.println("Input rows are greater than the graph's max rows.");
        }else{
            IO.println("Input cols are greater than the graph's max cols.");
        }
    }

    public HashMap<String, Node> getNodesMap() {
        return this.nodes;
    }

    public void addEdge(String start, String dest, float weight) {
        Node startNode = nodes.get(start);
        Node destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    public int getCols() {
        return Integer.parseInt(this.size) / 100;
    }

    public int getRows() {
        return Integer.parseInt(this.size) % 100;
    }

    public String getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.nodes.forEach(
                (key, value) -> sb.append(key).append(": ").append(value.toString()).append("\n"));
        return sb.toString();
    }
}
