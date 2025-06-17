package com.gosquad.usecase.voyages.impl;

import com.gosquad.core.exceptions.NotFoundException;
import com.gosquad.domain.voyages.VoyageEntity;
import com.gosquad.usecase.voyages.VoyageFicheService;
import com.gosquad.usecase.voyages.VoyageService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class VoyageFicheServiceImpl implements VoyageFicheService {

    private final VoyageService voyageService;

    public VoyageFicheServiceImpl(VoyageService voyageService) {
        this.voyageService = voyageService;
    }

    @Override
    public byte[] generateFichePDF(int voyageId) throws SQLException, NotFoundException {
        // Pour l'instant, on génère le HTML et on le convertit en PDF
        // Dans une implémentation complète, on utiliserait une bibliothèque comme iText
        String htmlContent = generateFicheHTML(voyageId);
        
        // Simulation de conversion PDF (à remplacer par une vraie implémentation)
        return htmlContent.getBytes();
    }

    @Override
    public String generateFicheHTML(int voyageId) throws SQLException, NotFoundException {
        VoyageEntity voyage = voyageService.getVoyageById(voyageId);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='fr'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Fiche Voyage - ").append(voyage.getTitre()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; color: #333; }");
        html.append(".header { text-align: center; border-bottom: 2px solid #007bff; padding-bottom: 20px; margin-bottom: 30px; }");
        html.append(".logo { font-size: 24px; font-weight: bold; color: #007bff; }");
        html.append(".title { font-size: 20px; margin: 10px 0; }");
        html.append(".section { margin: 20px 0; }");
        html.append(".section-title { font-size: 18px; font-weight: bold; color: #007bff; border-bottom: 1px solid #ddd; padding-bottom: 5px; margin-bottom: 15px; }");
        html.append(".info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }");
        html.append(".info-item { margin: 10px 0; }");
        html.append(".label { font-weight: bold; color: #555; }");
        html.append(".value { margin-left: 10px; }");
        html.append(".status { padding: 5px 10px; border-radius: 5px; font-weight: bold; }");
        html.append(".status-planifie { background-color: #fff3cd; color: #856404; }");
        html.append(".status-en-cours { background-color: #d1ecf1; color: #0c5460; }");
        html.append(".status-termine { background-color: #d4edda; color: #155724; }");
        html.append(".footer { margin-top: 40px; text-align: center; font-size: 12px; color: #666; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<div class='logo'>GoSquad - Agence de Voyage</div>");
        html.append("<div class='title'>Fiche de Voyage</div>");
        html.append("</div>");
        
        // Informations du voyage
        html.append("<div class='section'>");
        html.append("<div class='section-title'>Informations du Voyage</div>");
        html.append("<div class='info-grid'>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Titre:</span>");
        html.append("<span class='value'>").append(voyage.getTitre()).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Destination:</span>");
        html.append("<span class='value'>").append(voyage.getDestination()).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Date de départ:</span>");
        html.append("<span class='value'>").append(voyage.getDateDepart().format(dateFormatter)).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Date de retour:</span>");
        html.append("<span class='value'>").append(voyage.getDateRetour().format(dateFormatter)).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Nombre de participants:</span>");
        html.append("<span class='value'>").append(voyage.getNombreParticipants()).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Budget:</span>");
        html.append("<span class='value'>").append(currencyFormatter.format(voyage.getBudget())).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Statut:</span>");
        html.append("<span class='value'>");
        html.append("<span class='status status-").append(voyage.getStatut().toLowerCase().replace("_", "-")).append("'>");
        html.append(getStatusLabel(voyage.getStatut()));
        html.append("</span>");
        html.append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>ID Client:</span>");
        html.append("<span class='value'>").append(voyage.getClientId()).append("</span>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</div>");
        
        // Informations du client (simplifiées pour l'instant)
        html.append("<div class='section'>");
        html.append("<div class='section-title'>Informations du Client</div>");
        html.append("<div class='info-grid'>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>ID Client:</span>");
        html.append("<span class='value'>").append(voyage.getClientId()).append("</span>");
        html.append("</div>");
        
        html.append("<div class='info-item'>");
        html.append("<span class='label'>Note:</span>");
        html.append("<span class='value'>Informations détaillées du client disponibles via l'API clients</span>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Document généré le " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")) + "</p>");
        html.append("<p>GoSquad - Votre partenaire voyage</p>");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    private String getStatusLabel(String status) {
        switch (status) {
            case "PLANIFIE":
                return "Planifié";
            case "EN_COURS":
                return "En cours";
            case "TERMINE":
                return "Terminé";
            default:
                return status;
        }
    }
}