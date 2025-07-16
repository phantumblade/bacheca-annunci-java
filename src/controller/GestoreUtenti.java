package controller;

import java.util.List;
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
}
