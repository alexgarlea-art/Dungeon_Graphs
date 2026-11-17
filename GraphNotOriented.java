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
        String origin, target;
        float peso;
        Arco(String origin, String target, float peso) {
            this.origin = origin; this.target = target; this.peso = peso;
        }
    }

    public GraphNotOriented<String> prim(String radix) {
        GraphNotOriented<String> mst = new GraphNotOriented<>(this.getSize());
        if (!nodes.containsKey(radix)) return mst;

        HashSet<String> visitati = new HashSet<>();
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

            String nuovoNodo = arcoMinimo.target;
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
        Map<String, String> parent = new HashMap<>();
        List<Arco> tuttiGliArchi = new ArrayList<>();

        for (String id : nodes.keySet()) {
            mst.addNode(id);
            parent.put(id, id);

            for (var entry : nodes.get(id).getAdjacencies().entrySet()) {
                tuttiGliArchi.add(new Arco((T)id, (T)entry.getKey().getValue(), entry.getValue()));
            }
        }

        tuttiGliArchi.sort((a, b) -> Float.compare(a.peso, b.peso));

        for (Arco arco : tuttiGliArchi) {
            String radiceDa = arco.origin;
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
