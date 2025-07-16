package bacheca;
import java.util.List;

public class AnnuncioAcquisto extends Annuncio{
    //Costruttore SENZA ID (usato dalla CLI per creare nuovi annunci)
    public AnnuncioAcquisto(String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave) {
        super(Annuncio.getNextId(), titolo, descrizione, prezzo, utente, paroleChiave);
    }

    //Costruttore CON ID (usato per leggere dal file)
    public AnnuncioAcquisto(int id, String titolo, String descrizione, double prezzo, Utente utente, List<String> paroleChiave) {
        super(id, titolo, descrizione, prezzo, utente, paroleChiave);
    }

	@Override
	public String toString() {
		return super.toString() + '}';
	}
}
