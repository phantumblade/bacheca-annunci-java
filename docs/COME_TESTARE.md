# Come testare il progetto

## Prerequisiti
- Java 11 o superiore
- VSCode con Extension Pack for Java

## Metodo 1: VSCode (Raccomandato)

### Setup VSCode
1. Apri la cartella del progetto in VSCode
2. VSCode rileverà automaticamente i test JUnit
3. Vai a View → Testing (Ctrl+Shift+T)

### Eseguire i test
- **Tutti i test**: Click su "Run All Tests" nel pannello Testing
- **Test singolo**: Click sulla freccia verde accanto al nome del test
- **Test per classe**: Click sulla freccia verde accanto al nome della classe

### Visualizzare risultati
- Test passati: ✅ verde
- Test falliti: ❌ rosso con dettagli errore
- Output dettagliato nella finestra "Test Results"

## Metodo 2: Terminale/Prompt

### Compilazione
```bash
cd src
javac -cp ".:../lib/junit-platform-console-standalone-1.10.1.jar" test/*.java bacheca/*.java eccezioni/*.java controller/*.java
```

### Esecuzione tutti i test
```bash
java -cp ".:../lib/junit-platform-console-standalone-1.10.1.jar" org.junit.platform.console.ConsoleLauncher --class-path . --scan-class-path
```

### Esecuzione test specifico
```bash
java -cp ".:../lib/junit-platform-console-standalone-1.10.1.jar" org.junit.platform.console.ConsoleLauncher --class-path . --select-class test.TestBacheca
```

## Test disponibili
- `TestUtente` - Test per la classe Utente
- `TestAnnuncioAcquisto` - Test per annunci di acquisto
- `TestAnnuncioVendita` - Test per annunci di vendita
- `TestBacheca` - Test per la collezione di annunci
- `TestGestoreUtentiImpl` - Test per gestione utenti
- `TestGestoreBachecaImpl` - Test per business logic bacheca

## Struttura progetto
```
[final]_progetto/
├── src/
│   ├── bacheca/          # Classi del dominio
│   ├── controller/       # Business logic
│   ├── eccezioni/        # Eccezioni personalizzate
│   ├── test/             # Test JUnit 5
│   └── view/             # Interfacce utente
├── lib/
│   └── junit-platform-console-standalone-1.10.1.jar
└── .vscode/
    └── settings.json     # Configurazione VSCode
```