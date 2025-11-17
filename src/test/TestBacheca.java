package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bacheca.Bacheca;
import bacheca.Annuncio;
import bacheca.AnnuncioAcquisto;
import bacheca.AnnuncioVendita;
import bacheca.Utente;
import eccezioni.BachecaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe Bacheca.
 * Verifica la corretta gestione della collezione di annunci.
 */
public class TestBacheca {
    
    private Bacheca bacheca;
    private Utente utente1;
    private Utente utente2;
    
    @BeforeEach
    public void setUp() {
        bacheca = new Bacheca();
        utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        utente2 = new Utente("giulia.verdi@email.com", "Giulia Verdi");
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
        
        assertDoesNotThrow(() -> bacheca.aggiungiAnnuncio(annuncio));
        assertEquals(1, bacheca.getAnnunci().length);
    }
    
    // Test per verificare che venga lanciata un'eccezione con annuncio nullo
    @Test
    public void testAggiungiAnnuncioNullo() {
        Exception exception = assertThrows(BachecaException.class, () -> {
            bacheca.aggiungiAnnuncio(null);
        });
        
        assertEquals("Annuncio non può essere nullo", exception.getMessage());
    }
    
    // Test per verificare l'aggiunta di più annunci
    @Test
    public void testAggiungiPiuAnnunci() {
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente2,
            Arrays.asList("tablet")
        );
        
        assertDoesNotThrow(() -> bacheca.aggiungiAnnuncio(annuncio1));
        assertDoesNotThrow(() -> bacheca.aggiungiAnnuncio(annuncio2));
        
        assertEquals(2, bacheca.getAnnunci().length);
    }
    
    // Test per verificare la rimozione di un annuncio esistente
    @Test
    public void testRimuoviAnnuncioEsistente() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco smartphone",
            "Cerco smartphone Android",
            300.0,
            utente1,
            Arrays.asList("smartphone", "android")
        );
        
        bacheca.aggiungiAnnuncio(annuncio);
        assertEquals(1, bacheca.getAnnunci().length);
        
        boolean rimosso = bacheca.rimuoviAnnuncio(annuncio.getId(), utente1);
        assertTrue(rimosso);
        assertEquals(0, bacheca.getAnnunci().length);
    }
    
    // Test per verificare la rimozione di un annuncio inesistente
    @Test
    public void testRimuoviAnnuncioInesistente() {
        boolean rimosso = bacheca.rimuoviAnnuncio(999, utente1);
        assertFalse(rimosso);
        assertEquals(0, bacheca.getAnnunci().length);
    }
    
    // Test per verificare la ricerca per parole chiave
    @Test
    public void testCercaPerParoleChiave() {
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer", "lavoro")
        );
        
        AnnuncioVendita annuncio2 = new AnnuncioVendita(
            "Vendo smartphone",
            "Smartphone usato in ottime condizioni",
            250.0,
            utente2,
            Arrays.asList("smartphone", "telefono"),
            false,
            LocalDate.now().plusDays(10)
        );
        
        AnnuncioAcquisto annuncio3 = new AnnuncioAcquisto(
            "Cerco computer",
            "Cerco computer da gaming",
            1200.0,
            utente2,
            Arrays.asList("computer", "gaming", "desktop")
        );
        
        bacheca.aggiungiAnnuncio(annuncio1);
        bacheca.aggiungiAnnuncio(annuncio2);
        bacheca.aggiungiAnnuncio(annuncio3);
        
        ArrayList<Annuncio> risultati = bacheca.cercaPerParoleChiave(new ArrayList<>(Arrays.asList("computer")));
        assertEquals(2, risultati.size());
        assertTrue(risultati.contains(annuncio1));
        assertTrue(risultati.contains(annuncio3));
    }
    
    // Test per verificare la ricerca con parole chiave non trovate
    @Test
    public void testCercaPerParoleChiaveNonTrovate() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        bacheca.aggiungiAnnuncio(annuncio);
        
        ArrayList<Annuncio> risultati = bacheca.cercaPerParoleChiave(new ArrayList<>(Arrays.asList("bicicletta")));
        assertEquals(0, risultati.size());
    }
    
    // Test per verificare la ricerca con parole chiave nulle
    @Test
    public void testCercaPerParoleChiaveNulle() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop", "computer")
        );
        
        bacheca.aggiungiAnnuncio(annuncio);
        
        assertThrows(BachecaException.class, () -> {
            bacheca.cercaPerParoleChiave(null);
        });
    }
    
    // Test per verificare lo svuotamento della bacheca
    @Test
    public void testSvuotaBacheca() {
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
        
        bacheca.aggiungiAnnuncio(annuncio1);
        bacheca.aggiungiAnnuncio(annuncio2);
        assertEquals(2, bacheca.getAnnunci().length);
        
        bacheca.svuotaBacheca();
        assertEquals(0, bacheca.getAnnunci().length);
    }
    
    // Test per verificare l'iterazione con for-each
    @Test
    public void testIterazioneBacheca() {
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
        
        bacheca.aggiungiAnnuncio(annuncio1);
        bacheca.aggiungiAnnuncio(annuncio2);
        
        int contatore = 0;
        for (Annuncio annuncio : bacheca) {
            assertNotNull(annuncio);
            contatore++;
        }
        
        assertEquals(2, contatore);
    }
    
    // Test per verificare getAnnunci()
    @Test
    public void testGetAnnunci() {
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        bacheca.aggiungiAnnuncio(annuncio);
        
        Annuncio[] annunci = bacheca.getAnnunci();
        assertEquals(1, annunci.length);
        assertTrue(Arrays.asList(annunci).contains(annuncio));
    }
    
    // Test per verificare il numero di annunci
    @Test
    public void testGetNumeroAnnunci() {
        assertEquals(0, bacheca.getAnnunci().length);
        
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente1,
            Arrays.asList("laptop")
        );
        
        bacheca.aggiungiAnnuncio(annuncio1);
        assertEquals(1, bacheca.getAnnunci().length);
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente2,
            Arrays.asList("tablet")
        );
        
        bacheca.aggiungiAnnuncio(annuncio2);
        assertEquals(2, bacheca.getAnnunci().length);
    }
}