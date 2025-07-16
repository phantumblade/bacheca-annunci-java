// File descriptions database
const fileDescriptions = {
    // MODEL - Bacheca package
    'src/bacheca/Annuncio.java': {
        icon: 'fab fa-java',
        description: 'Classe astratta base per tutti gli annunci. Definisce le proprietà comuni come ID, titolo, descrizione, parole chiave e utente proprietario. Implementa i metodi fondamentali per l\'uguaglianza e la gestione degli ID.',
        dependencies: ['src/bacheca/Utente.java', 'src/eccezioni/DatiNonValidiException.java'],
        usage: 'Classe padre per AnnuncioAcquisto e AnnuncioVendita. Contiene la logica comune per la gestione degli annunci e fornisce il template per le classi figlie.'
    },
    'src/bacheca/AnnuncioAcquisto.java': {
        icon: 'fas fa-shopping-cart',
        description: 'Rappresenta un annuncio di richiesta di acquisto. Estende la classe Annuncio aggiungendo il campo prezzoMassimo. Gestisce la logica specifica per le richieste di acquisto di prodotti.',
        dependencies: ['src/bacheca/Annuncio.java', 'src/bacheca/Utente.java'],
        usage: 'Utilizzata per creare annunci di richiesta di acquisto. Gli utenti possono specificare il prezzo massimo che sono disposti a pagare per un determinato prodotto o servizio.'
    },
    'src/bacheca/AnnuncioVendita.java': {
        icon: 'fas fa-tags',
        description: 'Rappresenta un annuncio di offerta di vendita. Estende la classe Annuncio aggiungendo prezzo, data di scadenza e stato di vendita. Gestisce la logica per le offerte di vendita.',
        dependencies: ['src/bacheca/Annuncio.java', 'src/bacheca/Utente.java'],
        usage: 'Utilizzata per creare annunci di offerta di vendita. Include funzionalità per gestire prezzi, scadenze e lo stato di vendita degli articoli.'
    },
    'src/bacheca/Bacheca.java': {
        icon: 'fas fa-clipboard-list',
        description: 'Classe principale che gestisce la collezione di annunci. Implementa Iterable<Annuncio> per supportare il for-each. Fornisce metodi per aggiungere, rimuovere e cercare annunci.',
        dependencies: ['src/bacheca/Annuncio.java', 'src/bacheca/Utente.java', 'src/eccezioni/BachecaException.java'],
        usage: 'Cuore del sistema di gestione annunci. Mantiene la collezione di tutti gli annunci e fornisce le operazioni CRUD (Create, Read, Update, Delete) per la loro gestione.'
    },
    'src/bacheca/Utente.java': {
        icon: 'fas fa-user',
        description: 'Rappresenta un utente del sistema. Contiene informazioni personali come nome, email e numero di telefono. Implementa l\'uguaglianza basata sull\'email come identificatore univoco.',
        dependencies: ['src/eccezioni/DatiNonValidiException.java'],
        usage: 'Entità fondamentale per l\'identificazione degli utenti. Ogni annuncio è associato a un utente proprietario e viene utilizzata per l\'autenticazione e l\'autorizzazione.'
    },
    
    // CONTROLLER - Logic package
    'src/controller/GestoreBacheca.java': {
        icon: 'fas fa-cogs',
        description: 'Interfaccia che definisce le operazioni principali per la gestione della bacheca. Stabilisce il contratto per le operazioni di business logic del sistema.',
        dependencies: ['src/bacheca/Bacheca.java', 'src/bacheca/Annuncio.java', 'src/bacheca/Utente.java'],
        usage: 'Definisce l\'API per la gestione della bacheca. Implementata da GestoreBachecaImpl per fornire le funzionalità concrete di gestione degli annunci.'
    },
    'src/controller/GestoreBachecaImpl.java': {
        icon: 'fas fa-gear',
        description: 'Implementazione concreta dell\'interfaccia GestoreBacheca. Gestisce la business logic del sistema, inclusi salvataggio/caricamento su file, pulizia annunci scaduti e correlazioni.',
        dependencies: ['src/controller/GestoreBacheca.java', 'src/bacheca/Bacheca.java', 'src/controller/GestoreUtenti.java'],
        usage: 'Classe centrale della business logic. Coordina le operazioni tra model e view, gestisce la persistenza dei dati e implementa algoritmi di ricerca e correlazione.'
    },
    'src/controller/GestoreUtenti.java': {
        icon: 'fas fa-users-cog',
        description: 'Interfaccia per la gestione degli utenti del sistema. Definisce le operazioni di registrazione, ricerca e gestione degli utenti.',
        dependencies: ['src/bacheca/Utente.java'],
        usage: 'Contratto per la gestione degli utenti. Definisce le operazioni necessarie per mantenere la collezione di utenti registrati nel sistema.'
    },
    'src/controller/GestoreUtentiImpl.java': {
        icon: 'fas fa-user-circle',
        description: 'Implementazione concreta della gestione utenti. Mantiene la collezione di utenti registrati e fornisce operazioni per la loro gestione e validazione.',
        dependencies: ['src/controller/GestoreUtenti.java', 'src/bacheca/Utente.java'],
        usage: 'Gestisce la registrazione, validazione e ricerca degli utenti. Mantiene la consistenza dei dati utente e fornisce servizi di autenticazione di base.'
    },
    
    // VIEW - Interface package
    'src/view/cli/MainCLI.java': {
        icon: 'fas fa-terminal',
        description: 'Interfaccia utente testuale (CLI) completa per la gestione del sistema. Fornisce un menu interattivo per tutte le operazioni disponibili con validazione dell\'input.',
        dependencies: ['src/controller/GestoreBachecaImpl.java', 'src/controller/GestoreUtentiImpl.java', 'jbook/util/Input.java'],
        usage: 'Punto di accesso principale per l\'utilizzo del sistema via terminale. Offre un\'interfaccia completa e user-friendly per tutte le funzionalità del sistema.'
    },
    'src/view/cli/StartApp.java': {
        icon: 'fas fa-play',
        description: 'Classe di avvio per l\'applicazione CLI. Contiene il metodo main che inizializza e avvia l\'interfaccia testuale del sistema.',
        dependencies: ['src/view/cli/MainCLI.java'],
        usage: 'Entry point dell\'applicazione CLI. Esegue l\'inizializzazione del sistema e avvia l\'interfaccia utente testuale.'
    },
    'src/view/frontend/MainGUI.java': {
        icon: 'fas fa-desktop',
        description: 'Interfaccia grafica completa sviluppata con Swing. Implementa un layout responsivo con modalità dual-mode per gestione annunci e utenti. Include funzionalità avanzate come correlazioni automatiche.',
        dependencies: ['src/controller/GestoreBachecaImpl.java', 'src/controller/GestoreUtentiImpl.java', 'javax.swing.*'],
        usage: 'Interfaccia grafica principale del sistema. Offre un\'esperienza utente moderna e intuitiva con layout responsivo e funzionalità avanzate di visualizzazione dati.'
    },
    
    // EXCEPTIONS - Error handling
    'src/eccezioni/BachecaException.java': {
        icon: 'fas fa-exclamation-triangle',
        description: 'Eccezione personalizzata per errori specifici della gestione bacheca. Utilizzata per segnalare problemi durante le operazioni sugli annunci.',
        dependencies: ['java.lang.Exception'],
        usage: 'Gestisce errori specifici della bacheca come annunci duplicati, operazioni non valide o problemi di integrità dei dati.'
    },
    'src/eccezioni/DatiNonValidiException.java': {
        icon: 'fas fa-exclamation-circle',
        description: 'Eccezione per dati non validi o malformati. Utilizzata durante la validazione di input utente e dati di sistema.',
        dependencies: ['java.lang.Exception'],
        usage: 'Segnala errori di validazione dei dati come email malformate, campi obbligatori mancanti o formati non corretti.'
    },
    'src/eccezioni/FileBachecaException.java': {
        icon: 'fas fa-file-exclamation',
        description: 'Eccezione specializzata per errori di I/O durante le operazioni su file. Gestisce problemi di lettura/scrittura dei file CSV.',
        dependencies: ['java.lang.Exception'],
        usage: 'Gestisce errori durante il salvataggio e caricamento dei dati da file, inclusi problemi di permessi o file corrotti.'
    },
    'src/eccezioni/GestoreBachecaException.java': {
        icon: 'fas fa-cog',
        description: 'Eccezione generale per errori del gestore bacheca. Copre errori di sistema e problemi di business logic.',
        dependencies: ['java.lang.Exception'],
        usage: 'Gestisce errori generali del sistema di gestione bacheca, inclusi problemi di configurazione o stato inconsistente.'
    },
    'src/eccezioni/OperazioneNonValidaException.java': {
        icon: 'fas fa-times-circle',
        description: 'Eccezione per operazioni non permesse o non valide nel contesto corrente. Utilizzata per controlli di autorizzazione e validazione logica.',
        dependencies: ['java.lang.Exception'],
        usage: 'Segnala tentativi di operazioni non autorizzate come la modifica di annunci altrui o operazioni su dati inesistenti.'
    },
    
    // TESTING
    'src/test/TestAnnuncioVendita.java': {
        icon: 'fas fa-vial',
        description: 'Classe di test JUnit per la validazione della classe AnnuncioVendita. Contiene test per costruttori, metodi e comportamenti specifici degli annunci di vendita.',
        dependencies: ['src/bacheca/AnnuncioVendita.java', 'src/bacheca/Utente.java', 'org.junit.jupiter.api.*'],
        usage: 'Esegue test automatici per garantire la correttezza dell\'implementazione degli annunci di vendita. Verifica costruttori, getter/setter e logica di business.'
    },
    'src/test/TestUtente.java': {
        icon: 'fas fa-flask',
        description: 'Classe di test JUnit per la validazione della classe Utente. Testa la creazione, validazione e comportamento degli utenti del sistema.',
        dependencies: ['src/bacheca/Utente.java', 'org.junit.jupiter.api.*'],
        usage: 'Verifica la correttezza dell\'implementazione della classe Utente, inclusi costruttori, validazione email e metodi di uguaglianza.'
    },
    
    // DATA & EXTERNAL
    'data/annunci.csv': {
        icon: 'fas fa-file-csv',
        description: 'File CSV di persistenza per gli annunci del sistema. Contiene tutti gli annunci salvati in formato strutturato con campi separati da virgole.',
        dependencies: ['src/controller/GestoreBachecaImpl.java'],
        usage: 'Archiviazione permanente dei dati del sistema. Permette di mantenere gli annunci tra diverse sessioni di utilizzo dell\'applicazione.'
    },
    'docs/Progetto 24-25.pdf': {
        icon: 'fas fa-file-pdf',
        description: 'Documento ufficiale delle specifiche del progetto. Contiene tutti i requisiti, vincoli e caratteristiche richieste per l\'implementazione del sistema.',
        dependencies: [],
        usage: 'Riferimento per le specifiche del progetto. Utilizzato per verificare la conformità dell\'implementazione ai requisiti richiesti.'
    },
    'lib/junit-jupiter-api-5.12.0-RC1.jar': {
        icon: 'fab fa-java',
        description: 'Libreria JUnit 5 per l\'esecuzione dei test unitari. Fornisce le API necessarie per scrivere e eseguire test automatici del codice.',
        dependencies: [],
        usage: 'Framework di testing utilizzato per i test delle classi di dominio. Permette di eseguire test automatici per verificare la correttezza del codice.'
    },
    'jbook/util/Input.java': {
        icon: 'fas fa-keyboard',
        description: 'Classe utility per la gestione dell\'input utente da console. Fornisce metodi semplificati per leggere diversi tipi di dati dall\'input standard.',
        dependencies: ['java.util.Scanner'],
        usage: 'Utility per l\'interfaccia CLI. Semplifica la lettura di input utente fornendo metodi type-safe per stringhe, numeri e altri tipi di dati.'
    },
    'README.md': {
        icon: 'fab fa-markdown',
        description: 'Documentazione completa del progetto in formato Markdown. Contiene panoramica, istruzioni di installazione, architettura e conformità alle specifiche.',
        dependencies: [],
        usage: 'Documentazione principale del progetto. Fornisce tutte le informazioni necessarie per comprendere, installare e utilizzare il sistema.'
    }
};

// DOM Ready
document.addEventListener('DOMContentLoaded', function() {
    initializeFileTree();
    initializeModal();
});

// Initialize file tree functionality
function initializeFileTree() {
    const folderItems = document.querySelectorAll('.file-item.folder');
    const fileItems = document.querySelectorAll('.file-item.file');
    
    // Add click handlers for folders
    folderItems.forEach(folder => {
        folder.addEventListener('click', function(e) {
            e.stopPropagation();
            toggleFolder(this);
        });
        
        // Add keyboard support
        folder.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleFolder(this);
            }
        });
        
        // Make focusable
        folder.setAttribute('tabindex', '0');
    });
    
    // Add click handlers for files
    fileItems.forEach(file => {
        file.addEventListener('click', function(e) {
            e.stopPropagation();
            openFileModal(this);
        });
        
        // Add keyboard support
        file.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                openFileModal(this);
            }
        });
        
        // Make focusable
        file.setAttribute('tabindex', '0');
    });
}

// Toggle folder open/closed
function toggleFolder(folderElement) {
    const folderPath = folderElement.dataset.path;
    const children = document.querySelector(`[data-parent="${folderPath}"]`);
    const expandIcon = folderElement.querySelector('.expand-icon');
    
    if (children) {
        if (children.classList.contains('hidden')) {
            children.classList.remove('hidden');
            children.classList.add('visible');
            expandIcon.textContent = '▼';
            folderElement.classList.add('expanded');
            folderElement.setAttribute('aria-expanded', 'true');
        } else {
            children.classList.remove('visible');
            children.classList.add('hidden');
            expandIcon.textContent = '▶';
            folderElement.classList.remove('expanded');
            folderElement.setAttribute('aria-expanded', 'false');
            
            // Close all nested folders
            const nestedFolders = children.querySelectorAll('.file-item.folder');
            nestedFolders.forEach(nested => {
                const nestedPath = nested.dataset.path;
                const nestedChildren = document.querySelector(`[data-parent="${nestedPath}"]`);
                if (nestedChildren && !nestedChildren.classList.contains('hidden')) {
                    nestedChildren.classList.remove('visible');
                    nestedChildren.classList.add('hidden');
                    const nestedIcon = nested.querySelector('.expand-icon');
                    nestedIcon.textContent = '▶';
                    nested.classList.remove('expanded');
                    nested.setAttribute('aria-expanded', 'false');
                }
            });
        }
    }
}

// Initialize modal functionality
function initializeModal() {
    const modal = document.getElementById('fileModal');
    const closeBtn = document.querySelector('.close');
    
    // Close modal when clicking X
    closeBtn.addEventListener('click', function() {
        closeModal();
    });
    
    // Close modal when clicking outside
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });
    
    // Close modal with Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });
}

// Open file modal with details
function openFileModal(fileElement) {
    const filePath = fileElement.dataset.path;
    const fileName = filePath.split('/').pop();
    const fileInfo = fileDescriptions[filePath];
    
    if (!fileInfo) {
        console.warn(`No description found for file: ${filePath}`);
        return;
    }
    
    const modal = document.getElementById('fileModal');
    const modalIcon = document.getElementById('modalIcon');
    const modalTitle = document.getElementById('modalTitle');
    const modalDescription = document.getElementById('modalDescription');
    const modalDependencies = document.getElementById('modalDependencies');
    const modalUsage = document.getElementById('modalUsage');
    
    // Update modal content
    modalIcon.className = fileInfo.icon;
    modalTitle.textContent = fileName;
    modalDescription.textContent = fileInfo.description;
    modalUsage.textContent = fileInfo.usage;
    
    // Update dependencies list
    modalDependencies.innerHTML = '';
    if (fileInfo.dependencies && fileInfo.dependencies.length > 0) {
        fileInfo.dependencies.forEach(dep => {
            const li = document.createElement('li');
            li.textContent = dep;
            
            // Make clickable if it's a file with description
            if (fileDescriptions[dep]) {
                li.classList.add('clickable-dependency');
                li.addEventListener('click', function(e) {
                    e.stopPropagation();
                    // Create a temporary file element to pass to openFileModal
                    const tempFileElement = {
                        dataset: { path: dep }
                    };
                    openFileModal(tempFileElement);
                });
                
                // Add icon to show it's clickable
                const icon = document.createElement('i');
                icon.className = 'fas fa-external-link-alt';
                icon.style.marginRight = '8px';
                icon.style.fontSize = '11px';
                icon.style.color = 'var(--claude-primary)';
                li.insertBefore(icon, li.firstChild);
            }
            
            modalDependencies.appendChild(li);
        });
    } else {
        const li = document.createElement('li');
        li.textContent = 'Nessuna dipendenza esterna';
        li.style.fontStyle = 'italic';
        modalDependencies.appendChild(li);
    }
    
    // Show modal
    modal.style.display = 'block';
    
    // Focus management
    modal.focus();
}

// Close modal
function closeModal() {
    const modal = document.getElementById('fileModal');
    modal.style.display = 'none';
}

// Utility functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Search functionality (if needed in future)
function searchFiles(query) {
    const fileItems = document.querySelectorAll('.file-item');
    const searchQuery = query.toLowerCase();
    
    fileItems.forEach(item => {
        const fileName = item.querySelector('span').textContent.toLowerCase();
        const shouldShow = fileName.includes(searchQuery);
        
        if (shouldShow) {
            item.style.display = 'flex';
        } else {
            item.style.display = 'none';
        }
    });
}

// Accessibility enhancements
function enhanceAccessibility() {
    // Add ARIA labels
    const fileTree = document.querySelector('.file-tree');
    if (fileTree) {
        fileTree.setAttribute('role', 'tree');
        fileTree.setAttribute('aria-label', 'Struttura file del progetto');
    }
    
    // Add role to file items
    const fileItems = document.querySelectorAll('.file-item');
    fileItems.forEach(item => {
        if (item.classList.contains('folder')) {
            item.setAttribute('role', 'treeitem');
            item.setAttribute('aria-expanded', 'false');
        } else {
            item.setAttribute('role', 'treeitem');
            item.setAttribute('aria-describedby', 'file-description');
        }
    });
}

// Initialize accessibility on load
document.addEventListener('DOMContentLoaded', enhanceAccessibility);

// Export functions for testing (if needed)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        toggleFolder,
        openFileModal,
        closeModal,
        searchFiles,
        fileDescriptions
    };
}