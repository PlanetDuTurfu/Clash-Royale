package sources;

public class Carte {
    private String nom;
    private String rarete;

    public Carte(String nom, String rarete)
    {
        this.nom = nom;
        this.rarete = rarete;
    }

    public String getNom() { return this.nom; }
    public String getRarete() { return this.rarete; }
}
