package ORGANIGRAMMA_AZIENDALE;
public class LeggiUnitaOrganizzativaDecorator extends UnitaOrganizzativaDecorator {

    public LeggiUnitaOrganizzativaDecorator(UnitaOrganizzativaInterface decorata) {
        super(decorata);
    }

    @Override
    public void aggiungiDipendente(Dipendente dipendente) {
        System.out.println("Aggiungendo dipendente: " + dipendente.getNome());
        super.aggiungiDipendente(dipendente);
    }

    @Override
    public void rimuoviDipendente(Dipendente dipendente) {
        System.out.println("Rimuovendo dipendente: " + dipendente.getNome());
        super.rimuoviDipendente(dipendente);
    }
}
