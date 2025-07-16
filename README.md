# Sistema di Gestione Bacheca Annunci

Sistema completo per la gestione di annunci di compravendita sviluppato in Java con interfacce CLI e GUI.

## Caratteristiche

- **Gestione Annunci**: Creazione, modifica e ricerca di annunci di acquisto e vendita
- **Gestione Utenti**: Registrazione e gestione utenti con validazione email
- **Interfacce Multiple**: 
  - CLI (Command Line Interface) per uso da terminale
  - GUI (Graphical User Interface) con Swing
- **Persistenza Dati**: Salvataggio e caricamento da file CSV
- **Ricerca Avanzata**: Ricerca per parole chiave con correlazione tra annunci
- **Validazione Dati**: Sistema robusto di validazione con eccezioni personalizzate

## Struttura del Progetto

```
src/
├── bacheca/           # Modello dati (Model)
│   ├── Annuncio.java
│   ├── AnnuncioAcquisto.java
│   ├── AnnuncioVendita.java
│   ├── Bacheca.java
│   └── Utente.java
├── controller/        # Logica di business (Controller)
│   ├── GestoreBacheca.java
│   ├── GestoreBachecaImpl.java
│   ├── GestoreUtenti.java
│   └── GestoreUtentiImpl.java
├── view/             # Interfacce utente (View)
│   ├── cli/
│   │   ├── MainCLI.java
│   │   └── StartApp.java
│   └── frontend/
│       └── MainGUI.java
└── eccezioni/        # Eccezioni personalizzate
    ├── BachecaException.java
    ├── DatiNonValidiException.java
    └── ...
```

## Come Eseguire

### Interfaccia CLI
```bash
javac -cp ".:lib/*:jbook" src/view/cli/StartApp.java
java -cp ".:lib/*:jbook" view.cli.StartApp
```

### Interfaccia GUI
```bash
javac -cp ".:lib/*:jbook" src/view/frontend/MainGUI.java
java -cp ".:lib/*:jbook" view.frontend.MainGUI
```

## Funzionalità Principali

1. **Annunci di Vendita**
   - Data di scadenza
   - Stato venduto/disponibile
   - Rimozione automatica annunci scaduti

2. **Annunci di Acquisto**
   - Correlazione automatica con annunci di vendita
   - Suggerimenti basati su parole chiave

3. **Gestione Utenti**
   - Registrazione con validazione email
   - Associazione annunci agli utenti
   - Controllo autorizzazioni

4. **Persistenza**
   - Formato CSV per compatibilità
   - Caricamento automatico all'avvio
   - Salvataggio manuale e automatico

## Tecnologie Utilizzate

- **Java 17+**
- **Swing** per l'interfaccia grafica
- **Pattern MVC** per l'architettura
- **CSV** per la persistenza dati
- **JUnit** per i test (in sviluppo)

## Autore

Progetto sviluppato per il corso di Paradigmi di Programmazione 2024-2025