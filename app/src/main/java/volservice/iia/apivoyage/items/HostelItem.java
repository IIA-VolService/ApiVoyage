package volservice.iia.apivoyage.items;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HostelItem implements Serializable {

    private static final long serialVersionUID = -2163051469151804394L;

    private String nom;
    private String nbEtoile;
    private String tailleChambre;
    private String dateDebut;
    private String dateFin;
    private String prix;

    public HostelItem(String nom, String nbEtoile, String tailleChambre, String prix, Date dateDebut, Date dateFin) {
        this.nom = nom;
        this.nbEtoile = nbEtoile;
        this.tailleChambre = tailleChambre;
        this.prix = prix;
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        this.dateDebut = formatterDay.format(dateDebut);
        this.dateFin = formatterDay.format(dateFin);
    }
}