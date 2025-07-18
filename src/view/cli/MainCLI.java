package view.cli;

import controller.GestoreBacheca;
import controller.GestoreUtenti;
import eccezioni.GestoreBachecaException;
import bacheca.Annuncio;
import bacheca.AnnuncioAcquisto;
import bacheca.AnnuncioVendita;
import bacheca.Bacheca;
import bacheca.Utente;
import jbook.util.Input;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che fornisce un'interfaccia testuale (CLI)
 * per interagire con la Bacheca di annunci (AnnuncioAcquisto, AnnuncioVendita)
 * e con la gestione utenti.
 */
public class MainCLI {

    // Riferimenti ai gestori della bacheca e degli utenti
    private final GestoreBacheca gestoreBacheca;
    private final GestoreUtenti gestoreUtenti;

    // Costruttore della classe CLI
    public MainCLI(GestoreBacheca gestoreBacheca, GestoreUtenti gestoreUtenti) {
        this.gestoreBacheca = gestoreBacheca;
        this.gestoreUtenti = gestoreUtenti;
    }

    // Metodo principale che avvia l'interfaccia CLI
    public void avvia() {
        // Mostra banner di benvenuto
        stampaBanner();
        
        // Carica automaticamente i dati dal file all'avvio
        caricaDatiIniziali();
        
        // Mostra statistiche iniziali
        mostraStatisticheIniziali();

        boolean esegui = true;
        while (esegui) {
            stampaMenu(); // Mostra il menu delle opzioni
            int scelta = leggiIntero("Scegli un'opzione: "); // Legge la scelta dell'utente

            // Gestisce le diverse opzioni del menu
            switch (scelta) {
                case 1 -> aggiungiAnnuncio();         // Aggiunge un nuovo annuncio
                case 2 -> cercaAnnuncio();           // Cerca annunci per parole chiave
                case 3 -> pulisciBacheca();          // Pulisce tutti gli annunci dalla bacheca
                case 4 -> rimuoviAnnuncio();         // Rimuove un annuncio specifico
                case 5 -> salvaBachecaSuFile();      // Salva la bacheca su file
                case 6 -> leggiBachecaDaFile();      // Legge la bacheca da un file
                case 7 -> stampaTuttiAnnunci();      // Stampa tutti gli annunci
                case 8 -> aggiungiNuovoUtente();     // Registra un nuovo utente
                case 9 -> aggiungiParoleChiave();    // Aggiunge parole chiave a un annuncio esistente
                case 10 -> rimuoviUtente();          // Rimuove un utente dal sistema
                case 11 -> cercaUtente();            // Cerca un utente per email
                case 12 -> visualizzaUtenti();       // Visualizza tutti gli utenti registrati
                case 0 -> {
                    System.out.println("Uscita dal sistema.");
                    esegui = false; // Termina l'esecuzione
                }
                default -> System.out.println("Opzione non valida!");
            }
        }
    }

    // Stampa il menu delle opzioni disponibili
    private void stampaMenu() {
        System.out.println("""
                === Menu Bacheca Annunci ===
                1. Aggiungi Annuncio (Acquisto o Vendita)
                2. Cerca Annuncio
                3. Pulisci Bacheca
                4. Rimuovi Annuncio
                5. Salva Bacheca su File
                6. Leggi Bacheca da File
                7. Stampa Tutti gli Annunci
                8. Aggiungi Nuovo Utente
                9. Aggiungi Parole Chiave a un Annuncio Esistente
                10. Rimuovi Utente
                11. Cerca Utente
                12. Visualizza Tutti gli Utenti
                0. Esci
                """);
    }

    // Stampa il banner di benvenuto
    private void stampaBanner() {
        System.out.println("============================================================");
        System.out.println("                 BACHECA ANNUNCI CLI");
        System.out.println("============================================================");
        System.out.println();
    }
    
    // Carica automaticamente utenti e annunci dai file all'avvio
    private void caricaDatiIniziali() {
        System.out.println("Caricamento dati iniziali...");
        
        // Carica utenti
        caricaUtentiAuto();
        
        // Carica annunci
        caricaAnnunciAuto();
        
        System.out.println();
    }
    
    // Carica automaticamente gli utenti dal file all'avvio
    private void caricaUtentiAuto() {
        String filePath = "data/utenti.csv";
        try {
            gestoreUtenti.leggiDaFile(filePath);
            System.out.println("Utenti caricati da: " + filePath);
        } catch (IOException e) {
            System.out.println("File utenti non trovato: " + filePath + " (primo avvio)");
        } catch (Exception e) {
            System.out.println("Errore nel caricamento utenti: " + e.getMessage());
        }
    }

    // Carica automaticamente gli annunci dal file all'avvio
    private void caricaAnnunciAuto() {
        String filePath = "data/annunci.csv";
        try {
            gestoreBacheca.leggiDaFile(filePath);
            System.out.println("Annunci caricati da: " + filePath);
        } catch (IOException e) {
            System.out.println("File annunci non trovato: " + filePath + " (primo avvio)");
        } catch (Exception e) {
            System.out.println("Errore nel caricamento annunci: " + e.getMessage());
        }
    }
    
    // Mostra le statistiche iniziali del sistema
    private void mostraStatisticheIniziali() {
        System.out.println("STATISTICHE SISTEMA:");
        System.out.println("------------------------------");
        
        // Conta utenti
        int numUtenti = gestoreUtenti.getUtentiRegistrati().size();
        System.out.println("Utenti registrati: " + numUtenti);
        
        // Conta annunci
        Annuncio[] annunci = gestoreBacheca.getBacheca().getAnnunci();
        int numAnnunci = annunci.length;
        System.out.println("Annunci totali: " + numAnnunci);
        
        // Conta per tipo di annuncio
        int annunciVendita = 0;
        int annunciAcquisto = 0;
        for (Annuncio a : annunci) {
            if (a instanceof bacheca.AnnuncioVendita) {
                annunciVendita++;
            } else if (a instanceof bacheca.AnnuncioAcquisto) {
                annunciAcquisto++;
            }
        }
        
        System.out.println("- Annunci vendita: " + annunciVendita);
        System.out.println("- Annunci acquisto: " + annunciAcquisto);
        
        System.out.println("------------------------------");
        System.out.println();
    }

    // Legge un intero da input con gestione degli errori
    private int leggiIntero(String messaggio) {
        while (true) {
            try {
                return Input.readInt(messaggio);
            } catch (NumberFormatException e) {
                System.out.println("Inserisci un numero valido.");
            }
        }
    }

    // Aggiunge un nuovo annuncio alla bacheca
    private void aggiungiAnnuncio() {
        String email = Input.readString("Inserisci la tua email (utente già registrato): ");
        Utente utente = gestoreUtenti.cercaUtente(email);
        if (utente == null) {
            System.out.println("Utente non trovato. Registrati prima di aggiungere un annuncio.");
            return;
        }

        String tipo = Input.readString("Tipo di annuncio (acquisto/vendita): ").toLowerCase().trim();
        String titolo = Input.readString("Titolo dell'annuncio: ");
        String descrizione = Input.readString("Descrizione: ");
        double prezzo = Input.readDouble("Prezzo: ");
        String rawParole = Input.readString("Parole chiave (separate da virgola): ");
        List<String> paroleChiave = List.of(rawParole.split(","));

        try {
            Annuncio nuovo;
            if (tipo.equals("acquisto")) {
                // Crea un annuncio di acquisto
                nuovo = new AnnuncioAcquisto(titolo, descrizione, prezzo, utente, new ArrayList<>(paroleChiave));
            } else if (tipo.equals("vendita")) {
                // Crea un annuncio di vendita con data di scadenza
                LocalDate scadenza = leggiDataScadenza();
                nuovo = new AnnuncioVendita(titolo, descrizione, prezzo, utente, new ArrayList<>(paroleChiave), false, scadenza);
            } else {
                System.out.println("Tipo non riconosciuto. Inserire 'acquisto' o 'vendita'.");
                return;
            }

            gestoreBacheca.aggiungiAnnuncio(nuovo);
            System.out.println("Annuncio di tipo " + tipo + " aggiunto con successo.");

        } catch (Exception e) {
            System.out.println("Errore durante la creazione/aggiunta dell'annuncio: " + e.getMessage());
        }
    }

    // Legge una data di scadenza dall'utente
    private LocalDate leggiDataScadenza() {
        while (true) {
            String inserita = Input.readString("Inserisci la data di scadenza (yyyy-mm-dd): ");
            try {
                return LocalDate.parse(inserita.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato data non valido. Riprova (esempio: 2025-12-31).");
            }
        }
    }

    // Cerca annunci usando parole chiave inserite dall'utente
    private void cercaAnnuncio() {
        System.out.println("=== RICERCA ANNUNCI ===");
        String inputParole = Input.readString("Inserisci parole chiave (separate da virgola): ");
        List<String> paroleChiave = List.of(inputParole.split(","));

        List<Annuncio> risultati = gestoreBacheca.cercaPerParoleChiave(new ArrayList<>(paroleChiave));
        if (risultati.isEmpty()) {
            System.out.println("Nessun annuncio trovato con le parole chiave: " + String.join(", ", paroleChiave));
        } else {
            System.out.println("Trovati " + risultati.size() + " annunci con le parole chiave: " + String.join(", ", paroleChiave));
            System.out.println("------------------------------------------------------------");
            
            for (Annuncio annuncio : risultati) {
                if (annuncio instanceof bacheca.AnnuncioVendita) {
                    bacheca.AnnuncioVendita av = (bacheca.AnnuncioVendita) annuncio;
                    System.out.println("VENDITA - ID: " + av.getId() + " | " + av.getTitolo() + " | EUR " + av.getPrezzo());
                    System.out.println("   Venditore: " + av.getUtente().getNome() + " (" + av.getUtente().getEmail() + ")");
                    System.out.println("   Descrizione: " + av.getDescrizione());
                    System.out.println("   Venduto: " + (av.isVenduto() ? "Si" : "No") + " | Scadenza: " + av.getDataScadenza());
                } else if (annuncio instanceof bacheca.AnnuncioAcquisto) {
                    bacheca.AnnuncioAcquisto aa = (bacheca.AnnuncioAcquisto) annuncio;
                    System.out.println("ACQUISTO - ID: " + aa.getId() + " | " + aa.getTitolo() + " | Budget: EUR " + aa.getPrezzo());
                    System.out.println("   Acquirente: " + aa.getUtente().getNome() + " (" + aa.getUtente().getEmail() + ")");
                    System.out.println("   Descrizione: " + aa.getDescrizione());
                }
                System.out.println("   Parole chiave: " + String.join(", ", annuncio.getParoleChiave()));
                System.out.println();
            }
        }
    }

    // Rimuove tutti gli annunci dalla bacheca
    private void pulisciBacheca() {
        System.out.println("=== PULIZIA BACHECA ===");
        int annunciPrima = gestoreBacheca.getBacheca().getAnnunci().length;
        
        if (annunciPrima == 0) {
            System.out.println("La bacheca e' gia' vuota.");
            return;
        }
        
        gestoreBacheca.pulisciBacheca();
        System.out.println("Bacheca pulita! Rimossi " + annunciPrima + " annunci.");
    }

    // Rimuove un annuncio specifico usando ID e email del proprietario
    private void rimuoviAnnuncio() {
        int id = leggiIntero("Inserisci l'ID dell'annuncio da rimuovere: ");
        String email = Input.readString("Inserisci l'email del proprietario: ");

        try {
            boolean rimosso = gestoreBacheca.rimuoviAnnuncio(id, email);
            if (rimosso) {
                System.out.println("Annuncio rimosso con successo.");
            } else {
                System.out.println("Annuncio non rimosso (verifica ID e email).");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la rimozione: " + e.getMessage());
        }
    }

    // Salva la bacheca su un file specificato dall'utente
    private void salvaBachecaSuFile() {
        String filePath = Input.readString("Inserisci il percorso del file dove salvare: ");
        try {
            gestoreBacheca.salvaSuFile(filePath);
            System.out.println("Bacheca salvata correttamente su: " + filePath);
        } catch (IOException e) {
            System.out.println("Errore di I/O durante il salvataggio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore logico durante il salvataggio: " + e.getMessage());
        }
    }

    // Legge la bacheca da un file specificato dall'utente
    private void leggiBachecaDaFile() {
        String filePath = Input.readString("Inserisci il percorso del file da cui leggere: ");
        try {
            Bacheca bachecaLetta = gestoreBacheca.leggiDaFile(filePath);
            System.out.println("Bacheca caricata correttamente da: " + filePath);
            System.out.println("Annunci caricati:");
            for (Annuncio a : bachecaLetta.getAnnunci()) {
                System.out.println(a);
            }
        } catch (IOException e) {
            System.out.println("Errore di I/O durante la lettura del file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore logico durante la lettura del file: " + e.getMessage());
        }
    }

    // Stampa tutti gli annunci presenti nella bacheca
    private void stampaTuttiAnnunci() {
        System.out.println("=== TUTTI GLI ANNUNCI ===");
        System.out.println();
        
        Annuncio[] annunci = gestoreBacheca.getBacheca().getAnnunci();
        if (annunci.length == 0) {
            System.out.println("Nessun annuncio presente nella bacheca.");
            return;
        }
        
        // Separa annunci di vendita e acquisto
        List<bacheca.AnnuncioVendita> annunciVendita = new ArrayList<>();
        List<bacheca.AnnuncioAcquisto> annunciAcquisto = new ArrayList<>();
        
        for (Annuncio a : annunci) {
            if (a instanceof bacheca.AnnuncioVendita) {
                annunciVendita.add((bacheca.AnnuncioVendita) a);
            } else if (a instanceof bacheca.AnnuncioAcquisto) {
                annunciAcquisto.add((bacheca.AnnuncioAcquisto) a);
            }
        }
        
        // Stampa annunci di vendita
        if (!annunciVendita.isEmpty()) {
            System.out.println("ANNUNCI DI VENDITA (" + annunciVendita.size() + "):");
            System.out.println("--------------------------------------------------");
            for (bacheca.AnnuncioVendita av : annunciVendita) {
                System.out.println("ID: " + av.getId() + " | " + av.getTitolo() + " | EUR " + av.getPrezzo());
                System.out.println("   Venditore: " + av.getUtente().getNome() + " (" + av.getUtente().getEmail() + ")");
                System.out.println("   Descrizione: " + av.getDescrizione());
                System.out.println("   Venduto: " + (av.isVenduto() ? "Si" : "No") + " | Scadenza: " + av.getDataScadenza());
                System.out.println("   Parole chiave: " + String.join(", ", av.getParoleChiave()));
                System.out.println();
            }
        }
        
        // Stampa annunci di acquisto
        if (!annunciAcquisto.isEmpty()) {
            System.out.println("ANNUNCI DI ACQUISTO (" + annunciAcquisto.size() + "):");
            System.out.println("--------------------------------------------------");
            for (bacheca.AnnuncioAcquisto aa : annunciAcquisto) {
                System.out.println("ID: " + aa.getId() + " | " + aa.getTitolo() + " | Budget: EUR " + aa.getPrezzo());
                System.out.println("   Acquirente: " + aa.getUtente().getNome() + " (" + aa.getUtente().getEmail() + ")");
                System.out.println("   Descrizione: " + aa.getDescrizione());
                System.out.println("   Parole chiave: " + String.join(", ", aa.getParoleChiave()));
                System.out.println();
            }
        }
    }

    // Metodo per aggiungere un nuovo utente al sistema
    private void aggiungiNuovoUtente() {
        System.out.println("=== Registrazione Nuovo Utente ===");

        String email = Input.readString("Inserisci la tua email: ").trim();
        if (gestoreUtenti.cercaUtente(email) != null) {
            System.out.println("Utente già registrato con questa email.");
            return;
        }

        String nome = Input.readString("Inserisci il tuo nome: ").trim();

        try {
            Utente nuovoUtente = new Utente(email, nome);
            gestoreUtenti.aggiungiUtente(nuovoUtente);
            System.out.println("Utente registrato con successo!");
        } catch (Exception e) {
            System.out.println("Errore durante la registrazione dell'utente: " + e.getMessage());
        }
    }

    // Metodo per aggiungere parole chiave a un annuncio esistente
    private void aggiungiParoleChiave() {
        String email = Input.readString("Inserisci la tua email per modificare un annuncio: ");
        int id = leggiIntero("Inserisci l'ID dell'annuncio a cui aggiungere parole chiave: ");

        // Verifica che l'annuncio appartenga all'utente
        Annuncio annuncio = gestoreBacheca.cercaAnnuncioPerId(id);
        if (annuncio == null || !annuncio.getUtente().getEmail().equals(email)) {
            System.out.println("Annuncio non trovato o non appartiene a questo utente.");
            return;
        }

        String nuoveParole = Input.readString("Inserisci nuove parole chiave (separate da virgola): ");
        List<String> paroleChiaveAggiunte = List.of(nuoveParole.split(","));

        try {
            // Prova ad aggiungere le nuove parole chiave
            gestoreBacheca.aggiungiParoleChiave(id, paroleChiaveAggiunte);
            System.out.println("Parole chiave aggiunte con successo!");
        } catch (GestoreBachecaException e) {
            // Gestione dell'errore se l'aggiunta fallisce
            System.out.println("Errore durante l'aggiunta delle parole chiave: " + e.getMessage());
        }
    }

    // Metodo per rimuovere un utente dal sistema
    private void rimuoviUtente() {
        System.out.println("=== Rimozione Utente ===");

        String email = Input.readString("Inserisci l'email dell'utente da rimuovere: ").trim();

        try {
            boolean rimosso = gestoreUtenti.rimuoviUtente(email);
            if (rimosso) {
                System.out.println("Utente rimosso con successo!");
            } else {
                System.out.println("Utente non trovato con questa email.");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la rimozione dell'utente: " + e.getMessage());
        }
    }

    // Metodo per cercare un utente per email
    private void cercaUtente() {
        System.out.println("=== Ricerca Utente ===");

        String email = Input.readString("Inserisci l'email dell'utente da cercare: ").trim();

        try {
            Utente utente = gestoreUtenti.cercaUtente(email);
            if (utente != null) {
                System.out.println("Utente trovato:");
                System.out.println("Email: " + utente.getEmail());
                System.out.println("Nome: " + utente.getNome());
            } else {
                System.out.println("Utente non trovato con questa email.");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca dell'utente: " + e.getMessage());
        }
    }

    // Metodo per visualizzare tutti gli utenti registrati
    private void visualizzaUtenti() {
        System.out.println("=== TUTTI GLI UTENTI REGISTRATI ===");

        try {
            List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
            if (utenti.isEmpty()) {
                System.out.println("Nessun utente registrato nel sistema.");
            } else {
                System.out.println("Utenti registrati (" + utenti.size() + "):");
                System.out.println("--------------------------------------------------");
                
                for (Utente utente : utenti) {
                    // Conta gli annunci dell'utente
                    int annunciUtente = 0;
                    for (Annuncio a : gestoreBacheca.getBacheca().getAnnunci()) {
                        if (a.getUtente().equals(utente)) {
                            annunciUtente++;
                        }
                    }
                    
                    System.out.println("Nome: " + utente.getNome());
                    System.out.println("   Email: " + utente.getEmail());
                    System.out.println("   Annunci pubblicati: " + annunciUtente);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante la visualizzazione degli utenti: " + e.getMessage());
        }
    }
}
