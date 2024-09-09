package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;
import java.util.List;

public interface UnitaOrganizzativaInterface {
    String getNome();
    void setNome(String nome);
    List<UnitaOrganizzativa> getSottoUnità();
    void setSottoUnità(List<UnitaOrganizzativa> sottoUnità);
    List<Dipendente> getDipendenti();
    void setDipendenti(List<Dipendente> dipendenti);
    void aggiungiDipendente(Dipendente dipendente);
    void rimuoviDipendente(Dipendente dipendente);
    UnitaOrganizzativa getParent();
    void setParent(UnitaOrganizzativa parent);
    void aggiungiSottoUnita(UnitaOrganizzativa nuovaUnita);
    LivelloAccesso getLivelloAccesso();
    @SuppressWarnings("exports")
	Rectangle getBounds();
    @SuppressWarnings("exports")
	void setBounds(Rectangle bounds);
}
