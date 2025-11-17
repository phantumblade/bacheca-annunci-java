package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bacheca.Annuncio;
import bacheca.AnnuncioAcquisto;
import bacheca.AnnuncioVendita;
import bacheca.Bacheca;
import bacheca.Utente;
import eccezioni.DatiNonValidiException;
import eccezioni.GestoreBachecaException;

/**
 * Classe che implementa l'interfaccia GestoreBacheca
 * e utilizza la Bacheca per gestire gli Annuncio (aggiunta, rimozione,
 * ricerca e salvataggio/lettura da file).
 */
public class GestoreBachecaImpl implements GestoreBacheca {

    /** La bacheca su cui operano i metodi di questo gestore. */
    private final Bacheca bacheca;

    /**
     * Facoltativo: se desideri che parseAnnuncio possa recuperare
     * o creare utenti in maniera corretta, serve un gestore di Utenti.
     */
    private final GestoreUtenti gestoreUtenti;

    /**
     * Costruttore principale: riceve la bacheca e il gestore utenti.
     * @param bacheca la bacheca da gestire (non null)
     * @param gestoreUtenti il gestore per cercare/creare utenti (non null)
     */
    public GestoreBachecaImpl(Bacheca bacheca, GestoreUtenti gestoreUtenti) {
        if (bacheca == null) {
            throw new IllegalArgumentException("Bacheca non può essere null");
        }
        if (gestoreUtenti == null) {
            throw new IllegalArgumentException("GestoreUtenti non può essere null");
        }
        this.bacheca = bacheca;
        this.gestoreUtenti = gestoreUtenti;
    }

    // ---------------------------------------------------------------
    // METODI DI CREAZIONE ANNUNCIO (Acquisto o Vendita)
    // ---------------------------------------------------------------
    /**
     * Crea un Annuncio specificando se è in vendita o acquisto.
     * Controlla validità di titolo, descrizione, prezzo, utente, ecc.
     */
    public Annuncio creaAnnuncio(int id,
                                 String titolo,
                                 String descrizione,
                                 double prezzo,
                                 Utente utente,
                                 List<String> paroleChiave,
                                 boolean inVendita,
                                 LocalDate dataScadenza) {

        // Controlli di base
        if (titolo == null || titolo.isBlank()) {
            throw new DatiNonValidiException("Il titolo non può essere vuoto");
        }
        if (descrizione == null || descrizione.isBlank()) {
            throw new DatiNonValidiException("La descrizione non può essere vuota");
        }
        if (prezzo < 0) {
            throw new DatiNonValidiException("Il prezzo non può essere negativo");
        }
        if (utente == null) {
            throw new DatiNonValidiException("Utente non può essere nullo");
        }

        // Inizializza la lista di parole chiave se nulla
        if (paroleChiave == null) {
            paroleChiave = new ArrayList<>();
        }

        // Se è AnnuncioVendita, controlla la scadenza
        if (inVendita) {
            return creaAnnuncioVendita(id, titolo, descrizione, prezzo, utente, paroleChiave, dataScadenza);
        } else {
            return creaAnnuncioAcquisto(id, titolo, descrizione, prezzo, utente, paroleChiave);
        }
    }

    private Annuncio creaAnnuncioVendita(int id,
                                         String titolo,
                                         String descrizione,
                                         double prezzo,
                                         Utente utente,
                                         List<String> paroleChiave,
                                         LocalDate dataScadenza) {
        if (dataScadenza == null || dataScadenza.isBefore(LocalDate.now())) {
            throw new DatiNonValidiException("Data di scadenza non valida o già passata");
        }
        return new AnnuncioVendita(id, titolo, descrizione, prezzo, utente, paroleChiave, false, dataScadenza);
    }

    private Annuncio creaAnnuncioAcquisto(int id,
                                          String titolo,
                                          String descrizione,
                                          double prezzo,
                                          Utente utente,
                                          List<String> paroleChiave) {
        return new AnnuncioAcquisto(id, titolo, descrizione, prezzo, utente, paroleChiave);
    }

    // ---------------------------------------------------------------
    // METODI DELL'INTERFACCIA GestoreBacheca
    // ---------------------------------------------------------------
    @Override
    public void aggiungiAnnuncio(Annuncio annuncio) throws GestoreBachecaException {
        if (annuncio == null) {
            throw new GestoreBachecaException("L'annuncio non può essere null");
        }
        // Deleghiamo la logica di inserimento a Bacheca
        bacheca.aggiungiAnnuncio(annuncio);
    }

    @Override
    public void pulisciBacheca() {
        // Svuota completamente la bacheca
        bacheca.svuotaBacheca();
    }

    @Override
    public void stampaTuttiAnnunci() {
        // Recupera gli annunci in formato array
        Annuncio[] elenco = bacheca.getAnnunci();

        if (elenco.length == 0) {
            System.out.println("Nessun annuncio presente in bacheca.");
            return;
        }

        System.out.println("--- Elenco completo degli annunci ---");
        for (Annuncio annuncio : elenco) {
            // Puoi usare 'toString()' di Annuncio o personalizzare l'output
            System.out.println(annuncio);
        }
    }

    @Override
    public boolean rimuoviAnnuncio(int id, String emailUtente) {
        // Ricerca l'annuncio con quell'id e controlla la proprietà
        for (Annuncio annuncio : bacheca.getAnnunci()) {
            if (annuncio.getId() == id) {
                if (annuncio.getUtente().getEmail().equals(emailUtente)) {
                    // Effettiva rimozione delegata a Bacheca
                    bacheca.rimuoviAnnuncio(id, annuncio.getUtente());
                    return true;
                } else {
                    throw new IllegalArgumentException(
                        "Utente non autorizzato a rimuovere questo annuncio"
                    );
                }
            }
        }
        throw new IllegalArgumentException("Annuncio con ID " + id + " non trovato");
    }

    // ---------------------------------------------------------------
    // SALVATAGGIO SU FILE
    // ---------------------------------------------------------------
    @Override
    public void salvaSuFile(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Il percorso del file non può essere nullo o vuoto.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Intestazione generica del file
            writer.write("Bacheca Annunci Acquisto/Vendita");
            writer.newLine();

            // Salvo ogni annuncio con un formato CSV-like
            for (Annuncio annuncio : bacheca) {
                writer.write(serializeAnnuncio(annuncio));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new IOException("Errore durante la scrittura su file: " + e.getMessage(), e);
        }
    }

    /**
     * Converte un Annuncio (Acquisto o Vendita) in una riga testuale.
     * Esempio di output:
     *  "; Titolo; Descrizione; 100.0; email@ex.com; chiave1,chiave2; 2025-12-31; false"
     */
    private String serializeAnnuncio(Annuncio annuncio) {
        StringBuilder sb = new StringBuilder();

        // Mettiamo un punto e virgola iniziale solo come separatore
        sb.append("; ").append(annuncio.getTitolo());
        sb.append("; ").append(annuncio.getDescrizione());
        sb.append("; ").append(annuncio.getPrezzo());
        sb.append("; ").append(annuncio.getUtente().getEmail());
        sb.append("; ").append(String.join(",", annuncio.getParoleChiave()));

        // Se è AnnuncioVendita, aggiungiamo data di scadenza e stato venduto
        if (annuncio instanceof AnnuncioVendita vendita) {
            sb.append("; ").append(vendita.getDataScadenza());
            sb.append(";").append(vendita.isVenduto());
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------
    // LETTURA DA FILE
    // ---------------------------------------------------------------
    @Override
    public Bacheca leggiDaFile(String filePath) throws IOException, GestoreBachecaException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Il percorso del file non può essere nullo o vuoto.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return leggiDaReader(reader);
        }
    }

    @Override
    public Bacheca leggiDaReader(BufferedReader reader) throws IOException, GestoreBachecaException {
        bacheca.svuotaBacheca();  // Svuota la bacheca prima di caricare i nuovi annunci

        int maxId = 0;  // Per tenere traccia dell'ID più alto nel file
        int errori = 0;

        try {
            String header = reader.readLine();
            if (header == null || header.isBlank()) {
                throw new GestoreBachecaException("File vuoto o intestazione mancante.");
            }

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                try {
                    Annuncio annuncio = parseAnnuncio(linea);  // Tenta di parsare l'annuncio
                    bacheca.aggiungiAnnuncio(annuncio);

                    // Aggiorna l'ID massimo trovato nel file
                    if (annuncio.getId() > maxId) {
                        maxId = annuncio.getId();
                    }

                } catch (DatiNonValidiException ex) {
                    // Annuncio malformato: lo saltiamo e contiamo l'errore
                    errori++;
                    System.err.println("Annuncio saltato per dati non validi: " + ex.getMessage());
                }
            }

            // Reset del contatore ID solo dopo il caricamento di tutti gli annunci
            Annuncio.resetIdCounter(maxId + 1);

            if (errori > 0) {
                System.out.println("Sono stati saltati " + errori + " annunci per errori nei dati.");
            }

            return this.bacheca;

        } catch (IOException e) {
            throw new IOException("Errore durante la lettura del file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new GestoreBachecaException("Errore durante il parsing del file: " + e.getMessage());
        }
    }

    private Annuncio parseAnnuncio(String linea) throws DatiNonValidiException {
        String[] campi = linea.split(";");

        // Controllo sul numero minimo di campi
        if (campi.length < 6) {
            throw new DatiNonValidiException("Riga non valida, campi insufficienti: " + linea);
        }

        // Gestione dell'ID: usa quello del file se presente, altrimenti genera un nuovo ID
        int id;
        if (!campi[0].trim().isEmpty()) {
            try {
                id = Integer.parseInt(campi[0].trim());
            } catch (NumberFormatException e) {
                throw new DatiNonValidiException("ID non valido nel file: " + campi[0]);
            }
        } else {
            id = Annuncio.getNextId();  // Se l'ID non è presente, genera uno nuovo
        }

        String titolo = campi[1].trim();
        String descrizione = campi[2].trim();
        double prezzo = Double.parseDouble(campi[3].trim());
        String email = campi[4].trim();
        String paroleRaw = campi[5].trim();

        // Gestione delle parole chiave
        List<String> paroleChiave = new ArrayList<>();
        if (!paroleRaw.isEmpty()) {
            for (String kw : paroleRaw.split(",")) {
                paroleChiave.add(kw.trim());
            }
        }

        // Distinzione tra Annuncio di Vendita e Acquisto
        if (campi.length > 6) {  // Annuncio di Vendita
            LocalDate dataScadenza = LocalDate.parse(campi[6].trim());
            boolean venduto = (campi.length > 7) && Boolean.parseBoolean(campi[7].trim());
            return new AnnuncioVendita(id, titolo, descrizione, prezzo, recuperaOCreaUtente(email), paroleChiave, venduto, dataScadenza, true);
        } else {  // Annuncio di Acquisto
            return new AnnuncioAcquisto(id, titolo, descrizione, prezzo, recuperaOCreaUtente(email), paroleChiave);
        }
    }

    /**
     * Recupera un utente esistente (tramite email) o lo crea se non c'è.
     * Se c'è incongruenza di nomi, puoi lanciare eccezione o aggiornare.
     */
    private Utente recuperaOCreaUtente(String email) {
        Utente utente = gestoreUtenti.cercaUtente(email);
        if (utente == null) {
            // **Controlla qui se stai assegnando solo 'NomeDaFile'**
            utente = new Utente(email, email);  // Usa l'email come nome, se non c'è altro
            gestoreUtenti.aggiungiUtente(utente);
        }
        return utente;
    }

    // ---------------------------------------------------------------
    // METODO DI RICERCA: delega a bacheca
    // ---------------------------------------------------------------
    @Override
    public List<Annuncio> cercaPerParoleChiave(List<String> paroleChiave) {
        if (paroleChiave == null) {
            paroleChiave = new ArrayList<>();
        }
        // Converto in ArrayList se la bacheca richiede esplicitamente quell’implementazione
        return bacheca.cercaPerParoleChiave(new ArrayList<>(paroleChiave));
    }

    @Override
    public Annuncio cercaAnnuncioPerId(int id) {
        for (Annuncio annuncio : bacheca.getAnnunci()) {
            if (annuncio.getId() == id) {
                return annuncio;
            }
        }
        return null;  // Restituisce null se l'annuncio non è trovato
    }

    // Aggiunta di una nuova parola chiave ad un annuncio già esistente
    @Override
    public void aggiungiParoleChiave(int id, List<String> nuoveParole) throws GestoreBachecaException {
        Annuncio annuncio = cercaAnnuncioPerId(id);

        if (annuncio == null) {
            throw new GestoreBachecaException("Annuncio con ID " + id + " non trovato.");
        }

        // Crea una nuova lista con le parole esistenti
        List<String> paroleEsistenti = new ArrayList<>(annuncio.getParoleChiave());
        paroleEsistenti.addAll(nuoveParole);

        // Rimuove eventuali duplicati
        List<String> paroleUniche = new ArrayList<>(new HashSet<>(paroleEsistenti));
        annuncio.setParoleChiave(paroleUniche);
    }

    //--GUI--

    @Override
    public Bacheca getBacheca() {
    return this.bacheca;
    }

}
