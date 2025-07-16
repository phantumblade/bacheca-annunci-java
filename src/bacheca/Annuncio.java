package bacheca;

import java.util.List;
import eccezioni.DatiNonValidiException;

/**
 * Classe astratta Annuncio che rappresenta la struttura di base
 * per AnnuncioAcquisto e AnnuncioVendita.
 */
public abstract class Annuncio {

    // Contatore statico per generare ID univoci per ogni annuncio
    private static int idCounter = 1;

    // Attributi comuni a tutti gli annunci
    private int id;
    private String titolo;
    private String descrizione;
    private double prezzo;
    private final Utente utente;  // Utente che ha creato l'annuncio (non può essere modificato)
    private List<String> paroleChiave;

    // Regex per la validazione delle parole chiave (solo lettere, numeri e spazi)
    private static final String PAROLA_CHIAVE_REGEX = "^[a-zA-Z0-9\\s]+$";

    // Metodo per ottenere il prossimo ID
    public static int getNextId() {
        return idCounter++;
    }

    /**
     * Costruttore per la creazione di nuovi annunci.
     * Utilizza i setter per garantire la validità dei dati.
     */
    public Annuncio(String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave) {
        this.id = getNextId();  // Incremento automatico dell'ID

        // Uso dei setter per assicurare che tutti i dati siano validi
        setTitolo(titolo);
        setDescrizione(descrizione);
        setPrezzo(prezzo);
        setParoleChiave(paroleChiave);

        // Validazione diretta dell'utente poiché è final e non può essere modificato dopo
        if (utente == null) {
            throw new DatiNonValidiException("L'utente associato all'annuncio non può essere nullo.");
        }
        this.utente = utente;
    }

    /**
     * Costruttore per il ripristino di un annuncio da file (mantiene l'ID originale).
     * Se l'ID ripristinato è maggiore del contatore corrente, il contatore viene aggiornato.
     */
    public Annuncio(int id, String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave) {
        this.id = id;

        // Aggiorna il contatore ID se l'annuncio ripristinato ha un ID più alto
        if (id >= idCounter) {
            idCounter = id + 1;
        }

        // Validazioni tramite setter
        setTitolo(titolo);
        setDescrizione(descrizione);
        setPrezzo(prezzo);
        setParoleChiave(paroleChiave);

        if (utente == null) {
            throw new DatiNonValidiException("L'utente associato all'annuncio non può essere nullo.");
        }
        this.utente = utente;
    }

    // =================== GETTERS ===================

    public int getId() {
        return id;
    }

    public Utente getUtente() {
        return utente;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public List<String> getParoleChiave() {
        return paroleChiave;
    }

    // =================== SETTERS CON VALIDAZIONI ===================

    /**
     * Valida il titolo: non può essere nullo o vuoto.
     */
    public void setTitolo(String titolo) {
        if (titolo == null || titolo.isBlank()) {
            throw new DatiNonValidiException("Il titolo non può essere vuoto o nullo.");
        }
        this.titolo = titolo;
    }

    /**
     * Valida la descrizione: se nulla o vuota, assegna un valore predefinito.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = (descrizione != null && !descrizione.isBlank()) ? descrizione : "Nessuna descrizione disponibile.";
    }

    /**
     * Valida il prezzo: non può essere negativo.
     */
    public void setPrezzo(double prezzo) {
        if (prezzo < 0) {
            throw new DatiNonValidiException("Il prezzo non può essere negativo.");
        }
        this.prezzo = prezzo;
    }

    /**
     * Valida la lista di parole chiave:
     * - Deve contenere almeno un elemento.
     * - Ogni parola deve rispettare il formato definito da una regex.
     */
    public void setParoleChiave(List<String> paroleChiave) {
        if (paroleChiave == null || paroleChiave.isEmpty()) {
            throw new DatiNonValidiException("La lista di parole chiave non può essere vuota o nulla.");
        }

        for (String parola : paroleChiave) {
            if (!parola.matches(PAROLA_CHIAVE_REGEX)) {
                throw new DatiNonValidiException("Parola chiave non valida: " + parola);
            }
        }
        this.paroleChiave = paroleChiave;
    }

    // =================== METODI UTILI ===================

    /**
     * Verifica se l'annuncio contiene una determinata parola chiave.
     */
    public boolean contieneParolaChiave(String parola) {
        return paroleChiave.contains(parola);
    }

    // Metodo per resettare il contatore ID al valore specificato
    public static void resetIdCounter(int nextId) {
        idCounter = nextId;
    }

    /**
     * Rappresentazione leggibile dell'annuncio per debug e visualizzazione.
     * Include ID, titolo, prezzo, utente e parole chiave.
     */
    @Override
    public String toString() {
        return "Titolo: " + titolo + "\nDescrizione: " + descrizione + "\nPrezzo: " + prezzo + "\nUtente: " + utente.getEmail();
    }
}
