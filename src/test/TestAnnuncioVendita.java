package test;

import static org.junit.jupiter.api.Assertions.*;  // Importa le asserzioni di JUnit

import java.time.LocalDate;
import java.util.Arrays;

import bacheca.AnnuncioVendita;
import bacheca.Utente;
import eccezioni.DatiNonValidiException;

import org.junit.jupiter.api.Test;

public class TestAnnuncioVendita {

    // Test per verificare la creazione di un annuncio valido
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

        String expectedOutput = "Annuncio{ID=4, Titolo='Monitor 4K', Prezzo=200.0, Utente='Mario Rossi', Parole Chiave=[elettronica, monitor], venduto= false, scadenza= " + dataScadenza + '}';
        assertEquals(expectedOutput, annuncio.toString());
    }
}
