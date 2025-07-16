<div align="center">

# 📋 Sistema di Gestione Bacheca Annunci

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=java&logoColor=white)](https://www.java.com/)
[![Swing](https://img.shields.io/badge/Swing-GUI-4CAF50?style=flat&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success?style=flat)](https://github.com/phantumblade/bacheca-annunci-java)

**Sistema completo per la gestione di annunci di compravendita sviluppato in Java con interfacce CLI e GUI**

[🚀 Quick Start](#-quick-start) • [📖 Documentazione](#-documentazione) • [💻 Screenshots](#-screenshots) • [🤝 Contribuire](#-contribuire)

</div>

---

## 🎯 Panoramica

Il **Sistema di Gestione Bacheca Annunci** è un'applicazione Java completa che implementa un marketplace digitale per annunci di acquisto e vendita. Utilizza il pattern architetturale MVC e offre sia interfaccia grafica (GUI) che da riga di comando (CLI).

### ⭐ Caratteristiche Principali

| Funzionalità | Descrizione | Status |
|-------------|-------------|--------|
| 🏪 **Gestione Annunci** | Creazione, modifica e ricerca annunci | ✅ Completato |
| 👥 **Gestione Utenti** | Registrazione e autenticazione utenti | ✅ Completato |
| 🖥️ **Interfaccia GUI** | Interfaccia grafica moderna con Swing | ✅ Completato |
| ⌨️ **Interfaccia CLI** | Interfaccia testuale per terminale | ✅ Completato |
| 💾 **Persistenza CSV** | Salvataggio e caricamento dati | ✅ Completato |
| 🔍 **Ricerca Avanzata** | Ricerca per parole chiave con correlazione | ✅ Completato |
| 🛡️ **Validazione Dati** | Sistema robusto con eccezioni personalizzate | ✅ Completato |

---

## 📋 Conformità alle Specifiche Progetto 24-25

Basato sul documento ufficiale `docs/Progetto 24-25.pdf`, verifica completa dell'implementazione:

### ✅ Requisiti Obbligatori

| Requisito Specifico | Richiesto | Implementato | Implementazione | Note |
|-------------------|-----------|-------------|----------------|------|
| **Classi Test** | ✓ | ✅ | `src/test/TestAnnuncioVendita.java`<br>`src/test/TestUtente.java` | Test per classi del dominio |
| **Documentazione Javadoc** | ✓ | ✅ | Annotazioni complete nel codice | Documentazione API completa |
| **Relazione Progetto** | ✓ | ✅ | `README.md` dettagliato | Scelte architetturali documentate |
| **Interfaccia CLI** | ✓ | ✅ | `view/cli/MainCLI.java` | Menu completo con gestione eccezioni |
| **Interfaccia GUI** | ✓ | ✅ | `view/frontend/MainGUI.java` | Swing con layout responsivo |
| **Separazione UI/Logic** | ✓ | ✅ | Pattern MVC implementato | View separate da Controller/Model |

### 🎯 Funzionalità Core del Sistema

| Funzionalità | Richiesto | Implementato | Classe/Metodo | Verifica |
|-------------|-----------|-------------|---------------|----------|
| **Gestione Utenti** | ✓ | ✅ | `bacheca/Utente.java`<br>`controller/GestoreUtenti.java` | Email + Nome identificativi |
| **Annunci Acquisto/Vendita** | ✓ | ✅ | `bacheca/AnnuncioAcquisto.java`<br>`bacheca/AnnuncioVendita.java` | Ereditarietà da classe astratta |
| **Parole Chiave** | ✓ | ✅ | Campo `List<String> paroleChiave` | Separatore virgola supportato |
| **Data Scadenza Vendita** | ✓ | ✅ | `AnnuncioVendita.dataScadenza` | Tipo `LocalDate` |
| **ID Annunci** | ✓ | ✅ | `Annuncio.id` (contatore statico) | Assegnazione automatica |

### ⚙️ Operazioni Utente Richieste

| Operazione | Richiesto | Implementato | Metodo | Interfacce |
|-----------|-----------|-------------|--------|------------|
| **Inserire Parola Chiave** | ✓ | ✅ | `aggiungiParoleChiave(id, nuoveParole)` | CLI + GUI |
| **Inserire Annuncio** | ✓ | ✅ | `aggiungiAnnuncio(annuncio)` | CLI + GUI |
| **Lista Correlazioni Acquisto** | ✓ | ✅ | `cercaAnnunciVenditaCorrelati()` | Automatico in GUI |
| **Rimuovere Annuncio** | ✓ | ✅ | `rimuoviAnnuncio(id, emailUtente)` | Solo proprietario |
| **Cercare per Parole Chiave** | ✓ | ✅ | `cercaPerParoleChiave(paroleChiave)` | Intersezione parole |
| **Ripulire Bacheca** | ✓ | ✅ | `pulisciBacheca()` | Rimuove annunci scaduti |

### 🔧 Requisiti Tecnici

| Requisito Tecnico | Richiesto | Implementato | Implementazione | Dettagli |
|------------------|-----------|-------------|----------------|----------|
| **Memorizzazione File** | ✓ | ✅ | `salvaSuFile()` / `leggiDaFile()` | Formato CSV |
| **Iteratore Bacheca** | ✓ | ✅ | `implements Iterable<Annuncio>` | For-each supportato |
| **Gestione Eccezioni** | ✓ | ✅ | 5 eccezioni personalizzate | No stampe per errori |
| **Classi Astratte/Interfacce** | ✓ | ✅ | `Annuncio` astratta, interfacce Gestore | Principi OOP applicati |
| **Campi Privati** | ✓ | ✅ | Encapsulation completo | Getter/Setter pubblici |
| **Metodi Compatti** | ✓ | ✅ | Single Responsibility | Codice ben strutturato |

### 🚀 Funzionalità Extra Implementate

| Feature Extra | Descrizione | Valore Aggiunto | Implementazione |
|--------------|-------------|----------------|-----------------|
| **Layout Responsivo GUI** | Adattamento automatico finestra | ⭐ UX moderna | CardLayout dinamico |
| **Dual Mode GUI** | Toggle Annunci/Utenti | ⭐ Gestione completa | Bottone switch |
| **Auto-Save GUI** | Salvataggio automatico | ⭐ Protezione dati | File chooser integrato |
| **Correlazioni Visuali** | Popup annunci correlati | ⭐ Discovery intelligente | Tabelle filtrate |
| **Validazione Robusta** | Regex parole chiave | ⭐ Input sicuro | Pattern validation |

### 📁 Struttura File

```
📁 Project Root/
├── 📂 src/               # Codice sorgente Java
├── 📂 data/              # File CSV dati
├── 📂 docs/              # Documentazione progetto
├── 📂 lib/               # Librerie esterne (JUnit)
├── 📂 jbook/             # Utility input
└── 📄 README.md          # Documentazione principale
```

---

## 🚀 Quick Start

### Prerequisiti

```bash
# Verifica versione Java
java --version  # Richiede Java 17+
```

### Installazione

1. **Clona il repository**
   ```bash
   git clone https://github.com/phantumblade/bacheca-annunci-java.git
   cd bacheca-annunci-java
   ```

2. **Compila il progetto**
   ```bash
   # Compila tutti i file
   javac -cp ".:lib/*:jbook" src/**/*.java
   ```

3. **Esegui l'applicazione**
   
   **🖥️ Interfaccia Grafica (GUI)**
   ```bash
   java -cp ".:lib/*:jbook:src" view.frontend.MainGUI
   ```
   
   **⌨️ Interfaccia Testuale (CLI)**
   ```bash
   java -cp ".:lib/*:jbook:src" view.cli.StartApp
   ```

---

## 📖 Documentazione

### 🏗️ Architettura del Sistema

Il progetto segue il pattern **MVC (Model-View-Controller)** per una separazione chiara delle responsabilità:

```
📁 src/
├── 📂 bacheca/           # 🎯 MODEL - Modello dati
│   ├── 📄 Annuncio.java          # Classe base astratta
│   ├── 📄 AnnuncioAcquisto.java  # Annunci di acquisto
│   ├── 📄 AnnuncioVendita.java   # Annunci di vendita
│   ├── 📄 Bacheca.java           # Collezione annunci
│   └── 📄 Utente.java            # Entità utente
├── 📂 controller/        # 🎮 CONTROLLER - Logica business
│   ├── 📄 GestoreBacheca.java     # Interfaccia gestore
│   ├── 📄 GestoreBachecaImpl.java # Implementazione gestore
│   ├── 📄 GestoreUtenti.java      # Interfaccia utenti
│   └── 📄 GestoreUtentiImpl.java  # Implementazione utenti
├── 📂 view/             # 🖼️ VIEW - Interfacce utente
│   ├── 📂 cli/                    # Interfaccia testuale
│   │   ├── 📄 MainCLI.java
│   │   └── 📄 StartApp.java
│   └── 📂 frontend/               # Interfaccia grafica
│       └── 📄 MainGUI.java
└── 📂 eccezioni/        # ⚠️ Eccezioni personalizzate
    ├── 📄 BachecaException.java
    ├── 📄 DatiNonValidiException.java
    └── 📄 ...
```

### 🔧 Funzionalità Dettagliate

#### 📋 Gestione Annunci

| Tipo Annuncio | Caratteristiche | Funzionalità |
|---------------|-----------------|--------------|
| 🛒 **Acquisto** | • Richiesta di acquisto<br>• Parole chiave<br>• Prezzo massimo | • Correlazione automatica<br>• Suggerimenti vendita |
| 🏪 **Vendita** | • Offerta di vendita<br>• Data scadenza<br>• Stato venduto | • Rimozione automatica scaduti<br>• Gestione stato |

#### 👥 Gestione Utenti

- **Registrazione**: Validazione email e dati utente
- **Autenticazione**: Sistema di controllo accessi
- **Autorizzazioni**: Solo proprietari possono modificare annunci

#### 💾 Persistenza Dati

- **Formato**: CSV per compatibilità e semplicità
- **Caricamento**: Automatico all'avvio dell'applicazione
- **Salvataggio**: Manuale e automatico con validazione

---

## 💻 Screenshots

### 🖥️ Interfaccia Grafica (GUI)

La GUI presenta un layout responsivo che si adatta alle dimensioni della finestra:

- **Vista Compatta**: Menu centralizzato con popup per visualizzazione dati
- **Vista Estesa**: Tabelle laterali con controlli a sinistra
- **Gestione Dual-Mode**: Toggle tra gestione annunci e utenti

### ⌨️ Interfaccia Testuale (CLI)

Menu interattivo completo con:
- Navigazione numerica
- Validazione input
- Messaggi di errore informativi
- Caricamento automatico dati

---

## 🛠️ Tecnologie Utilizzate

### Core Technologies
- **☕ Java 17+**: Linguaggio principale
- **🎨 Swing**: Framework GUI nativo
- **📊 CSV**: Formato dati per persistenza

### Patterns & Practices
- **🏗️ MVC Pattern**: Separazione responsabilità
- **🔧 Factory Pattern**: Creazione oggetti
- **⚠️ Exception Handling**: Gestione errori robusta
- **✅ Data Validation**: Validazione dati completa

### Tools & Libraries
- **📚 JUnit**: Framework per unit testing
- **📋 Input Utilities**: Gestione input utente
- **🎯 Custom Exceptions**: Eccezioni personalizzate

---

## 🚦 Guida Utilizzo

### 📋 Operazioni Principali

1. **Gestione Annunci**
   ```
   ✅ Crea nuovo annuncio (acquisto/vendita)
   🔍 Cerca annunci per parole chiave
   ✏️ Modifica parole chiave esistenti
   🗑️ Rimuovi annunci propri
   🧹 Pulisci annunci scaduti
   ```

2. **Gestione Utenti**
   ```
   👤 Registra nuovo utente
   🔍 Cerca utente per email
   🗑️ Rimuovi utente
   📊 Visualizza statistiche utenti
   ```

3. **Persistenza**
   ```
   💾 Salva bacheca su file
   📂 Carica bacheca da file
   🔄 Sincronizzazione automatica
   ```

### 🎯 Workflow Consigliato

1. **Primo Utilizzo**
   - Registra utente
   - Crea primi annunci
   - Testa ricerca

2. **Utilizzo Quotidiano**
   - Controlla annunci scaduti
   - Aggiungi nuovi annunci
   - Ricerca correlazioni

3. **Manutenzione**
   - Backup dati
   - Pulizia annunci vecchi
   - Aggiornamento parole chiave

---

## 🤝 Contribuire

### 🐛 Segnalazione Bug

1. Controlla [Issues esistenti](https://github.com/phantumblade/bacheca-annunci-java/issues)
2. Crea nuovo issue con:
   - Descrizione dettagliata
   - Passi per riprodurre
   - Screenshot se necessario

### 💡 Suggerimenti

- **Nuove funzionalità**: Apri discussion per proposte
- **Miglioramenti**: Fork → Branch → Pull Request
- **Documentazione**: Correzioni sempre benvenute

### 🔧 Sviluppo

```bash
# Setup ambiente sviluppo
git clone https://github.com/phantumblade/bacheca-annunci-java.git
cd bacheca-annunci-java

# Crea branch feature
git checkout -b feature/nome-feature

# Develop, test, commit
git commit -m "feat: aggiunta nuova funzionalità"

# Push e crea PR
git push origin feature/nome-feature
```

---

## 📝 License

Questo progetto è distribuito sotto licenza MIT. Vedi il file [LICENSE](LICENSE) per dettagli.

---

## 👨‍💻 Autore

**Sviluppato per il corso di Paradigmi di Programmazione 2024-2025**

- 📧 Email: [Contatta l'autore](mailto:your.email@example.com)
- 🐙 GitHub: [@phantumblade](https://github.com/phantumblade)
- 📚 Università: Paradigmi di Programmazione A.A. 2024-2025

---

<div align="center">

**⭐ Se questo progetto ti è stato utile, considera di dargli una stella! ⭐**

[🔝 Torna all'inizio](#-sistema-di-gestione-bacheca-annunci)

</div>