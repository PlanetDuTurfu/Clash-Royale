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

	// Attributs du jeu
	private List<Jeu> jeux;
	private List<Joueur> joueurEnRecherche;

	private String tri;

	public Serveur(ClashRoyale cr)
	{
		this.tri = "";
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
						sortie.println("Connexion acceptée");
						Serveur.this.sauverJoueur(joueur);

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
		if (message.equals("to"))
		{
			String affichage = "@to#";
			for (Carte c : joueur.getCartes())
				affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
			joueur.getSortie().println(affichage);
			this.sauverJoueur(joueur);
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
				if (joueur.getCartes().size() <= Integer.parseInt(message.split("  ")[1]))
					joueur.getSortie().println("Cet indice est supérieur au nombre de cartes");
				else if (joueur.ameliorer(Integer.parseInt(message.split("  ")[1])))
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
		else if (message.equals("nextTri"))
		{
			System.out.println("nextTri");
			switch (this.tri)
			{
				case "" : this.tri = "Rarete"; joueur.trier(1); break;
				case "Rarete" : this.tri = "Niveau"; joueur.trier(2); break;
				case "Niveau" : this.tri = "Nom"; joueur.trier(3); break;
				case "Nom" : this.tri = "Prix"; joueur.trier(4); break;
				case "Prix" : this.tri = "PV"; joueur.trier(5); break;
				case "PV" : this.tri = "DEG"; joueur.trier(6); break;
				case "Deg" : this.tri = "Rarete"; joueur.trier(1); break;
			}

			String affichage = "@to#";
			for (Carte c : joueur.getCartes())
				affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
			joueur.getSortie().println(affichage);
			// String[] tabTri = message.split(" ");
			// for (int i = tabTri.length-1; i > 0; i--)
			// 	if (tabTri[i].substring(0,2).equals("trier".substring(0,2))) break;
			// 	else if (tabTri[i].equals("Rarete")) joueur.trier(1);
			// 	else if (tabTri[i].equals("Niveau")) joueur.trier(2);
			// 	else if (tabTri[i].equals("Nom")) joueur.trier(3);
			// 	else joueur.getSortie().println("Tri inconnu : " + tabTri[i]);
			
			// joueur.getSortie().println("Inventaire trié !");
		}
	
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

	public void deconnecter(Thread tj, Joueur j)
	{
		tj.interrupt();
	}

	private Joueur chargerJoueur(String pseudo, Socket socket, ClashRoyale cr, Thread t, BufferedReader entree, PrintWriter sortie)
	{
		System.out.println("Pseudo : " + pseudo);
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
					ArrayList<Carte> cartes = new ArrayList<Carte>();

					while (sc.hasNextLine())
					{
						String[] ligne = sc.nextLine().split("#");
						if (ligne[0].charAt(0) == 'A')
						{
							Carte tmp2 = cr.getCarteParNom(ligne[1]);
							System.out.println("\t" + Integer.parseInt(ligne[3]) + " niveau doublons " + Integer.parseInt(ligne[2]));
							tmp2 = new Carte(tmp2.getNom(), tmp2.getRarete(), tmp2.getPV(), tmp2.getDeg(), tmp2.getVitAtt(), 0, 0, tmp2.getPrix());
							for (int i = 0; i < Integer.parseInt(ligne[2]); i++) tmp2.addDoublon();
							for (int i = 0; i < Integer.parseInt(ligne[3]); i++) tmp2.ameliorer();
							cartes.add(tmp2);
						}						
						if (ligne[0].charAt(0) == 'O') j.ajouterCoffre(cr.getCoffreParNom(ligne[1]));
					}

					j.setCartes(cartes);
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
		sortie.println("Compte non existant.\nCréez un mot de passe : ");
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