package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.model.Administrateur;
import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.CreneauReserve;
import ma.emi.cabinet.model.Dentiste;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.model.RendezVous;
import ma.emi.cabinet.model.enums.StatutRendezVous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class DataStore {

    private static final String DOSSIER = "data";

    private static DataStore instance;

    private List<Administrateur> administrateurs = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Dentiste> dentistes = new ArrayList<>();
    private List<Acte> actes = new ArrayList<>();
    private List<Creneau> creneaux = new ArrayList<>();
    private List<RendezVous> rendezVous = new ArrayList<>();

    private long dernierId = 0;

    private DataStore() {
        charger();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }


    public long nouvelId() {
        dernierId++;
        return dernierId;
    }

    public List<Administrateur> getAdministrateurs() {
        return administrateurs;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Dentiste> getDentistes() {
        return dentistes;
    }

    public List<Acte> getActes() {
        return actes;
    }

    public List<Creneau> getCreneaux() {
        return creneaux;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }


    private void charger() {
        for (String[] l : lireFichier("administrateurs.csv")) {
            administrateurs.add(new Administrateur(Long.parseLong(l[0]), l[1], l[2], l[3], l[4]));
        }
        for (String[] l : lireFichier("patients.csv")) {
            LocalDate naissance = null;
            if (!l[7].isEmpty()) {
                naissance = LocalDate.parse(l[7]);
            }
            patients.add(new Patient(Long.parseLong(l[0]), l[1], l[2], l[3], l[4], l[5], l[6], naissance));
        }
        for (String[] l : lireFichier("dentistes.csv")) {
            dentistes.add(new Dentiste(Long.parseLong(l[0]), l[1], l[2], l[3], l[4]));
        }
        for (String[] l : lireFichier("actes.csv")) {
            actes.add(new Acte(Long.parseLong(l[0]), l[1], l[2], Double.parseDouble(l[3]), Integer.parseInt(l[4])));
        }


        Map<Long, Dentiste> dentisteParId = new HashMap<>();
        for (Dentiste d : dentistes) {
            dentisteParId.put(d.getId(), d);
        }
        for (String[] l : lireFichier("creneaux.csv")) {
            Creneau c = new Creneau(Long.parseLong(l[0]), dentisteParId.get(Long.parseLong(l[1])),
                    LocalDate.parse(l[2]), LocalTime.parse(l[3]), LocalTime.parse(l[4]));
            if (l[5].isEmpty()) {
                creneaux.add(c);
            } else {
                creneaux.add(new CreneauReserve(c, LocalDateTime.parse(l[5])));
            }
        }

        Map<Long, Patient> patientParId = new HashMap<>();
        for (Patient p : patients) {
            patientParId.put(p.getId(), p);
        }
        Map<Long, Acte> acteParId = new HashMap<>();
        for (Acte a : actes) {
            acteParId.put(a.getId(), a);
        }
        Map<Long, Creneau> creneauParId = new HashMap<>();
        for (Creneau c : creneaux) {
            creneauParId.put(c.getId(), c);
        }
        for (String[] l : lireFichier("rendezvous.csv")) {
            Creneau c = creneauParId.get(Long.parseLong(l[3]));
            StatutRendezVous statut = StatutRendezVous.valueOf(l[5]);
            CreneauReserve creneau;
            if (statut == StatutRendezVous.EN_ATTENTE || statut == StatutRendezVous.VALIDE) {
                creneau = (CreneauReserve) c;
            } else {
                creneau = new CreneauReserve(c, LocalDateTime.parse(l[4]));
            }
            rendezVous.add(new RendezVous(Long.parseLong(l[0]), patientParId.get(Long.parseLong(l[1])),
                    acteParId.get(Long.parseLong(l[2])), creneau, statut, l[6], l.length > 7 && Boolean.parseBoolean(l[7])));
        }


        for (Administrateur a : administrateurs) dernierId = Math.max(dernierId, a.getId());
        for (Patient p : patients) dernierId = Math.max(dernierId, p.getId());
        for (Dentiste d : dentistes) dernierId = Math.max(dernierId, d.getId());
        for (Acte a : actes) dernierId = Math.max(dernierId, a.getId());
        for (Creneau c : creneaux) dernierId = Math.max(dernierId, c.getId());
        for (RendezVous r : rendezVous) dernierId = Math.max(dernierId, r.getId());
    }


    private List<String[]> lireFichier(String nom) {
        List<String[]> lignes = new ArrayList<>();
        Path chemin = Paths.get(DOSSIER, nom);
        if (!Files.exists(chemin)) {
            return lignes;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(chemin.toFile()));
            reader.readLine();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (!ligne.trim().isEmpty()) {
                    lignes.add(ligne.split(";", -1));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + nom + " : " + e.getMessage());
        }
        return lignes;
    }


    public void sauvegarder() {
        List<String> lignes = new ArrayList<>();
        for (Administrateur a : administrateurs) {
            lignes.add(a.getId() + ";" + texte(a.getNom()) + ";" + texte(a.getPrenom()) + ";" + texte(a.getEmail())
                    + ";" + texte(a.getMotDePasse()));
        }
        ecrireFichier("administrateurs.csv", "id;nom;prenom;email;motDePasse", lignes);

        lignes = new ArrayList<>();
        for (Patient p : patients) {
            String naissance = "";
            if (p.getDateNaissance() != null) {
                naissance = p.getDateNaissance().toString();
            }
            lignes.add(p.getId() + ";" + texte(p.getNom()) + ";" + texte(p.getPrenom()) + ";" + texte(p.getEmail())
                    + ";" + texte(p.getMotDePasse()) + ";" + texte(p.getCin()) + ";" + texte(p.getTelephone())
                    + ";" + naissance);
        }
        ecrireFichier("patients.csv", "id;nom;prenom;email;motDePasse;cin;telephone;dateNaissance", lignes);

        lignes = new ArrayList<>();
        for (Dentiste d : dentistes) {
            lignes.add(d.getId() + ";" + texte(d.getNom()) + ";" + texte(d.getPrenom()) + ";"
                    + texte(d.getSpecialite()) + ";" + texte(d.getTelephone()));
        }
        ecrireFichier("dentistes.csv", "id;nom;prenom;specialite;telephone", lignes);

        lignes = new ArrayList<>();
        for (Acte a : actes) {
            lignes.add(a.getId() + ";" + texte(a.getCode()) + ";" + texte(a.getLibelle()) + ";" + a.getTarif()
                    + ";" + a.getDureeMinutes());
        }
        ecrireFichier("actes.csv", "id;code;libelle;tarif;dureeMinutes", lignes);

        lignes = new ArrayList<>();
        for (Creneau c : creneaux) {
            String reservation = "";
            if (c instanceof CreneauReserve) {
                reservation = ((CreneauReserve) c).getDateReservation().toString();
            }
            lignes.add(c.getId() + ";" + c.getDentiste().getId() + ";" + c.getDate() + ";" + c.getHeureDebut()
                    + ";" + c.getHeureFin() + ";" + reservation);
        }
        ecrireFichier("creneaux.csv", "id;dentisteId;date;heureDebut;heureFin;dateReservation", lignes);

        lignes = new ArrayList<>();
        for (RendezVous r : rendezVous) {
            lignes.add(r.getId() + ";" + r.getPatient().getId() + ";" + r.getActe().getId() + ";"
                    + r.getCreneau().getId() + ";" + r.getCreneau().getDateReservation() + ";"
                    + r.getStatut().name() + ";" + texte(r.getNotes()) + ";" + r.isUrgent());
        }
        ecrireFichier("rendezvous.csv", "id;patientId;acteId;creneauId;dateReservation;statut;notes;urgent", lignes);
    }

    private void ecrireFichier(String nom, String entete, List<String> lignes) {
        try {
            Files.createDirectories(Paths.get(DOSSIER));
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(DOSSIER, nom).toFile()));
            writer.write(entete);
            writer.newLine();
            for (String ligne : lignes) {
                writer.write(ligne);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur d'écriture du fichier " + nom + " : " + e.getMessage());
        }
    }


    private String texte(String valeur) {
        if (valeur == null) {
            return "";
        }
        return valeur.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
}
