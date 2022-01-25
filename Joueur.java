package Sources;

import java.util.HashMap;
public class Joueur {
    private String nom;
    private HashMap<String, Integer> hmCartes;
    public Joueur (String nom)
    {
        this.nom = nom;
        this.hmCartes = new HashMap<String, Integer>();
    }

    public void ajouterCarte(String[] tabCartes)
    {
        for (String s : tabCartes)
        {
            if (this.hmCartes.get(s) == null) this.hmCartes.put(s, 1);
            else
            {
                this.hmCartes.put(s,this.hmCartes.get(s) + 1);
            }
        }
    }

    public String toString()
    {
        String sRet = "";

        sRet += "Joueur " + this.nom + " : \n";

        for (String s : this.hmCartes.keySet())
        {
            sRet += "\t" + this.hmCartes.get(s) + " " + s + "\n";
        }

        return sRet;
    }
}
