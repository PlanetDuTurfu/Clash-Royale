package sources;

import java.util.HashMap;
public class Joueur {
    private String nom;
    private HashMap<Carte, Integer> hmCartes;
    private int or;

    public Joueur (String nom)
    {
        this.nom = nom;
        this.hmCartes = new HashMap<Carte, Integer>();
        this.or = 1000;
    }

    public void ajouterCarte(Carte[] tabCartes)
    {
        for (Carte c : tabCartes)
            if ( !this.isCarteDansLinv(c.getNom()) ) this.hmCartes.put(new Carte(c.getNom(), c.getRarete(), c.getPV(), c.getDeg(), c.getVitAtt(), 1), 1);
            else
            {
                Carte cTmp = this.getCarteParNom(c.getNom());
                this.hmCartes.put(cTmp, this.hmCartes.get(cTmp) + 1);
                this.ameliorer(cTmp);
            }
    }

    private void ameliorer(Carte carte)
    {
        if (this.hmCartes.get(carte) < (int)(Math.pow(2,carte.getNiveau())) || this.or < (int)(Math.pow(2,carte.getNiveau()))*15 || carte.getNiveau() == 10) return;

        this.enleverOr((int)(Math.pow(2,carte.getNiveau()))*15);
        carte.ameliorer();
        this.hmCartes.put(carte, this.hmCartes.get(carte) - (int)(Math.pow(2,carte.getNiveau()-1)));
    }

    private boolean isCarteDansLinv(String nom)
    {
        for (Carte c : this.hmCartes.keySet())
            if (nom.equals(c.getNom())) return true;
        return false;
    }

    private Carte getCarteParNom(String nom)
    {
        for (Carte c : this.hmCartes.keySet())
            if (nom.equals(c.getNom())) return c;
        return null;
    }

    public void ajouterOr(int or)
    {
        this.or += or;
    }

    private void enleverOr(int or)
    {
        this.or -= or;
    }

    public String toString()
    {
        String sRet = "";
        sRet += "Joueur " + this.nom + " ("+this.or+" d'or) : \n";
        for (Carte c : this.hmCartes.keySet())
        {
            sRet += String.format("\t%5d %-27s %-10s",this.hmCartes.get(c), c.getNom(),c.getRarete());
            sRet += String.format(" niveau %2d PV=%4d DEG=%4d Vit_Att=%4s\n",c.getNiveau(), c.getPV(), c.getDeg(), c.getVitAtt());
        }
        return sRet;
    }
}