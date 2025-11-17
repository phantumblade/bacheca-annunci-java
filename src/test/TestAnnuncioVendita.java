package test;

import static org.junit.jupiter.api.Assertions.*;  // Importa le asserzioni di JUnit

import java.time.LocalDate;
import java.util.Arrays;

import bacheca.AnnuncioVendita;
import bacheca.Utente;
import eccezioni.DatiNonValidiException;

import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe AnnuncioVendita.
 * Verifica la corretta creazione, validazione e comportamento degli annunci di vendita.
 * 
 * Testa tutte le funzionalità specifiche della classe AnnuncioVendita e quelle ereditate da Annuncio:
 * - Costruttori (senza ID, con ID, con bypassValidazione)
 * - Getter e Setter: getDataScadenza(), setDataScadenza(), isVenduto(), setVenduto()
 * - Metodi utili: isScaduto(), toString()
 * - Validazioni della data di scadenza con DatiNonValidiException
 * - Test di successo e fallimento per ogni funzionalità
 */
public class TestAnnuncioVendita {

    /**
     * Test di successo: verifica che sia possibile creare un annuncio di vendita valido
     * con ID specificato e tutti i parametri corretti.
     * Testa: costruttore con ID, getDataScadenza(), isVenduto(), isScaduto()
     */
    @Test
    public void testCreazioneAnnuncioValido() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScadenza = LocalDate.now().plusDays(10);  // Data futura

        AnnuncioVendita annuncio = new AnnuncioVendita(
            1,
            "Laptop in vendita",
            "Ottime condizioni",
            500.0,
            utente,
            Arrays.asList("elettronica", "computer"),
            false,
            dataScadenza
        );

        assertEquals("Laptop in vendita", annuncio.getTitolo());
        assertFalse(annuncio.isVenduto());
        assertFalse(annuncio.isScaduto());
        assertEquals(dataScadenza, annuncio.getDataScadenza());
    }

    // Test per verificare che venga lanciata un'eccezione se la data di scadenza è nulla
    @Test
    public void testCreazioneAnnuncioDataNulla() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");

        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioVendita(
                2,
                "Bicicletta da corsa",
                "Perfetta per allenamento",
                300.0,
                utente,
                Arrays.asList("sport", "bicicletta"),
                false,
                null  // Data nulla
            );
        });

        assertEquals("La data di scadenza non può essere nulla o nel passato.", exception.getMessage());
    }

    // Test per verificare che venga lanciata un'eccezione se la data di scadenza è nel passato
    @Test
    public void testCreazioneAnnuncioDataNelPassato() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScaduta = LocalDate.now().minusDays(1);  // Data nel passato

        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioVendita(
                3,
                "Vecchio televisore",
                "Funzionante ma datato",
                50.0,
                utente,
                Arrays.asList("elettronica", "tv"),
                false,
                dataScaduta
            );
        });

        assertEquals("La data di scadenza non può essere nulla o nel passato.", exception.getMessage());
    }

    // Test per verificare il metodo toString()
    @Test
    public void testToString() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScadenza = LocalDate.now().plusDays(5);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            4,
            "Monitor 4K",
            "Usato ma in ottime condizioni",
            200.0,
            utente,
            Arrays.asList("elettronica", "monitor"),
            false,
            dataScadenza
        );

        String actualOutput = annuncio.toString();
        assertTrue(actualOutput.contains("Titolo: Monitor 4K"));
        assertTrue(actualOutput.contains("Prezzo: 200.0"));
        assertTrue(actualOutput.contains("Utente: test@email.com"));
        assertTrue(actualOutput.contains("venduto= false"));
        assertTrue(actualOutput.contains("scadenza= " + dataScadenza));
    }
    
    /**
     * Test di successo: verifica che sia possibile creare un annuncio di vendita
     * usando il costruttore senza ID.
     * Testa: costruttore senza ID
     */
    @Test
    public void testCreazioneAnnuncioSenzaID() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScadenza = LocalDate.now().plusDays(7);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Smartphone",
            "Ultimo modello",
            400.0,
            utente,
            Arrays.asList("telefono", "smartphone"),
            false,
            dataScadenza
        );

        assertEquals("Smartphone", annuncio.getTitolo());
        assertFalse(annuncio.isVenduto());
        assertEquals(dataScadenza, annuncio.getDataScadenza());
        assertTrue(annuncio.getId() > 0); // ID generato automaticamente
    }
    
    /**
     * Test di successo: verifica che sia possibile creare un annuncio di vendita
     * usando il costruttore con bypassValidazione per date nel passato.
     * Testa: costruttore con bypassValidazione
     */
    @Test
    public void testCreazioneAnnuncioConBypassValidazione() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScaduta = LocalDate.now().minusDays(5); // Data nel passato

        // Con bypassValidazione = true, dovrebbe funzionare
        AnnuncioVendita annuncio = new AnnuncioVendita(
            5,
            "Vecchio annuncio",
            "Caricato dal file",
            100.0,
            utente,
            Arrays.asList("usato"),
            true,
            dataScaduta,
            true // bypassValidazione
        );

        assertEquals("Vecchio annuncio", annuncio.getTitolo());
        assertTrue(annuncio.isVenduto());
        assertEquals(dataScaduta, annuncio.getDataScadenza());
        assertTrue(annuncio.isScaduto());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore con bypassValidazione = false
     * lanci eccezione con data nel passato.
     * Testa: costruttore con bypassValidazione = false
     */
    @Test
    public void testCreazioneAnnuncioConBypassValidazioneFalse() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScaduta = LocalDate.now().minusDays(5); // Data nel passato

        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new AnnuncioVendita(
                6,
                "Annuncio scaduto",
                "Non dovrebbe funzionare",
                100.0,
                utente,
                Arrays.asList("test"),
                false,
                dataScaduta,
                false // bypassValidazione = false
            );
        });

        assertEquals("La data di scadenza non può essere nulla o nel passato.", exception.getMessage());
    }
    
    /**
     * Test di successo: verifica che setDataScadenza() modifichi correttamente
     * la data di scadenza con una data valida.
     * Testa: setDataScadenza() con data valida
     */
    @Test
    public void testSetDataScadenzaValida() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataIniziale = LocalDate.now().plusDays(5);
        LocalDate nuovaData = LocalDate.now().plusDays(10);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Prodotto",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false,
            dataIniziale
        );

        annuncio.setDataScadenza(nuovaData);
        
        assertEquals(nuovaData, annuncio.getDataScadenza());
    }
    
    /**
     * Test di fallimento: verifica che setDataScadenza() lanci eccezione
     * quando viene passata una data nel passato.
     * Testa: setDataScadenza() con data nel passato
     */
    @Test
    public void testSetDataScadenzaNelPassato() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataIniziale = LocalDate.now().plusDays(5);
        LocalDate dataPassata = LocalDate.now().minusDays(1);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Prodotto",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false,
            dataIniziale
        );

        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setDataScadenza(dataPassata);
        });

        assertEquals("La data di scadenza non può essere nel passato.", exception.getMessage());
        assertEquals(dataIniziale, annuncio.getDataScadenza()); // Data originale immutata
    }
    
    /**
     * Test di fallimento: verifica che setDataScadenza() lanci eccezione
     * quando viene passata una data nulla.
     * Testa: setDataScadenza() con data nulla
     */
    @Test
    public void testSetDataScadenzaNulla() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataIniziale = LocalDate.now().plusDays(5);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Prodotto",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false,
            dataIniziale
        );

        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            annuncio.setDataScadenza(null);
        });

        assertEquals("La data di scadenza non può essere nel passato.", exception.getMessage());
        assertEquals(dataIniziale, annuncio.getDataScadenza()); // Data originale immutata
    }
    
    /**
     * Test di successo: verifica che setVenduto() modifichi correttamente
     * lo stato di vendita dell'annuncio.
     * Testa: setVenduto() e isVenduto()
     */
    @Test
    public void testSetVenduto() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScadenza = LocalDate.now().plusDays(5);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Prodotto",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false, // Non venduto inizialmente
            dataScadenza
        );

        assertFalse(annuncio.isVenduto());
        
        annuncio.setVenduto(true);
        assertTrue(annuncio.isVenduto());
        
        annuncio.setVenduto(false);
        assertFalse(annuncio.isVenduto());
    }
    
    /**
     * Test di successo: verifica che isScaduto() restituisca true
     * per annunci con data di scadenza passata.
     * Testa: isScaduto() con data passata
     */
    @Test
    public void testIsScadutoConDataPassata() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataScaduta = LocalDate.now().minusDays(1);

        // Usa bypassValidazione per creare annuncio scaduto
        AnnuncioVendita annuncio = new AnnuncioVendita(
            10,
            "Prodotto scaduto",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false,
            dataScaduta,
            true // bypassValidazione
        );

        assertTrue(annuncio.isScaduto());
    }
    
    /**
     * Test di successo: verifica che isScaduto() restituisca false
     * per annunci con data di scadenza futura.
     * Testa: isScaduto() con data futura
     */
    @Test
    public void testIsScadutoConDataFutura() {
        Utente utente = new Utente("test@email.com", "Mario Rossi");
        LocalDate dataFutura = LocalDate.now().plusDays(10);

        AnnuncioVendita annuncio = new AnnuncioVendita(
            "Prodotto valido",
            "Descrizione",
            300.0,
            utente,
            Arrays.asList("test"),
            false,
            dataFutura
        );

        assertFalse(annuncio.isScaduto());
    }
}
