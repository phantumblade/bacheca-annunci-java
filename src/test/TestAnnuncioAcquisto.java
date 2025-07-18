package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import bacheca.AnnuncioAcquisto;
import bacheca.Utente;
import eccezioni.DatiNonValidiException;

import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe AnnuncioAcquisto.
 * Verifica la corretta creazione, validazione e comportamento degli annunci di acquisto.
 */
public class TestAnnuncioAcquisto {

    // Test per verificare la creazione di un annuncio di acquisto valido
    @Test
    public void testCreazioneAnnuncioAcquistoValido() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        List<String> paroleChiave = Arrays.asList("laptop", "computer", "usato");
        
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Cerco laptop usato",
            "Cerco laptop per lavoro, budget massimo 800€",
            800.0,
            utente,
            paroleChiave
        );
        
        assertEquals("Cerco laptop usato", annuncio.getTitolo());
        assertEquals("Cerco laptop per lavoro, budget massimo 800€", annuncio.getDescrizione());
        assertEquals(800.0, annuncio.getPrezzo());
        assertEquals(utente, annuncio.getUtente());
        assertEquals(paroleChiave, annuncio.getParoleChiave());
    }
    
    // Test per verificare la creazione con ID personalizzato
    @Test
    public void testCreazioneAnnuncioAcquistoConID() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        List<String> paroleChiave = Arrays.asList("smartphone", "telefono");
        
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            100,
            "Cerco smartphone",
            "Cerco smartphone Android recente",
            300.0,
            utente,
            paroleChiave
        );
        
        assertEquals(100, annuncio.getId());
        assertEquals("Cerco smartphone", annuncio.getTitolo());
        assertEquals(300.0, annuncio.getPrezzo());
    }
    
    // Test per verificare che venga lanciata un'eccezione con titolo nullo
    @Test
    public void testCreazioneAnnuncioTitoloNullo() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioAcquisto(
                null,
                "Descrizione valida",
                500.0,
                utente,
                Arrays.asList("test")
            );
        });
        
        assertEquals("Titolo non può essere nullo o vuoto.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con descrizione nulla
    @Test
    public void testCreazioneAnnuncioDescrizioneNulla() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioAcquisto(
                "Titolo valido",
                null,
                500.0,
                utente,
                Arrays.asList("test")
            );
        });
        
        assertEquals("Descrizione non può essere nulla o vuota.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con prezzo negativo
    @Test
    public void testCreazioneAnnuncioPrezzoNegativo() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioAcquisto(
                "Titolo valido",
                "Descrizione valida",
                -100.0,
                utente,
                Arrays.asList("test")
            );
        });
        
        assertEquals("Prezzo non può essere negativo.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con utente nullo
    @Test
    public void testCreazioneAnnuncioUtenteNullo() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioAcquisto(
                "Titolo valido",
                "Descrizione valida",
                500.0,
                null,
                Arrays.asList("test")
            );
        });
        
        assertEquals("Utente non può essere nullo.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con parole chiave nulle
    @Test
    public void testCreazioneAnnuncioParoleChiaveNulle() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioAcquisto(
                "Titolo valido",
                "Descrizione valida",
                500.0,
                utente,
                null
            );
        });
        
        assertEquals("Parole chiave non possono essere nulle.", exception.getMessage());
    }
    
    // Test per verificare il metodo equals() con annunci uguali
    @Test
    public void testEqualsAnnunciUguali() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        List<String> paroleChiave = Arrays.asList("tablet", "ipad");
        
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            1,
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente,
            paroleChiave
        );
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            1,
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente,
            paroleChiave
        );
        
        assertEquals(annuncio1, annuncio2);
    }
    
    // Test per verificare il metodo equals() con annunci diversi
    @Test
    public void testEqualsAnnunciDiversi() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            1,
            "Cerco tablet",
            "Cerco tablet per studio",
            400.0,
            utente,
            Arrays.asList("tablet")
        );
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            2,
            "Cerco laptop",
            "Cerco laptop per lavoro",
            800.0,
            utente,
            Arrays.asList("laptop")
        );
        
        assertNotEquals(annuncio1, annuncio2);
    }
    
    // Test per verificare il metodo toString()
    @Test
    public void testToString() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        List<String> paroleChiave = Arrays.asList("bicicletta", "mountain bike");
        
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            5,
            "Cerco mountain bike",
            "Cerco mountain bike usata",
            300.0,
            utente,
            paroleChiave
        );
        
        String expectedOutput = "Annuncio{ID=5, Titolo='Cerco mountain bike', Prezzo=300.0, Utente='Compratore', Parole Chiave=[bicicletta, mountain bike]}";
        assertEquals(expectedOutput, annuncio.toString());
    }
}