package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HostelItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String nom;
    private String classe;
    private String dateDebut;
    private String dateFin;
    private String prix;
    private String adresse;
    private String ville;
    private String pays;
    private String id;
    private int nbEtoile;

    private String description;
    private String type;
    private String mail;
    private String telephone;
    private Double latitude;
    private Double longitude;

    public HostelItem(String id, String nom, String description, String type, int nbEtoile, double latitude, double longitude, String telephone, String mail, double prix, String dateDebut, String dateFin, String adresse, String ville, String pays) {
        this.description = description;
        this.type = type;
        this.mail = mail;
        this.telephone = telephone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.nom = nom;
        this.classe = nbEtoile > 2 ? nbEtoile == 5 ? "LUXE" : "PREMIUM" : "ECONOMIQUE";
        this.prix = String.valueOf(prix);
        this.adresse = adresse;
        this.ville = ville;
        this.nbEtoile = nbEtoile;
        this.pays = pays;
        Date dateArr = null;
        Date dateRet = null;
        try {
            dateArr = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateDebut.replace('T', ' '));
            dateRet = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateFin.replace('T', ' '));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
        this.dateDebut = formatterDay.format(dateArr);
        this.dateFin = formatterDay.format(dateRet);
    }

    public String getClasse() {
        return classe;
    }

    public String getFullAdress() {
        return adresse + " " + ville;
    }

    public String getFormatPrix() {
        return prix + "€ / nuit";
    }

    public String getNom() {
        return nom;
    }

    public int getEtoiles() {
        return nbEtoile;
    }

    public String getId() {
        return id;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getPrix() {
        return prix;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }

    public int getNbEtoile() {
        return nbEtoile;
    }

    public String getDescription() {
        return description;
    }

    public String getMail() {
        return mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }
}