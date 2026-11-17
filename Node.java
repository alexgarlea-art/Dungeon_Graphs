import java.util.HashMap;

public class Node {
    private final String id;
    private int col;
    private int row;
    private final HashMap<Node, Float> adjacencies;

    public Node(int row, int col) {
        this.adjacencies = new HashMap<>();
        this.row = row;
        this.col = col;
        this.id = String.format("%2s%2s", row, col);
    }

    public String getValue() {
        return this.id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addAdjacency(Node node, float weight) {
        this.adjacencies.put(node, weight);
    }

    public HashMap<Node, Float> getAdjacencies() {
        return this.adjacencies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Value: %5s | Adjacencies:\n", this.id));

        adjacencies.forEach((key, value) -> sb.append(String.format(
                "%25s[Value: %5s | Weight: %2s]\n",
                " ",
                key.getValue(),
                value
        )));
        return sb.toString();
    }
}
