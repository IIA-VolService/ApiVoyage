package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String modele;
    private String dateDebut;
    private String dateFin;
    private String prix;
    private String nbPlaces;
    private String loueur;
    private String classe;
    private String marque;
    private int type;
    private int id;

    public CarItem(int id, String modele, int prix, Date dateDebut, Date dateFin, int nbPlaces, String loueur, String marque) {
        this.id = id;
        this.modele = modele;
        this.prix = "" + prix;
        this.classe = prix > 50 ? prix > 100 ? "PREMIMUM" : "AFFAIRE" : "ECONONIMQUE";
        this.type = prix > 50 ? prix > 100 ? 1 : 2 : 3;
        this.nbPlaces = String.valueOf(nbPlaces) + (nbPlaces > 1 ? "places" : "place");
        this.loueur = loueur;
        this.marque = marque;
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