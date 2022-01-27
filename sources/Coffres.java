package sources;

import sources.Reseau.ClashRoyale;

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
        coffreNaze       = new Coffre("Coffre naze"      ,   80,  20,  3,  0, 0);
        coffreEnBois     = new Coffre("Coffre en bois"   ,  170,  35,  6,  0, 0);
        coffreStylax     = new Coffre("Coffre stylax"    ,  300,  70, 15,  2, 0);
        coffreEz         = new Coffre("Coffre EZ"        ,  600, 120, 30,  3, 0);
        coffreDuFutur    = new Coffre("Coffre du futur"  , 1000, 170, 20,  4, 0);
        coffreOP         = new Coffre("Coffre OP"        , 1600, 210, 50,  7, 1);
        rodoCoffre       = new Coffre("Rodocoffre"       , 2200, 300, 65, 10, 1);
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
            if (Math.random() * 1000 > 950)
            {
                if (Math.random() * 1000 > 900)
                {
                    if (Math.random() * 1000 > 910)
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