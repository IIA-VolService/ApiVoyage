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

    public HostelItem(String id, String nom, int nbEtoile, double prix, String dateDebut, String dateFin, String adresse, String ville, String pays) {
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

        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        this.dateDebut = formatterDay.format(dateArr);
        this.dateFin = formatterDay.format(dateRet);
    }

    public String getClasse() {
        return classe;
    }

    public String getFullAdress() {
        return adresse + " " + ville;
    }

    public String getPrix() {
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
}