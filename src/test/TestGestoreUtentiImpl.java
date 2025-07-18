package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.List;

import controller.GestoreUtentiImpl;
import bacheca.Utente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe GestoreUtentiImpl.
 * Verifica la corretta gestione degli utenti registrati.
 */
public class TestGestoreUtentiImpl {
    
    private GestoreUtentiImpl gestoreUtenti;
    
    @BeforeEach
    public void setUp() {
        gestoreUtenti = new GestoreUtentiImpl();
    }
    
    // Test per verificare l'aggiunta di un utente valido
    @Test
    public void testAggiungiUtenteValido() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        assertDoesNotThrow(() -> gestoreUtenti.aggiungiUtente(utente));
        
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(1, utenti.size());
        assertTrue(utenti.contains(utente));
    }
    
    // Test per verificare che venga lanciata un'eccezione con utente nullo
    @Test
    public void testAggiungiUtenteNullo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestoreUtenti.aggiungiUtente(null);
        });
        
        assertEquals("L'utente non può essere nullo.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con utente duplicato
    @Test
    public void testAggiungiUtenteDuplicato() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("mario.rossi@email.com", "Mario Bianchi");
        
        gestoreUtenti.aggiungiUtente(utente1);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestoreUtenti.aggiungiUtente(utente2);
        });
        
        assertEquals("Utente con email 'mario.rossi@email.com' già registrato.", exception.getMessage());
    }
    
    // Test per verificare la rimozione di un utente esistente
    @Test
    public void testRimuoviUtenteEsistente() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        gestoreUtenti.aggiungiUtente(utente);
        
        boolean rimosso = gestoreUtenti.rimuoviUtente("mario.rossi@email.com");
        assertTrue(rimosso);
        
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(0, utenti.size());
    }
    
    // Test per verificare la rimozione di un utente inesistente
    @Test
    public void testRimuoviUtenteInesistente() {
        boolean rimosso = gestoreUtenti.rimuoviUtente("inesistente@email.com");
        assertFalse(rimosso);
    }
    
    // Test per verificare la ricerca di un utente esistente
    @Test
    public void testCercaUtenteEsistente() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        gestoreUtenti.aggiungiUtente(utente);
        
        Utente trovato = gestoreUtenti.cercaUtente("mario.rossi@email.com");
        assertNotNull(trovato);
        assertEquals(utente, trovato);
    }
    
    // Test per verificare la ricerca di un utente inesistente
    @Test
    public void testCercaUtenteInesistente() {
        Utente trovato = gestoreUtenti.cercaUtente("inesistente@email.com");
        assertNull(trovato);
    }
    
    // Test per verificare la ricerca con email nulla
    @Test
    public void testCercaUtenteEmailNulla() {
        Utente trovato = gestoreUtenti.cercaUtente(null);
        assertNull(trovato);
    }
    
    // Test per verificare la lista degli utenti registrati
    @Test
    public void testGetUtentiRegistrati() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("giulia.verdi@email.com", "Giulia Verdi");
        
        gestoreUtenti.aggiungiUtente(utente1);
        gestoreUtenti.aggiungiUtente(utente2);
        
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(2, utenti.size());
        assertTrue(utenti.contains(utente1));
        assertTrue(utenti.contains(utente2));
    }
    
    // Test per verificare la lista vuota inizialmente
    @Test
    public void testGetUtentiRegistratiVuota() {
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(0, utenti.size());
    }
    
    // Test per verificare la lettura da BufferedReader
    @Test
    public void testLeggiDaReader() throws IOException {
        String csvContent = "email,nome\n" +
                           "mario.rossi@email.com,Mario Rossi\n" +
                           "giulia.verdi@email.com,Giulia Verdi\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreUtenti.leggiDaReader(reader));
        
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(2, utenti.size());
        
        Utente mario = gestoreUtenti.cercaUtente("mario.rossi@email.com");
        assertNotNull(mario);
        assertEquals("Mario Rossi", mario.getNome());
        
        Utente giulia = gestoreUtenti.cercaUtente("giulia.verdi@email.com");
        assertNotNull(giulia);
        assertEquals("Giulia Verdi", giulia.getNome());
    }
    
    // Test per verificare la lettura da BufferedReader con dati non validi
    @Test
    public void testLeggiDaReaderDatiNonValidi() throws IOException {
        String csvContent = "email,nome\n" +
                           "mario.rossi@email.com,Mario Rossi\n" +
                           "email-non-valida,Nome Valido\n" +  // Email non valida
                           "giulia.verdi@email.com,Giulia Verdi\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreUtenti.leggiDaReader(reader));
        
        // Dovrebbe avere solo 2 utenti (quello non valido viene ignorato)
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(2, utenti.size());
        
        // Verifica che gli utenti validi siano stati caricati
        assertNotNull(gestoreUtenti.cercaUtente("mario.rossi@email.com"));
        assertNotNull(gestoreUtenti.cercaUtente("giulia.verdi@email.com"));
    }
    
    // Test per verificare la lettura da BufferedReader con header mancante
    @Test
    public void testLeggiDaReaderSenzaHeader() throws IOException {
        String csvContent = "mario.rossi@email.com,Mario Rossi\n" +
                           "giulia.verdi@email.com,Giulia Verdi\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreUtenti.leggiDaReader(reader));
        
        // Il primo utente viene saltato perché considerato header
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(1, utenti.size());
        
        // Solo il secondo utente dovrebbe essere presente
        assertNotNull(gestoreUtenti.cercaUtente("giulia.verdi@email.com"));
    }
    
    // Test per verificare la lettura da BufferedReader vuoto
    @Test
    public void testLeggiDaReaderVuoto() throws IOException {
        String csvContent = "";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        
        assertDoesNotThrow(() -> gestoreUtenti.leggiDaReader(reader));
        
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(0, utenti.size());
    }
    
    // Test per verificare che la lettura pulisca la lista esistente
    @Test
    public void testLeggiDaReaderPulisceListaEsistente() throws IOException {
        // Aggiungi un utente manualmente
        Utente utente = new Utente("esistente@email.com", "Utente Esistente");
        gestoreUtenti.aggiungiUtente(utente);
        assertEquals(1, gestoreUtenti.getUtentiRegistrati().size());
        
        // Carica da reader
        String csvContent = "email,nome\n" +
                           "nuovo@email.com,Nuovo Utente\n";
        
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));
        gestoreUtenti.leggiDaReader(reader);
        
        // La lista dovrebbe contenere solo l'utente dal file
        List<Utente> utenti = gestoreUtenti.getUtentiRegistrati();
        assertEquals(1, utenti.size());
        assertNotNull(gestoreUtenti.cercaUtente("nuovo@email.com"));
        assertNull(gestoreUtenti.cercaUtente("esistente@email.com"));
    }
}