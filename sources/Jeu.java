package sources;

import sources.Reseau.Joueur;

public class Jeu
{
    private char[][] plateau;
	private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurActuel;
    private boolean victoire = false;

    public Jeu(Joueur j1, Joueur j2)
    {
        this.plateau = new char[1][1];
        this.joueur1 = j1;
        this.joueur2 = j2;
    }

    public void lancerPartie(Joueur j1, Joueur j2)
    {
        if (Math.random() * 100 > 49) this.joueurActuel = this.joueur1;
        else this.joueurActuel = this.joueur2;
    }

    public boolean placer (int col)
 	{
		
 	 	return false;
 	}

    // Méthode qui permet de transformer le tableau en chaine de caractère
	public void afficher(Joueur joueur)
	{
	}

    // Getteur du caractère d'une certaine case
    public char getCaractere(int col) { return this.plateau[0][col - 1]; }
    // Getteur du joueur actuel
    public Joueur getJoueur() { return this.joueurActuel   ; }
    // Getteur du boolean victoire
    public boolean getVictoire     () { return this.victoire       ; }
}