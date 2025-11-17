package test;
import static org.junit.jupiter.api.Assertions.*;

import bacheca.Utente;
import eccezioni.DatiNonValidiException;

import org.junit.jupiter.api.Test;

/**
 * Test unitari per la classe Utente.
 * Verifica la corretta creazione, validazione e comportamento degli utenti.
 * 
 * Testa tutte le funzionalità della classe Utente:
 * - Costruttore con validazione email e nome
 * - Getter per email e nome
 * - Setter per nome con validazione
 * - Metodi equals(), hashCode() e toString()
 * - Gestione eccezioni DatiNonValidiException
 */
class TestUtente {
    
    /**
     * Test di successo: verifica che sia possibile creare un utente valido
     * con email e nome corretti.
     * Testa: costruttore Utente(String, String), getEmail(), getNome()
     */
    @Test
    public void testCreazioneUtenteValido() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        assertEquals("mario.rossi@email.com", utente.getEmail());
        assertEquals("Mario Rossi", utente.getNome());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore lanci DatiNonValidiException
     * quando viene passata un'email nulla.
     * Testa: validazione email nel costruttore
     */
    @Test
    public void testCreazioneUtenteEmailNulla() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new Utente(null, "Mario Rossi");
        });
        
        assertEquals("L'email non è valida. Riprovare!", exception.getMessage());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore lanci DatiNonValidiException
     * quando viene passata un'email vuota.
     * Testa: validazione email nel costruttore
     */
    @Test
    public void testCreazioneUtenteEmailVuota() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new Utente("", "Mario Rossi");
        });
        
        assertEquals("L'email non è valida. Riprovare!", exception.getMessage());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore lanci DatiNonValidiException
     * quando viene passata un'email che non rispetta il formato regex.
     * Testa: validazione email con regex nel costruttore
     */
    @Test
    public void testCreazioneUtenteEmailNonValida() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new Utente("email-non-valida", "Mario Rossi");
        });
        
        assertEquals("L'email non è valida. Riprovare!", exception.getMessage());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore lanci DatiNonValidiException
     * quando viene passato un nome nullo.
     * Testa: validazione nome nel costruttore
     */
    @Test
    public void testCreazioneUtenteNomeNullo() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new Utente("mario.rossi@email.com", null);
        });
        
        assertEquals("Il nome deve contenere almeno un carattere.", exception.getMessage());
    }
    
    /**
     * Test di fallimento: verifica che il costruttore lanci DatiNonValidiException
     * quando viene passato un nome vuoto.
     * Testa: validazione nome nel costruttore
     */
    @Test
    public void testCreazioneUtenteNomeVuoto() {
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            new Utente("mario.rossi@email.com", "");
        });
        
        assertEquals("Il nome deve contenere almeno un carattere.", exception.getMessage());
    }
    
    /**
     * Test di successo: verifica che il metodo equals() restituisca true
     * per due utenti con la stessa email (anche se hanno nomi diversi).
     * Testa: metodo equals() basato sull'email
     */
    @Test
    public void testEqualsUtentiUguali() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        assertEquals(utente1, utente2);
    }
    
    /**
     * Test di successo: verifica che il metodo equals() restituisca false
     * per due utenti con email diverse.
     * Testa: metodo equals() basato sull'email
     */
    @Test
    public void testEqualsUtentiDiversi() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("giulia.verdi@email.com", "Giulia Verdi");
        
        assertNotEquals(utente1, utente2);
    }
    
    /**
     * Test di successo: verifica che il metodo equals() consideri uguali
     * due utenti con la stessa email, indipendentemente dal nome.
     * Testa: metodo equals() basato solo sull'email (email come identificatore univoco)
     */
    @Test
    public void testEqualsStessaEmailNomeDiverso() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("mario.rossi@email.com", "Mario Bianchi");
        
        // Gli utenti sono uguali se hanno la stessa email (email è l'identificatore univoco)
        assertEquals(utente1, utente2);
    }
    
    /**
     * Test di successo: verifica che il metodo hashCode() restituisca
     * lo stesso valore per utenti uguali (stessa email).
     * Testa: metodo hashCode() basato sull'email
     */
    @Test
    public void testHashCode() {
        Utente utente1 = new Utente("mario.rossi@email.com", "Mario Rossi");
        Utente utente2 = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        assertEquals(utente1.hashCode(), utente2.hashCode());
    }
    
    /**
     * Test di successo: verifica che il metodo toString() restituisca
     * una rappresentazione in stringa corretta dell'utente.
     * Testa: metodo toString()
     */
    @Test
    public void testToString() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        String expectedOutput = "Utente{email='mario.rossi@email.com', nome='Mario Rossi'}";
        assertEquals(expectedOutput, utente.toString());
    }
    
    /**
     * Test di successo: verifica che il metodo setNome() modifichi correttamente
     * il nome dell'utente mantenendo l'email invariata.
     * Testa: metodo setNome() con input valido
     */
    @Test
    public void testSetNomeValido() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        // Modifica il nome
        utente.setNome("Mario Bianchi");
        
        // Verifica che il nome sia stato modificato
        assertEquals("Mario Bianchi", utente.getNome());
        // Verifica che l'email sia rimasta invariata
        assertEquals("mario.rossi@email.com", utente.getEmail());
    }
    
    /**
     * Test di fallimento: verifica che il metodo setNome() lanci DatiNonValidiException
     * quando viene passato un nome nullo, mantenendo il nome originale.
     * Testa: validazione nome nel metodo setNome()
     */
    @Test
    public void testSetNomeNullo() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            utente.setNome(null);
        });
        
        assertEquals("Il nome deve contenere almeno un carattere.", exception.getMessage());
        // Verifica che il nome originale sia rimasto invariato
        assertEquals("Mario Rossi", utente.getNome());
    }
    
    /**
     * Test di fallimento: verifica che il metodo setNome() lanci DatiNonValidiException
     * quando viene passato un nome vuoto, mantenendo il nome originale.
     * Testa: validazione nome nel metodo setNome()
     */
    @Test
    public void testSetNomeVuoto() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            utente.setNome("");
        });
        
        assertEquals("Il nome deve contenere almeno un carattere.", exception.getMessage());
        // Verifica che il nome originale sia rimasto invariato
        assertEquals("Mario Rossi", utente.getNome());
    }
    
    /**
     * Test di fallimento: verifica che il metodo setNome() lanci DatiNonValidiException
     * quando viene passato un nome contenente solo spazi, mantenendo il nome originale.
     * Testa: validazione nome con isBlank() nel metodo setNome()
     */
    @Test
    public void testSetNomeSoloSpazi() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        Exception exception = assertThrows(DatiNonValidiException.class, () -> {
            utente.setNome("   ");
        });
        
        assertEquals("Il nome deve contenere almeno un carattere.", exception.getMessage());
        // Verifica che il nome originale sia rimasto invariato
        assertEquals("Mario Rossi", utente.getNome());
    }
    
    /**
     * Test di successo: verifica che il metodo setNome() rimuova automaticamente
     * gli spazi iniziali e finali dal nome tramite trim().
     * Testa: funzionalità trim() nel metodo setNome()
     */
    @Test
    public void testSetNomeConSpazi() {
        Utente utente = new Utente("mario.rossi@email.com", "Mario Rossi");
        
        // Imposta nome con spazi iniziali e finali
        utente.setNome("  Mario Verde  ");
        
        // Verifica che gli spazi siano stati rimossi
        assertEquals("Mario Verde", utente.getNome());
    }
}