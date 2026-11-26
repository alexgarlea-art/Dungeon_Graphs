private static final String HOST = "192.168.4.44";
private static final int PORTA = 9000;

void main(String[] args) throws IOException {
    int personaggio = args.length > 0
            ? Integer.parseInt(args[0])
            : 0;

    if (personaggio < 0 || personaggio > 2) {
        throw new IllegalArgumentException(
                "Il personaggio deve essere 0, 1 o 2");
    }

    try (Client client = new Client()) {
        client.start(HOST, PORTA);

        Random random = new Random();
        DungeonMap mappa = client.login(personaggio);

        IO.println("Personaggio " + personaggio + " collegato.");
        Thread.sleep(1000);
        IO.println("Mappa ricevuta:");
        IO.println(mappa);

        if (mappa.getNodes().isEmpty()) {
            throw new IllegalStateException("Mappa senza nodi");
        }

        String radice = args.length > 1
                ? args[1]
                : mappa.getNodes().getFirst();

        if (!mappa.containsNode(radice)) {
            throw new IllegalArgumentException(
                    "Radice non presente nella mappa: " + radice);
        }

        GraphNotOriented<String> mst =
                mappa.minimumSpanningTreeFrom(radice);

        IO.println();
        IO.println("Minimum spanning tree con radice " + radice + ":");
        IO.println(mst);

        List<String> nodi = mappa.getNodes();
        String posizione = mappa.containsNode("0000") ? "0000" : radice;
        String targetServer = null;

        IO.println("Posizione iniziale: " + posizione);

        while (true) {
            HashMap<String, Distance<String>> distanze =
                    mappa.distancesFrom(posizione);

            List<String> destinazioniRaggiungibili = new ArrayList<>();

            for (String nodo : nodi) {
                Distance<String> distanza = distanze.get(nodo);

                if (!nodo.equals(posizione)
                        && distanza != null
                        && distanza.getDistance() != Float.POSITIVE_INFINITY) {
                    destinazioniRaggiungibili.add(nodo);
                }
            }

            if (destinazioniRaggiungibili.isEmpty()) {
                IO.println(
                        "Nessun altro nodo raggiungibile da "
                                + posizione + ".");
                break;
            }

            String destinazione = targetServer;

            if (destinazione == null) {
                destinazione = destinazioniRaggiungibili.get(
                        random.nextInt(destinazioniRaggiungibili.size()));
            }

            IO.println("Destinazione: " + destinazione);

            List<String> percorso =
                    mappa.shortestPath(posizione, destinazione);

            if (percorso.size() < 2) {
                IO.println(
                        "Percorso non valido da "
                                + posizione
                                + " a "
                                + destinazione
                                + "; termino.");
                break;
            }

            boolean ricalcolaPercorso = false;

            for (int i = 1; i < percorso.size(); i++) {
                String prossimoNodo = percorso.get(i);
                NodeMap passo = new NodeMap(prossimoNodo);

                String comando = "move,"
                        + passo.getColonna() + ","
                        + passo.getRiga();

                IO.println(
                        "Invio " + comando
                                + " da " + posizione
                                + " verso " + prossimoNodo);

                try {
                    String risposta = client.sendCommand(comando);
                    IO.println(comando + " -> " + risposta);

                    posizione = prossimoNodo;

                    if (risposta.startsWith("TARGET,")) {
                        String[] campi = risposta.split(",");

                        if (campi.length != 3) {
                            throw new IOException(
                                    "Risposta TARGET non valida: "
                                            + risposta);
                        }

                        int colonnaTarget =
                                Integer.parseInt(campi[1].trim());
                        int rigaTarget =
                                Integer.parseInt(campi[2].trim());

                        targetServer = String.format(
                                "%02d%02d",
                                colonnaTarget,
                                rigaTarget);

                        IO.println(
                                "Meta assegnata dal server: "
                                        + targetServer);

                        if (!targetServer.equals(destinazione)) {
                            ricalcolaPercorso = true;
                            break;
                        }
                    }

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (IOException e) {
                    throw new IOException(
                            "Movimento fallito da "
                                    + posizione
                                    + " verso "
                                    + prossimoNodo
                                    + "; comando="
                                    + comando,
                            e);
                }
            }

            if (ricalcolaPercorso) {
                continue;
            }
        }

    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}