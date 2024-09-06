package ORGANIGRAMMA_AZIENDALE;

import java.io.*;

public class OrganigrammaFileManager {

    public static void salvaOrganigramma(UnitaOrganizzativa radice, String nomeAzienda, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(radice);
            oos.writeObject(nomeAzienda);
        }
    }

    public static UnitaOrganizzativa caricaOrganigramma(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            UnitaOrganizzativa radice = (UnitaOrganizzativa) ois.readObject();
            ois.readObject(); // Legge e scarta il nome dell'azienda
            return radice;
        }
    }
}
