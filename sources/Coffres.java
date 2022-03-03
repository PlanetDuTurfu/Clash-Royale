package sources;

import java.util.ArrayList;

public class Coffres {
    private ClashRoyale ctrl;
    private ArrayList<Coffre> alCoffres = new ArrayList<Coffre>();

    public Coffres(ClashRoyale ctrl)
    {
        this.ctrl = ctrl;
        //                        Nom du coffre       NbOr  nbC nbR nbE nbL
        alCoffres.add(new Coffre("Coffre naze"      ,   80,  17,  3,  0, 0));
        alCoffres.add(new Coffre("Coffre en bois"   ,  170,  30,  6,  0, 0));
        alCoffres.add(new Coffre("Coffre stylax"    ,  300,  65, 14,  1, 0));
        alCoffres.add(new Coffre("Coffre EZ"        ,  600, 100, 20,  2, 0));
        alCoffres.add(new Coffre("Coffre du futur"  , 1000, 150, 16,  3, 0));
        alCoffres.add(new Coffre("Coffre OP"        , 1600, 195, 30,  6, 1));
        alCoffres.add(new Coffre("Rodocoffre"       , 2200, 275, 48, 10, 1));
        alCoffres.add(new Coffre("Coffre commun"    ,  800, 370,  0,  0, 0));
        alCoffres.add(new Coffre("Coffre rare"      , 1200,   0, 90,  0, 0));
        alCoffres.add(new Coffre("Coffre épique"    , 1500,   0,  0, 20, 0));
        alCoffres.add(new Coffre("Coffre légendaire", 2000,   0,  0,  0, 2));
    }

    public Coffre getCoffreParNom(String nom)
    {
        for (Coffre c : alCoffres)
            if (c.getNom().contains(nom)) return c;
        return null;
    }

    public Coffre getRandomCoffre(int proba)
    {
        if (proba == 60) return this.getCoffreParNom("Rodocoffre");
        if (Math.random() * 1000 > 800 + proba) return this.getRandomCoffre(proba+10);
        return alCoffres.get(proba/10);
    }

    public Carte[] ouvrirCoffre(Coffre coffre)
    {
        int tmpCommune = coffre.getCommune();
        int tmpRare = coffre.getRare();
        int tmpEpique = coffre.getEpique();
        int tmpLegendaire = coffre.getLegendaire();
        Carte[] tabCartes = new Carte[tmpCommune + tmpRare + tmpEpique + tmpLegendaire];

        System.out.println("Cartes avant transformation : \n"+
        "Communes : "+tmpCommune+"\n"+
        "Rares : "+tmpRare+"\n"+
        "Epiques : "+tmpEpique+"\n"+
        "Légendaires : "+tmpLegendaire+"\n");
    
        for (int i = 0; i < coffre.getCommune(); i++)
        {
            if (Math.random() * 100 > 95)
            {
                if (Math.random() * 100 > 70)
                {
                    if (Math.random() * 100 > 80)
                    {
                        tmpCommune--;
                        tmpLegendaire++;
                    }
                    else
                    {
                        tmpCommune--;
                        tmpEpique++;
                    }
                }
                else
                {
                    tmpCommune--;
                    tmpRare++;
                }
            }
        }

        System.out.println("Cartes après transformation : \n"+
        "Communes : "+tmpCommune+"\n"+
        "Rares : "+tmpRare+"\n"+
        "Epiques : "+tmpEpique+"\n"+
        "Légendaires : "+tmpLegendaire+"\n\n\n");

        int nbCarteTiree = 0;
        int nbCarte = 0;

        while (nbCarteTiree != tmpCommune)
        {
            nbCarte = (int) (Math.random() * (tmpCommune - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Commune"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpRare)
        {
            nbCarte = (int) (Math.random() * (tmpRare - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Rare"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpEpique)
        {
            nbCarte = (int) (Math.random() * (tmpEpique - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Epique"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpLegendaire)
        {
            nbCarte = (int) (Math.random() * (tmpLegendaire - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Legendaire"), tabCartes);
            nbCarteTiree += nbCarte;
        }

        return tabCartes;
    }

    private Carte[] ajouterAuTableau(int nbCarte, Carte nomCarte, Carte[] tab)
    {
        while (nbCarte > 0)
            for (int i = 0; i < tab.length; i++)
                if (tab[i] == null)
                {
                    tab[i] = nomCarte;
                    nbCarte--;
                    break;
                }
        return tab;
    }

    private Carte carteAuHasard(String rarete)
    {
        switch (rarete)
        {
            case "Commune"      : return ctrl.getCartesCommune   ().get((int)(Math.random()*ctrl.getCartesCommune   ().size()));
            case "Rare"         : return ctrl.getCartesRare      ().get((int)(Math.random()*ctrl.getCartesRare      ().size()));
            case "Epique"       : return ctrl.getCartesEpique    ().get((int)(Math.random()*ctrl.getCartesEpique    ().size()));
            case "Legendaire"   : return ctrl.getCartesLegendaire().get((int)(Math.random()*ctrl.getCartesLegendaire().size()));
        }
        
        return null;
    }
}