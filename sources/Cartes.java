package sources;

import java.util.ArrayList;

public class Cartes {
    private ArrayList<Carte> alCarteCommune = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteRare = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteEpique = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteLegendaire = new ArrayList<Carte>();

    public Cartes()
    {
        alCarteCommune.add(new Carte("Byvan", "Commune"));
        alCarteCommune.add(new Carte("Gatéo", "Commune"));
        alCarteCommune.add(new Carte("Roman", "Commune"));
        alCarteCommune.add(new Carte("Benjos", "Commune"));
        alCarteCommune.add(new Carte("Le chauve", "Commune"));
        alCarteCommune.add(new Carte("Baise-vache", "Commune"));
        alCarteCommune.add(new Carte("Ce gros chien d'Antoine", "Commune"));
        alCarteRare.add(new Carte("Alan", "Rare"));
        alCarteRare.add(new Carte("Paulo", "Rare"));
        alCarteRare.add(new Carte("Duflowww", "Rare"));
        alCarteRare.add(new Carte("Nzeko Kouam", "Rare"));
        alCarteRare.add(new Carte("Steve Carlos", "Rare"));
        alCarteRare.add(new Carte("Racaille de rue", "Rare"));
        alCarteRare.add(new Carte("Liquide visqueux et suspect", "Rare"));
        alCarteEpique.add(new Carte("Bobël", "Epique"));
        alCarteEpique.add(new Carte("Benzo", "Epique"));
        alCarteEpique.add(new Carte("Laulau", "Epique"));
        alCarteEpique.add(new Carte("Violeur d'enfants", "Epique"));
        alCarteLegendaire.add(new Carte("PLP", "Légendaire"));
        alCarteLegendaire.add(new Carte("RodoBoss", "Légendaire"));
        alCarteLegendaire.add(new Carte("Steph Maing", "Légendaire"));
    }
    public ArrayList<Carte> getCartesCommune    () { return alCarteCommune   ; }
    public ArrayList<Carte> getCartesRare       () { return alCarteRare      ; }
    public ArrayList<Carte> getCartesEpique     () { return alCarteEpique    ; }
    public ArrayList<Carte> getCartesLegendaire () { return alCarteLegendaire; }
}
