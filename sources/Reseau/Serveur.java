package sources.Reseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import sources.Carte;
import sources.ClashRoyale;
import sources.Coffre;
import sources.Jeu;

import java.util.ArrayList;

public class Serveur
{
	// Attributs du serveur
	private ServerSocket serveur;
	private List<Joueur> ensJoueur;

	// Attributs du jeu
	private List<Jeu> jeux;
	private List<Joueur> joueurEnRecherche;

	public Serveur(ClashRoyale cr)
	{

		// Création des joueurs
		this.ensJoueur = new ArrayList<Joueur>();

		// Initialisation des attributs du jeu
		this.joueurEnRecherche = new ArrayList<Joueur>();
		this.jeux = new ArrayList<Jeu>();

		// Création du serveur
		try { this.serveur = new ServerSocket(6000); }
		catch(Exception e){ e.printStackTrace(); }
		System.out.println("Serveur créé avec succès !");

		Thread t = new Thread() { public void run() { while(true) {
					try {
						// Initialisation
						Socket socket = Serveur.this.serveur.accept();
						System.out.println("Client reçu");
						BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter sortie = new PrintWriter   (socket.getOutputStream(), true);

						// Informations du joueur
						sortie.println("Entrez votre pseudo : ");
						Joueur joueur = Serveur.this.chargerJoueur(entree.readLine(), socket, cr, this, entree, sortie);
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
			if (joueur.ouvrirCoffre()) joueur.getSortie().println("Un coffre a été ouvert !");
			else joueur.getSortie().println("Vous n'avez plus de coffres !");
		}
		else if (message.substring(0,2).equals("ameliorer".substring(0,2)))
		{
			try
			{
				if (joueur.ameliorer(Integer.parseInt(message.split("  ")[1])))
					joueur.getSortie().println("Carte améliorée");
				else joueur.getSortie().println("Cette carte ne peut pas être améliorée");
			}
			catch(Exception e)
			{
				if (joueur.ameliorer(joueur.getCarteParNom(message.split("  ")[1])))
					joueur.getSortie().println("Carte améliorée");
				else joueur.getSortie().println("Cette carte ne peut pas être améliorée");
			}
		}
		else if (message.substring(0,2).equals("trier".substring(0,2)))
		{
			String[] tabTri = message.split(" ");
			for (int i = tabTri.length-1; i > 0; i--)
				if (tabTri[i].substring(0,2).equals("trier".substring(0,2))) break;
				else if (tabTri[i].equals("Rarete")) joueur.trier(1);
				else if (tabTri[i].equals("Niveau")) joueur.trier(2);
				else if (tabTri[i].equals("Nom")) joueur.trier(3);
				else joueur.getSortie().println("Tri inconnu : " + tabTri[i]);
			
			joueur.getSortie().println("Inventaire trié !");
		}
		else joueur.getSortie().println("Petite aide :\n - co : ouvrir un coffre;\n - to : toString votre inventaire;\n - go : lancer une partie." +
										"\n - am + nom : améliorer une troupe;\n - tr + type : trier l'inventaire;");
	
		this.sauverJoueur(joueur);
	}

	private void nouvellePartie()
	{
		Joueur j1 = this.joueurEnRecherche.get(0);
		Joueur j2 = this.joueurEnRecherche.get(1);
		this.joueurEnRecherche.remove(this.joueurEnRecherche.get(0));
		this.joueurEnRecherche.remove(this.joueurEnRecherche.get(0));
		this.jeux.add( new Jeu(j1,j2) );
	}

	public void deconnecter(Thread tj)
	{
		tj.interrupt();
	}

	private Joueur chargerJoueur(String pseudo, Socket socket, ClashRoyale cr, Thread t, BufferedReader entree, PrintWriter sortie)
	{
		Joueur j = null;

		File f = new File("./data/sauvegarde/"+pseudo+".account");
		if(f.isFile())
		{
			sortie.println("Compte déjà existant.");
			try {
				Scanner sc = new Scanner( new FileReader("./data/sauvegarde/"+pseudo+".account"));
				String mdp = sc.nextLine();
				sortie.println("Entrez le mot de passe : ");
				if (entree.readLine().equals(mdp))
				{
					j = new Joueur(this, socket, cr, t, mdp, entree, sortie);
					j.setNom(pseudo);
					j.ajouterOr(Integer.parseInt(sc.nextLine()) - 1000);

					while (sc.hasNextLine())
					{
						String[] ligne = sc.nextLine().split("#");
						if (ligne[0].charAt(0) == 'A')
						{
							Carte tmp2 = cr.getCarteParNom(ligne[1]);
							for (int i = 0; i < Integer.parseInt(ligne[2]) / 2; i++) tmp2.addDoublon();
							for (int i = 0; i < Integer.parseInt(ligne[3]) / 2; i++) tmp2.ameliorer ();
							j.ajouterCarte(tmp2);
						}						
						if (ligne[0].charAt(0) == 'O') j.ajouterCoffre(cr.getCoffreParNom(ligne[1]));
					}
					return j;
				}
				else
				{
					sortie.println("Mauvais mot de passe");
					t.interrupt();
				}
			} catch (Exception e) {}
			return j;
		}
		sortie.println("Créez un mot de passe : ");
		String mdp = "";
		try {mdp = entree.readLine();}catch(Exception e){}
		j = new Joueur(this, socket, cr, t, mdp, entree, sortie);
		j.setNom(pseudo);
		return j;
	}

	private void sauverJoueur(Joueur j)
	{
		try {
			File file = new File("./data/sauvegarde/"+j.getNom()+".account");
			if (!file.exists()) file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bw.write(j.getMdp()+"\n");
			bw.write(j.getOr ()+"\n");
			ArrayList<Carte> tmpCartes = j.getCartes();
			ArrayList<Coffre> tmpCoffres = j.getCoffres();
			for (Carte c : tmpCartes) bw.write("A#"+c.getNom()+"#"+c.getDoublons()+"#"+c.getNiveau()+"\n");
			for (Coffre c : tmpCoffres) bw.write("O#"+c.getNom()+"\n");
			bw.close();
		} catch (Exception e) {}
	}
}