package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String date;
    private String villeDepart;
    private String villeArrivee;
    private String codeVilleDepart;
    private String codeVilleArrivee;
    private String heureDepart;
    private String heureArrivee;
    private String categorie;
    private String prix;
    private String placesRestantes;

    public FlightItem(String villeDepart, String villeArrivee, String codeVilleDepart, String codeVilleArrivee, String categorie, String prix, String placesRestantes, Date dateDepart, Date dateArrivee) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.codeVilleDepart = codeVilleDepart;
        this.codeVilleArrivee = codeVilleArrivee;
        this.categorie = categorie;
        this.prix = prix + "€";
        this.placesRestantes = placesRestantes;
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatterHour = new SimpleDateFormat("hh:mm");
        this.date = formatterDay.format(dateDepart);
        this.heureDepart = formatterHour.format(dateDepart);
        this.heureArrivee = formatterHour.format(dateArrivee);
    }

    public String getDate() {
        return date;
    }

    public String getVolInfos() {
        return villeDepart + " → " + villeArrivee + " : " + heureDepart + " → " + heureArrivee;
    }

    public String getPrix() {
        return prix;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getCodeVilleDepart() {
        return codeVilleDepart;
    }

    public String getCodeVilleArrivee() {
        return codeVilleArrivee;
    }

    public String getPlacesRestantes() {
        return "Places disponibles : " + placesRestantes;
    }
}
