import java.util.HashMap;

public abstract class Graph<T> {
    protected HashMap<String, Node> nodes;
    private String size = "";
    public Graph(String rrcc) {
        this.nodes = new HashMap<>();
        this.size = rrcc;
    }

    public void addNode(String id) {
        if (!nodes.containsKey(id)) {
            nodes.put(id, new Node(Integer.parseInt(id)/100, Integer.parseInt(id)%100));
        } else {
            System.out.println("Element already inside the graph.");
        }
    }

    public void addNode(int row, int col){
        String id = "" + row + col;
        if (!nodes.containsKey(id)) {
            nodes.put(id, new Node(row, col));
        } else {
            System.out.println("Element already inside the graph.");
        }
    }

    public HashMap<String, Node> getNodesMap() {
        return this.nodes;
    }


    public void addEdge(T start, T dest, float weight) {
        Node startNode = nodes.get(start);
        Node destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    public int getCols(){
        return Integer.parseInt(this.size)%100;
    }

    public int getRows(){
        return Integer.parseInt(this.size)/100;
    }

    public String getSize() {
        return this.size;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.nodes.forEach(
                (key, value) -> sb.append(key).append(": ").append(value.toString()).append("\n"));
        return sb.toString();
    }
}
