import java.util.ArrayList;
import java.util.HashMap;

public abstract class Graph<T> {

    protected HashMap<T, HashMap<T, Adiacenza<T>>> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public void addNode(T value) {
        if (!nodes.containsKey(value)) {
            nodes.put(value, new HashMap<>());
        }
    }

    public abstract void addEdge(T source, T dest, float weight);

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        ArrayList<T> keys = new ArrayList<>(nodes.keySet());
        for (T u : keys) {
            res.append(u).append(" - [ ");
            ArrayList<Adiacenza<T>> adj = new ArrayList<>(nodes.get(u).values());
            for (Adiacenza<T> tAdiacenza : adj) {
                res.append(tAdiacenza).append(" ");
            }
            res.append("]\n");
        }

        return res.toString();
    }
}