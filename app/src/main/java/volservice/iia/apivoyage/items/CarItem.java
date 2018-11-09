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

    public CarItem(String modele, String prix, Date dateDebut, Date dateFin) {
        this.modele = modele;
        this.prix = prix;
        DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
        this.dateDebut = formatterDay.format(dateDebut);
        this.dateFin = formatterDay.format(dateFin);
    }
}