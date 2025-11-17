# Progetto Programmazione ad Oggetti 2024/2025
## Gestione di una bacheca di annunci (Acquisto/Vendita)
**Studente:** Andrea Perini | 20044839

## Introduzione
Il presente progetto ha come obiettivo la realizzazione di un sistema di gestione di una bacheca di annunci. La bacheca consente agli utenti registrati di inserire, cercare, rimuovere e modificare annunci di vendita e di acquisto. Inoltre, è prevista la possibilità di salvare gli annunci su nuovo file csv.

Le funzionalità principali implementate sono:
- Registrazione, rimozione e ricerca utenti
- Inserimento, modifica, rimozione e ricerca di annunci
- Gestione automatica degli annunci scaduti
- Salvataggio e caricamento dei dati su file CSV
- Interfaccia utente testuale (CLI) e grafica (GUI)
- Test coverage completa con JUnit 5

L'applicazione utilizza eccezioni personalizzate per la gestione degli errori e garantisce la separazione tra la logica del sistema e le interfacce utente attraverso un'architettura MVC.

## Implementazione delle classi

### 1. Classe Utente (File: Utente.java)
La classe Utente modella un utente della bacheca ed è definita nel file Utente.java. Ogni utente è identificato in modo univoco dalla propria email ed è caratterizzato dai seguenti attributi:
- **email** (final, String): identificativo univoco dell'utente
- **nome** (String): nome dell'utente, modificabile

La classe include un costruttore che valida la correttezza dell'email tramite una regex (campo EMAIL_ER). Se il formato dell'email è errato, viene sollevata una **DatiNonValidiException**. Anche il nome è sottoposto a controllo per evitare inserimenti nulli o vuoti.

**Metodi principali:**
- `public String getEmail()`: restituisce l'email dell'utente
- `public String getNome()`: restituisce il nome dell'utente
- `public void setNome(String nome)`: imposta un nuovo nome, validando l'input
- `public boolean equals(Object obj)`: confronta due utenti sulla base dell'email
- `public int hashCode()`: calcola hash code basato sull'email
- `public String toString()`: restituisce una rappresentazione testuale dell'utente

### 2. Classe GestoreUtentiImpl (File: GestoreUtentiImpl.java)
Questa classe implementa l'interfaccia GestoreUtenti e si occupa della gestione della lista di utenti registrati. La lista è definita come statica per essere condivisa tra eventuali istanze della classe.

**Metodi principali:**
- `public void aggiungiUtente(Utente utente)`: aggiunge un nuovo utente dopo averne verificato l'unicità
- `public boolean rimuoviUtente(String email)`: rimuove l'utente corrispondente all'email specificata
- `public Utente cercaUtente(String email)`: cerca e restituisce un utente in base all'email
- `public List<Utente> getUtentiRegistrati()`: restituisce l'elenco degli utenti registrati
- `public void salvaSuFile(String filePath)`: salva gli utenti su file CSV
- `public void leggiDaFile(String filePath)`: carica gli utenti da file CSV
- `public void leggiDaReader(BufferedReader reader)`: carica utenti da un reader

Le operazioni sono protette da eccezioni **IllegalArgumentException** per utenti nulli o email duplicate, e **IOException** per errori di I/O.

### 3. Classe Annuncio, AnnuncioAcquisto e AnnuncioVendita
La gestione degli annunci è implementata attraverso una classe astratta **Annuncio** e due sottoclassi: **AnnuncioAcquisto** e **AnnuncioVendita**.

#### Classe Annuncio (File: Annuncio.java)
**Attributi principali:**
- **id** (int): identificativo univoco generato automaticamente
- **titolo** (String): titolo dell'annuncio
- **descrizione** (String): descrizione dell'annuncio
- **prezzo** (double): costo dell'oggetto
- **utente** (Utente): riferimento all'utente che ha pubblicato l'annuncio
- **paroleChiave** (List<String>): lista di parole chiave per la ricerca

**Metodi principali:**
- `public void setTitolo(String titolo)`: imposta un nuovo titolo dopo validazione
- `public void setDescrizione(String descrizione)`: imposta una nuova descrizione
- `public void setPrezzo(double prezzo)`: imposta un nuovo prezzo dopo verifica
- `public void setParoleChiave(List<String> paroleChiave)`: aggiorna la lista di parole chiave
- `public boolean contieneParolaChiave(String parola)`: verifica la presenza di una parola chiave
- `public boolean equals(Object obj)`: confronta annunci basato sull'ID
- `public int hashCode()`: calcola hash code basato sull'ID
- `public String toString()`: implementa una rappresentazione leggibile dell'annuncio

#### Sottoclassi:
- **AnnuncioAcquisto**: rappresenta gli annunci di acquisto, eredita tutti i metodi da Annuncio
- **AnnuncioVendita**: introduce i campi **dataScadenza** (LocalDate) e **venduto** (boolean) con metodi aggiuntivi:
  - `public boolean isScaduto()`: determina se un annuncio è scaduto
  - `public boolean isVenduto()`: restituisce lo stato di vendita
  - `public void setVenduto(boolean venduto)`: imposta lo stato di vendita
  - `public LocalDate getDataScadenza()`: restituisce la data di scadenza
  - `public void setDataScadenza(LocalDate data)`: imposta la data di scadenza

### 4. Classe Bacheca (File: Bacheca.java)
La classe Bacheca gestisce la raccolta degli annunci, permettendo operazioni di aggiunta, rimozione e ricerca. Implementa l'interfaccia **Iterable** per permettere l'iterazione con for-each.

**Metodi principali:**
- `public void aggiungiAnnuncio(Annuncio annuncio)`: aggiunge un annuncio
- `public boolean rimuoviAnnuncio(int id, Utente utente)`: rimuove un annuncio se l'utente è il proprietario
- `public void rimuoviAnnuncio(List<Annuncio> annunci)`: rimozione multipla di annunci
- `public ArrayList<Annuncio> cercaPerParoleChiave(ArrayList<String> paroleChiave)`: ricerca annunci in base alle parole chiave
- `public Annuncio[] getAnnunci()`: restituisce un array di annunci presenti
- `public void svuotaBacheca()`: svuota completamente la bacheca
- `public Iterator<Annuncio> iterator()`: permette l'iterazione sugli annunci

### 5. Classe GestoreBachecaImpl (File: GestoreBachecaImpl.java)
Questa classe gestisce la bacheca e introduce la logica di business e la persistenza dei dati.

**Metodi principali:**
- `public void aggiungiAnnuncio(Annuncio annuncio)`: aggiunge un annuncio con controlli di validità
- `public void pulisciBacheca()`: svuota completamente la bacheca
- `public boolean rimuoviAnnuncio(int id, String emailUtente)`: rimuove annuncio con controllo autorizzazione
- `public List<Annuncio> cercaPerParoleChiave(List<String> parole)`: ricerca annunci
- `public Annuncio cercaAnnuncioPerId(int id)`: cerca annuncio per ID
- `public void aggiungiParoleChiave(int id, List<String> parole)`: aggiunge parole chiave a un annuncio
- `public void leggiDaReader(BufferedReader reader)`: carica annunci da CSV
- `public Bacheca getBacheca()`: restituisce l'istanza della bacheca
- `public void stampaTuttiAnnunci()`: stampa tutti gli annunci su console

## Gestione delle Eccezioni personalizzate
Per garantire una gestione degli errori più chiara ed efficace, sono state implementate diverse eccezioni personalizzate:

### DatiNonValidiException
Questa eccezione viene sollevata quando un dato fornito dall'utente non è valido:
- Titolo o descrizione di un annuncio sono vuoti o nulli
- Il prezzo è negativo
- La data di scadenza di un annuncio di vendita è nel passato
- Le parole chiave contengono caratteri non ammessi
- Email non valida o nome utente vuoto

Utilizzata nelle classi **Utente**, **Annuncio**, **AnnuncioAcquisto** e **AnnuncioVendita**.

### BachecaException
Eccezione usata per gestire errori nelle operazioni della bacheca:
- Tentativo di aggiungere un annuncio nullo
- Parole chiave nulle per la ricerca

Utilizzata nella classe **Bacheca**.

### GestoreBachecaException
Eccezione usata per errori generali nella gestione della bacheca:
- Tentativo di modificare un annuncio inesistente
- Operazioni non autorizzate

Utilizzata nella classe **GestoreBachecaImpl**.

### FileBachecaException
Eccezione usata per gestire errori nella lettura o scrittura su file:
- Il file non esiste o non è accessibile
- I dati nel file sono formattati in modo errato

### OperazioneNonValidaException
Eccezione per operazioni non permesse nel sistema.

## FUNZIONAMENTO CLI E GUI

### Interfaccia a Riga di Comando (CLI)
L'interfaccia a riga di comando è implementata nella classe `MainCLI.java` e presenta un menu interattivo con le seguenti funzionalità:

**Caratteristiche principali:**
- Caricamento automatico dei dati da file CSV all'avvio
- Visualizzazione statistiche sistema (numero utenti, annunci per tipo)
- Banner di benvenuto e menu numerato interattivo

**Menu principale (12 opzioni):**
1. **Aggiungi Annuncio**: supporta sia annunci di acquisto che vendita con validazione automatica
2. **Cerca Annuncio**: ricerca per parole chiave con visualizzazione dettagliata dei risultati
3. **Pulisci Bacheca**: svuota completamente la bacheca (non solo annunci scaduti)
4. **Rimuovi Annuncio**: rimozione tramite ID con controllo autorizzazione proprietario
5. **Salva Bacheca su File**: salvataggio manuale su percorso specificato
6. **Leggi Bacheca da File**: caricamento da file con percorso personalizzato
7. **Stampa Tutti gli Annunci**: visualizzazione organizzata per tipo (vendita/acquisto)
8. **Aggiungi Nuovo Utente**: registrazione con validazione email e nome
9. **Aggiungi Parole Chiave**: modifica annunci esistenti (solo proprietari)
10. **Rimuovi Utente**: eliminazione utente dal sistema
11. **Cerca Utente**: ricerca per email con dettagli
12. **Visualizza Utenti**: lista completa con conteggio annunci per utente
0. **Esci**: terminazione applicazione

### Interfaccia Grafica (GUI)
La GUI è implementata nella classe `MainGUI.java` con architettura responsive che si adatta alle dimensioni della finestra:

**Architettura Responsive:**
- **Modalità Compatta** (larghezza ≤ 900px): bottoni centrali con popup per visualizzazione dati
- **Modalità Espansa** (larghezza > 900px): layout con tabelle integrate e bottoni laterali

**Funzionalità principali:**
- **Doppia Modalità**: Switch tra gestione Bacheca (📋) e gestione Utenti (👤)
- **Visualizzazione Tabellare**: tabelle separate per annunci vendita/acquisto e utenti
- **Gestione Annunci**:
  - Aggiunta con form dinamico (campo data scadenza appare solo per vendita)
  - Ricerca con filtri e aggiornamento tabelle in tempo reale
  - Rimozione con controllo autorizzazione
  - Aggiunta parole chiave con validazione proprietario
- **Gestione Utenti**:
  - Registrazione, rimozione, ricerca utenti
  - Visualizzazione statistiche annunci per utente
- **Interfaccia Utente**:
  - Reset button (🔄) per ripristinare vista completa dopo filtri
  - Bottoni  💾 salva, ⋮ opzioni salvataggio su nuovo file dati bacheca, 👤/📋 switch modalità
  - Tabelle personalizzate con checkbox per stato "venduto"
  - Dialog modali per tutte le operazioni di input

## Testing
Il progetto include una suite completa di test con **JUnit 5**:
- **94 test totali** distribuiti in 6 classi di test
- **Copertura del 100%** di tutti i metodi pubblici
- **Test di successo** per verificare il corretto funzionamento
- **Test di fallimento** per validare la gestione delle eccezioni
- **Test di edge cases** per valori limite

**Classi di Test:**
- `TestUtente.java` - 16 test
- `TestAnnuncioAcquisto.java` - 21 test
- `TestAnnuncioVendita.java` - 13 test
- `TestBacheca.java` - 12 test
- `TestGestoreUtentiImpl.java` - 15 test
- `TestGestoreBachecaImpl.java` - 17 test

## Struttura del Progetto
Il progetto è organizzato secondo un'architettura modulare con **17 classi di implementazione** e **6 classi di test**:

**Package bacheca**: Entità del dominio (5 classi)
- `Annuncio.java` (classe astratta)
- `AnnuncioAcquisto.java`, `AnnuncioVendita.java` (sottoclassi)
- `Utente.java`, `Bacheca.java` (entità principali)

**Package controller**: Logica di business (4 classi)
- `GestoreUtenti.java`, `GestoreUtentiImpl.java`
- `GestoreBacheca.java`, `GestoreBachecaImpl.java`

**Package view**: Interfacce utente (3 classi)
- `view.cli.MainCLI.java` (interfaccia testuale)
- `view.frontend.MainGUI.java` (interfaccia grafica)
- `util.Input.java` (utility per input CLI)

**Package eccezioni**: Eccezioni personalizzate (5 classi)
- `DatiNonValidiException.java`, `BachecaException.java`
- `GestoreBachecaException.java`, `FileBachecaException.java`
- `OperazioneNonValidaException.java`
