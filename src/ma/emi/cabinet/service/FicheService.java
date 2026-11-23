package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ReservationException;
import ma.emi.cabinet.model.RendezVous;
import ma.emi.cabinet.model.enums.StatutRendezVous;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;



public class FicheService {

    public void exporterPdf(RendezVous rdv, File destination) throws ReservationException, IOException {
        if (rdv == null) {
            throw new ReservationException("Choisissez un rendez-vous.");
        }
        if (rdv.getStatut() != StatutRendezVous.VALIDE && rdv.getStatut() != StatutRendezVous.TERMINE) {
            throw new ReservationException("La fiche existe seulement pour un rendez-vous validé.");
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDType1Font gras = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        PDPageContentStream contenu = new PDPageContentStream(document, page);
        contenu.beginText();


        contenu.setFont(gras, 22);
        contenu.newLineAtOffset(50, 720);
        contenu.showText("CABINET DENTAIRE");

        contenu.setFont(gras, 16);
        contenu.newLineAtOffset(0, -40);
        contenu.showText("Fiche de rendez-vous n° " + rdv.getId());


        contenu.setFont(normal, 14);
        contenu.newLineAtOffset(0, -40);
        contenu.showText("Patient : " + rdv.getPatient().getNomComplet() + " (CIN : " + rdv.getPatient().getCin() + ")");
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Dentiste : " + rdv.getDentiste().getNomComplet());
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Acte : " + rdv.getActe().getLibelle());
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Date : " + rdv.getCreneau().getDate());
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Heure : " + rdv.getCreneau().getHeureDebut() + " - " + rdv.getCreneau().getHeureFin());
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Tarif : " + rdv.getActe().getTarif() + " DH");
        contenu.newLineAtOffset(0, -25);
        contenu.showText("Statut : " + rdv.getStatut());

        contenu.setFont(normal, 11);
        contenu.newLineAtOffset(0, -60);
        contenu.showText("Merci de vous présenter 10 minutes avant l'heure du rendez-vous.");

        contenu.endText();
        contenu.close();

        document.save(destination);
        document.close();
    }
}
