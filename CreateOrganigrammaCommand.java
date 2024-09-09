package ORGANIGRAMMA_AZIENDALE;

public class CreateOrganigrammaCommand implements Command {
    private PaginaIniziale paginaIniziale;

    public CreateOrganigrammaCommand(PaginaIniziale paginaIniziale) {
        this.paginaIniziale = paginaIniziale;
    }

    @Override
    public void execute() {
        paginaIniziale.creaNuovoOrganigramma();
    }
}

