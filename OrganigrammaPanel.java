package ORGANIGRAMMA_AZIENDALE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrganigrammaPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private UnitaOrganizzativa root;
    private String nomeAzienda;
    private File fileAperto;
    private int totalWidth = 800;
    private int totalHeight = 600;

    public OrganigrammaPanel(UnitaOrganizzativa root, String nomeAzienda) {
        this.root = root;
        this.nomeAzienda = nomeAzienda;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
        
        // Aggiungi un ascoltatore per il ridimensionamento della finestra
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ricalcola la dimensione del pannello
                totalWidth = getWidth();
                totalHeight = getHeight();
                setPreferredSize(new Dimension(totalWidth, totalHeight));
                revalidate();
            }
        });
    }

    public void aggiornaOrganigramma(UnitaOrganizzativa root, String nomeAzienda) {
        this.root = root;
        this.nomeAzienda = nomeAzienda;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNomeAzienda(g);
        drawOrganigramma(g, root, getWidth() / 2, 50, getWidth() / 4, 0);
    }

    private void drawNomeAzienda(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Cambria", Font.BOLD, 25));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textWidth = metrics.stringWidth(nomeAzienda);
        int x = (getWidth() - textWidth)/ 2;
        int y = metrics.getAscent() + 10;
        g.drawString(nomeAzienda, x, y);
    }

    private void drawOrganigramma(Graphics g, UnitaOrganizzativa unita, int x, int y, int offsetX, int livello) {
        int boxWidth = 175;
        int boxHeight = 50;
        int boxSpacingY = 100;
        int dipendentiSpacingY = 20;  
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 16));

        g.drawRect(x - boxWidth / 2, y, boxWidth, boxHeight);

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textWidth = metrics.stringWidth(unita.getNome());
        int textX = x - textWidth / 2;
        int textY = y + ((boxHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(unita.getNome(), textX, textY);

        unita.setBounds(new Rectangle(x - boxWidth / 2, y, boxWidth, boxHeight));

        // Disegna i dipendenti sotto l'unità organizzativa
        int dipendentiY = y + boxHeight + 5;
        drawDipendenti(g, unita.getDipendenti(), x, dipendentiY);

        // Calcola la nuova posizione per le linee di collegamento
        int lineStartY = dipendentiY + dipendentiSpacingY * unita.getDipendenti().size();
        int subY = lineStartY + boxSpacingY;
        int subX = x - offsetX * (unita.getSottoUnità().size() - 1) / 2;

        // Disegna le linee di collegamento e le sub-unità
        for (UnitaOrganizzativa subUnita : unita.getSottoUnità()) {
            g.setColor(Color.BLACK);
            g.drawLine(x, lineStartY, x, subY); // Linea verticale dal centro del rettangolo padre
            g.drawLine(x, subY, subX, subY); // Linea orizzontale verso il centro del rettangolo figlio
            g.drawLine(subX, subY, subX, subY + boxHeight); // Linea verticale verso il rettangolo figlio
            drawOrganigramma(g, subUnita, subX, subY + boxHeight, offsetX / 2, livello + 1);
            subX += offsetX;
        }
    }

    private void drawDipendenti(Graphics g, List<Dipendente> dipendenti, int x, int y) {
        g.setFont(new Font("Cambria", Font.PLAIN, 14));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int textY = y;

        for (Dipendente dipendente : dipendenti) {
            String dipendenteText = dipendente.getRuolo() + ": " + dipendente.getNome() + " " + dipendente.getCognome();
            int textWidth = metrics.stringWidth(dipendenteText);
            int textX = x - textWidth / 2;
            g.drawString(dipendenteText, textX, textY + metrics.getAscent());

            dipendente.setBounds(new Rectangle(textX, textY, textWidth, metrics.getHeight()));

            textY += metrics.getHeight() + 5;
        }
    }

    public void apriFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int scelta = fileChooser.showOpenDialog(this);
        if (scelta == JFileChooser.APPROVE_OPTION) {
            fileAperto = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileAperto))) {
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    sb.append(linea).append("\n");
                }
                aggiornaOrganigrammaDaTesto(sb.toString());
                JOptionPane.showMessageDialog(this, "File aperto con successo.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Errore durante l'apertura del file.");
                e.printStackTrace();
            }
        }
    }

    public void salvaFile() {
        if (fileAperto != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileAperto))) {
                writer.write(getOrganigrammaAsText());
                JOptionPane.showMessageDialog(this, "File salvato con successo.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del file.");
                e.printStackTrace();
            }
        } else {
            salvaFileCome();
        }
    }

    public void salvaFileCome() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int scelta = fileChooser.showSaveDialog(this);
        if (scelta == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(getOrganigrammaAsText());
                JOptionPane.showMessageDialog(this, "File salvato con successo.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void aggiornaOrganigrammaDaTesto(String testo) {
        String[] righe = testo.split("\n");
        UnitaOrganizzativa unitaRadice = null;
        List<UnitaOrganizzativa> stackUnita = new ArrayList<>();
        UnitaOrganizzativa unitaCorrente = null;
        String nomeAziendaLocal = "";

        for (String riga : righe) {
            // Rimuovere eventuali spazi bianchi all'inizio e alla fine della riga
            riga = riga.trim();

            if (riga.startsWith("Organigramma Azienda:")) {
                nomeAziendaLocal = riga.substring("Organigramma Azienda:".length()).trim();
            } else if (riga.startsWith("-")) {
                // Indentazione calcolata come il numero di tabulazioni all'inizio della riga
                int indentazione = countLeadingTabs(riga);

                // Creare una nuova unità organizzativa
                UnitaOrganizzativa nuovaUnita = new UnitaOrganizzativa(riga.substring(1).trim());

                if (indentazione == 0) {
                    // Se l'indentazione è 0, questa è la radice o un livello superiore
                    if (stackUnita.isEmpty()) {
                        unitaRadice = nuovaUnita;
                    } else {
                        UnitaOrganizzativa unitaPadre = stackUnita.get(stackUnita.size() - 1);
                        unitaPadre.aggiungiSottoUnita(nuovaUnita);
                    }
                } else {
                    // Altrimenti, aggiungere come sotto-unità dell'unità corrente
                    while (stackUnita.size() > indentazione) {
                        stackUnita.remove(stackUnita.size() - 1);
                    }
                    if (!stackUnita.isEmpty()) {
                        UnitaOrganizzativa unitaPadre = stackUnita.get(stackUnita.size() - 1);
                        unitaPadre.aggiungiSottoUnita(nuovaUnita);
                    } else {
                        unitaRadice = nuovaUnita;
                    }
                }

                stackUnita.add(nuovaUnita);
                unitaCorrente = nuovaUnita;
            } else if (riga.startsWith("*")) {
                String[] partiDipendente = riga.substring(1).trim().split(":");
                if (partiDipendente.length == 2) {
                    String ruolo = partiDipendente[0].trim();
                    String[] nomeCognome = partiDipendente[1].trim().split(" ");
                    if (nomeCognome.length >= 2) {
                        String nome = nomeCognome[0].trim();
                        String cognome = nomeCognome[1].trim();
                        Dipendente dipendente = new Dipendente(nome, cognome, ruolo, null);
                        if (unitaCorrente != null) {
                            unitaCorrente.aggiungiDipendente(dipendente);
                        }
                    }
                }
            } else if (riga.isEmpty()) {
                if (!stackUnita.isEmpty()) {
                    stackUnita.remove(stackUnita.size() - 1);
                }
            }
        }

        this.nomeAzienda = nomeAziendaLocal;
        this.root = unitaRadice;
        repaint();
    }

    public String getOrganigrammaAsText() {
        StringBuilder sb = new StringBuilder();

        sb.append("Organigramma Azienda: ").append(nomeAzienda).append("\n");
        sb.append("\n");

        buildOrganigrammaText(root, sb, 0);

        return sb.toString();
    }

    private void buildOrganigrammaText(UnitaOrganizzativa unita, StringBuilder sb, int livello) {
        String indent = "\t".repeat(livello);

        sb.append(indent).append("- ").append(unita.getNome()).append("\n");

        for (Dipendente dipendente : unita.getDipendenti()) {
            sb.append(indent).append("\t* ").append(dipendente.getRuolo())
              .append(": ").append(dipendente.getNome()).append(" ")
              .append(dipendente.getCognome()).append("\n");
        }

        for (UnitaOrganizzativa sottoUnita : unita.getSottoUnità()) {
            buildOrganigrammaText(sottoUnita, sb, livello + 1);
        }
    }

    // Metodo ausiliario per contare il numero di tabulazioni all'inizio di una riga
    private int countLeadingTabs(String riga) {
        int count = 0;
        while (count < riga.length() && riga.charAt(count) == '\t') {
            count++;
        }
        return count;
    }

}
