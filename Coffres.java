package sources;

import java.util.ArrayList;

public class Coffres {
    private ClashRoyale ctrl;
    private Coffre coffreNaze;
    private Coffre coffreEz;
    private Coffre coffreStylax;
    private Coffre coffreEnBois;
    private Coffre coffreOP;
    private Coffre coffreDuFutur;
    private Coffre rodoCoffre;
    private Coffre coffreCommun;
    private Coffre coffreRare;
    private Coffre coffreEpique;
    private Coffre coffreLegendaire;

    public Coffres(ClashRoyale ctrl)
    {
        this.ctrl = ctrl;
        //                             Nom du coffre       NbOr  nbC nbR nbE nbL
        coffreNaze       = new Coffre("Coffre naze"      ,  100,  20,  3,  0, 0);
        coffreEnBois     = new Coffre("Coffre en bois"   ,  150,  35,  6,  0, 0);
        coffreStylax     = new Coffre("Coffre stylax"    ,  320,  70, 15,  2, 0);
        coffreEz         = new Coffre("Coffre EZ"        ,  700, 120, 30,  3, 0);
        coffreDuFutur    = new Coffre("Coffre du futur"  , 1100, 170, 20,  4, 0);
        coffreOP         = new Coffre("Coffre OP"        , 2200, 210, 50,  7, 1);
        rodoCoffre       = new Coffre("Rodocoffre"       , 3500, 300, 65, 10, 1);
        coffreCommun     = new Coffre("Coffre commun"    , 1000, 600,  0,  0, 0);
        coffreRare       = new Coffre("Coffre rare"      , 1200,   0,100,  0, 0);
        coffreEpique     = new Coffre("Coffre épique"    , 1500,   0,  0, 20, 0);
        coffreLegendaire = new Coffre("Coffre légendaire", 2000,   0,  0,  0, 2);
    }

    public Coffre getNaze      () { return coffreNaze      ; }
    public Coffre getEnBois    () { return coffreEnBois    ; }
    public Coffre getStylax    () { return coffreStylax    ; }
    public Coffre getEz        () { return coffreEz        ; }
    public Coffre getDuFutur   () { return coffreDuFutur   ; }
    public Coffre getOP        () { return coffreOP        ; }
    public Coffre getRodo      () { return rodoCoffre      ; }
    public Coffre getCommun    () { return coffreCommun    ; }
    public Coffre getRare      () { return coffreRare      ; }
    public Coffre getEpique    () { return coffreEpique    ; }
    public Coffre getLegendaire() { return coffreLegendaire; }

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
            if ( Math.random() * 100 > 99.85)
            {
                tmpCommune--;
                tmpLegendaire++;
            }
            else if (Math.random() * 100 + 1 > 99.5)
            {
                tmpCommune--;
                tmpEpique++;
            }
            else if (Math.random() * 100 + 1 > 97.5)
            {
                tmpCommune--;
                tmpRare++;
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
        ArrayList<Carte> alCartes = new ArrayList<Carte>();
        switch (rarete)
        {
            case "Commune" : alCartes = ctrl.getCartesCommune(); break;
            case "Rare" : alCartes = ctrl.getCartesRare(); break;
            case "Epique" : alCartes = ctrl.getCartesEpique(); break;
            case "Legendaire" : alCartes = ctrl.getCartesLegendaire(); break;
        }
        
        return alCartes.get((int) (Math.random()*alCartes.size()));
    }
}