import java.util.HashMap;

public class Nodo<T> {
    private final T value;
    private final HashMap<T, Float> adjacencies;

    public Nodo(T value) {
        this.value = value;
        this.adjacencies = new HashMap<>();
    }

    public T getValue() {
        return value;
    }

    public void addAdjacency(T nodo, float weight) {
        adjacencies.put(nodo, weight);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object key) {
        if (this == key) return true;
        if (!(key instanceof Nodo)) {
            return false;
        }
        Nodo altro = (Nodo) key;
        return value.equals(altro.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}