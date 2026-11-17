import java.util.HashMap;

public class Node<T> {
    private final T id;
    private int col;
    private int row;
    private final HashMap<Node<T>, Float> adjacencies;

    public Node(T id, int row, int col) {
        this.id = id;
        this.adjacencies = new HashMap<>();
        this.row = row;
        this.col = col;
    }

    public T getValue() {
        return this.id;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addAdjacency(Node<T> node, float weight) {
        this.adjacencies.put(node, weight);
    }

    public HashMap<Node<T>, Float> getAdjacencies() {
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
