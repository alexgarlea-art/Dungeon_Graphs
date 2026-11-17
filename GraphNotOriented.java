import java.util.*;

public class GraphNotOriented<T> extends Graph<T> {

    public GraphNotOriented(String rrcc) {
        super(rrcc);
    }

    @Override
    public void addEdge(T source, T dest, float weight) {
        Node startNode = nodes.get(source);
        Node destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
            destNode.addAdjacency(startNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    private class Arco {
        T origin, target;
        float peso;
        Arco(T origin, T target, float peso) {
            this.origin = origin; this.target = target; this.peso = peso;
        }
    }

    public GraphNotOriented<T> prim(T radix) {
        GraphNotOriented<T> mst = new GraphNotOriented<>(this.getSize());
        if (!nodes.containsKey(radix)) return mst;

        HashSet<T> visitati = new HashSet<>();
        PriorityQueue<Arco> codaArchi = new PriorityQueue<>((a, b) -> Float.compare(a.peso, b.peso));

        mst.addNode(radix);
        visitati.add(radix);

        for (var entry : nodes.get(radix).getAdjacencies().entrySet()) {
            codaArchi.add(new Arco(radix, entry.getKey().getValue(), entry.getValue()));
        }

        while (!codaArchi.isEmpty()) {
            Arco arcoMinimo = codaArchi.poll();

            if (visitati.contains(arcoMinimo.target)) continue;

            mst.addNode(arcoMinimo.target);
            mst.addEdge(arcoMinimo.origin, arcoMinimo.target, arcoMinimo.peso);

            T nuovoNodo = arcoMinimo.target;
            visitati.add(nuovoNodo);

            for (var entry : nodes.get(nuovoNodo).getAdjacencies().entrySet()) {
                T vicino = entry.getKey().getValue();
                if (!visitati.contains(vicino)) {
                    codaArchi.add(new Arco(nuovoNodo, vicino, entry.getValue()));
                }
            }
        }
        return mst;
    }

    public GraphNotOriented<T> kruskal() {
        GraphNotOriented<T> mst = new GraphNotOriented<>();
        Map<T, T> parent = new HashMap<>();
        List<Arco> tuttiGliArchi = new ArrayList<>();

        for (T nodoVal : nodes.keySet()) {
            mst.addNode(nodoVal);
            parent.put(nodoVal, nodoVal);

            for (var entry : nodes.get(nodoVal).getAdjacencies().entrySet()) {
                tuttiGliArchi.add(new Arco(nodoVal, entry.getKey().getValue(), entry.getValue()));
            }
        }

        tuttiGliArchi.sort((a, b) -> Float.compare(a.peso, b.peso));

        for (Arco arco : tuttiGliArchi) {
            T radiceDa = arco.origin;
            while (!parent.get(radiceDa).equals(radiceDa)) {
                radiceDa = parent.get(radiceDa);
            }

            T radiceA = arco.target;
            while (!parent.get(radiceA).equals(radiceA)) {
                radiceA = parent.get(radiceA);
            }

            if (!radiceDa.equals(radiceA)) {
                mst.addEdge(arco.origin, arco.target, arco.peso);
                parent.put(radiceDa, radiceA);
            }
        }

        return mst;
    }
}
