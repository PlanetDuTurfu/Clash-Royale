package sources;

public class Carte {
    private String nom;
    private String rarete;
    private int pv;
    private int deg;
    private double vit_att;
    private double vit_dep;
    private int niveau;
    private int doublons;
    private int prix;

    public Carte(String nom, String rarete, int pv, int deg, double vit_att, double vit_dep, int niveau, int doublons, int prix)
    {
        this.nom = nom;
        this.rarete = rarete;
        this.pv = pv;
        this.deg = deg;
        this.vit_att = vit_att;
        this.vit_dep = vit_dep;
        this.niveau = niveau;
        this.doublons = doublons;
        this.prix = prix;
    }

    public String getNom()    { return this.nom;      }
    public String getRarete() { return this.rarete;   }
    public int    getNiveau() { return this.niveau;   }
    public int    getPV()     { return this.pv;       }
    public int    getDeg()    { return this.deg;      }
    public double getVitAtt() { return this.vit_att;  }
    public double getVitDep() { return this.vit_dep;  }
    public int  getDoublons() { return this.doublons; }
    public int    getPrix(  ) { return this.prix;     }

    public void addDoublon() { this.doublons++; }
    public void retirerDoublon(int nb) { this.doublons -= nb; }

    public void ameliorer()
    {
        int pourcentAmelioration = 10 - this.niveau / 2;
        this.pv += this.pv * pourcentAmelioration / 100;
        this.deg += this.deg * pourcentAmelioration / 100;
        this.niveau++;
    }
}
