package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;
    private static int AUTO_ID = 1;

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

    private int idCar;

    public CarItem(EnumAPI enumAPI, String modele, String prix, String dateDebut, String dateFin, int nbPlaces, String localisation, String marque, int idCar) {
        this.idCar = idCar;
        this.id = AUTO_ID++;
        this.api = enumAPI;
        this.modele = modele;
        this.prix = "" + prix;
        String calcPx = prix;
        calcPx = calcPx.length() > 2 ? calcPx.substring(0, 3) : calcPx;
        if (calcPx.contains(".")) {
            if (calcPx.charAt(2) == '.') {
                calcPx = calcPx.replace(".", "");
            } else calcPx = calcPx.substring(0, 1);
        }
        int pxToCalc = Integer.valueOf(calcPx);
        this.classe = pxToCalc > 100 ? pxToCalc > 200 ? "PREMIUM" : "AFFAIRE" : "ECONONIMQUE";
        this.type = pxToCalc > 100 ? pxToCalc > 200 ? 1 : 2 : 3;
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
        DateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
        this.dateDebut = formatterDay.format(dateArr);
        this.dateFin = formatterDay.format(dateRet);
    }

    public String getModeleAndPlaces() {
        return modele + " - " + nbPlaces;
    }

    public String getFormatPrix() {
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

    public String getModele() {
        return modele;
    }

    public String getPrix() {
        return prix;
    }

    public String getNbPlaces() {
        return nbPlaces;
    }

    public String getLocalisation() {
        return localisation;
    }

    public EnumAPI getApi() {
        return api;
    }

    public int getIdCar() {
        return idCar;
    }
}