import java.util.HashMap;

public class Main {
    private static void stampaRisultatiDistanze(HashMap<String, Distance> mappaRisultati) {
        if (mappaRisultati == null) {
            System.out.println("Nessun risultato disponibile");
            return;
        }
        mappaRisultati.forEach((nodoDestinazione, infoDistanza) -> {
            String distStr = (infoDistanza.getDistance() == Float.MAX_VALUE) ? "Infinito" : String.valueOf(infoDistanza.getDistance());
            String prevStr = (infoDistanza.getPrev() == null) ? "Nessuno" : infoDistanza.getPrev();
            System.out.println("Nodo [" + nodoDestinazione + "] -> Distanza Totale: " + distStr + ", Nodo Precedente: " + prevStr);
        });
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("1. TEST GRAFO ORIENTATO");
        GraphOriented go = new GraphOriented("1010");
        go.addNode("0000");
        go.addNode("0001");
        go.addNode("0002");
        go.addNode("0003");
        go.addNode("0004");
        go.addNode("0005");

        go.addEdge("0000", "0001", 2);
        go.addEdge("0000", "0002", 7);
        go.addEdge("0000", "0003", 9);
        go.addEdge("0000", "0005", 20);
        go.addEdge("0001", "0003", 6);
        go.addEdge("0001", "0002", 3);
        go.addEdge("0002", "0003", 1);
        go.addEdge("0002", "0004", 8);
        go.addEdge("0003", "0004", 4);
        go.addEdge("0003", "0005", 3);
        go.addEdge("0004", "0005", 2);
        go.addEdge("0004", "0002", 1);
        go.addEdge("0005", "0003", 3);

        System.out.println("\nStruttura del Grafo Orientato");
        System.out.print(go);

        System.out.println("Esecuzione Dijkstra (Sorgente: 0000)");
        HashMap<String, Distance> resDijkstra = go.dijkstra("0000");
        stampaRisultatiDistanze(resDijkstra);

        System.out.println("Esecuzione Bellman-Ford (Sorgente: 0000)");
        HashMap<String, Distance> resBellman = go.bellman_ford("0000");
        stampaRisultatiDistanze(resBellman);

        System.out.println("2. TEST GRAFO NON ORIENTATO (Algoritmo di Prim)");
        GraphNotOriented gno = new GraphNotOriented("1010");
        gno.addNode("0000");
        gno.addNode("0001");
        gno.addNode("0002");
        gno.addNode("0003");
        gno.addNode("0004");
        gno.addNode("0005");

        gno.addEdge("0000", "0001", 1);
        gno.addEdge("0000", "0002", 3);
        gno.addEdge("0001", "0002", 3);
        gno.addEdge("0001", "0003", 6);
        gno.addEdge("0003", "0002", 2);
        gno.addEdge("0003", "0004", 4);
        gno.addEdge("0003", "0005", 5);
        gno.addEdge("0002", "0004", 4);
        gno.addEdge("0005", "0004", 2);

        System.out.println("\nStruttura del Grafo Non Orientato Originale");
        System.out.print(gno);

        System.out.println("Calcolo del Minimum Spanning Tree Prim");
        GraphNotOriented mstPrim = gno.prim("0000");
        System.out.println("\nStruttura del MST");
        System.out.print(mstPrim);

        System.out.println("Calcolo del Minimum Spanning Tree Kruskal");
        GraphNotOriented mstKru = gno.kruskal();
        System.out.println("\nStruttura del MST");
        System.out.print(mstKru);
    }
}
