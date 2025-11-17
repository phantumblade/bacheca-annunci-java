# RELAZIONE DEL PROGETTO - BACHECA ANNUNCI

## 1. ARCHITETTURA GENERALE

Il progetto implementa un sistema di gestione annunci con architettura MVC (Model-View-Controller):

- **Model**: Package `bacheca` - Entità del dominio
- **Controller**: Package `controller` - Logica di business  
- **View**: Package `view` - Interfacce utente (CLI e GUI)
- **Exceptions**: Package `eccezioni` - Eccezioni personalizzate

## 2. CLASSI DEL MODELLO (`bacheca`)

### **Utente.java**
**Scopo**: Rappresenta un utente del sistema
**Attributi**: `email` (final, identificatore univoco), `nome` (modificabile)
**Metodi pubblici**:
- `Utente(String email, String nome)` - Costruttore con validazione
- `String getEmail()` - Getter email
- `String getNome()` - Getter nome  
- `void setNome(String nome)` - Setter nome con validazione
- `boolean equals(Object obj)` - Confronto basato su email
- `int hashCode()` - Hash basato su email
- `String toString()` - Rappresentazione testuale

**Eccezioni**:
- `DatiNonValidiException` - Email non valida (regex), nome nullo/vuoto

### **Annuncio.java (Abstract)**
**Scopo**: Classe base per tutti gli annunci
**Attributi**: `id`, `titolo`, `descrizione`, `prezzo`, `utente`, `paroleChiave`
**Metodi pubblici**:
- Costruttori con/senza ID
- Getter: `getId()`, `getTitolo()`, `getDescrizione()`, `getPrezzo()`, `getUtente()`, `getParoleChiave()`
- Setter con validazione: `setTitolo()`, `setDescrizione()`, `setPrezzo()`, `setParoleChiave()`
- `boolean contieneParolaChiave(String parola)` - Ricerca parola chiave
- `boolean equals(Object obj)` - Confronto basato su ID
- `int hashCode()` - Hash basato su ID
- `String toString()` - Rappresentazione testuale

**Eccezioni**:
- `DatiNonValidiException` - Titolo nullo/vuoto, prezzo negativo, utente nullo, parole chiave nulle/vuote

### **AnnuncioAcquisto.java**
**Scopo**: Annuncio di richiesta acquisto
**Eredita**: Tutte le funzionalità da `Annuncio`
**Costruttori**: Con/senza ID
**Override**: `toString()` per formato specifico

### **AnnuncioVendita.java**
**Scopo**: Annuncio di offerta vendita
**Attributi aggiuntivi**: `venduto`, `dataScadenza`
**Metodi pubblici aggiuntivi**:
- `LocalDate getDataScadenza()` - Getter data scadenza
- `void setDataScadenza(LocalDate data)` - Setter con validazione
- `boolean isVenduto()` - Getter stato vendita
- `void setVenduto(boolean venduto)` - Setter stato vendita
- `boolean isScaduto()` - Verifica se scaduto
**Costruttori**: Con/senza ID, con bypassValidazione per caricamento da file

**Eccezioni**:
- `DatiNonValidiException` - Data scadenza nulla/nel passato

### **Bacheca.java**
**Scopo**: Collezione di annunci con operazioni CRUD
**Metodi pubblici**:
- `void aggiungiAnnuncio(Annuncio annuncio)` - Aggiunge annuncio
- `boolean rimuoviAnnuncio(int id, Utente utente)` - Rimuove per ID/utente
- `void rimuoviAnnuncio(List<Annuncio> annunci)` - Rimozione multipla
- `ArrayList<Annuncio> cercaPerParoleChiave(ArrayList<String> parole)` - Ricerca
- `Annuncio[] getAnnunci()` - Ottiene tutti gli annunci
- `void svuotaBacheca()` - Svuota collezione
- `Iterator<Annuncio> iterator()` - Implementa Iterable

**Eccezioni**:
- `BachecaException` - Annuncio nullo, parole chiave nulle

## 3. CLASSI DEL CONTROLLER (`controller`)

### **GestoreUtentiImpl.java**
**Scopo**: Gestione utenti con persistenza CSV
**Metodi pubblici**:
- `void aggiungiUtente(Utente utente)` - Aggiunge utente
- `boolean rimuoviUtente(String email)` - Rimuove per email
- `Utente cercaUtente(String email)` - Ricerca per email
- `List<Utente> getUtentiRegistrati()` - Lista utenti
- `void salvaSuFile(String filePath)` - Salva su CSV
- `void leggiDaFile(String filePath)` - Carica da CSV
- `void leggiDaReader(BufferedReader reader)` - Carica da reader

**Eccezioni**:
- `IllegalArgumentException` - Utente nullo, email duplicata
- `IOException` - Errori I/O file

### **GestoreBachecaImpl.java**
**Scopo**: Gestione bacheca con logica business
**Metodi pubblici**:
- `void aggiungiAnnuncio(Annuncio annuncio)` - Aggiunge annuncio
- `void pulisciBacheca()` - Svuota bacheca
- `boolean rimuoviAnnuncio(int id, String emailUtente)` - Rimozione autorizzata
- `List<Annuncio> cercaPerParoleChiave(List<String> parole)` - Ricerca
- `Annuncio cercaAnnuncioPerId(int id)` - Ricerca per ID
- `void aggiungiParoleChiave(int id, List<String> parole)` - Modifica parole chiave
- `void leggiDaReader(BufferedReader reader)` - Carica da CSV
- `Bacheca getBacheca()` - Ottiene bacheca
- `void stampaTuttiAnnunci()` - Stampa console

**Eccezioni**:
- `GestoreBachecaException` - Annuncio nullo, ID non trovato
- `IllegalArgumentException` - Bacheca/gestore nulli, utente non autorizzato

## 4. ECCEZIONI PERSONALIZZATE (`eccezioni`)

### **DatiNonValidiException.java**
**Scopo**: Validazione dati di input
**Uso**: Email non valida, nome vuoto, prezzo negativo, date invalide

### **BachecaException.java**
**Scopo**: Errori operazioni bacheca
**Uso**: Annuncio nullo, parole chiave nulle

### **GestoreBachecaException.java**
**Scopo**: Errori logica business
**Uso**: Annunci non trovati, operazioni non autorizzate

### **FileBachecaException.java**
**Scopo**: Errori I/O file
**Uso**: Problemi caricamento/salvataggio dati

### **OperazioneNonValidaException.java**
**Scopo**: Operazioni non permesse
**Uso**: Azioni non autorizzate sul sistema

## 5. INTERFACCE UTENTE (`view`)

### **MainCLI.java**
**Scopo**: Interfaccia a linea di comando
**Funzionalità**: Menu interattivo, gestione completa annunci/utenti

### **MainGUI.java**
**Scopo**: Interfaccia grafica Swing
**Funzionalità**: Finestre, tabelle, form per gestione dati

## 6. TESTING

**Test Coverage**: 100% dei metodi pubblici
**Test Files**: 6 classi di test con 75+ test totali
**Test Types**: 
- Test di successo (funzionalità corretta)
- Test di fallimento (validazione eccezioni)
- Test di edge cases (valori limite)

**Classi di Test**:
- `TestUtente.java` - 16 test
- `TestAnnuncioAcquisto.java` - 21 test
- `TestAnnuncioVendita.java` - 13 test
- `TestBacheca.java` - 12 test
- `TestGestoreUtentiImpl.java` - 15 test
- `TestGestoreBachecaImpl.java` - 17 test

## 7. PERSISTENZA

**Formato**: CSV con delimitatore ';'
**File**: `annunci.csv`, `utenti.csv`
**Gestione**: Caricamento automatico all'avvio, salvataggio su richiesta
**Robustezza**: Gestione errori parsing, dati corrotti ignorati

## 8. PATTERN UTILIZZATI

- **MVC** - Separazione responsabilità
- **Singleton** - Gestori centralizzati
- **Template Method** - Classe astratta Annuncio
- **Iterator** - Bacheca iterabile
- **Exception Handling** - Gestione errori robusta
- **Factory Method** - Creazione annunci tramite costruttori

## 9. VALIDAZIONI E SICUREZZA

- **Email**: Validazione con regex completa
- **Autorizzazioni**: Solo proprietari possono modificare/rimuovere annunci
- **Dati**: Validazione completa input utente
- **Eccezioni**: Gestione robusta errori con messaggi specifici

## 10. CONCLUSIONI

Il progetto implementa un sistema completo di gestione annunci con:
- Architettura MVC ben strutturata
- Validazione robusta dei dati
- Gestione errori completa
- Test coverage al 100%
- Interfacce multiple (CLI/GUI)
- Persistenza dati affidabile

Il sistema è pronto per uso in ambiente di produzione con possibilità di estensioni future.