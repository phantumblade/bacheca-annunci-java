package bacheca;
import java.time.LocalDate;
import java.util.List;

import eccezioni.DatiNonValidiException;

public class AnnuncioVendita extends Annuncio {

    private boolean venduto;
    private LocalDate dataScadenza;

    // Costruttore migliorato: rimosso il controllo su 'venduto'
    //Costruttore SENZA ID (usato per creare nuovi annunci)
    public AnnuncioVendita(String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave, boolean venduto, LocalDate dataScadenza) {
        super(Annuncio.getNextId(), titolo, descrizione, prezzo, utente, paroleChiave);

        if (dataScadenza == null || dataScadenza.isBefore(LocalDate.now())) {
            throw new DatiNonValidiException("La data di scadenza non può essere nulla o nel passato.");
        }

        this.dataScadenza = dataScadenza;
        this.venduto = venduto;
    }

    //Costruttore CON ID (usato per leggere dal file)
    public AnnuncioVendita(int id, String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave, boolean venduto, LocalDate dataScadenza) {
        super(id, titolo, descrizione, prezzo, utente, paroleChiave);

        if (dataScadenza == null || dataScadenza.isBefore(LocalDate.now())) {
            throw new DatiNonValidiException("La data di scadenza non può essere nulla o nel passato.");
        }

        this.dataScadenza = dataScadenza;
        this.venduto = venduto;

    }

    // Costruttore CON bypassValidazione (usato per leggere annunci scaduti dal file)
    public AnnuncioVendita(int id, String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave, boolean venduto, LocalDate dataScadenza, boolean bypassValidazione) {
        super(id, titolo, descrizione, prezzo, utente, paroleChiave);

        // Validazione della data solo se bypassValidazione è false
        if (!bypassValidazione && (dataScadenza == null || dataScadenza.isBefore(LocalDate.now()))) {
            throw new DatiNonValidiException("La data di scadenza non può essere nulla o nel passato.");
        }

        this.dataScadenza = dataScadenza;
        this.venduto = venduto;
    }

    // Getter e Setter per la data di scadenza con validazione
    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        if (dataScadenza == null || dataScadenza.isBefore(LocalDate.now())) {
            throw new DatiNonValidiException("La data di scadenza non può essere nel passato.");
        }
        this.dataScadenza = dataScadenza;
    }

    // Gestione dello stato di vendita
    public boolean isVenduto() {
        return venduto;
    }

    public void setVenduto(boolean venduto) {
        this.venduto = venduto;
    }

    // Verifica se l'annuncio è scaduto
    public boolean isScaduto() {
        return dataScadenza != null && LocalDate.now().isAfter(dataScadenza);
    }

    // Miglioramento della rappresentazione testuale
    @Override
    public String toString() {
        return super.toString() + ", venduto= " + venduto + ", scadenza= " + dataScadenza + '}';
    }
}
