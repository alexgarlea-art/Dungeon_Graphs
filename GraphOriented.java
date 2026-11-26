import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GraphOriented<T> extends Graph<T> {

    @Override
    public void addEdge(T source, T dest, float weight) {
        addNode(source);
        addNode(dest);
        nodes.get(source).put(dest, new Adiacenza<>(dest, weight));
    }

    public HashMap<T, Distance<T>> bellman_ford(T source) {
        if (!nodes.containsKey(source)) {
            throw new IllegalArgumentException("Nodo sorgente inesistente: " + source);
        }

        ArrayList<T> keys = new ArrayList<>(nodes.keySet());

        HashMap<T, Distance<T>> dist = new HashMap<>();
        for (T key : keys) {
            dist.put(key, new Distance<>(null, Float.POSITIVE_INFINITY));
        }
        dist.get(source).setDistance(0);

        for (int i = 1; i < keys.size(); i++) {
            boolean modificato = false;
            for (T u : keys) {
                if (dist.get(u).getDistance() == Float.POSITIVE_INFINITY) continue;
                ArrayList<Adiacenza<T>> adj = new ArrayList<>(nodes.get(u).values());
                for (Adiacenza<T> a : adj) {
                    float nd = dist.get(u).getDistance() + a.getPeso();
                    if (nd < dist.get(a.getNodo()).getDistance()) {
                        dist.get(a.getNodo()).setDistance(nd);
                        dist.get(a.getNodo()).setPrev(u);
                        modificato = true;
                    }
                }
            }
            if (!modificato) break;
        }

        for (T u : keys) {
            if (dist.get(u).getDistance() == Float.POSITIVE_INFINITY) continue;
            ArrayList<Adiacenza<T>> adj = new ArrayList<>(nodes.get(u).values());
            for (Adiacenza<T> a : adj) {
                if (dist.get(u).getDistance() + a.getPeso() < dist.get(a.getNodo()).getDistance()) {
                    throw new IllegalStateException("Il grafo contiene un ciclo negativo");
                }
            }
        }

        return dist;
    }

    public HashMap<T, Distance<T>> dijkstra(T source) {
        if (!nodes.containsKey(source)) {
            throw new IllegalArgumentException("Nodo sorgente inesistente: " + source);
        }

        ArrayList<T> keys = new ArrayList<>(nodes.keySet());

        HashMap<T, Distance<T>> dist = new HashMap<>();
        for (T key : keys) {
            dist.put(key, new Distance<>(null, Float.POSITIVE_INFINITY));
        }
        dist.get(source).setDistance(0);

        HashSet<T> visitati = new HashSet<>();

        while (visitati.size() < keys.size()) {
            T u = null;
            for (T n : keys) {
                if (!visitati.contains(n)
                        && (u == null || dist.get(n).getDistance() < dist.get(u).getDistance())) {
                    u = n;
                }
            }

            if (dist.get(u).getDistance() == Float.POSITIVE_INFINITY) break;

            visitati.add(u);

            ArrayList<Adiacenza<T>> adj = new ArrayList<>(nodes.get(u).values());
            for (Adiacenza<T> a : adj) {
                if (a.getPeso() < 0) {
                    throw new IllegalStateException("Dijkstra non ammette pesi negativi");
                }
                float nd = dist.get(u).getDistance() + a.getPeso();
                if (nd < dist.get(a.getNodo()).getDistance()) {
                    dist.get(a.getNodo()).setDistance(nd);
                    dist.get(a.getNodo()).setPrev(u);
                }
            }
        }

        return dist;
    }
}