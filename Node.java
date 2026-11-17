import java.util.HashMap;

public class Node<T> {
    private final T value;
    private final HashMap<Node<T>, Float> adjacencies;

    public Node(T value) {
        this.value = value;
        this.adjacencies = new HashMap<>();
    }

    public T getValue() {
        return this.value;
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
        sb.append(String.format("Value: %5s | Adjacencies:\n", this.value));

        adjacencies.forEach((key, value) -> sb.append(String.format(
                "%25s[Value: %5s | Weight: %2s]\n",
                " ",
                key.getValue(),
                value
        )));
        return sb.toString();
    }
}
