package sources;

import sources.Reseau.Joueur;

public class JoueurTMP {
    private Tour tour;
    private String nom;
    private Joueur j;

    public JoueurTMP(Joueur j)
    {
        this.nom = j.getNom();
        this.j = j;
    }

    public void setTour(Tour tour) { this.tour = tour; }

    public Joueur getJoueur() { return this.j; }
    public String getNom () { return this.nom; }
    public Tour  getTour (){ return this.tour; }
}
