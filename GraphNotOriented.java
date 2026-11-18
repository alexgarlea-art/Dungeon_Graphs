import java.util.*;

public class GraphNotOriented extends Graph {

    public GraphNotOriented(String ccrr) {
        super(ccrr);
    }

    @Override
    public void addEdge(String source, String dest, float weight) {
        Node startNode = nodes.get(source);
        Node destNode = nodes.get(dest);

        if (startNode != null && destNode != null) {
            startNode.addAdjacency(destNode, weight);
            destNode.addAdjacency(startNode, weight);
        } else {
            System.out.println("One or both nodes do not exist.");
        }
    }

    private static class Arco {
        final String origin, target;
        final float peso;

        Arco(String origin, String target, float peso) {
            this.origin = origin;
            this.target = target;
            this.peso = peso;
        }
    }

    private void copyNodeInto(GraphNotOriented other, String id) {
        Node n = nodes.get(id);
        other.addNode(n.getRow(), n.getCol());
    }

    public GraphNotOriented prim(String radix) {
        GraphNotOriented mst = new GraphNotOriented(this.getSize());
        if (!nodes.containsKey(radix)) return mst;

        HashSet<String> visitati = new HashSet<>();
        PriorityQueue<Arco> codaArchi = new PriorityQueue<>((a, b) -> Float.compare(a.peso, b.peso));

        copyNodeInto(mst, radix);
        visitati.add(radix);

        for (var entry : nodes.get(radix).getAdjacencies().entrySet()) {
            codaArchi.add(new Arco(radix, entry.getKey().getValue(), entry.getValue()));
        }

        while (!codaArchi.isEmpty()) {
            Arco arcoMinimo = codaArchi.poll();

            if (visitati.contains(arcoMinimo.target)) continue;

            copyNodeInto(mst, arcoMinimo.target);
            mst.addEdge(arcoMinimo.origin, arcoMinimo.target, arcoMinimo.peso);

            String nuovoNodo = arcoMinimo.target;
            visitati.add(nuovoNodo);

            for (var entry : nodes.get(nuovoNodo).getAdjacencies().entrySet()) {
                String vicino = entry.getKey().getValue();
                if (!visitati.contains(vicino)) {
                    codaArchi.add(new Arco(nuovoNodo, vicino, entry.getValue()));
                }
            }
        }
        return mst;
    }

    private String find(Map<String, String> parent, String x) {
        while (!parent.get(x).equals(x)) {
            x = parent.get(x);
        }
        return x;
    }

    public GraphNotOriented kruskal() {
        GraphNotOriented mst = new GraphNotOriented(this.getSize());
        Map<String, String> parent = new HashMap<>();
        List<Arco> tuttiGliArchi = new ArrayList<>();

        for (String id : nodes.keySet()) {
            copyNodeInto(mst, id);
            parent.put(id, id);

            for (var entry : nodes.get(id).getAdjacencies().entrySet()) {
                tuttiGliArchi.add(new Arco(id, entry.getKey().getValue(), entry.getValue()));
            }
        }

        tuttiGliArchi.sort((a, b) -> Float.compare(a.peso, b.peso));

        for (Arco arco : tuttiGliArchi) {
            String radiceDa = find(parent, arco.origin);
            String radiceA = find(parent, arco.target);

            if (!radiceDa.equals(radiceA)) {
                mst.addEdge(arco.origin, arco.target, arco.peso);
                parent.put(radiceDa, radiceA);
            }
        }

        return mst;
    }
}
