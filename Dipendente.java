package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;
import java.io.Serializable;

public class Dipendente implements DipendenteInterface, Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String cognome;
    private String ruolo;
    private LivelloAccesso livelloAccesso;
    private Rectangle bounds;

    public Dipendente(String nome, String cognome, String ruolo, LivelloAccesso livelloAccesso) {
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.livelloAccesso = livelloAccesso;
        this.bounds = new Rectangle();
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String getRuolo() {
        return ruolo;
    }

    @Override
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public LivelloAccesso getLivelloAccesso() {
        return livelloAccesso;
    }

    @Override
    public void setLivelloAccesso(LivelloAccesso livelloAccesso) {
        this.livelloAccesso = livelloAccesso;
    }

    @SuppressWarnings("exports")
	@Override
    public Rectangle getBounds() {
        return bounds;
    }

    @SuppressWarnings("exports")
	@Override
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
