package controller;

import java.util.List;
import java.io.IOException;
import bacheca.Utente;

/**
 * Interfaccia che definisce i metodi per la gestione degli Utenti.
 * Può essere implementata in diversi modi (es. in memoria, su DB, ecc.).
 */
public abstract interface GestoreUtenti {

    /**
     * Aggiunge un utente all'elenco degli utenti registrati.
     *
     * @param utente l'utente da aggiungere
     */
    void aggiungiUtente(Utente utente);

    /**
     * Rimuove un utente identificato da una data email.
     *
     * @param email l'email dell'utente da rimuovere
     * @return true se l'utente è stato rimosso, false altrimenti
     */
    boolean rimuoviUtente(String email);

    /**
     * Cerca e restituisce un utente in base all'email.
     *
     * @param email l'email da cercare
     * @return l'Utente corrispondente o null se non trovato
     */
    Utente cercaUtente(String email);

    /**
     * Restituisce la lista di tutti gli utenti registrati.
     *
     * @return lista di utenti
     */
    List<Utente> getUtentiRegistrati();

    /**
     * Salva tutti gli utenti registrati su file CSV.
     *
     * @param filePath il percorso del file dove salvare
     * @throws IOException se ci sono problemi di I/O
     */
    void salvaSuFile(String filePath) throws IOException;

    /**
     * Carica gli utenti da file CSV.
     *
     * @param filePath il percorso del file da cui caricare
     * @throws IOException se ci sono problemi di I/O
     */
    void leggiDaFile(String filePath) throws IOException;

    /**
     * Carica gli utenti da un BufferedReader (per risorse interne).
     *
     * @param reader il reader da cui leggere i dati
     * @throws IOException se ci sono problemi di I/O
     */
    void leggiDaReader(java.io.BufferedReader reader) throws IOException;
}
