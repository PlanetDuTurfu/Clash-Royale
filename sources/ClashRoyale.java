package sources;

import java.util.ArrayList;

import sources.Reseau.Joueur;
import sources.Reseau.Serveur;

public class ClashRoyale {
    private Coffres coffres;
    private Cartes cartes;

    public ClashRoyale()
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
    public Carte       getCarteParNom(String nom) { return cartes.getCarteParNom  (nom); }
    public Coffre     getCoffreParNom(String nom) { return coffres.getCoffreParNom(nom); }
}
