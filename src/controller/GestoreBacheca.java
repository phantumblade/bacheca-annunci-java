package controller;

import java.io.IOException;
import java.util.List;

import bacheca.Annuncio;
import bacheca.Bacheca;
import eccezioni.GestoreBachecaException;

/**
 * Interfaccia che definisce i metodi per gestire la Bacheca di annunci.
 * I metodi possono sollevare eccezioni specifiche di tipo
 * GestoreBachecaException o IOException (per operazioni su file).
 */
public abstract interface GestoreBacheca {

    /**
     * Aggiunge un Annuncio alla Bacheca.
     * @throws GestoreBachecaException se l'annuncio è nullo
     *         o non può essere aggiunto.
     */
    void aggiungiAnnuncio(Annuncio annuncio) throws GestoreBachecaException;

    /**
     * Elimina tutti gli annunci presenti nella Bacheca.
     */
    void pulisciBacheca();

    /**
    * Stampa (o elenca) tutti gli annunci attualmente presenti in bacheca.
    */
    void stampaTuttiAnnunci();

    /**
     * Rimuove un Annuncio cercandolo per ID,
     * solo se l'utente corrispondente è il proprietario.
     * @return true se la rimozione avviene, false altrimenti
     * @throws GestoreBachecaException per errori logici di rimozione
     */
    boolean rimuoviAnnuncio(int id, String emailUtente) throws GestoreBachecaException;

    /**
     * Salva gli annunci della Bacheca su file.
     * @throws IOException se si verifica un errore di scrittura
     * @throws GestoreBachecaException se la bacheca è in uno stato non salvabile
     */
    void salvaSuFile(String filePath) throws IOException, GestoreBachecaException;

    /**
     * Legge e ricostruisce la Bacheca da un file.
     * @return l'oggetto Bacheca ricostruito
     * @throws IOException se si verifica un errore di lettura
     * @throws GestoreBachecaException se il file contiene dati invalidi
     */
    Bacheca leggiDaFile(String filePath) throws IOException, GestoreBachecaException;

    /**
     * Cerca tutti gli annunci contenenti almeno
     * una delle parole chiave specificate.
     * @return lista di annunci che corrispondono ai criteri
     */
    List<Annuncio> cercaPerParoleChiave(List<String> paroleChiave);

    // Cerca un annuncio specifico tramite ID
    Annuncio cercaAnnuncioPerId(int id);

    // Aggiunge nuove parole chiave a un annuncio esistente
    void aggiungiParoleChiave(int id, List<String> nuoveParole) throws GestoreBachecaException;

    /*
     * ---------------------------
     * Metodi Di accesso per la GUI
     * ---------------------------
     */
    Bacheca getBacheca();


}
