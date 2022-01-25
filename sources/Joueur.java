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
            if (this.hmCartes.get(c) == null) this.hmCartes.put(c, 1);
            else this.hmCartes.put(c,this.hmCartes.get(c) + 1);
    }
    public void ajouterOr   (int or)
    {
        this.or += or;
    }

    public String toString()
    {
        String sRet = "";

        sRet += "Joueur " + this.nom + " ("+this.or+" d'or) : \n";

        for (Carte c : this.hmCartes.keySet())
            sRet += "\t" + this.hmCartes.get(c) + " " + c.getNom() + " (" + c.getRarete() + ")\n";

        return sRet;
    }
}
