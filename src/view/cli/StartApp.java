package view.cli;

import bacheca.Bacheca;
import controller.GestoreBacheca;
import controller.GestoreBachecaImpl;
import controller.GestoreUtenti;
import controller.GestoreUtentiImpl;

public class StartApp {
    public static void main(String[] args) {
        // 1. Crea l'istanza di Bacheca (e tutto ciò che serve)
        Bacheca bacheca = new Bacheca();

        // 2. Crea un gestore utenti
        GestoreUtenti gestoreUtenti = new GestoreUtentiImpl();

        // 3. Crea un gestore bacheca
        GestoreBacheca gestoreBacheca = new GestoreBachecaImpl(bacheca, gestoreUtenti);

        // 4. Crea il MainCLI e avvia il menu
        MainCLI mainCLI = new MainCLI(gestoreBacheca, gestoreUtenti);
        mainCLI.avvia();
    }
}
