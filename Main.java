private static int passed = 0;
private static int failed = 0;

// adds the edge in both directions, works both as monodirectional (will be redundant) and as bidirectional
private static void link(Graph graph, String node1, String node2, float weight) {
    graph.addEdge(node1, node2, weight);
    graph.addEdge(node2, node1, weight);
}

private static void check(String name, String[] actual, String... expected) {
    boolean ok = Arrays.equals(actual, expected);
    if (ok) passed++;
    else failed++;
    IO.println((ok ? "[OK]   " : "[FAIL] ") + name + " -> " + Arrays.toString(actual)
            + (ok ? "" : "  (atteso " + Arrays.toString(expected) + ")"));
}

private static void check(String name, boolean condition) {
    if (condition) passed++;
    else failed++;
    IO.println((condition ? "[OK]   " : "[FAIL] ") + name);
}

private static float totalWeight(Graph g) {
    float sum = 0;
    for (Node n : g.getNodesMap().values())
        for (float w : n.getAdjacencies().values()) sum += w;
    return sum / 2; // ogni arco e' contato due volte
}

/* Griglia 3x3, id = colonna*100 + riga

        col0   col1   col2
 riga0  0000   0100   0200
 riga1  0001   0101   0201
 riga2  0002   0102   0202(isolato)*/
private static GraphOriented buildGraph() {
    GraphOriented g = new GraphOriented("0505");

    for (int c = 0; c < 3; c++)
        for (int r = 0; r < 3; r++)
            g.addNode(r, c);

    link(g, "0000", "0001", 1);
    link(g, "0001", "0002", 1);
    link(g, "0000", "0100", 4);
    link(g, "0001", "0101", 1);
    link(g, "0100", "0101", 1);
    link(g, "0101", "0102", 5);
    link(g, "0002", "0102", 1);
    link(g, "0100", "0200", 2);
    link(g, "0200", "0201", 3);
    link(g, "0201", "0101", 6);
    // 0202 isolato
    return g;
}

private static GraphNotOriented getMstK() {
    GraphNotOriented gn = new GraphNotOriented("0505");
    for (int c = 0; c < 3; c++)
        for (int r = 0; r < 3; r++)
            gn.addNode(r, c);
    gn.addEdge("0000", "0001", 1);
    gn.addEdge("0001", "0002", 1);
    gn.addEdge("0000", "0100", 4);
    gn.addEdge("0001", "0101", 1);
    gn.addEdge("0100", "0101", 1);
    gn.addEdge("0101", "0102", 5);
    gn.addEdge("0002", "0102", 1);
    gn.addEdge("0100", "0200", 2);
    gn.addEdge("0200", "0201", 3);
    gn.addEdge("0201", "0101", 6);
    gn.addEdge("0201", "0202", 7);

    // MST atteso: 1+1+1+1+1+2+3+7 = 17  (9 nodi -> 8 archi)
    GraphNotOriented mstP = gn.prim("0000");
    GraphNotOriented mstK = gn.kruskal();
    check("Prim: 9 nodi", mstP.getNodesMap().size() == 9);
    check("Prim: peso totale 17", totalWeight(mstP) == 17f);
    check("Kruskal: 9 nodi", mstK.getNodesMap().size() == 9);
    check("Kruskal: peso totale 17", totalWeight(mstK) == 17f);
    return mstK;
}

void main() {
    IO.println("=== Creazione nodi ===");
    GraphOriented g = buildGraph();
    check("9 nodi creati", g.getNodesMap().size() == 9);
    check("getCols() == 5", g.getCols() == 5);
    check("getRows() == 5", g.getRows() == 5);

    // id da riga/colonna: riga 2, colonna 1 -> "0102"
    check("addNode(row=2, col=1) crea 0102", g.getNodesMap().containsKey("0102"));
    Node n = g.getNodesMap().get("0102");
    check("Node 0102 ha row=2 e col=1", n.getRow() == 2 && n.getCol() == 1);

    IO.println("\n=== Input non validi (attesi messaggi di errore) ===");
    int before = g.getNodesMap().size();
    g.addNode("12");          // id troppo corto
    g.addNode("abcd");        // non numerico
    g.addNode("0000");        // duplicato
    g.addNode(-1, 0);         // riga negativa
    g.addNode(0, -1);         // colonna negativa
    g.addNode(99, 0);         // riga fuori griglia
    g.addNode(0, 99);         // colonna fuori griglia
    g.addEdge("0000", "9999", 1); // nodo inesistente
    check("nessun nodo aggiunto dagli input non validi", g.getNodesMap().size() == before);

    IO.println("\n=== Archi bidirezionali ===");
    Node n0000 = g.getNodesMap().get("0000");
    Node n0001 = g.getNodesMap().get("0001");
    check("0000 -> 0001", n0000.getAdjacencies().containsKey(n0001));
    check("0001 -> 0000", n0001.getAdjacencies().containsKey(n0000));

    IO.println("\n=== Dijkstra ===");
    check("Dijkstra 0000 -> 0102",
            g.dijkstra("0000", "0102"), "0000", "0001", "0002", "0102");
    check("Dijkstra 0000 -> 0101",
            g.dijkstra("0000", "0101"), "0000", "0001", "0101");
    check("Dijkstra 0000 -> 0201",
            g.dijkstra("0000", "0201"), "0000", "0001", "0101", "0100", "0200", "0201");
    check("Dijkstra source == dest",
            g.dijkstra("0100", "0100"), "0100");
    check("Dijkstra destinazione irraggiungibile (0202)",
            g.dijkstra("0000", "0202") /* atteso vuoto */);
    try {
        String[] p = g.dijkstra("7777", "0000");
        check("Dijkstra con sorgente inesistente restituisce array vuoto", p.length == 0);
    } catch (Exception e) {
        check("Dijkstra con sorgente inesistente (eccezione: " + e.getClass().getSimpleName() + ")", false);
    }

    IO.println("\n=== Bellman-Ford ===");
    check("Bellman-Ford 0000 -> 0102",
            g.bellman_ford("0000", "0102"), "0000", "0001", "0002", "0102");
    check("Bellman-Ford 0000 -> 0101",
            g.bellman_ford("0000", "0101"), "0000", "0001", "0101");
    check("Bellman-Ford 0000 -> 0201",
            g.bellman_ford("0000", "0201"), "0000", "0001", "0101", "0100", "0200", "0201");
    check("Bellman-Ford destinazione irraggiungibile (0202)",
            g.bellman_ford("0000", "0202") /* atteso vuoto */);

    IO.println("\n=== Dijkstra e Bellman-Ford concordano su tutte le coppie raggiungibili ===");
    for (String s : g.getNodesMap().keySet()) {
        for (String d : g.getNodesMap().keySet()) {
            if (s.equals("0202") || d.equals("0202")) continue;
            if (!Arrays.equals(g.dijkstra(s, d), g.bellman_ford(s, d))) {
                // cammini diversi possono avere lo stesso costo: segnalo solo
                IO.println("   nota: percorsi diversi per " + s + " -> " + d);
            }
        }
    }
    check("percorsi identici su tutte le coppie (o solo equivalenti)", true);

    IO.println("\n=== MST (GraphNotOriented) ===");
    GraphNotOriented mstK = getMstK();

    IO.println("\n=== toString del grafo ===");
    IO.println(mstK);

    IO.println("Risultato: " + passed + " ok, " + failed + " falliti");
}

