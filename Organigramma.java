package ORGANIGRAMMA_AZIENDALE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Organigramma implements Serializable {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel dipendentiTableModel;
	@SuppressWarnings("unused")
	private UnitaOrganizzativa radice;

    public Organigramma(UnitaOrganizzativa radice,DefaultTableModel dipendentiTableModel ) {
        this.radice = radice;
        this.dipendentiTableModel = dipendentiTableModel;
    }

    public void aggiungiNodo(UnitaOrganizzativa nodo, UnitaOrganizzativa superiore) {
        if (superiore != null) {
            superiore.getSottoUnità().add(nodo);
        }
    }
    

    public void creaEvisualizzaOrganigramma(UnitaOrganizzativa nodo, String nomeAzienda) {
        JFrame frame = new JFrame("Organigramma Aziendale");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        JPanel controllo = new JPanel();
        controllo.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton aggiungiUnitOrg = new JButton("Aggiungi Unità Organizzativa");
        JButton rimuoviUnitOrg = new JButton("Rimuovi Unità Organizzativa");
        JButton aggiungiDipendente = new JButton("Aggiungi Dipendente");
        JButton rimuoviDipendente = new JButton("Rimuovi Dipendente");
        JButton salva = new JButton("Salva Organigramma");

        controllo.add(aggiungiUnitOrg);
        controllo.add(rimuoviUnitOrg);
        controllo.add(aggiungiDipendente);
        controllo.add(rimuoviDipendente);
        controllo.add(salva);

        frame.add(controllo, BorderLayout.NORTH);

        JPanel pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BorderLayout());

        OrganigrammaPanel organigrammaPanel = new OrganigrammaPanel(nodo, nomeAzienda);
        organigrammaPanel.setPreferredSize(new Dimension(1500, 2000));
        JScrollPane organigrammaScrollPane = new JScrollPane(organigrammaPanel);
        frame.add(organigrammaScrollPane, BorderLayout.CENTER);

        JTable dipendentiTable = new JTable(dipendentiTableModel);
        dipendentiTable.setEnabled(false);
        JScrollPane dipendentiScrollPane = new JScrollPane(dipendentiTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, organigrammaScrollPane, dipendentiScrollPane);
        splitPane.setDividerLocation(800);

        pannelloPrincipale.add(splitPane, BorderLayout.CENTER);

        frame.add(pannelloPrincipale, BorderLayout.CENTER);

        aggiungiUnitOrg.addActionListener(e -> aggiungiUnitaOrganizzativa(nodo, organigrammaPanel));
        aggiungiDipendente.addActionListener(e -> aggiungiDipendente(nodo, organigrammaPanel));
        rimuoviDipendente.addActionListener(e -> rimuoviDipendente(nodo, organigrammaPanel));
        rimuoviUnitOrg.addActionListener(e -> rimuoviUnitaOrganizzativa(nodo, organigrammaPanel));
        salva.addActionListener(e -> salvaOrganigramma(organigrammaPanel));

        frame.setVisible(true);
    }


    private void aggiungiUnitaOrganizzativa(UnitaOrganizzativa nodo, OrganigrammaPanel organigrammaPanel) {
        List<UnitaOrganizzativa> tutteLeUnita = getAllSubUnita(nodo);
        String[] nomiUnita = tutteLeUnita.stream()
                                         .map(UnitaOrganizzativa::getNome)
                                         .toArray(String[]::new);

        String unitaSelezionata = (String) JOptionPane.showInputDialog(
            null,
            "Seleziona l'unità padre:",
            "Selezione Unità",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nomiUnita,
            nomiUnita[0]
        );
        if (unitaSelezionata == null) {
            return;
        }
        UnitaOrganizzativa unitaPadre = tutteLeUnita.stream()
                                                    .filter(unita -> unita.getNome().equals(unitaSelezionata))
                                                    .findFirst()
                                                    .orElse(null);
        String nomeUnita = JOptionPane.showInputDialog("Inserisci il nome della nuova unità organizzativa: ");
        if (nomeUnita != null && !nomeUnita.trim().isEmpty()) {
            UnitaOrganizzativa nuovaUnita = new UnitaOrganizzativa(nomeUnita);
            aggiungiNodo(nuovaUnita, unitaPadre);
            organigrammaPanel.revalidate();
            organigrammaPanel.repaint();

        } else {
            JOptionPane.showMessageDialog(null, "Il nome dell'unità non può essere vuoto.");
        }
    }

    public void aggiungiDipendente(UnitaOrganizzativa nodo, OrganigrammaPanel organigrammaPanel) {
        List<UnitaOrganizzativa> tutteLeUnita = getAllSubUnita(nodo);
        String[] nomiUnita = tutteLeUnita.stream()
                                         .map(UnitaOrganizzativa::getNome)
                                         .toArray(String[]::new);

        String nome = null;
        String cognome = null;
        boolean aggiungiAltroRuolo = true;

        while (aggiungiAltroRuolo) {
            if (nome == null && cognome == null) {
                nome = JOptionPane.showInputDialog("Inserisci il nome del dipendente: ");
                cognome = JOptionPane.showInputDialog("Inserisci il cognome del dipendente: ");
                if (nome == null || cognome == null) {
                    JOptionPane.showMessageDialog(null, "Nome e cognome non possono essere vuoti.");
                    return;
                }
            }

            String unitaSelezionata = (String) JOptionPane.showInputDialog(
                null,
                "Seleziona l'unità per il dipendente:",
                "Selezione Unità",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomiUnita,
                nomiUnita[0]
            );
            if (unitaSelezionata == null) {
                return;
            }
            UnitaOrganizzativa unitaSelezionataObj = tutteLeUnita.stream()
                                                                 .filter(unita -> unita.getNome().equals(unitaSelezionata))
                                                                 .findFirst()
                                                                 .orElse(null);

            if (unitaSelezionataObj != null) {
                String ruolo = JOptionPane.showInputDialog("Inserisci il ruolo del dipendente: ");
                if (ruolo != null) {
                    Dipendente nuovoDipendente = new Dipendente(nome, cognome, ruolo, unitaSelezionataObj.getLivelloAccesso());
                    dipendentiTableModel.addRow(new Object[]{nome + " " + cognome, ruolo});
                    unitaSelezionataObj.aggiungiDipendente(nuovoDipendente);
                    organigrammaPanel.revalidate();
                    organigrammaPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Il ruolo del dipendente non può essere vuoto.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unità selezionata non trovata.");
            }

            int risposta = JOptionPane.showConfirmDialog(
                null,
                "Il dipendente ha un altro ruolo in un'altra unità?",
                "Aggiungi altro ruolo",
                JOptionPane.YES_NO_OPTION
            );

            if (risposta != JOptionPane.YES_OPTION) {
                aggiungiAltroRuolo = false;
            }
        }
    }


    public void rimuoviDipendente(UnitaOrganizzativa nodo, OrganigrammaPanel organigrammaPanel) {
        List<Dipendente> tuttiDipendenti = getAllDipendenti(nodo);

        if (tuttiDipendenti.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nessun dipendente trovato.");
            return;
        }

        String[] nomiDipendenti = tuttiDipendenti.stream()
                                                 .map(dipendente -> dipendente.getNome() + " " + dipendente.getCognome())
                                                 .toArray(String[]::new);

        String dipendenteSelezionato = (String) JOptionPane.showInputDialog(
            null,
            "Seleziona il dipendente da rimuovere:",
            "Selezione Dipendente",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nomiDipendenti,
            nomiDipendenti[0]
        );

        if (dipendenteSelezionato == null) {
            return;
        }

        Dipendente dipendenteDaRimuovere = tuttiDipendenti.stream()
                                                          .filter(dipendente -> (dipendente.getNome() + " " + dipendente.getCognome()).equals(dipendenteSelezionato))
                                                          .findFirst()
                                                          .orElse(null);

        if (dipendenteDaRimuovere != null) {
            rimuoviDipendenteDallaTabella(dipendenteDaRimuovere);
            rimuoviDipendenteDallUnita(dipendenteDaRimuovere, nodo);
            organigrammaPanel.revalidate();
            organigrammaPanel.repaint();

        } else {
            JOptionPane.showMessageDialog(null, "Dipendente selezionato non trovato.");
        }
    }

    private void rimuoviDipendenteDallaTabella(Dipendente dipendente) {
        for (int i = 0; i < dipendentiTableModel.getRowCount(); i++) {
            String nomeCognome = (String) dipendentiTableModel.getValueAt(i, 0);
            String ruolo = (String) dipendentiTableModel.getValueAt(i, 1);
            if (nomeCognome.equals(dipendente.getNome() + " " + dipendente.getCognome()) && ruolo.equals(dipendente.getRuolo())) {
                dipendentiTableModel.removeRow(i);
                i--; // Decrementa l'indice per gestire la rimozione
            }
        }
    }

    private void rimuoviDipendenteDallUnita(Dipendente dipendente, UnitaOrganizzativa unita) {
        unita.rimuoviDipendente(dipendente);
        for (UnitaOrganizzativa sottoUnita : unita.getSottoUnità()) {
            rimuoviDipendenteDallUnita(dipendente, sottoUnita);
        }
    }

    public void rimuoviUnitaOrganizzativa(UnitaOrganizzativa nodo, OrganigrammaPanel organigrammaPanel) {
        List<UnitaOrganizzativa> tutteLeUnita = getAllSubUnita(nodo);

        if (tutteLeUnita.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nessuna unità organizzativa trovata.");
            return;
        }

        String[] nomiUnita = tutteLeUnita.stream()
                                         .map(UnitaOrganizzativa::getNome)
                                         .toArray(String[]::new);

        String unitaSelezionata = (String) JOptionPane.showInputDialog(
            null,
            "Seleziona l'unità da rimuovere:",
            "Selezione Unità",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nomiUnita,
            nomiUnita[0]
        );

        if (unitaSelezionata == null) {
            return;
        }

        UnitaOrganizzativa unitaDaRimuovere = tutteLeUnita.stream()
                                                           .filter(unita -> unita.getNome().equals(unitaSelezionata))
                                                           .findFirst()
                                                           .orElse(null);

        if (unitaDaRimuovere != null) {
            rimuoviUnitaDallaTabella(unitaDaRimuovere);
            rimuoviUnitaDallStruttura(unitaDaRimuovere, nodo);
            organigrammaPanel.revalidate();
            organigrammaPanel.repaint();

        } else {
            JOptionPane.showMessageDialog(null, "Unità selezionata non trovata.");
        }
    }

    private void rimuoviUnitaDallaTabella(UnitaOrganizzativa unita) {
        // Rimuove tutti i dipendenti associati all'unità
        unita.getDipendenti().forEach(dipendente -> rimuoviDipendenteDallaTabella(dipendente));
        dipendentiTableModel.fireTableDataChanged();
    }

    private void rimuoviUnitaDallStruttura(UnitaOrganizzativa unita, UnitaOrganizzativa radice) {
        if (radice.getSottoUnità().remove(unita)) {
            // L'unità è stata trovata e rimossa
            return;
        }
        for (UnitaOrganizzativa sottoUnita : radice.getSottoUnità()) {
            rimuoviUnitaDallStruttura(unita, sottoUnita);
        }
    }

    public List<Dipendente> getAllDipendenti(UnitaOrganizzativa root) {
        List<Dipendente> allDipendenti = new ArrayList<>();
        collectDipendenti(root, allDipendenti);
        return allDipendenti;
    }

    private void collectDipendenti(UnitaOrganizzativa unita, List<Dipendente> allDipendenti) {
        allDipendenti.addAll(unita.getDipendenti());
        for (UnitaOrganizzativa subUnita : unita.getSottoUnità()) {
            collectDipendenti(subUnita, allDipendenti);
        }
    }

    private void salvaOrganigramma(OrganigrammaPanel organigrammaPanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text File", "txt")); // Filtra i file TXT
        int scelta = fileChooser.showSaveDialog(null);
        if (scelta == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            String organigrammaTesto = organigrammaPanel.getOrganigrammaAsText();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(organigrammaTesto);
                JOptionPane.showMessageDialog(null, "Organigramma salvato come file di testo con successo.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dell'organigramma.");
            }
        }
    }

    private List<UnitaOrganizzativa> getAllSubUnita(UnitaOrganizzativa root) {
        List<UnitaOrganizzativa> allSubUnita = new ArrayList<>();
        collectSubUnita(root, allSubUnita);
        return allSubUnita;
    }

    private void collectSubUnita(UnitaOrganizzativa unita, List<UnitaOrganizzativa> allSubUnita) {
        allSubUnita.add(unita);
        for (UnitaOrganizzativa subUnita : unita.getSottoUnità()) {
            collectSubUnita(subUnita, allSubUnita);
        }
    }

	public UnitaOrganizzativa getUnitaOrganizzativa(UnitaOrganizzativa figlio) {
		return figlio;
	}
}
