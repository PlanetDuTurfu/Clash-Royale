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
            if (!this.isCarteDansLinv(c.getNom())){
                this.hmCartes.put(new Carte(c.getNom(), c.getRarete(), 1), 1); System.out.println("existe pas déjà"); }
            else
            {
                System.out.println("existe déjà | 1)"+ c.getNom() + " " + this.hmCartes.get(this.getCarteParNom(c.getNom())));
                this.hmCartes.put(this.getCarteParNom(c.getNom()),this.hmCartes.get(this.getCarteParNom(c.getNom())) + 1);
                System.out.println("2)"+ c.getNom() + " " + this.hmCartes.get(this.getCarteParNom(c.getNom())));
                if ( this.hmCartes.get(this.getCarteParNom(c.getNom())) == 2)
                {
                    this.hmCartes.put(c, this.hmCartes.get(c) - 2);
                    c.setNiveau(c.getNiveau() + 1);
                }
            }
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

    public String toString()
    {
        String sRet = "";

        sRet += "Joueur " + this.nom + " ("+this.or+" d'or) : \n";

        for (Carte c : this.hmCartes.keySet())
            sRet += "\t" + this.hmCartes.get(c) + " " + c.getNom() + " (" + c.getRarete() + ") niveau "+c.getNiveau()+"\n";

        return sRet;
    }
}
