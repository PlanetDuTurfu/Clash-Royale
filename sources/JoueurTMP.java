package sources;

import sources.Reseau.Joueur;

public class JoueurTMP {
    private int posX;
    private int posY;
    private String nom;

    public JoueurTMP(Joueur j)
    {
        this.nom = j.getNom();
    }

    public void setTourPos(int x, int y)
    {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }

    public String getNom() { return this.nom; }
}
