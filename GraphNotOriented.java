import java.util.*;

public class GraphNotOriented<T> extends Graph<T> {

    public GraphNotOriented(String rrcc) {
        super(rrcc);
    }

    @Override
    public void addEdge(T source, T dest, float weight) {
        Node<T> startNode = nodes.get(source);
        Node<T> destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
            destNode.addAdjacency(startNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    private static class Arco<T> {
        final T origin, target;
        final float peso;

        Arco(T origin, T target, float peso) {
            this.origin = origin;
            this.target = target;
            this.peso = peso;
        }
    }

    private void copyNodeInto(GraphNotOriented<T> other, T id) {
        Node<T> n = nodes.get(id);
        other.addNode(id, n.getRow(), n.getCol());
    }

    public GraphNotOriented<T> prim(T radix) {
        GraphNotOriented<T> mst = new GraphNotOriented<>(this.getSize());
        if (!nodes.containsKey(radix)) return mst;

        HashSet<T> visitati = new HashSet<>();
        PriorityQueue<Arco<T>> codaArchi = new PriorityQueue<>((a, b) -> Float.compare(a.peso, b.peso));

        copyNodeInto(mst, radix);
        visitati.add(radix);

        for (var entry : nodes.get(radix).getAdjacencies().entrySet()) {
            codaArchi.add(new Arco<>(radix, entry.getKey().getValue(), entry.getValue()));
        }

        while (!codaArchi.isEmpty()) {
            Arco<T> arcoMinimo = codaArchi.poll();

            if (visitati.contains(arcoMinimo.target)) continue;

            copyNodeInto(mst, arcoMinimo.target);
            mst.addEdge(arcoMinimo.origin, arcoMinimo.target, arcoMinimo.peso);

            T nuovoNodo = arcoMinimo.target;
            visitati.add(nuovoNodo);

            for (var entry : nodes.get(nuovoNodo).getAdjacencies().entrySet()) {
                T vicino = entry.getKey().getValue();
                if (!visitati.contains(vicino)) {
                    codaArchi.add(new Arco<>(nuovoNodo, vicino, entry.getValue()));
                }
            }
        }
        return mst;
    }

    private T find(Map<T, T> parent, T x) {
        while (!parent.get(x).equals(x)) {
            x = parent.get(x);
        }
        return x;
    }

    public GraphNotOriented<T> kruskal() {
        GraphNotOriented<T> mst = new GraphNotOriented<>(this.getSize());
        Map<T, T> parent = new HashMap<>();
        List<Arco<T>> tuttiGliArchi = new ArrayList<>();

        for (T id : nodes.keySet()) {
            copyNodeInto(mst, id);
            parent.put(id, id);

            for (var entry : nodes.get(id).getAdjacencies().entrySet()) {
                tuttiGliArchi.add(new Arco<>(id, entry.getKey().getValue(), entry.getValue()));
            }
        }

        tuttiGliArchi.sort((a, b) -> Float.compare(a.peso, b.peso));

        for (Arco<T> arco : tuttiGliArchi) {
            T radiceDa = find(parent, arco.origin);
            T radiceA = find(parent, arco.target);

            if (!radiceDa.equals(radiceA)) {
                mst.addEdge(arco.origin, arco.target, arco.peso);
                parent.put(radiceDa, radiceA);
            }
        }

        return mst;
    }
}
