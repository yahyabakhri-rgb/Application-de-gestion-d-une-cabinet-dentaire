package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ReservationException;
import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.CreneauReserve;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.model.RendezVous;
import ma.emi.cabinet.model.enums.StatutRendezVous;
import ma.emi.cabinet.persistence.CreneauRepository;
import ma.emi.cabinet.persistence.RendezVousRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {


    private static final Object VERROU = new Object();


    private static List<EcouteurReservation> ecouteurs = new ArrayList<>();

    private CreneauRepository creneauRepository = new CreneauRepository();
    private RendezVousRepository rendezVousRepository = new RendezVousRepository();


    public void ajouterEcouteur(EcouteurReservation ecouteur) {
        ecouteurs.add(ecouteur);
    }


    public static void viderEcouteurs() {
        ecouteurs.clear();
    }

    private void notifierChangement() {
        for (EcouteurReservation e : ecouteurs) {
            e.reservationsModifiees();
        }
    }


    public RendezVous reserver(Patient patient, Acte acte, Creneau creneau, String notes) throws ReservationException {
        return reserver(patient, acte, creneau, notes, false);
    }

    public RendezVous reserver(Patient patient, Acte acte, Creneau creneau, String notes, boolean urgent)
            throws ReservationException {
        if (patient == null || acte == null || creneau == null) {
            throw new ReservationException("Choisissez un patient, un acte et un créneau.");
        }
        RendezVous rdv;
        synchronized (VERROU) {

            creneau = creneauRepository.trouverParId(creneau.getId());
            if (creneau == null) {
                throw new ReservationException("Ce créneau n'existe plus.");
            }
            if (creneau.estReserve()) {
                throw new ReservationException("Ce créneau est déjà réservé.");
            }
            if (creneau.getDate().atTime(creneau.getHeureDebut()).isBefore(LocalDateTime.now())) {
                throw new ReservationException("Ce créneau est déjà passé.");
            }
            if (creneau.getDureeMinutes() < acte.getDureeMinutes()) {
                throw new ReservationException("Ce créneau est trop court pour cet acte ("
                        + acte.getDureeMinutes() + " min).");
            }

            for (RendezVous r : rendezVousRepository.trouverParPatient(patient)) {
                if (r.estActif() && r.getCreneau().chevauche(creneau)) {
                    throw new ReservationException("Ce patient a déjà un rendez-vous à cette heure-là.");
                }
            }

            CreneauReserve reserve = new CreneauReserve(creneau, LocalDateTime.now());
            creneauRepository.remplacer(reserve);
            rdv = new RendezVous(rendezVousRepository.nouvelId(), patient, acte, reserve,
                    StatutRendezVous.EN_ATTENTE, notes.trim(), urgent);
            rendezVousRepository.ajouter(rdv);
        }
        notifierChangement();
        return rdv;
    }


    public void valider(RendezVous rdv) throws ReservationException {
        if (rdv == null) {
            throw new ReservationException("Choisissez un rendez-vous dans la liste.");
        }
        synchronized (VERROU) {
            if (rdv.getStatut() != StatutRendezVous.EN_ATTENTE) {
                throw new ReservationException("Seul un rendez-vous en attente peut être validé.");
            }
            rdv.setStatut(StatutRendezVous.VALIDE);
            rendezVousRepository.sauvegarder();
        }
        notifierChangement();
    }


    public void annuler(RendezVous rdv) throws ReservationException {
        if (rdv == null) {
            throw new ReservationException("Choisissez un rendez-vous dans la liste.");
        }
        synchronized (VERROU) {
            if (!rdv.estActif()) {
                throw new ReservationException("Ce rendez-vous ne peut plus être annulé.");
            }
            rdv.setStatut(StatutRendezVous.ANNULE);
            creneauRepository.remplacer(rdv.getCreneau().liberer());
        }
        notifierChangement();
    }


    public void terminer(RendezVous rdv) throws ReservationException {
        if (rdv == null) {
            throw new ReservationException("Choisissez un rendez-vous dans la liste.");
        }
        synchronized (VERROU) {
            if (rdv.getStatut() != StatutRendezVous.VALIDE) {
                throw new ReservationException("Seul un rendez-vous validé peut être terminé.");
            }
            rdv.setStatut(StatutRendezVous.TERMINE);
            rendezVousRepository.sauvegarder();
        }
        notifierChangement();
    }




    public RendezVous rendezVousDuCreneau(Creneau creneau) {
        for (RendezVous r : rendezVousRepository.lister()) {
            if (r.getCreneau().getId() == creneau.getId() && r.getStatut() != StatutRendezVous.ANNULE) {
                return r;
            }
        }
        return null;
    }

    public List<RendezVous> lister() {
        List<RendezVous> liste = rendezVousRepository.lister();
        liste.sort((a, b) -> a.getCreneau().getDate().compareTo(b.getCreneau().getDate()));
        return liste;
    }

    public List<RendezVous> listerParPatient(Patient patient) {
        List<RendezVous> liste = rendezVousRepository.trouverParPatient(patient);
        liste.sort((a, b) -> a.getCreneau().getDate().compareTo(b.getCreneau().getDate()));
        return liste;
    }
}
