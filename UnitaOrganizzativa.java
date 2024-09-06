package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UnitaOrganizzativa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private List<UnitaOrganizzativa> sottoUnità;
    private List<Dipendente> dipendenti;
    private UnitaOrganizzativa parent; // Riferimento al genitore
    private Rectangle bounds; 
    
    public UnitaOrganizzativa(String nome) {
        this.nome = nome;
        this.sottoUnità = new ArrayList<>();
        this.dipendenti = new ArrayList<>();
        this.bounds = new Rectangle(); 
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<UnitaOrganizzativa> getSottoUnità() {
        return sottoUnità;
    }
    
    public void setSottoUnità(List<UnitaOrganizzativa> sottoUnità) {
        this.sottoUnità = sottoUnità;
    }

    public List<Dipendente> getDipendenti() {
        return dipendenti;
    }
    
    public void setDipendenti(List<Dipendente> dipendenti) {
        this.dipendenti = dipendenti;
    }
    
    public void aggiungiDipendente(Dipendente dipendente) {
        dipendenti.add(dipendente);
    }
    
    public void rimuoviDipendente(Dipendente dipendente) {
    	dipendenti.remove(dipendente);
    }
    
    public UnitaOrganizzativa getParent() {
        return parent;
    }

    public void setParent(UnitaOrganizzativa parent) {
        this.parent = parent;
    }

    public void aggiungiSottoUnita(UnitaOrganizzativa nuovaUnita) {// Aggiunge la nuova unità all'elenco delle sotto-unità
        sottoUnità.add(nuovaUnita);
        // Imposta il genitore della nuova unità come l'unità corrente
        nuovaUnita.setParent(this);
    }

    public LivelloAccesso getLivelloAccesso() {// Determina il livello di accesso dell'unità organizzativa
        int livello = 0;
        UnitaOrganizzativa corrente = this;
        // Calcola la profondità nella gerarchia
        while (corrente.getParent() != null) {
            corrente = corrente.getParent();
            livello++;
        }
        // Restituisce il livello di accesso basato sulla profondità
        switch (livello) {
            case 0:
                return LivelloAccesso.PRIMO_LIVELLO;
            case 1:
                return LivelloAccesso.SECONDO_LIVELLO;
            case 2:
                return LivelloAccesso.TERZO_LIVELLO;
            // Aggiungi altri casi se necessario per livelli ulteriori
            default:
                throw new IllegalArgumentException("Livello di accesso non definito per la profondità: " + livello);
        }
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
