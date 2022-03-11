package sources;

import sources.Reseau.Joueur;

public class JoueurTMP {
    private int posX;
    private int posY;
    private String nom;
    private Joueur j;

    public JoueurTMP(Joueur j)
    {
        this.nom = j.getNom();
        this.j = j;
    }

    public void setTourPos(int x, int y)
    {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public Joueur getJoueur() { return this.j; }
    public String getNom() { return this.nom; }
}
