package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HostelItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String nom;
    private String classe;
    private String tailleChambre;
    private String dateDebut;
    private String dateFin;
    private String prix;
    private String adresse;
    private String ville;
    private String pays;
    private int nbEtoile;

    public HostelItem(String nom, int nbEtoile, String tailleChambre, String prix, Date dateDebut, Date dateFin, String adresse, String ville, String pays) {
        this.nom = nom;
        this.classe = nbEtoile > 2 ? nbEtoile == 5 ? "LUXE" : "PREMIUM" : "ECONOMIQUE";
        this.tailleChambre = tailleChambre;
        this.prix = prix;
        this.adresse = adresse;
        this.ville = ville;
        this.nbEtoile = nbEtoile;
        this.pays = pays;
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        this.dateDebut = formatterDay.format(dateDebut);
        this.dateFin = formatterDay.format(dateFin);
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
}