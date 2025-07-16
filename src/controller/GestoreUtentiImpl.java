package controller;

import java.util.ArrayList;
import java.util.List;

import bacheca.Utente;

/**
 * Classe che implementa l'interfaccia GestoreUtenti
 * e gestisce la lista di utenti registrati.
 *
 * In questa implementazione, usiamo un ArrayList statico
 * per memorizzare gli utenti. Significa che la lista è unica
 * e condivisa da tutte le eventuali istanze di GestoreUtentiImpl
 * (se ne crei più di una). Questo potrebbe andare bene per un
 * progetto piccolo, ma in un sistema reale potresti preferire
 * un approccio "non static" o un database.
 */
public class GestoreUtentiImpl implements GestoreUtenti {

    /**
     * Lista (statica) che contiene tutti gli utenti registrati.
     * Se il progetto richiede più sessioni di gestori o un diverso
     * ciclo di vita, potresti preferire rimuovere "static" e renderla
     * un campo d'istanza.
     *
     * Anche una HashMap<String, Utente> (con chiave = email)
     * potrebbe essere preferibile se vuoi ricerche più rapide per email.
     */
    private static final List<Utente> UTENTI_REGISTRATI = new ArrayList<>();

    // ------------------------------------------------------------------
    // METODO: aggiungiUtente (ri-nominato per chiarezza)
    // ------------------------------------------------------------------
    /**
     * Aggiunge un nuovo utente alla lista, dopo aver controllato:
     * - Che l'oggetto utente non sia null
     * - Che non esista già nella lista un utente con la stessa email
     *
     * @param utente l'utente da aggiungere
     * @throws IllegalArgumentException se l'utente è null o la sua email è già presente
     */
    @Override
    public void aggiungiUtente(Utente utente) {
        // 1. Verifica che l'utente non sia null
        if (utente == null) {
            throw new IllegalArgumentException("Utente non può essere nullo");
        }

        // 2. Controlla se l'email di questo utente esiste già
        for(Utente iter : UTENTI_REGISTRATI) {
            if(iter.getEmail().equals(utente.getEmail())) {
                throw new IllegalArgumentException("Email già presente: " + utente.getEmail());
            }
        }

        // 3. Se i controlli vanno a buon fine, l'utente viene aggiunto
        UTENTI_REGISTRATI.add(utente);
    }

    // ------------------------------------------------------------------
    // METODO: rimuoviUtente
    // ------------------------------------------------------------------
    /**
     * Rimuove il primo utente trovato nella lista (se presente)
     * la cui email coincide con quella fornita.
     *
     * @param email l'email dell'utente da rimuovere
     * @return true se l'utente è stato rimosso, false se non presente
     */
    @Override
    public boolean rimuoviUtente(String email) {
        // removeIf restituisce true se almeno un elemento è stato rimosso
        return UTENTI_REGISTRATI.removeIf(
            iter -> iter.getEmail().equals(email)
        );
    }

    // ------------------------------------------------------------------
    // METODO: cercaUtente
    // ------------------------------------------------------------------
    /**
     * Cerca un utente in base all'email fornita.
     *
     * @param email l'email da cercare
     * @return l'utente corrispondente o null se non trovato
     */
    @Override
    public Utente cercaUtente(String email) {
        // Cicla la lista e restituisce l'utente se trova un match
        for(Utente iter : UTENTI_REGISTRATI) {
            if(iter.getEmail().equals(email)) {
                return iter;
            }
        }
        // Se non trova corrispondenza, restituisce null
        return null;
    }

    // ------------------------------------------------------------------
    // METODO: getUtentiRegistrati
    // ------------------------------------------------------------------
    /**
     * Restituisce una copia della lista di tutti gli utenti registrati.
     *
     * @return una nuova ArrayList contenente gli utenti
     */
    @Override
    public List<Utente> getUtentiRegistrati() {
        // Ritorniamo una nuova lista per evitare che dall'esterno
        // qualcuno modifichi direttamente l'array statico.
        return new ArrayList<>(UTENTI_REGISTRATI);
    }
}
