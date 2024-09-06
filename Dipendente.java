package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;
import java.io.Serializable;

public class Dipendente implements Serializable{
	private static final long serialVersionUID = 1L;
    private String nome;
    private String cognome;
    private String ruolo;
    private LivelloAccesso livelloAccesso;
    private Rectangle bounds;
    
    public Dipendente (String nome, String cognome, String ruolo, LivelloAccesso livelloAccesso ) {
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.livelloAccesso = livelloAccesso;
        this.bounds = new Rectangle();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    
    public LivelloAccesso getLivelloAccesso() {
        return livelloAccesso;
    }
}

