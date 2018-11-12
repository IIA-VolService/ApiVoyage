package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String modele;
    private String dateDebut;
    private String dateFin;
    private String prix;
    private String nbPlaces;
    private String localisation;
    private String classe;
    private String marque;
    private int type;
    private int id;
    private EnumAPI api;

    public CarItem(int id, EnumAPI enumAPI, String modele, int prix, String dateDebut, String dateFin, int nbPlaces, String localisation, String marque) {
        this.id = id;
        this.api = enumAPI;
        this.modele = modele;
        this.prix = "" + prix;
        this.classe = prix > 50 ? prix > 100 ? "PREMIMUM" : "AFFAIRE" : "ECONONIMQUE";
        this.type = prix > 50 ? prix > 100 ? 1 : 2 : 3;
        this.nbPlaces = String.valueOf(nbPlaces) + (nbPlaces > 1 ? "places" : "place");
        this.localisation = localisation;
        this.marque = marque;
        Date dateArr = null;
        Date dateRet = null;
        try {
            dateArr = new SimpleDateFormat("dd-MM-yyyy").parse(dateDebut);
            dateRet = new SimpleDateFormat("dd-MM-yyyy").parse(dateFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        this.dateDebut = formatterDay.format(dateDebut);
        this.dateFin = formatterDay.format(dateFin);
    }

    public String getModeleAndPlaces() {
        return modele + " - " + nbPlaces;
    }

    public String getPrix() {
        return prix + "€ / jour";
    }

    public String getClasse() {
        return classe;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getMarque() {
        return marque;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}