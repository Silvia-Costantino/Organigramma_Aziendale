package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;

public interface DipendenteInterface {
    String getNome();
    void setNome(String nome);
    String getCognome();
    void setCognome(String cognome);
    String getRuolo();
    void setRuolo(String ruolo);
    LivelloAccesso getLivelloAccesso();
    void setLivelloAccesso(LivelloAccesso livelloAccesso);
    @SuppressWarnings("exports")
	Rectangle getBounds();
    @SuppressWarnings("exports")
	void setBounds(Rectangle bounds);
}
