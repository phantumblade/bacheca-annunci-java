package view.frontend;

import controller.GestoreBacheca;
import controller.GestoreBachecaImpl;
import controller.GestoreUtenti;
import controller.GestoreUtentiImpl;
import eccezioni.GestoreBachecaException;
import bacheca.Annuncio;
import bacheca.AnnuncioAcquisto;
import bacheca.AnnuncioVendita;
import bacheca.Bacheca;
import bacheca.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

public class MainGUI extends JFrame {
    private JPanel optionsPanelCompatto;
    private JPanel optionsPanelEsteso;
    private JButton switchModeButton; // Bottone per alternare modalità bacheca/utenti
    private boolean modalitaUtenti = false; // Stato: false = Bacheca, true = Utenti
    private JButton resetButton; // Pulsante di reset
    private JPanel tabellaPanel;
    private GestoreBacheca gestoreBacheca;
    private GestoreUtenti gestoreUtenti;

    public MainGUI(GestoreBacheca gestoreBacheca, GestoreUtenti gestoreUtenti) {
        this.gestoreBacheca = gestoreBacheca;
        this.gestoreUtenti = gestoreUtenti;


        setTitle("Bacheca Annunci");
        setSize(700, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        caricaAnnunciDaFile("data/annunci.csv");
        caricaUtentiDaFile("data/utenti.csv");
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new CardLayout());  // Useremo CardLayout per passare tra le visualizzazioni

        // Pannello per la visualizzazione compatta (finestra piccola)
        JPanel compactPanel = creaPannelloCompatto();

        // Pannello per la visualizzazione espansa (finestra grande)
        JPanel expandedPanel = creaPannelloEspanso();

        // Aggiungiamo entrambi i pannelli al mainPanel
        mainPanel.add(compactPanel, "Compatto");
        mainPanel.add(expandedPanel, "Espanso");

        add(mainPanel);

        // Ascoltatore per il ridimensionamento della finestra
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                CardLayout cl = (CardLayout) (mainPanel.getLayout());
                int width = getWidth();

                if (width > 900) {
                    cl.show(mainPanel, "Espanso");  // Mostra il layout con le tabelle
                    aggiornaVistaEstesa();  // Aggiorna la vista estesa mantenendo la modalità corrente
                } else {
                    cl.show(mainPanel, "Compatto");  // Torna alla visualizzazione compatta
                    aggiornaVistaCompatta();  // Aggiorna la vista compatta mantenendo la modalità corrente
                }
            }
        });
    }

    // Metodo per aggiungere le tabelle al pannello espanso
    private void aggiungiTabelle(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // ====== Annunci di Vendita ======
        JLabel venditaLabel = new JLabel("Annunci di Vendita", SwingConstants.CENTER);
        venditaLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(venditaLabel, gbc);

        String[] colonneVendita = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave", "Scadenza", "Venduto"};
        DefaultTableModel modelVendita = new DefaultTableModel(colonneVendita, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? Boolean.class : String.class;
            }
        };

        JTable tableVendita = new JTable(modelVendita);
        personalizzaTabella(tableVendita);
        JScrollPane scrollVendita = new JScrollPane(tableVendita);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(scrollVendita, gbc);

        // ====== Annunci di Acquisto ======
        JLabel acquistoLabel = new JLabel("Annunci di Acquisto", SwingConstants.CENTER);
        acquistoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(acquistoLabel, gbc);

        String[] colonneAcquisto = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave"};
        DefaultTableModel modelAcquisto = new DefaultTableModel(colonneAcquisto, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableAcquisto = new JTable(modelAcquisto);
        personalizzaTabella(tableAcquisto);
        JScrollPane scrollAcquisto = new JScrollPane(tableAcquisto);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(scrollAcquisto, gbc);

        // ** POPOLA LE TABELLE CON I DATI DELLA BACHECA **
        for (Annuncio annuncio : gestoreBacheca.getBacheca().getAnnunci()) {
            if (annuncio instanceof AnnuncioVendita vendita) {
                modelVendita.addRow(new Object[]{
                    vendita.getId(), vendita.getTitolo(), vendita.getDescrizione(),
                    vendita.getPrezzo(), vendita.getUtente().getEmail(),
                    String.join(", ", vendita.getParoleChiave()),
                    vendita.getDataScadenza(), vendita.isVenduto()
                });
            } else if (annuncio instanceof AnnuncioAcquisto acquisto) {
                modelAcquisto.addRow(new Object[]{
                    acquisto.getId(), acquisto.getTitolo(), acquisto.getDescrizione(),
                    acquisto.getPrezzo(), acquisto.getUtente().getEmail(),
                    String.join(", ", acquisto.getParoleChiave())
                });
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private JPanel creaPannelloCompatto() {
        JPanel panel = new JPanel(new BorderLayout());

        // Titolo
        JLabel benvenutoLabel = new JLabel("Benvenuto nella Bacheca Annunci!", SwingConstants.CENTER);
        benvenutoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        benvenutoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.add(benvenutoLabel, BorderLayout.NORTH);

        // Bottoni centrati - pannello specifico per modalità compatta
        optionsPanelCompatto = new JPanel();
        optionsPanelCompatto.setLayout(new BoxLayout(optionsPanelCompatto, BoxLayout.Y_AXIS));
        optionsPanelCompatto.setBorder(BorderFactory.createEmptyBorder(30, 120, 20, 120));  // Centrato

        // Carica i bottoni iniziali (modalità annunci)
        aggiornaVistaCompatta();

        panel.add(optionsPanelCompatto, BorderLayout.CENTER);

        // Bottoni inferiori (Exit e Salva)
        panel.add(creaBottomPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creaPannelloEspanso() {
        JPanel panel = new JPanel(new BorderLayout());

        // Pannello che contiene sia il titolo che i bottoni
        JPanel sinistraPanel = new JPanel();
        sinistraPanel.setLayout(new BoxLayout(sinistraPanel, BoxLayout.Y_AXIS));
        sinistraPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        // Titolo allineato a sinistra incolonnato con i bottoni
        JLabel benvenutoLabel = new JLabel("Benvenuto nella Bacheca Annunci!", SwingConstants.LEFT);
        benvenutoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        benvenutoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);  // Assicura che il titolo sia allineato ai bottoni
        sinistraPanel.add(benvenutoLabel);
        sinistraPanel.add(Box.createVerticalStrut(20));  // Spazio tra il titolo e i bottoni

        // Pannello bottoni spostato a sinistra - pannello specifico per modalità estesa
        optionsPanelEsteso = new JPanel();
        optionsPanelEsteso.setLayout(new BoxLayout(optionsPanelEsteso, BoxLayout.Y_AXIS));
        optionsPanelEsteso.setAlignmentX(Component.LEFT_ALIGNMENT);  // Allineamento con il titolo
        optionsPanelEsteso.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        sinistraPanel.add(optionsPanelEsteso);

        // Aggiungi il pannello sinistra al layout principale
        panel.add(sinistraPanel, BorderLayout.WEST);

        // Pannello per le tabelle a destra
        tabellaPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tabellaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        aggiungiTabelle(tabellaPanel);  // Aggiunge le tabelle al caricamento

        panel.add(tabellaPanel, BorderLayout.CENTER);
        
        // Carica i bottoni iniziali (modalità annunci) DOPO aver creato tabellaPanel
        aggiornaVistaEstesa();

        // Bottoni inferiori (Exit e Salva)
        panel.add(creaBottomPanel(), BorderLayout.SOUTH);

        return panel;
    }


    private JPanel creaBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton esciButton = new JButton("Exit");
        esciButton.setFont(new Font("Arial", Font.BOLD, 16));
        esciButton.setPreferredSize(new Dimension(100, 50));
        esciButton.addActionListener(e -> System.exit(0));

        JButton salvaButton = new JButton("💾");
        salvaButton.setFont(new Font("Arial", Font.PLAIN, 25));
        salvaButton.setPreferredSize(new Dimension(60, 50));
        salvaButton.addActionListener(e -> salvaAnnunciSuFile());

        JButton scegliFileButton = new JButton("⋮");
        scegliFileButton.setFont(new Font("Arial", Font.BOLD, 20));
        scegliFileButton.setPreferredSize(new Dimension(30, 50));
        scegliFileButton.addActionListener(e -> salvaAnnunciSuFilePersonalizzato());

        // Bottone di switch modalità (inizialmente impostato su gestione utenti 👤)
        switchModeButton = new JButton("👤");
        switchModeButton.setFont(new Font("Arial", Font.BOLD, 18));
        switchModeButton.setPreferredSize(new Dimension(50, 50));
        switchModeButton.addActionListener(e -> switchModalita());

        // Bottone di reset (inizialmente nascosto)
        resetButton = new JButton("🔄");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setPreferredSize(new Dimension(50, 50));
        resetButton.setVisible(false);
        resetButton.addActionListener(e -> ripristinaVistaCompleta());

        // Aggiunta dei bottoni al pannello
        bottomPanel.add(switchModeButton); // **Aggiunto il bottone di switch**
        bottomPanel.add(esciButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(salvaButton);
        bottomPanel.add(scegliFileButton);

        return bottomPanel;
    }

    private void switchModalita() {
        modalitaUtenti = !modalitaUtenti; // Inverte lo stato

        // Cambia l'icona del bottone tra 👤 (utenti) e 📋 (bacheca)
        switchModeButton.setText(modalitaUtenti ? "📋" : "👤");

        // Aggiorna la vista corretta
        if (getWidth() > 900) {
            aggiornaVistaEstesa();
        } else {
            aggiornaVistaCompatta();
        }
    }

    private void aggiornaVistaBottoni() {
        optionsPanelEsteso.removeAll(); // Svuota i bottoni
        
        // Imposta il padding corretto per la modalità estesa
        optionsPanelEsteso.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        if (modalitaUtenti) {
            // Mostra i bottoni per la gestione utenti
            addOption(optionsPanelEsteso, "Aggiungi Utente", e -> mostraFormAggiungiUtente(e));
            addOption(optionsPanelEsteso, "Rimuovi Utente", e -> mostraFormRimuoviUtente(e));
            addOption(optionsPanelEsteso, "Cerca Utente", e -> mostraFormCercaUtente(e));
            // Non aggiungere "Visualizza Utenti" in modalità estesa perché la tabella è già visibile
        } else {
            // Mostra i bottoni per la gestione della bacheca
            addOption(optionsPanelEsteso, "Aggiungi un nuovo annuncio", e -> mostraFormAggiungiAnnuncio(e));
            addOption(optionsPanelEsteso, "Aggiungi Parola chiave a un Annuncio", e -> mostraFormAggiungiParolaChiave(e));
            addOption(optionsPanelEsteso, "Pulisci la bacheca da annunci scaduti", e -> pulisciBacheca(e));
            addOption(optionsPanelEsteso, "Rimuovi un annuncio esistente", e -> mostraFormRimuoviAnnuncio(e));
            addOption(optionsPanelEsteso, "Cerca Annunci per Parola chiave", e -> mostraFormCercaAnnunci(e));
        }

        optionsPanelEsteso.revalidate();
        optionsPanelEsteso.repaint();
    }

    private void aggiornaVistaCompatta() {
        optionsPanelCompatto.removeAll();
        
        // Imposta il padding corretto per la modalità compatta
        optionsPanelCompatto.setBorder(BorderFactory.createEmptyBorder(30, 120, 20, 120));

        if (modalitaUtenti) {
            addOption(optionsPanelCompatto, "Aggiungi Utente", e -> mostraFormAggiungiUtente(e));
            addOption(optionsPanelCompatto, "Rimuovi Utente", e -> mostraFormRimuoviUtente(e));
            addOption(optionsPanelCompatto, "Cerca Utente", e -> mostraFormCercaUtente(e));
            addOption(optionsPanelCompatto, "Visualizza Utenti", e -> mostraUtenti());
        } else {
            addOption(optionsPanelCompatto, "Aggiungi un nuovo annuncio", e -> mostraFormAggiungiAnnuncio(e));
            addOption(optionsPanelCompatto, "Aggiungi Parola chiave a un Annuncio", e -> mostraFormAggiungiParolaChiave(e));
            addOption(optionsPanelCompatto, "Visualizza tutti gli annunci", e -> aggiornaListaAnnunci(e));
            addOption(optionsPanelCompatto, "Pulisci la bacheca da annunci scaduti", e -> pulisciBacheca(e));
            addOption(optionsPanelCompatto, "Rimuovi un annuncio esistente", e -> mostraFormRimuoviAnnuncio(e));
            addOption(optionsPanelCompatto, "Cerca Annunci per Parola chiave", e -> mostraFormCercaAnnunci(e));
        }

        optionsPanelCompatto.revalidate();
        optionsPanelCompatto.repaint();
    }

    private void aggiornaVistaEstesa() {
        // Aggiorna i bottoni per la vista estesa
        aggiornaVistaBottoni();
        
        tabellaPanel.removeAll();

        if (modalitaUtenti) {
            tabellaPanel.setLayout(new BorderLayout());
            tabellaPanel.add(creaTabellaUtenti(), BorderLayout.CENTER);
        } else {
            aggiungiTabelle(tabellaPanel);
        }

        tabellaPanel.revalidate();
        tabellaPanel.repaint();
    }

    private JScrollPane creaTabellaUtenti() {
        String[] colonne = {"Nome", "Email", "Annunci Vendita", "Annunci Acquisto"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);

        for (Utente utente : gestoreUtenti.getUtentiRegistrati()) {
            long annunciVendita = Arrays.stream(gestoreBacheca.getBacheca().getAnnunci())
                .filter(a -> a instanceof AnnuncioVendita && a.getUtente().equals(utente))
                .count();

            long annunciAcquisto = Arrays.stream(gestoreBacheca.getBacheca().getAnnunci())
                .filter(a -> a instanceof AnnuncioAcquisto && a.getUtente().equals(utente))
                .count();

            model.addRow(new Object[]{utente.getNome(), utente.getEmail(), annunciVendita, annunciAcquisto});
        }

        JTable table = new JTable(model);
        personalizzaTabella(table);
        return new JScrollPane(table);
    }

    private void mostraFormAggiungiUtente(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Aggiungi Utente", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();
        JTextField nomeField = new JTextField();

        JButton aggiungiButton = new JButton("Aggiungi");
        aggiungiButton.addActionListener(event -> {
            String email = emailField.getText().trim();
            String nome = nomeField.getText().trim();

            if (!email.isEmpty() && !nome.isEmpty()) {
                try {
                    gestoreUtenti.aggiungiUtente(new Utente(email, nome));
                    salvaUtentiSuFile(); // Salva automaticamente
                    JOptionPane.showMessageDialog(dialog, "Utente aggiunto con successo!");
                    dialog.dispose();
                    // Aggiorna la vista se siamo in modalità utenti
                    if (modalitaUtenti) {
                        if (getWidth() > 900) {
                            aggiornaVistaEstesa();
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Compila tutti i campi.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(aggiungiButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void mostraFormRimuoviUtente(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Rimuovi Utente", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();

        JButton removeButton = new JButton("Rimuovi Utente");
        removeButton.addActionListener(event -> {
            String email = emailField.getText().trim();

            if (!email.isEmpty()) {
                boolean success = gestoreUtenti.rimuoviUtente(email);
                if (success) {
                    salvaUtentiSuFile(); // Salva automaticamente
                    JOptionPane.showMessageDialog(dialog, "Utente rimosso con successo!");
                    // Aggiorna la vista se siamo in modalità utenti
                    if (modalitaUtenti && getWidth() > 900) {
                        aggiornaVistaEstesa();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Utente non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Inserisci un'email valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Email Utente:"));
        formPanel.add(emailField);
        formPanel.add(removeButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void mostraFormCercaUtente(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Cerca Utente", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();

        JButton cercaButton = new JButton("Cerca Utente");
        cercaButton.addActionListener(event -> {
            String email = emailField.getText().trim();

            if (!email.isEmpty()) {
                Utente utente = gestoreUtenti.cercaUtente(email);
                if (utente != null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Nome: " + utente.getNome() + "\nEmail: " + utente.getEmail(),
                        "Utente Trovato", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Utente non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Inserisci un'email valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Email Utente:"));
        formPanel.add(emailField);
        formPanel.add(cercaButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void mostraUtenti() {
        JDialog dialog = new JDialog(this, "Utenti Registrati", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        String[] colonne = {"Nome", "Email", "Annunci Vendita", "Annunci Acquisto"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);

        for (Utente utente : gestoreUtenti.getUtentiRegistrati()) {
            long annunciVendita = Arrays.stream(gestoreBacheca.getBacheca().getAnnunci())
                .filter(a -> a instanceof AnnuncioVendita && a.getUtente().equals(utente))
                .count();

            long annunciAcquisto = Arrays.stream(gestoreBacheca.getBacheca().getAnnunci())
                .filter(a -> a instanceof AnnuncioAcquisto && a.getUtente().equals(utente))
                .count();

            model.addRow(new Object[]{utente.getNome(), utente.getEmail(), annunciVendita, annunciAcquisto});
        }

        JTable table = new JTable(model);
        personalizzaTabella(table);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    //BOTTONI DELLE FUNZIONI DEL PROGETTO
    private void addOption(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(275, 40));
        button.setMaximumSize(new Dimension(325, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        panel.add(Box.createVerticalStrut(10));
        panel.add(button);
    }

    private void caricaAnnunciDaFile(String filePath) {
        try {
            gestoreBacheca.leggiDaFile(filePath);
        } catch (IOException | GestoreBachecaException e) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento del file: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaUtentiDaFile(String filePath) {
        try {
            gestoreUtenti.leggiDaFile(filePath);
        } catch (IOException e) {
            // Non mostrare errore se il file non esiste (primo avvio)
            System.out.println("File utenti non trovato (primo avvio): " + filePath);
        }
    }

    private void salvaUtentiSuFile() {
        try {
            gestoreUtenti.salvaSuFile("data/utenti.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio degli utenti: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvaAnnunciSuFile() {
        try {
            gestoreBacheca.salvaSuFile("data/annunci.csv");
            JOptionPane.showMessageDialog(this, "Annunci salvati correttamente su file!", "Salvataggio Completato", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | GestoreBachecaException e) {
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio degli annunci: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvaAnnunciSuFilePersonalizzato() {
        JFileChooser fileChooser = new JFileChooser();

        // Imposta il filtro per salvare solo file .csv o .txt
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("File CSV e TXT", "csv", "txt"));

        int scelta = fileChooser.showSaveDialog(this);  // Apre il Finder/Esplora File

        if (scelta == JFileChooser.APPROVE_OPTION) {
            // L'utente ha scelto un file
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Aggiunge l'estensione se l'utente non l'ha messa
            if (!filePath.endsWith(".csv") && !filePath.endsWith(".txt")) {
                filePath += ".csv";  // Predefinito su .csv
            }

            try {
                gestoreBacheca.salvaSuFile(filePath);  // Salva sul percorso scelto
                JOptionPane.showMessageDialog(this, "Annunci salvati correttamente su: " + filePath, "Salvataggio Completato", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | GestoreBachecaException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio degli annunci: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // L'utente ha annullato il salvataggio
            JOptionPane.showMessageDialog(this, "Salvataggio annullato.", "Annullato", JOptionPane.WARNING_MESSAGE);
        }
    }

    /*
     * ------------------------------
     * PERSoNALIZZAZIONE TABELLE
     * ------------------------------
     */
    private void personalizzaTabella(JTable table) {
    // Colore grigio chiaro per l'intestazione delle colonne
    JTableHeader header = table.getTableHeader();
    header.setBackground(Color.LIGHT_GRAY);
    header.setFont(new Font("Arial", Font.PLAIN, 14));
    header.setForeground(Color.BLACK);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    // Aggiungere bordi leggeri alle celle per separare i dati
    table.setShowGrid(true);
    table.setGridColor(new Color(200, 200, 200));  // Grigio molto chiaro

    // Font delle celle
    table.setFont(new Font("Arial", Font.PLAIN, 12));
    table.setRowHeight(25);  // Altezza delle righe
    }

    private void aggiornaListaAnnunci(ActionEvent e) {
        // Crea una finestra di dialogo modale per mostrare gli annunci
        JDialog dialog = new JDialog(this, "Annunci", true);
        dialog.setSize(800, 600);
        dialog.setMinimumSize(new Dimension(600, 400));
        dialog.setMinimumSize(new Dimension(600, 400));
        dialog.setLocationRelativeTo(this);

        // Pannello contenitore per centrare e gestire la spaziatura delle tabelle
        JPanel contenitoreTabelle = new JPanel();
        contenitoreTabelle.setLayout(new BoxLayout(contenitoreTabelle, BoxLayout.Y_AXIS));
        contenitoreTabelle.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));  // Margini ai lati

        // ====================
        // Annunci di Vendita
        // ====================
        JLabel venditaLabel = new JLabel("Annunci di Vendita", SwingConstants.CENTER);
        venditaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        venditaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        venditaLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // Spaziatura sopra e sotto il titolo
        contenitoreTabelle.add(venditaLabel);

        String[] colonneVendita = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave", "Scadenza", "Venduto"};
        DefaultTableModel modelVendita = new DefaultTableModel(colonneVendita, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Rende tutte le celle non modificabili
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) { // Colonna "Venduto"
                    return Boolean.class;  // Visualizza come checkbox
                }
                return String.class;  // Altre colonne come stringhe
            }
        };

        JTable tableVendita = new JTable(modelVendita);
        personalizzaTabella(tableVendita);
        tableVendita.setFocusable(false);  // Rimuove il bordo di selezione
        tableVendita.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableVendita.getColumnModel().getColumn(7).setPreferredWidth(60);  // "Venduto"


        // Custom renderer per disabilitare la checkbox, evitando modifiche da parte dell'utente
        tableVendita.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) value);  // Imposta il valore della checkbox
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                checkBox.setEnabled(false);  // Disabilita l'interazione con la checkbox
                if (isSelected) {
                    checkBox.setBackground(table.getSelectionBackground());
                } else {
                    checkBox.setBackground(table.getBackground());
                }
                return checkBox;
            }
        });

        JScrollPane scrollVendita = new JScrollPane(tableVendita);
        scrollVendita.getViewport().setBackground(Color.WHITE);  // Sfondo bianco
        scrollVendita.setBorder(BorderFactory.createEmptyBorder());  // Rimuove i bordi azzurri
        scrollVendita.setAlignmentX(Component.CENTER_ALIGNMENT);  // Centra la tabella

        contenitoreTabelle.add(scrollVendita);
        contenitoreTabelle.add(Box.createVerticalStrut(20));  // Spaziatura tra le tabelle

        // ====================
        // Annunci di Acquisto
        // ====================
        JLabel acquistoLabel = new JLabel("Annunci di Acquisto", SwingConstants.CENTER);
        acquistoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        acquistoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        acquistoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contenitoreTabelle.add(acquistoLabel);

        String[] colonneAcquisto = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave"};
        DefaultTableModel modelAcquisto = new DefaultTableModel(colonneAcquisto, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableAcquisto = new JTable(modelAcquisto);
        personalizzaTabella(tableAcquisto);
        tableAcquisto.setFocusable(false);

        tableAcquisto.getColumnModel().getColumn(0).setPreferredWidth(10);
        tableAcquisto.getColumnModel().getColumn(5).setPreferredWidth(150);  // "Parole Chiave"


        JScrollPane scrollAcquisto = new JScrollPane(tableAcquisto);
        scrollAcquisto.getViewport().setBackground(Color.WHITE);
        scrollAcquisto.setBorder(BorderFactory.createEmptyBorder());
        scrollAcquisto.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenitoreTabelle.add(scrollAcquisto);

        // ====================
        // Popolamento delle Tabelle
        // ====================
        for (Annuncio annuncio : gestoreBacheca.getBacheca().getAnnunci()) {
            if (annuncio instanceof AnnuncioVendita vendita) {
                modelVendita.addRow(new Object[]{
                    vendita.getId(), vendita.getTitolo(), vendita.getDescrizione(),
                    vendita.getPrezzo(), vendita.getUtente().getEmail(),
                    String.join(", ", vendita.getParoleChiave()),
                    vendita.getDataScadenza(), vendita.isVenduto()
                });
            } else if (annuncio instanceof AnnuncioAcquisto acquisto) {
                modelAcquisto.addRow(new Object[]{
                    acquisto.getId(), acquisto.getTitolo(), acquisto.getDescrizione(),
                    acquisto.getPrezzo(), acquisto.getUtente().getEmail(),
                    String.join(", ", acquisto.getParoleChiave())
                });
            }
        }

        dialog.add(contenitoreTabelle);
        dialog.setVisible(true);
    }

    private void pulisciBacheca(ActionEvent e) {
        // Ottieni il numero di annunci prima della pulizia
        int annunciPrima = gestoreBacheca.getBacheca().getAnnunci().length;

        // Esegui la pulizia della bacheca
        gestoreBacheca.pulisciBacheca();

        // Ottieni il numero di annunci dopo la pulizia
        int annunciDopo = gestoreBacheca.getBacheca().getAnnunci().length;

        // Calcola quanti annunci sono stati eliminati
        int annunciEliminati = annunciPrima - annunciDopo;

        // Aggiorna la tabella principale
        aggiungiTabelle(tabellaPanel);
        tabellaPanel.revalidate();
        tabellaPanel.repaint();

        // Controlla la dimensione della finestra per decidere se mostrare il popup
        if (getWidth() <= 900) {
            // Se siamo in modalità compatta, mostra il popup con gli annunci aggiornati
            aggiornaListaAnnunci(e);
        }

        // Mostra un messaggio con il numero di annunci eliminati
        JOptionPane.showMessageDialog(this,
            "Bacheca pulita dagli annunci scaduti.\nAnnunci rimossi: " + annunciEliminati,
            "Pulizia Completata",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    //cerca annunci di vendita che condividono parole chiave con l’annuncio di acquisto.
    private List<AnnuncioVendita> cercaAnnunciVenditaCorrelati(List<String> paroleChiaveAcquisto) {
        return Arrays.stream(gestoreBacheca.getBacheca().getAnnunci())
            .filter(annuncio -> annuncio instanceof AnnuncioVendita)
            .map(annuncio -> (AnnuncioVendita) annuncio)
            .filter(vendita -> vendita.getParoleChiave().stream()
                .map(String::toLowerCase)
                .anyMatch(paroleChiaveAcquisto::contains))
            .toList();
    }

    //visualizzare gli annunci di vendita che hanno parole chiave in comune.
    private void mostraAnnunciVenditaCorrelati(List<AnnuncioVendita> annunciCorrelati) {
        JDialog dialog = new JDialog(this, "Annunci di Vendita Correlati", true);
        dialog.setSize(600, 400);
        dialog.setMinimumSize(new Dimension(500, 300));
        dialog.setLocationRelativeTo(this);

        String[] colonne = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave", "Scadenza", "Venduto"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Rende tutte le celle non modificabili
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? Boolean.class : String.class;  // Colonna "Venduto" con checkbox
            }
        };

        for (AnnuncioVendita vendita : annunciCorrelati) {
            model.addRow(new Object[]{
                vendita.getId(), vendita.getTitolo(), vendita.getDescrizione(),
                vendita.getPrezzo(), vendita.getUtente().getEmail(),
                String.join(", ", vendita.getParoleChiave()),
                vendita.getDataScadenza(), vendita.isVenduto()
            });
        }

        JTable table = new JTable(model);
        personalizzaTabella(table);
        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private void mostraFormAggiungiAnnuncio(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Aggiungi Annuncio", true);
        dialog.setSize(400, 400);
        dialog.setMinimumSize(new Dimension(350, 350));
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();
        JTextField titoloField = new JTextField();
        JTextField descrizioneField = new JTextField();
        JTextField prezzoField = new JTextField();
        JTextField paroleChiaveField = new JTextField();

        String[] tipiAnnuncio = {"Acquisto", "Vendita"};
        JComboBox<String> tipoAnnuncioBox = new JComboBox<>(tipiAnnuncio);

        JLabel dataScadenzaLabel = new JLabel("Data di Scadenza (YYYY-MM-DD):");
        JTextField dataScadenzaField = new JTextField();
        dataScadenzaLabel.setVisible(false);
        dataScadenzaField.setVisible(false);

        tipoAnnuncioBox.addActionListener(event -> {
            boolean isVendita = tipoAnnuncioBox.getSelectedItem().equals("Vendita");
            dataScadenzaLabel.setVisible(isVendita);
            dataScadenzaField.setVisible(isVendita);
        });

        JButton submitButton = new JButton("Aggiungi Annuncio");
        submitButton.addActionListener(event -> {
            try {
                String email = emailField.getText().trim();
                String titolo = titoloField.getText().trim();
                String descrizione = descrizioneField.getText().trim();
                double prezzo = Double.parseDouble(prezzoField.getText().trim());
                List<String> paroleChiave = List.of(paroleChiaveField.getText().split(","));
                Utente utente = gestoreUtenti.cercaUtente(email);

                if (utente == null) {
                    utente = new Utente(email, email);
                    gestoreUtenti.aggiungiUtente(utente);
                }

                Annuncio nuovoAnnuncio;
                if (tipoAnnuncioBox.getSelectedItem().equals("Vendita")) {
                    LocalDate dataScadenza = LocalDate.parse(dataScadenzaField.getText().trim());
                    nuovoAnnuncio = new AnnuncioVendita(titolo, descrizione, prezzo, utente, paroleChiave, false, dataScadenza);
                } else {
                    nuovoAnnuncio = new AnnuncioAcquisto(titolo, descrizione, prezzo, utente, paroleChiave);

                    // Cerca annunci di vendita con parole chiave in comune
                    List<AnnuncioVendita> annunciCorrelati = cercaAnnunciVenditaCorrelati(paroleChiave);

                    // Mostra gli annunci correlati in una finestra se ci sono corrispondenze
                    if (!annunciCorrelati.isEmpty()) {
                        mostraAnnunciVenditaCorrelati(annunciCorrelati);
                    }
                }

                gestoreBacheca.aggiungiAnnuncio(nuovoAnnuncio);
                JOptionPane.showMessageDialog(dialog, "Annuncio aggiunto con successo!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore nell'inserimento dell'annuncio: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Email Utente:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Titolo:"));
        formPanel.add(titoloField);
        formPanel.add(new JLabel("Descrizione:"));
        formPanel.add(descrizioneField);
        formPanel.add(new JLabel("Prezzo:"));
        formPanel.add(prezzoField);
        formPanel.add(new JLabel("Parole Chiave (separate da virgola):"));
        formPanel.add(paroleChiaveField);
        formPanel.add(new JLabel("Tipo di Annuncio:"));
        formPanel.add(tipoAnnuncioBox);
        formPanel.add(dataScadenzaLabel);
        formPanel.add(dataScadenzaField);
        formPanel.add(submitButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void mostraFormRimuoviAnnuncio(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Rimuovi Annuncio", true);
        dialog.setSize(300, 200);
        dialog.setMinimumSize(new Dimension(300, 200));
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();
        JTextField idField = new JTextField();

        JButton removeButton = new JButton("Rimuovi Annuncio");
        removeButton.addActionListener(event -> {
            try {
                String email = emailField.getText().trim();
                int id = Integer.parseInt(idField.getText().trim());

                if (gestoreBacheca.rimuoviAnnuncio(id, email)) {
                    JOptionPane.showMessageDialog(dialog, "Annuncio rimosso con successo!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Annuncio non trovato o non autorizzato.", "Errore", JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore nella rimozione dell'annuncio: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("Email Utente:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("ID Annuncio:"));
        formPanel.add(idField);
        formPanel.add(removeButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
        // Forza l'aggiornamento della tabella principale
        aggiungiTabelle(tabellaPanel);  // Ricostruisce le tabelle nel pannello principale

        tabellaPanel.revalidate();  // Rende effettivo il cambiamento
        tabellaPanel.repaint();  // Ridisegna il pannello
    }

    private void mostraFormAggiungiParolaChiave(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Aggiungi Parola Chiave", true);
        dialog.setSize(400, 300);
        dialog.setMinimumSize(new Dimension(350, 250));
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField emailField = new JTextField();
        JTextField idField = new JTextField();
        JTextField nuoveParoleField = new JTextField();

        JButton aggiungiButton = new JButton("Aggiungi Parola Chiave");
        aggiungiButton.addActionListener(event -> {
            try {
                String email = emailField.getText().trim();
                int id = Integer.parseInt(idField.getText().trim());
                String[] nuoveParoleArray = nuoveParoleField.getText().split(",");
                List<String> nuoveParole = List.of(nuoveParoleArray);

                Annuncio annuncio = gestoreBacheca.cercaAnnuncioPerId(id);
                if (annuncio == null || !annuncio.getUtente().getEmail().equals(email)) {
                    JOptionPane.showMessageDialog(dialog, "Annuncio non trovato o non appartenente all'utente.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Email inserita: " + email);
                System.out.println("ID Annuncio: " + id);
                System.out.println("Nuove parole chiave: " + nuoveParole);

                gestoreBacheca.aggiungiParoleChiave(id, nuoveParole);
                JOptionPane.showMessageDialog(dialog, "Parole chiave aggiunte con successo!");
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID non valido. Inserisci un numero intero.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore durante l'aggiunta: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aggiunta dei campi al pannello
        formPanel.add(new JLabel("Email Utente:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("ID Annuncio:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nuove Parole Chiave (separate da virgole):"));
        formPanel.add(nuoveParoleField);
        formPanel.add(aggiungiButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
        // Forza l'aggiornamento della tabella principale
        aggiungiTabelle(tabellaPanel);  // Ricostruisce le tabelle nel pannello principale

        tabellaPanel.revalidate();  // Rende effettivo il cambiamento
        tabellaPanel.repaint();  // Ridisegna il pannello
    }


    private void aggiornaTabelleConRisultati(List<AnnuncioVendita> annunciCorrelati) {
        if (annunciCorrelati.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nessun annuncio trovato con le parole chiave specificate.",
                "Ricerca Annunci",
                JOptionPane.INFORMATION_MESSAGE);
            return; // Esce dalla funzione senza modificare la tabella
        }

        tabellaPanel.removeAll(); // Rimuove il contenuto precedente
        tabellaPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // ====== Annunci di Vendita filtrati ======
        JLabel venditaLabel = new JLabel("Annunci di Vendita", SwingConstants.CENTER);
        venditaLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = 0;
        gbc.weightx = 1.0;
        tabellaPanel.add(venditaLabel, gbc);

        String[] colonneVendita = {"ID", "Titolo", "Descrizione", "Prezzo", "Utente", "Parole Chiave", "Scadenza", "Venduto"};
        DefaultTableModel modelVendita = new DefaultTableModel(colonneVendita, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? Boolean.class : String.class;
            }
        };

        JTable tableVendita = new JTable(modelVendita);
        personalizzaTabella(tableVendita);
        JScrollPane scrollVendita = new JScrollPane(tableVendita);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        tabellaPanel.add(scrollVendita, gbc);

        // ** Popola la tabella con gli annunci di vendita filtrati **
        for (AnnuncioVendita vendita : annunciCorrelati) {
            modelVendita.addRow(new Object[]{
                vendita.getId(), vendita.getTitolo(), vendita.getDescrizione(),
                vendita.getPrezzo(), vendita.getUtente().getEmail(),
                String.join(", ", vendita.getParoleChiave()),
                vendita.getDataScadenza(), vendita.isVenduto()
            });
        }

        // **Rende visibile il bottone Reset**
        resetButton.setVisible(true);

        tabellaPanel.revalidate();
        tabellaPanel.repaint();
    }

    // ** Metodo per ripristinare la vista completa con tutti gli annunci **
    private void ripristinaVistaCompleta() {
        tabellaPanel.removeAll();
        aggiungiTabelle(tabellaPanel); // Ricarica le tabelle originali
        resetButton.setVisible(false); // Nasconde il bottone di reset
        tabellaPanel.revalidate();
        tabellaPanel.repaint();
    }

    private void mostraFormCercaAnnunci(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Cerca Annunci per Parole Chiave", true);
        dialog.setSize(400, 200);
        dialog.setMinimumSize(new Dimension(350, 200));
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 1));

        JTextField paroleChiaveField = new JTextField();

        JButton cercaButton = new JButton("Cerca");
        cercaButton.addActionListener(event -> {
            String[] paroleChiaveArray = paroleChiaveField.getText().split(",");

            // Normalizzazione delle parole chiave: rimuove spazi e converte tutto in minuscolo
            List<String> paroleChiave = Arrays.stream(paroleChiaveArray)
                                              .map(String::trim)
                                              .map(String::toLowerCase)
                                              .toList();

            // Cerca gli annunci di vendita correlati
            List<AnnuncioVendita> annunciCorrelati = cercaAnnunciVenditaCorrelati(paroleChiave);

            // Se siamo nella vista compatta -> Mostra popup con i risultati
            if (getWidth() <= 900) {
                if (annunciCorrelati.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nessun annuncio trovato con le parole chiave specificate.");
                } else {
                    mostraAnnunciVenditaCorrelati(annunciCorrelati);
                }
            } else {
                // Altrimenti aggiorniamo direttamente le tabelle senza gli annunci di acquisto
                aggiornaTabelleConRisultati(annunciCorrelati);
            }

            dialog.dispose(); // Chiude la finestra di ricerca
        });

        // Aggiungi i componenti al pannello
        formPanel.add(new JLabel("Inserisci parole chiave (separate da virgola):"));
        formPanel.add(paroleChiaveField);
        formPanel.add(cercaButton);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bacheca bacheca = new Bacheca();
            GestoreUtenti gestoreUtenti = new GestoreUtentiImpl();
            GestoreBacheca gestoreBacheca = new GestoreBachecaImpl(bacheca, gestoreUtenti);
            new MainGUI(gestoreBacheca, gestoreUtenti).setVisible(true);
        });
    }
}
