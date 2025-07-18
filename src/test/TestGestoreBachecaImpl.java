package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import controller.GestoreBachecaImpl;
import controller.GestoreUtentiImpl;
import bacheca.Bacheca;
import bacheca.Annuncio;
import bacheca.AnnuncioAcquisto;
import bacheca.AnnuncioVendita;
import bacheca.Utente;
import eccezioni.GestoreBachecaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe GestoreBachecaImpl.
 * Verifica la corretta gestione della business logic della bacheca.
 */
public class TestGestoreBachecaImpl {
    
    private GestoreBachecaImpl gestoreBacheca;
    private GestoreUtentiImpl gestoreUtenti;
    private Bacheca bacheca;
    private Utente utente1;
    private Utente utente2;
    
    @BeforeEach
    public void setUp() {
        bacheca = new Bacheca();
        gestoreUtenti = new GestoreUtentiImpl();
        gestoreBacheca = new GestoreBachecaImpl(bacheca, gestoreUtenti);
        
        utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        utente2 = new Utente("giulia.verdi@email.com", "Giulia Verdi");
        
        gestoreUtenti.aggiungiUtente(utente1);
        gestoreUtenti.aggiungiUtente(utente2);
    }
    
    // Test per verificare l'aggiunta di un annuncio valido
    @Test
    public void testAggiungiAnnuncioValido() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        assertDoesNotThrow(() -> gestoreBacheca.aggiungiAnnuncio(annuncio));
        assertEquals(1, bacheca.getNumeroAnnunci());
    }
    
    // Test per verificare che venga lanciata un'eccezione con annuncio nullo
    @Test
    public void testAggiungiAnnuncioNullo() {
        Exception exception = assertThrows(GestoreBachecaException.class, () -> {
            gestoreBacheca.aggiungiAnnuncio(null);
        });
        
        assertEquals("L'annuncio non può essere nullo.", exception.getMessage());
    }
    
    // Test per verificare la pulizia della bacheca
    @Test
    public void testPulisciBacheca() {
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente2,
            Arrays.asList("tablet")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio1);
        gestoreBacheca.aggiungiAnnuncio(annuncio2);
        assertEquals(2, bacheca.getNumeroAnnunci());
        
        gestoreBacheca.pulisciBacheca();
        assertEquals(0, bacheca.getNumeroAnnunci());
    }
    
    // Test per verificare la rimozione di un annuncio dal proprietario
    @Test
    public void testRimuoviAnnuncioProprietario() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio);
        
        boolean rimosso = gestoreBacheca.rimuoviAnnuncio(annuncio.getId(), "mario.rossi@email.com");
        assertTrue(rimosso);
        assertEquals(0, bacheca.getNumeroAnnunci());
    }
    
    // Test per verificare che non si possa rimuovere un annuncio di altri utenti
    @Test
    public void testRimuoviAnnuncioNonProprietario() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio);
        
        boolean rimosso = gestoreBacheca.rimuoviAnnuncio(annuncio.getId(), "giulia.verdi@email.com");
        assertFalse(rimosso);
        assertEquals(1, bacheca.getNumeroAnnunci());
    }
    
    // Test per verificare la rimozione di un annuncio inesistente
    @Test
    public void testRimuoviAnnuncioInesistente() {
        boolean rimosso = gestoreBacheca.rimuoviAnnuncio(999, "mario.rossi@email.com");
        assertFalse(rimosso);
    }
    
    // Test per verificare la ricerca per parole chiave
    @Test
    public void testCercaPerParoleChiave() {
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        AnnuncioVendita annuncio2 = new AnnuncioVendita(
            "Vendo computer",
            "Computer da gaming",
            1200.0,
            utente2,
            Arrays.asList("computer", "gaming"),
            false,
            LocalDate.now().plusDays(10)
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio1);
        gestoreBacheca.aggiungiAnnuncio(annuncio2);
        
        List<Annuncio> risultati = gestoreBacheca.cercaPerParoleChiave(Arrays.asList("computer"));
        assertEquals(2, risultati.size());
    }
    
    // Test per verificare la ricerca di un annuncio per ID
    @Test
    public void testCercaAnnuncioPerId() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio);
        
        Annuncio trovato = gestoreBacheca.cercaAnnuncioPerId(annuncio.getId());
        assertNotNull(trovato);
        assertEquals(annuncio, trovato);
    }
    
    // Test per verificare la ricerca di un annuncio inesistente per ID
    @Test
    public void testCercaAnnuncioPerIdInesistente() {
        Annuncio trovato = gestoreBacheca.cercaAnnuncioPerId(999);
        assertNull(trovato);
    }
    
    // Test per verificare l'aggiunta di parole chiave a un annuncio
    @Test
    public void testAggiungiParoleChiave() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio);
        
        List<String> nuoveParole = Arrays.asList("usato", "offerta");
        assertDoesNotThrow(() -> gestoreBacheca.aggiungiParoleChiave(annuncio.getId(), nuoveParole));
        
        Annuncio aggiornato = gestoreBacheca.cercaAnnuncioPerId(annuncio.getId());
        List<String> paroleChiave = aggiornato.getParoleChiave();
        
        assertTrue(paroleChiave.contains("laptop"));
        assertTrue(paroleChiave.contains("computer"));
        assertTrue(paroleChiave.contains("usato"));
        assertTrue(paroleChiave.contains("offerta"));
    }
    
    // Test per verificare l'aggiunta di parole chiave a un annuncio inesistente
    @Test
    public void testAggiungiParoleChiaveAnnuncioInesistente() {
        List<String> nuoveParole = Arrays.asList("usato", "offerta");
        
        Exception exception = assertThrows(GestoreBachecaException.class, () -> {
            gestoreBacheca.aggiungiParoleChiave(999, nuoveParole);
        });
        
        assertEquals("Annuncio con ID 999 non trovato.", exception.getMessage());
    }
    
    // Test per verificare la lettura da BufferedReader
    @Test
    public void testLeggiDaReader() throws Exception {
        String csvContent = "ID;Titolo;Descrizione;Prezzo;Email;Parole Chiave;Data Scadenza;Venduto\n" +
                           "1;Vendo laptop;Laptop in ottime condizioni;800.0;mario.rossi@email.com;laptop,computer;2025-12-31;false\n" +
                           "2;Cerco smartphone;Cerco iPhone usato;400.0;giulia.verdi@email.com;smartphone,iphone\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreBacheca.leggiDaReader(reader));
        
        assertEquals(2, bacheca.getNumeroAnnunci());
        
        Annuncio annuncio1 = gestoreBacheca.cercaAnnuncioPerId(1);
        assertNotNull(annuncio1);
        assertEquals("Vendo laptop", annuncio1.getTitolo());
        assertTrue(annuncio1 instanceof AnnuncioVendita);
        
        Annuncio annuncio2 = gestoreBacheca.cercaAnnuncioPerId(2);
        assertNotNull(annuncio2);
        assertEquals("Cerco smartphone", annuncio2.getTitolo());
        assertTrue(annuncio2 instanceof AnnuncioAcquisto);
    }
    
    // Test per verificare la lettura da BufferedReader con dati non validi
    @Test
    public void testLeggiDaReaderDatiNonValidi() throws Exception {
        String csvContent = "ID;Titolo;Descrizione;Prezzo;Email;Parole Chiave\n" +
                           "1;Vendo laptop;Laptop in ottime condizioni;800.0;mario.rossi@email.com;laptop,computer\n" +
                           "dati-non-validi;Titolo;Descrizione;Prezzo;Email;Parole\n" +  // Riga non valida
                           "2;Cerco smartphone;Cerco iPhone usato;400.0;giulia.verdi@email.com;smartphone,iphone\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreBacheca.leggiDaReader(reader));
        
        // Dovrebbe avere solo 2 annunci (quello non valido viene ignorato)
        assertEquals(2, bacheca.getNumeroAnnunci());
    }
    
    // Test per verificare getBacheca()
    @Test
    public void testGetBacheca() {
        Bacheca bachecaRitornata = gestoreBacheca.getBacheca();
        assertNotNull(bachecaRitornata);
        assertEquals(bacheca, bachecaRitornata);
    }
    
    // Test per verificare stampaTuttiAnnunci() (non lancia eccezioni)
    @Test
    public void testStampaTuttiAnnunci() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        gestoreBacheca.aggiungiAnnuncio(annuncio);
        
        // Questo test verifica solo che il metodo non lanci eccezioni
        assertDoesNotThrow(() -> gestoreBacheca.stampaTuttiAnnunci());
    }
    
    // Test per verificare che venga lanciata un'eccezione con bacheca nulla nel costruttore
    @Test
    public void testCostruttoreBachecaNulla() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GestoreBachecaImpl(null, gestoreUtenti);
        });
        
        assertEquals("Bacheca non può essere null", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con gestore utenti nullo nel costruttore
    @Test
    public void testCostruttoreGestoreUtentiNullo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new GestoreBachecaImpl(bacheca, null);
        });
        
        assertEquals("GestoreUtenti non può essere null", exception.getMessage());
    }
}