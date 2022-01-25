package sources;

import java.util.ArrayList;

public class ClashRoyale {
    private Coffres coffres;
    private Cartes cartes;
    private ArrayList<Joueur> alJoueurs;

    private ClashRoyale()
    {
        this.alJoueurs = new ArrayList<Joueur>();
        cartes  = new Cartes ();
        coffres = new Coffres(this);
        alJoueurs.add(new Joueur("Yvan"));
        alJoueurs.add(new Joueur("Nono"));
        alJoueurs.add(new Joueur("Enzo"));
        alJoueurs.add(new Joueur("Charlotte"));

        for (Joueur j : alJoueurs)
            this.attribuerCarte(j);
        for (Joueur j : alJoueurs)
            System.out.println(j.toString());
    }

    private void attribuerCarte(Joueur j)
    {
        for (int i = 0; i < 3; i++)
            this.ouvrirCoffre(coffres.getNaze(), j);
    }

    public void ouvrirCoffre(Coffre coffre, Joueur j)
    {
        j.ajouterCarte(coffres.ouvrirCoffre(coffre));
        j.ajouterOr(coffre.getOr());
    }

    public ArrayList<Carte> getCartesCommune   () { return cartes.getCartesCommune   (); }
    public ArrayList<Carte> getCartesRare      () { return cartes.getCartesRare      (); }
    public ArrayList<Carte> getCartesEpique    () { return cartes.getCartesEpique    (); }
    public ArrayList<Carte> getCartesLegendaire() { return cartes.getCartesLegendaire(); }

    public static void main(String[] args) {
        new ClashRoyale();
    }
}
