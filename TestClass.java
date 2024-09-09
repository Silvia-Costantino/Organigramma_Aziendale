package ORGANIGRAMMA_AZIENDALE;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestClass {
    private Organigramma organigramma;
    private UnitaOrganizzativa radice;
    private DefaultTableModel dipendentiTableModel;
    private LivelloAccesso livello;

    @Before
    public void setUp() {
        radice = new UnitaOrganizzativa("Radice");
        dipendentiTableModel = new DefaultTableModel(new String[]{"Nome", "Ruolo"}, 0);
        organigramma = new Organigramma(radice, dipendentiTableModel);
    }

    @Test
    public void testAggiungiNodo() {
        UnitaOrganizzativa unita1 = new UnitaOrganizzativa("Unità 1");
        organigramma.aggiungiNodo(unita1, radice);

        assertTrue(radice.getSottoUnità().contains(unita1));
    }
    
    @Test
    public void testAggiungiUnitaOrganizzativa() {
        UnitaOrganizzativa unita1 = new UnitaOrganizzativa("Unità 1");
        organigramma.aggiungiNodo(unita1, radice);

        assertEquals(1, radice.getSottoUnità().size());
        assertEquals("Unità 1", radice.getSottoUnità().get(0).getNome());
    }

    @Test
    public void testAggiungiDipendente() {
    	livello=LivelloAccesso.PRIMO_LIVELLO;
        UnitaOrganizzativa unita1 = new UnitaOrganizzativa("Unità 1");
        organigramma.aggiungiNodo(unita1, radice);

        Dipendente dipendente = new Dipendente("Mario", "Rossi", "Manager", livello);
        unita1.aggiungiDipendente(dipendente);
        organigramma.aggiungiDipendente(radice, new OrganigrammaPanel(radice, "Azienda Test"));

        List<Dipendente> dipendenti = organigramma.getAllDipendenti(radice);
        assertTrue(dipendenti.contains(dipendente));
        assertEquals(1, dipendentiTableModel.getRowCount());
        assertEquals("Mario Rossi", dipendentiTableModel.getValueAt(0, 0));
        assertEquals("Manager", dipendentiTableModel.getValueAt(0, 1));
    }

    @Test
    public void testRimuoviDipendente() {
        UnitaOrganizzativa unita1 = new UnitaOrganizzativa("Unità 1");
        organigramma.aggiungiNodo(unita1, radice);

        Dipendente dipendente = new Dipendente("Mario", "Rossi", "Manager", livello);
        unita1.aggiungiDipendente(dipendente);
        organigramma.rimuoviDipendente(radice, new OrganigrammaPanel(radice, "Azienda Test"));

        List<Dipendente> dipendenti = organigramma.getAllDipendenti(radice);
        assertFalse(dipendenti.contains(dipendente));
        assertEquals(0, dipendentiTableModel.getRowCount());
    }

    
    private static final long TIME_LIMIT_MS = 3000; //3 secondi
    private static File tempFile;

    @Before
    public void setUp1() throws IOException {
    	StringBuilder content = new StringBuilder();
    	content.append("Organigramma Azienda: Test Company\n");
    	for (int i = 0; i < 10; i++) {
    		content.append("- Unit ").append(i).append("\n");
    		for (int j = 0; j < 10; j++) {
    			content.append("\t* Ruolo ").append(j).append(": Dipendente ")
    			.append(i).append(" ").append(j).append("\n");
    		}
    	}
    	tempFile = Files.createTempFile("organigramma", ".txt").toFile();
    	Files.write(tempFile.toPath(), content.toString().getBytes());
    }

    @After
    public void tearDown() {
    	if (tempFile != null && tempFile.exists()) {
    		tempFile.delete();
    	}
    }

    @Test
    public void testCaricamentoOrganigramma() {
    	long startTime = System.currentTimeMillis();
    	PaginaIniziale.caricaOrganigramma(tempFile);

    	long endTime = System.currentTimeMillis();
    	long duration = endTime - startTime;

    	System.out.println("Tempo di caricamento: " + duration + " ms");
    	assertTrue(duration <= TIME_LIMIT_MS, "Il caricamento dell'organigramma "
    			+ "ha superato il tempo limite di " + TIME_LIMIT_MS + " ms");
    }

}
