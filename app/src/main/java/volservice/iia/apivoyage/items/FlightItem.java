package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
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
    private int id;

    public FlightItem(int id, String villeDepart, String villeArrivee, String codeVilleDepart, String codeVilleArrivee, String categorie, int prix, int placesRestantes, String dateDepart, String dateArrivee) {
        this.id = id;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.codeVilleDepart = codeVilleDepart;
        this.categorie = categorie;
        this.prix = prix + "€";
        this.placesRestantes = placesRestantes + "";

        Date dateArr = null;
        Date dateRet = null;
        try {
            dateArr = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateDepart);
            dateRet = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateArrivee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatterHour = new SimpleDateFormat("hh:mm");
        this.date = formatterDay.format(dateArr);
        this.heureDepart = formatterHour.format(dateArr);
        this.heureArrivee = formatterHour.format(dateRet);
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

    public int getId() {
        return id;
    }
}
