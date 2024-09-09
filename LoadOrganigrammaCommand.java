package ORGANIGRAMMA_AZIENDALE;

import java.io.File;

public class LoadOrganigrammaCommand implements Command {
    @SuppressWarnings("unused")
	private PaginaIniziale paginaIniziale;
    private File file;

    public LoadOrganigrammaCommand(PaginaIniziale paginaIniziale, File file) {
        this.paginaIniziale = paginaIniziale;
        this.file = file;
    }

    @Override
    public void execute() {
        PaginaIniziale.caricaOrganigramma(file);
    }
}
