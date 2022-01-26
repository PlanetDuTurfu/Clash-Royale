package sources;

public class Carte {
    private String nom;
    private String rarete;
    private int niveau;

    public Carte(String nom, String rarete)
    {
        this.nom = nom;
        this.rarete = rarete;
        this.niveau = 1;
    }

    public Carte(String nom, String rarete, int niveau)
    {
        this.nom = nom;
        this.rarete = rarete;
        this.niveau = niveau;
    }

    public String getNom()    { return this.nom; }
    public String getRarete() { return this.rarete; }
    public int    getNiveau() { return this.niveau; }

    public void setNiveau(int n) { this.niveau = n; }
}
