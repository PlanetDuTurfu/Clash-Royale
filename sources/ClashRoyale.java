package sources;

import java.util.ArrayList;

public class ClashRoyale {
    private Coffres coffres;
    private Cartes cartes;
    private ArrayList<Joueur> alJoueurs;

    private ClashRoyale()
    {
        this.alJoueurs = new ArrayList<Joueur>();
        cartes  = new Cartes();
        coffres = new Coffres(this);
        alJoueurs.add(new Joueur("Yvan"));

        for (Joueur j : alJoueurs)
            this.attribuerCarte(j);
        for (Joueur j : alJoueurs)
            System.out.println(j.toString());
    }

    private void attribuerCarte(Joueur j)
    {
        for (int i = 0; i < 1; i++)
            this.ouvrirCoffre(coffres.getRodo(), j);
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

    public static void main(String[] args) {
        new ClashRoyale();
    }
}
