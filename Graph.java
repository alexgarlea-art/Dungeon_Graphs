import java.util.HashMap;

public abstract class Graph<T> {
    protected HashMap<T, Node<T>> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(T value) {
        if (!nodes.containsKey(value)) {
            nodes.put(value, new Node<>(value));
        } else {
            System.out.println("Element already inside the graph.");
        }
    }

    public HashMap<T, Node<T>> getNodesMap() {
        return this.nodes;
    }


    public void addEdge(T start, T dest, float weight) {
        Node<T> startNode = nodes.get(start);
        Node<T> destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.nodes.forEach(
                (key, value) -> sb.append(key).append(": ").append(value.toString()).append("\n"));
        return sb.toString();
    }
}
