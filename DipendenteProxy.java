package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;

public class DipendenteProxy implements DipendenteInterface {
    private DipendenteInterface dipendenteReale;

    public DipendenteProxy(DipendenteInterface dipendenteReale) {
        this.dipendenteReale = dipendenteReale;
    }

    @Override
    public String getNome() {
        return dipendenteReale.getNome();
    }

    @Override
    public void setNome(String nome) {
        // Aggiungi comportamenti extra come logging
        System.out.println("Modifica nome del dipendente: " + nome);
        dipendenteReale.setNome(nome);
    }

    @Override
    public String getCognome() {
        return dipendenteReale.getCognome();
    }

    @Override
    public void setCognome(String cognome) {
        // Aggiungi comportamenti extra come logging
        System.out.println("Modifica cognome del dipendente: " + cognome);
        dipendenteReale.setCognome(cognome);
    }

    @Override
    public String getRuolo() {
        return dipendenteReale.getRuolo();
    }

    @Override
    public void setRuolo(String ruolo) {
        // Aggiungi comportamenti extra come logging
        System.out.println("Modifica ruolo del dipendente: " + ruolo);
        dipendenteReale.setRuolo(ruolo);
    }

    @Override
    public LivelloAccesso getLivelloAccesso() {
        return dipendenteReale.getLivelloAccesso();
    }

    @Override
    public void setLivelloAccesso(LivelloAccesso livelloAccesso) {
        // Aggiungi comportamenti extra come logging
        System.out.println("Modifica livello di accesso del dipendente: " + livelloAccesso);
        dipendenteReale.setLivelloAccesso(livelloAccesso);
    }

    @SuppressWarnings("exports")
	@Override
    public Rectangle getBounds() {
        return dipendenteReale.getBounds();
    }

    @SuppressWarnings("exports")
	@Override
    public void setBounds(Rectangle bounds) {
        // Aggiungi comportamenti extra come logging
        System.out.println("Modifica bounds del dipendente: " + bounds);
        dipendenteReale.setBounds(bounds);
    }

	
}
