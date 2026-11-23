import java.util.*;
public class GraphOriented extends Graph {

    public GraphOriented(String rrcc) {
        super(rrcc);
    }

    private String[] shortestPath(HashMap<String, Distance> map, String source, String dest) {
        if (!map.containsKey(source)) {
            IO.println("Source not in map.");
            return new String[0];
        }
        if (!map.containsKey(dest)) {
            IO.println("Destination not in map.");
            return new String[0];
        }
        if (map.get(dest).getDistance() == Float.MAX_VALUE) {
            return new String[0]; // unreachable
        }

        List<String> spath = new ArrayList<>();

        for (String cur = dest; cur != null; cur = map.get(cur).getPrev()) {
            spath.add(cur);
        }

        Collections.reverse(spath);
        return spath.toArray(String[]::new);
    }

    public String[] dijkstra(String source, String dest) {
        HashMap<String, Distance> risultati = new HashMap<>();
        if (!nodes.containsKey(source)) return shortestPath(risultati, source, dest);
        for (String key : nodes.keySet()) {
            risultati.put(key, new Distance(null, Float.MAX_VALUE));
        }
        if (risultati.containsKey(source)) {
            risultati.get(source).setDistance(0);
        }

        PriorityQueue<String> coda = new PriorityQueue<>((a, b) ->
                Float.compare(risultati.get(a).getDistance(), risultati.get(b).getDistance())
        );

        coda.add(source);

        while (!coda.isEmpty()) {
            String corrente = coda.poll();
            Node nodoCorrente = nodes.get(corrente);
            float distCorrente = risultati.get(corrente).getDistance();

            if (distCorrente == Float.MAX_VALUE) break;

            for (var entry : nodoCorrente.getAdjacencies().entrySet()) {
                Node vicino = entry.getKey();
                float pesoArco = entry.getValue();
                String valoreVicino = vicino.getValue();

                float nuovaDistanza = distCorrente + pesoArco;

                if (nuovaDistanza < risultati.get(valoreVicino).getDistance()) {
                    risultati.get(valoreVicino).setDistance(nuovaDistanza);
                    risultati.get(valoreVicino).setPrev(corrente);

                    coda.remove(valoreVicino);
                    coda.add(valoreVicino);
                }
            }
        }
        return shortestPath(risultati, source, dest);
    }

    public String[] bellman_ford(String source, String dest) {
        HashMap<String, Distance> risultati = new HashMap<>();

        for (String key : nodes.keySet()) {
            risultati.put(key, new Distance(null, Float.MAX_VALUE));
        }
        if (risultati.containsKey(source)) {
            risultati.get(source).setDistance(0);
        }

        int numeroNodi = nodes.size();

        for (int i = 0; i < numeroNodi - 1; i++) {
            for (Node u : nodes.values()) {
                float distU = risultati.get(u.getValue()).getDistance();
                if (distU == Float.MAX_VALUE) continue;

                for (var entry : u.getAdjacencies().entrySet()) {
                    Node v = entry.getKey();
                    float peso = entry.getValue();
                    float distV = risultati.get(v.getValue()).getDistance();

                    if (distU + peso < distV) {
                        risultati.get(v.getValue()).setDistance(distU + peso);
                        risultati.get(v.getValue()).setPrev(u.getValue());
                    }
                }
            }
        }
        return shortestPath(risultati, source, dest);
    }
}
