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
 * 
 * Testa tutte le funzionalità della classe AnnuncioAcquisto e quelle ereditate da Annuncio:
 * - Costruttori (con e senza ID)
 * - Getter: getId(), getTitolo(), getDescrizione(), getPrezzo(), getUtente(), getParoleChiave()
 * - Setter: setTitolo(), setDescrizione(), setPrezzo(), setParoleChiave()
 * - Metodi utili: contieneParolaChiave(), equals(), hashCode(), toString()
 * - Validazioni con eccezioni personalizzate DatiNonValidiException
 * - Test di successo e fallimento per ogni funzionalità
 */
public class TestAnnuncioAcquisto {

    /**
     * Test di successo: verifica che sia possibile creare un annuncio di acquisto valido
     * con tutti i parametri corretti.
     * Testa: costruttore senza ID, tutti i getter
     */
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
        
        assertEquals("Il titolo non può essere vuoto o nullo.", exception.getMessage());
    }
    
    // Test per verificare che venga lanciata un'eccezione con descrizione nulla
    @Test
    public void testCreazioneAnnuncioDescrizioneNulla() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo valido",
            null,
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        // Il test dovrebbe passare perché la descrizione nulla non lancia eccezione
        // ma assegna un valore predefinito
        assertEquals("Nessuna descrizione disponibile.", annuncio.getDescrizione());
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
        
        assertEquals("Il prezzo non può essere negativo.", exception.getMessage());
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
        
        assertEquals("L'utente associato all'annuncio non può essere nullo.", exception.getMessage());
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
        
        assertEquals("La lista di parole chiave non può essere vuota o nulla.", exception.getMessage());
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
        
        String actualOutput = annuncio.toString();
        assertTrue(actualOutput.contains("Titolo: Cerco mountain bike"));
        assertTrue(actualOutput.contains("Prezzo: 300.0"));
        assertTrue(actualOutput.contains("Utente: buyer@email.com"));
    }
    
    // Test per verificare il metodo setTitolo() con titolo valido
    @Test
    public void testSetTitoloValido() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo originale",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        annuncio.setTitolo("Nuovo titolo");
        
        assertEquals("Nuovo titolo", annuncio.getTitolo());
    }
    
    // Test per verificare che setTitolo() lanci eccezione con titolo nullo
    @Test
    public void testSetTitoloNullo() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo originale",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setTitolo(null);
        });
        
        assertEquals("Il titolo non può essere vuoto o nullo.", exception.getMessage());
        assertEquals("Titolo originale", annuncio.getTitolo());
    }
    
    // Test per verificare che setTitolo() lanci eccezione con titolo vuoto
    @Test
    public void testSetTitoloVuoto() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo originale",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setTitolo("");
        });
        
        assertEquals("Il titolo non può essere vuoto o nullo.", exception.getMessage());
        assertEquals("Titolo originale", annuncio.getTitolo());
    }
    
    // Test per verificare il metodo setDescrizione() con descrizione valida
    @Test
    public void testSetDescrizioneValida() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione originale",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        annuncio.setDescrizione("Nuova descrizione");
        
        assertEquals("Nuova descrizione", annuncio.getDescrizione());
    }
    
    // Test per verificare che setDescrizione() assegni valore predefinito con descrizione nulla
    @Test
    public void testSetDescrizioneNulla() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione originale",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        annuncio.setDescrizione(null);
        
        assertEquals("Nessuna descrizione disponibile.", annuncio.getDescrizione());
    }
    
    // Test per verificare il metodo setPrezzo() con prezzo valido
    @Test
    public void testSetPrezzoValido() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        annuncio.setPrezzo(750.0);
        
        assertEquals(750.0, annuncio.getPrezzo());
    }
    
    // Test per verificare che setPrezzo() lanci eccezione con prezzo negativo
    @Test
    public void testSetPrezzoNegativo() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setPrezzo(-100.0);
        });
        
        assertEquals("Il prezzo non può essere negativo.", exception.getMessage());
        assertEquals(500.0, annuncio.getPrezzo());
    }
    
    // Test per verificare il metodo setParoleChiave() con parole chiave valide
    @Test
    public void testSetParoleChiaveValide() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        List<String> nuoveParole = Arrays.asList("nuovo", "parola", "chiave");
        annuncio.setParoleChiave(nuoveParole);
        
        assertEquals(nuoveParole, annuncio.getParoleChiave());
    }
    
    // Test per verificare che setParoleChiave() lanci eccezione con lista nulla
    @Test
    public void testSetParoleChiaveNulle() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setParoleChiave(null);
        });
        
        assertEquals("La lista di parole chiave non può essere vuota o nulla.", exception.getMessage());
    }
    
    // Test per verificare il metodo contieneParolaChiave()
    @Test
    public void testContieneParolaChiave() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        AnnuncioAcquisto annuncio = new AnnuncioAcquisto(
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("laptop", "computer", "usato")
        );
        
        assertTrue(annuncio.contieneParolaChiave("laptop"));
        assertTrue(annuncio.contieneParolaChiave("computer"));
        assertFalse(annuncio.contieneParolaChiave("tablet"));
    }
    
    // Test per verificare il metodo hashCode()
    @Test
    public void testHashCode() {
        Utente utente = new Utente("buyer@email.com", "Compratore");
        
        AnnuncioAcquisto annuncio1 = new AnnuncioAcquisto(
            1,
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        AnnuncioAcquisto annuncio2 = new AnnuncioAcquisto(
            1,
            "Titolo",
            "Descrizione",
            500.0,
            utente,
            Arrays.asList("test")
        );
        
        assertEquals(annuncio1.hashCode(), annuncio2.hashCode());
    }
}