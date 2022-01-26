package sources;

public class Carte {
    private String nom;
    private String rarete;
    private int pv;
    private int deg;
    private double vit_att;
    private int niveau;

    public Carte(String nom, String rarete, int pv, int deg, double vit_att, int niveau)
    {
        this.nom = nom;
        this.rarete = rarete;
        this.pv = pv;
        this.deg = deg;
        this.vit_att = vit_att;
        this.niveau = niveau;
    }

    public String getNom()    { return this.nom;    }
    public String getRarete() { return this.rarete; }
    public int    getNiveau() { return this.niveau; }
    public int    getPV()     { return this.pv;     }
    public int    getDeg()    { return this.deg;    }
    public double getVitAtt() { return this.vit_att;}

    public void ameliorer()
    {
        int pourcentAmelioration = 10 - this.niveau;
        this.pv += (int) (this.pv * pourcentAmelioration / 100);
        this.deg += (int) (this.deg * pourcentAmelioration / 100);
        this.niveau++;
    }
}
