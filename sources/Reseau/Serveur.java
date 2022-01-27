package sources.Reseau;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Serveur
{
	// Attributs du serveur
	private ServerSocket serveur;
	private List<Joueur> ensJoueur;
	private ClashRoyale cr;

	// Attributs du jeu
	private List<Jeu> jeux;
	private List<Joueur> joueurEnRecherche;

	public Serveur(ClashRoyale cr)
	{
		this.cr = cr;

		// Création des joueurs
		this.ensJoueur = new ArrayList<Joueur>();

		// Initialisation des attributs du jeu
		this.joueurEnRecherche = new ArrayList<Joueur>();
		this.jeux = new ArrayList<Jeu>();

		// Création du serveur
		try { this.serveur = new ServerSocket(6000); }
		catch(Exception e){ e.printStackTrace(); }
		System.out.println("Serveur créé avec succès !");

		Thread t = new Thread() { public void run() {
				while(true)
				{
					try {
						// Initialisation
						Socket socket = Serveur.this.serveur.accept();
						System.out.println("Client reçu");

						// Informations du joueur
						Joueur joueur = new Joueur(Serveur.this, socket);
						Serveur.this.ensJoueur.add(joueur);

						//Thread
						Thread tj = new Thread(joueur);
						tj.start();
					} catch(Exception e){ e.printStackTrace(); }
				}
			}
		};

		t.start();
		System.out.println("En attente de client...");
	}

	public void lire(String message, Joueur joueur)
	{
		// Si la partie est lancée et que le jeu n'est pas terminé
		// if (this.partieLancee && !this.jeu.getVictoire())
		if (message.equals("to"))
		{
			joueur.getSortie().println(joueur.toString());
			// Si le message est valide, on place le pion et on passe au tour suivant, sinon on affiche un message d'erreur
			// if (this.isValide(message, gdc))
			// {
				// this.jeux.get(Integer.parseInt(message.charAt(0))).placer(message, gdc);
			// }
			// afficher l'erreur
			// else gdc.getSortie().println("emplacement impossible");
		}
		else if (message.equals("go"))
		{
			//Ajout du joueur dans la recherche de partie
			this.joueurEnRecherche.add(joueur);
			if ( this.joueurEnRecherche.size() >= 2 ) this.nouvellePartie();
		}
		else if (message.equals("co"))
		{
			cr.ouvrirCoffre(cr.getStylax(), joueur);
			joueur.getSortie().println("Un coffre a été ouvert !");
		}
		else if (message.substring(0,2).equals("ameliorer".substring(0,2)))
		{
			if (joueur.ameliorer(joueur.getCarteParNom(message.split("  ")[1])))
				joueur.getSortie().println("Carte améliorée");
		}
		else joueur.getSortie().println("Petit aide :\n - co : ouvrir un coffre;\n - to : toString votre inventaire;\n - go : lancer une partie.");
	}

	private void nouvellePartie()
	{
		Joueur j1 = this.joueurEnRecherche.get(0);
		Joueur j2 = this.joueurEnRecherche.get(1);
		this.joueurEnRecherche.remove(this.joueurEnRecherche.get(0));
		this.joueurEnRecherche.remove(this.joueurEnRecherche.get(0));
		this.jeux.add( new Jeu(j1,j2) );
	}
}