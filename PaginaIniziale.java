package ORGANIGRAMMA_AZIENDALE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class PaginaIniziale {

    private static DefaultTableModel dipendentiTableModel;

    public static void main(String[] args) {
    	dipendentiTableModel = new DefaultTableModel(new Object[]{"Dipendente", "Ruolo"}, 0);
    	
        JFrame startFrame = new JFrame("ORGANIGRAMMA AZIENDALE");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(400, 200);
        startFrame.setLayout(new BorderLayout());
        startFrame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton nuovoOrg = new JButton("Crea nuovo organigramma");
        buttonPanel.add(nuovoOrg);
        JButton caricaOrg = new JButton("Apri organigramma esistente");
        buttonPanel.add(caricaOrg);

        startFrame.add(buttonPanel, BorderLayout.CENTER);
        startFrame.setVisible(true);

        nuovoOrg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose();
                creaNuovoOrganigramma();
            }
        });

        caricaOrg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    startFrame.dispose();
                    caricaOrganigramma(selectedFile);
                }
            }
        });
    }

    private static void creaNuovoOrganigramma() {
        String nomeAzienda = JOptionPane.showInputDialog("Inserisci il nome dell'azienda: ");
        if (nomeAzienda == null || nomeAzienda.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Il nome dell'azienda non può essere vuoto.");
            return;
        }

        String nomeUnitaPrincipale = JOptionPane.showInputDialog("Inserisci il nome dell'unità principale: ");
        if (nomeUnitaPrincipale == null || nomeUnitaPrincipale.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Il nome dell'unità principale non può essere vuoto.");
            return;
        }

        UnitaOrganizzativa radice = new UnitaOrganizzativa(nomeUnitaPrincipale);
        Organigramma organigramma = new Organigramma(radice,dipendentiTableModel);
        organigramma.creaEvisualizzaOrganigramma(radice, nomeAzienda);

    }

    private static void caricaOrganigramma(File file) {
        String nomeAzienda = "";
        UnitaOrganizzativa root = null;
        Stack<UnitaOrganizzativa> stack = new Stack<>();
        int currentIndentation = -1;
        DefaultTableModel dipendentiTableModel = new DefaultTableModel(new Object[]{"Nome Dipendente", "Ruolo"}, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Organigramma Azienda:")) {
                    nomeAzienda = line.substring("Organigramma Azienda:".length()).trim();
                    System.out.println("Nome Azienda: " + nomeAzienda);
                } else {
                    int indentation = countLeadingTabs(line);
                    String lineContent = line.trim();

                    if (lineContent.startsWith("- ")) {
                        // Unità organizzativa
                        String nomeUnita = lineContent.substring(2).trim();
                        UnitaOrganizzativa newUnit = new UnitaOrganizzativa(nomeUnita);

                        if (indentation > currentIndentation) {
                            // Nuovo livello
                            if (!stack.isEmpty()) {
                                stack.peek().aggiungiSottoUnita(newUnit);
                            } else {
                                root = newUnit;
                            }
                            stack.push(newUnit);
                        } else {
                            // Livello superiore
                            while (currentIndentation >= indentation && stack.size() > 1) {
                                stack.pop();
                                currentIndentation--;
                            }
                            if (!stack.isEmpty()) {
                                stack.peek().aggiungiSottoUnita(newUnit);
                            } else {
                                root = newUnit;
                            }
                            stack.push(newUnit);
                        }

                        currentIndentation = indentation;
                        System.out.println("Unità aggiunta: " + nomeUnita + " con indentazione " + indentation);
                    } else if (lineContent.startsWith("* ")) {
                        // Aggiungi dipendente
                        if (!stack.isEmpty()) {
                            String[] parts = lineContent.substring(2).split(":");
                            if (parts.length == 2) {
                                String ruolo = parts[0].trim();
                                String nomeDipendente = parts[1].trim();

                                String[] nomeCognome = nomeDipendente.split(" ");
                                String nome = nomeCognome[0];
                                String cognome = nomeCognome.length > 1 ? nomeCognome[1] : "";

                                Dipendente dipendente = new Dipendente(nome, cognome, ruolo, stack.peek().getLivelloAccesso());
                                stack.peek().aggiungiDipendente(dipendente);
                                dipendentiTableModel.addRow(new Object[]{nome + " " + cognome, ruolo});
                                System.out.println("Dipendente aggiunto: " + nome + " " + cognome + " alla unità " + stack.peek().getNome());
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nel caricamento dell'organigramma: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (root != null && !nomeAzienda.isEmpty()) {
            System.out.println("Creazione organigramma per l'azienda: " + nomeAzienda);
            Organigramma organigramma = new Organigramma(root, dipendentiTableModel);
            organigramma.creaEvisualizzaOrganigramma(root, nomeAzienda);
        } else {
            System.out.println("Errore: root o nome azienda non valido.");
        }
    }


    // Metodo di supporto per contare le tabulazioni che rappresentano l'indentazione
    private static int countLeadingTabs(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == '\t') {
            count++;
        }
        return count;
    }
}
