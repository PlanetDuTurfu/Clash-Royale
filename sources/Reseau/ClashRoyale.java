package sources.Reseau;

import java.util.ArrayList;

import sources.Carte;
import sources.Cartes;
import sources.Coffre;
import sources.Coffres;

public class ClashRoyale {
    private Coffres coffres;
    private Cartes cartes;

    private ClashRoyale()
    {
        new Serveur(this);
        cartes  = new Cartes();
        coffres = new Coffres(this);
    }

    public void ouvrirCoffre(Coffre coffre, Joueur j)
    {
        j.ajouterOr(coffre.getOr());
        j.ajouterCarte(coffres.ouvrirCoffre(coffre));
    }

    public ArrayList<Carte> getCartesCommune   () { return cartes.getCartesCommune   (); }
    public ArrayList<Carte> getCartesRare      () { return cartes.getCartesRare      (); }
    public ArrayList<Carte> getCartesEpique    () { return cartes.getCartesEpique    (); }
    public ArrayList<Carte> getCartesLegendaire() { return cartes.getCartesLegendaire(); }

    public Coffre getNaze      () { return this.coffres.getNaze      (); }
    public Coffre getEnBois    () { return this.coffres.getEnBois    (); }
    public Coffre getStylax    () { return this.coffres.getStylax    (); }
    public Coffre getEz        () { return this.coffres.getEz        (); }
    public Coffre getDuFutur   () { return this.coffres.getDuFutur   (); }
    public Coffre getOP        () { return this.coffres.getOP        (); }
    public Coffre getRodo      () { return this.coffres.getRodo      (); }
    public Coffre getCommun    () { return this.coffres.getCommun    (); }
    public Coffre getRare      () { return this.coffres.getRare      (); }
    public Coffre getEpique    () { return this.coffres.getEpique    (); }
    public Coffre getLegendaire() { return this.coffres.getLegendaire(); }

    public static void main(String[] args) {
        new ClashRoyale();
    }
}
