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
        // Carica automaticamente gli annunci dal file all'avvio
        caricaAnnunciAuto();

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
                0. Esci
                """);
    }

    // Carica automaticamente gli annunci dal file all'avvio
    private void caricaAnnunciAuto() {
        String filePath = "file/annunci.csv"; // Percorso del file da caricare automaticamente
        try {
            Bacheca bachecaLetta = gestoreBacheca.leggiDaFile(filePath);
            System.out.println("Annunci caricati automaticamente da: " + filePath);
            System.out.println("Annunci presenti:");
            for (Annuncio a : bachecaLetta.getAnnunci()) {
                System.out.println(a);
            }
        } catch (IOException e) {
            System.out.println("Errore di I/O durante il caricamento automatico degli annunci: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore logico durante il caricamento automatico degli annunci: " + e.getMessage());
        }
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
        String inputParole = Input.readString("Inserisci parole chiave (separate da virgola): ");
        List<String> paroleChiave = List.of(inputParole.split(","));

        List<Annuncio> risultati = gestoreBacheca.cercaPerParoleChiave(new ArrayList<>(paroleChiave));
        if (risultati.isEmpty()) {
            System.out.println("Nessun annuncio trovato con le parole chiave inserite.");
        } else {
            System.out.println("=== Annunci trovati ===");
            for (Annuncio annuncio : risultati) {
                System.out.println(annuncio);
            }
        }
    }

    // Rimuove tutti gli annunci dalla bacheca
    private void pulisciBacheca() {
        gestoreBacheca.pulisciBacheca();
        System.out.println("Bacheca pulita (annunci rimossi).");
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
        gestoreBacheca.stampaTuttiAnnunci();
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
}
