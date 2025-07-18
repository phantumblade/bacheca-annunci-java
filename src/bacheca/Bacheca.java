package bacheca;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import eccezioni.BachecaException;

/**
 * Classe che rappresenta una singola bacheca di annunci.
 * Gestisce la raccolta di Annuncio, consentendo aggiunta, ricerca
 * e rimozione degli stessi.
 *
 * Implementa l'interfaccia Iterable<Annuncio> per permettere
 * il for-each sugli annunci.
 */
public class Bacheca implements Iterable<Annuncio> {

    // ------------------------------------------------------------------
    // CAMPO: una lista di Annuncio che rappresenta
    //        tutti gli annunci contenuti in questa bacheca.
    // ------------------------------------------------------------------
    private final ArrayList<Annuncio> annunci;

    // ------------------------------------------------------------------
    // COSTRUTTORE: inizializza la lista degli annunci vuota.
    // ------------------------------------------------------------------
    public Bacheca() {
        // 1. Inizializza la lista ArrayList per memorizzare gli annunci.
        this.annunci = new ArrayList<>();
    }

    // ------------------------------------------------------------------
    // METODO: aggiunge un Annuncio alla bacheca
    // ------------------------------------------------------------------
    /**
     * Aggiunge un nuovo Annuncio se non già presente,
     * incrementando il contatore statico idCounter nella classe Annuncio.
     *
     * @param annuncio l'Annuncio da aggiungere
     * @throws BachecaException se l'Annuncio è nullo o già presente
     */
    public void aggiungiAnnuncio(Annuncio annuncio) {
        // 1. Controllo che l'annuncio non sia nullo.
        if (annuncio == null) {
            throw new BachecaException("Annuncio non può essere nullo");
        }

        // 2. Incremento del contatore statico idCounter nella classe Annuncio.
        //    (Nota: dipende dalla logica in Annuncio, se lo gestisci qui o nel costruttore di Annuncio)
        Annuncio.getNextId(); // gestito in gestore bacheca,  (temporaneo)

        // 3. Verifica se l'annuncio è già presente (equals).
        //    Se non è presente, lo aggiunge, altrimenti lancia un'eccezione.
        if (!annunci.contains(annuncio)) {
            annunci.add(annuncio);
        } else {
            throw new BachecaException("Annuncio già presente in bacheca");
        }
    }

    // ------------------------------------------------------------------
    // METODO: ricerca di Annuncio in base a parole chiave
    // ------------------------------------------------------------------
    /**
     * Cerca gli annunci che contengono almeno una delle parole chiave fornite.
     * La ricerca è case-sensitive (distingue maiuscole/minuscole).
     *
     * @param paroleChiave lista di parole chiave da ricercare
     * @return lista di annunci che contengono almeno una parola chiave
     * @throws BachecaException se la lista di parole chiave è nulla o vuota
     */
    public ArrayList<Annuncio> cercaPerParoleChiave(ArrayList<String> paroleChiave) {
        // 1. Verifica che la lista di parole chiave non sia nulla o vuota.
        if (paroleChiave == null || paroleChiave.isEmpty()) {
            throw new BachecaException("Lista parole chiave non può essere nulla o vuota");
        }

        // 2. Utilizza un Set per evitare di aggiungere due volte
        //    lo stesso Annuncio (in caso contenga più parole chiave).
        Set<Annuncio> risultati = new HashSet<>();

        // 3. Per ogni annuncio nella bacheca...
        for (Annuncio annuncio : annunci) {
            // 4. ...controlla ogni parola chiave.
            for (String parola : paroleChiave) {
                // 5. Se la parola chiave è presente tra le parole dell'annuncio,
                //    aggiunge l'annuncio ai risultati.
                if (annuncio.getParoleChiave().contains(parola)) {
                    risultati.add(annuncio);
                }
            }
        }

        // 6. Restituisce la lista degli annunci (convertendo il Set in ArrayList).
        return new ArrayList<>(risultati);
    }

    // ------------------------------------------------------------------
    // METODI: rimozione di Annuncio
    // ------------------------------------------------------------------
    /**
     * Rimuove un Annuncio identificato dall'id, ma solo se l'utente specificato
     * è il proprietario dell'annuncio.
     *
     * @param id     l'id dell'annuncio da rimuovere
     * @param utente l'utente proprietario che richiede la rimozione
     * @return true se la rimozione è avvenuta, false altrimenti
     */
    public boolean rimuoviAnnuncio(int id, Utente utente) {
        // Rimuove l'annuncio se corrisponde sia per id che per proprietario (Utente).
        return annunci.removeIf(
            annuncio -> annuncio.getId() == id && annuncio.getUtente().equals(utente)
        );
    }

    /**
     * Rimuove in blocco (bulk) tutti gli annunci presenti nella lista fornita
     * (senza controllare se l'utente ne è proprietario).
     *
     * @param rimossi lista di annunci da rimuovere
     * @return true se almeno un annuncio è stato effettivamente rimosso,
     *         false in caso contrario
     */
    public boolean rimuoviAnnuncio(List<Annuncio> rimossi) {
        // 1. Se la lista è nulla o vuota, non rimuoviamo nulla e restituiamo false.
        if (rimossi == null || rimossi.isEmpty()) {
            return false;
        }
        // 2. Rimuove tutti gli annunci contenuti nella lista 'rimossi'.
        return annunci.removeAll(rimossi);
    }

    // ------------------------------------------------------------------
    // METODI: supporto per lettura degli annunci
    // ------------------------------------------------------------------
    /**
     * Restituisce un array di tutti gli Annuncio presenti in bacheca.
     *
     * @return array di Annuncio
     */
    public Annuncio[] getAnnunci() {
        // 1. Converte la lista in un array di Annuncio (comodo se servono array "puri").
        return annunci.toArray(new Annuncio[0]);
    }

    public void svuotaBacheca() {
        annunci.clear();  // Rimuove tutti gli annunci dalla lista
        Annuncio.resetIdCounter(1);  // Resetta il contatore ID a 1 (opzionale)
    }

    // ------------------------------------------------------------------
    // ITERATORE: permette l'uso di for-each sugli annunci
    // ------------------------------------------------------------------
    @Override
    public Iterator<Annuncio> iterator() {
        // Restituisce l'iteratore su annunci
        return annunci.iterator();
    }

}
