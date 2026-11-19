import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GraphOriented extends Graph {

    public GraphOriented(String rrcc) {
        super(rrcc);
    }

    private String[] shortestPath(HashMap<String, Distance> map, String source, String dest){
        List<String> spath = new ArrayList<>();
        AtomicReference<String> toSearch = new AtomicReference<>("");
        AtomicBoolean arrived = new AtomicBoolean(false);
        if(map.containsKey(dest) && map.containsKey(source)){
            spath.add(dest);

            while(!arrived.get()){
                map.forEach((name, dist) -> {
                    if(name.equals(source)){
                        spath.add(source);
                        arrived.set(true);
                    }else if(name.equals(toSearch)){
                        toSearch.set(dist.getPrev());
                        spath.add(name);
                    }
                });
            }

        }else if(!map.containsKey(source)){
            IO.println("Source not in map.");
        }else IO.println("Destination not in map.");

        return spath.toArray(new String[spath.toArray().length]);
    }

    public String[] dijkstra(String source, String dest) {
        HashMap<String, Distance> risultati = new HashMap<>();

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
