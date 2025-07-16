package bacheca;

// AGGIUNGERE CLASSE UTENTI CON ARRAYLIST<UTENTE>
// (Potresti avere una struttura esterna che gestisce una lista di Utente,
//  ad esempio "GestoreUtenti" o simile. Per ora, la classe Utente rimane autonoma.)

/**
 * Classe che modella l'entità "Utente".
 * Ogni utente è identificato da un'email univoca e ha un nome.
 */
public class Utente {

    // ---------------------------------------------------------
    // DICHIARAZIONE DEI CAMPI
    // ---------------------------------------------------------

    /**
     * L'email dell'utente. È "final" perché non vogliamo
     * che cambi una volta creato l'oggetto (l'email funge da identificatore univoco).
     */
    private final String email;

    /**
     * Il nome dell'utente. Non è final perché può essere modificato
     * dopo la creazione dell'utente (tramite setNome).
     *
     * (In passato era final, ma magari ora si vuole permettere la modifica.)
     */
    private String nome;

    // Opzionalmente si potrebbe introdurre un numero di telefono (commentato).
    // private String numTelefono;

    /**
     * Espressione regolare (regex) usata per validare il formato dell'email.
     * In questo caso:
     * - Prima della chiocciola (@): caratteri alfanumerici e i simboli ._%+-
     * - Dominio: alfanumerico, trattino, punto
     * - TLD: 2 o più caratteri alfabetici (ad es. "com", "it", "org")
     */
    private static final String EMAIL_ER = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    // ---------------------------------------------------------
    // COSTRUTTORE
    // ---------------------------------------------------------

    /**
     * Costruttore che inizializza l'Utente con email e nome.
     *
     * @param email L'email dell'utente (non può essere null né invalida)
     * @param nome  Il nome dell'utente (non può essere null né vuoto)
     * @throws IllegalArgumentException se l'email è invalida o il nome è nullo/vuoto
     */
    public Utente(String email, String nome) {
        // 1. Controllo se l'email rispetta la regex, altrimenti lancio eccezione.
        if (!checkEmail(email)) {
            throw new IllegalArgumentException("L'email non è valida. Riprovare!");
        }

        // 2. Controllo se il nome è nullo o vuoto.
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome deve contenere almeno un carattere.");
        }

        // 3. Assegno i valori (trim per rimuovere eventuali spazi prima/dopo il nome).
        this.nome = nome.trim();
        this.email = email;
    }

    // ---------------------------------------------------------
    // GETTER
    // ---------------------------------------------------------

    /**
     * Restituisce l'email dell'utente.
     *
     * @return L'email registrata per questo utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome corrente dell'utente.
     */
    public String getNome() {
        return nome;
    }

    // ---------------------------------------------------------
    // SETTER
    // ---------------------------------------------------------

    /**
     * Aggiorna il nome dell'utente.
     *
     * @param nome Nuovo nome, non nullo né vuoto.
     * @throws IllegalArgumentException se il nome è nullo o vuoto
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome deve contenere almeno un carattere.");
        }
        this.nome = nome;
    }

    // ---------------------------------------------------------
    // METODO PRIVATO DI VALIDAZIONE EMAIL
    // ---------------------------------------------------------

    /**
     * Metodo di utilità che verifica se l'email passata rispetta
     * l'espressione regolare definita in EMAIL_ER.
     *
     * @param email L'email da verificare
     * @return true se valida, false altrimenti
     */
    private static boolean checkEmail(String email) {
        // 1. Se l'email è null, torna subito false.
        if (email == null) return false;
        // 2. Altrimenti verifica il match con la regex.
        return email.matches(EMAIL_ER);
    }

    // ---------------------------------------------------------
    // OVERRIDE DEI METODI EREDITATI DA OBJECT
    // ---------------------------------------------------------

    /**
     * Verifica l'uguaglianza tra due Utente.
     * Vengono considerati uguali se hanno la stessa email.
     *
     * @param obj L'oggetto da confrontare
     * @return true se sono lo stesso oggetto o se l'email coincide, altrimenti false
     */
    @Override
    public boolean equals(Object obj) {
        // 1. Se sono lo stesso riferimento in memoria, true.
        if (this == obj)
            return true;
        // 2. Se l'altro oggetto è null o di classe diversa, false.
        if (obj == null || getClass() != obj.getClass())
            return false;
        // 3. Cast a Utente e confronto email.
        Utente utente = (Utente) obj;
        //    N.B.: se volessi ignorare maiuscole/minuscole, potresti usare:
        //    return email.equalsIgnoreCase(utente.email);
        return email.equals(utente.email);
    }

    /**
     * Fornisce una rappresentazione in stringa di un utente,
     * utile per debugging o stampe di log.
     *
     * @return Stringa con email e nome.
     */
    @Override
    public String toString() {
        return "Utente{" + "email='" + email + '\'' + ", nome='" + nome + '\'' + '}';
    }
}
