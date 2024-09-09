package ORGANIGRAMMA_AZIENDALE;

import java.awt.Rectangle;
import java.util.List;

public abstract class UnitaOrganizzativaDecorator implements UnitaOrganizzativaInterface {
    protected UnitaOrganizzativaInterface decorate;

    public UnitaOrganizzativaDecorator(UnitaOrganizzativaInterface decorate) {
        this.decorate = decorate;
    }

    @Override
    public String getNome() { 
    	return decorate.getNome(); 
    }

    @Override
    public void setNome(String nome) { 
    	decorate.setNome(nome); 
    }

    @Override
    public List<UnitaOrganizzativa> getSottoUnità() { 
    	return decorate.getSottoUnità(); 
    }

    @Override
    public void setSottoUnità(List<UnitaOrganizzativa> sottoUnità) { 
    	decorate.setSottoUnità(sottoUnità); 
    }

    @Override
    public List<Dipendente> getDipendenti() { 
    	return decorate.getDipendenti(); 
    }

    @Override
    public void setDipendenti(List<Dipendente> dipendenti) { 
    	decorate.setDipendenti(dipendenti); 
    }

    @Override
    public void aggiungiDipendente(Dipendente dipendente) { 
    	decorate.aggiungiDipendente(dipendente); 
    }

    @Override
    public void rimuoviDipendente(Dipendente dipendente) { 
    	decorate.rimuoviDipendente(dipendente); 
    }

    @Override
    public UnitaOrganizzativa getParent() { 
    	return decorate.getParent(); 
    }

    @Override
    public void setParent(UnitaOrganizzativa parent) { 
    	decorate.setParent(parent); 
    }

    @Override
    public void aggiungiSottoUnita(UnitaOrganizzativa nuovaUnita) { 
    	decorate.aggiungiSottoUnita(nuovaUnita); 
    }

    @Override
    public LivelloAccesso getLivelloAccesso() { 
    	return decorate.getLivelloAccesso(); 
    }

    @SuppressWarnings("exports")
	@Override
    public Rectangle getBounds() { 
    	return decorate.getBounds(); 
    }

    @SuppressWarnings("exports")
	@Override
    public void setBounds(Rectangle bounds) { 
    	decorate.setBounds(bounds); 
    }
}
