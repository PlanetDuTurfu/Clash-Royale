package sources;

public class Coffre {
    private String nom;
    private int nbOr;
    private int nbCommune;
    private int nbRare;
    private int nbEpique;
    private int nbLegendaire;

    public Coffre(String nom, int nbOr, int nbCommune, int nbRare, int nbEpique, int nbLegendaire)
    {
        this.nom = nom;
        this.nbOr = nbOr;
        this.nbCommune = nbCommune;
        this.nbRare = nbRare;
        this.nbEpique = nbEpique;
        this.nbLegendaire = nbLegendaire;
    }

    public String getNom() { return this.nom; }
    public int    getOr() { return this.nbOr; }
    public int getCommune   () { return this.nbCommune   ; }
    public int getRare      () { return this.nbRare      ; }
    public int getEpique    () { return this.nbEpique    ; }
    public int getLegendaire() { return this.nbLegendaire; }
}