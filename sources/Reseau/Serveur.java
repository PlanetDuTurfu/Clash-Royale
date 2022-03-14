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
	private ClashRoyale cr;

	public Serveur(ClashRoyale cr)
	{
		// Initialisation des attributs du jeu
		this.joueurEnRecherche = new ArrayList<Joueur>();
		this.jeux = new ArrayList<Jeu>();
		this.cr = cr;

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
						sortie.println("wait#pseudo#mdp");
						Joueur joueur = Serveur.this.chargerJoueur(entree.readLine(), socket, cr, this, entree, sortie);
						sortie.println("connexion#accepted");
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
		if (message.contains("to "))
		{
			String affichage = "@to#"+joueur.getTri()+"#"+joueur.getOr()+"#";
			int cpt = 1;
			int debut = 0;
			if (message.split(" ")[1].equals("+"))
			{
				if (joueur.getCartes().size() - joueur.getIndiceScroll() <= 4) return;
				joueur.setIndiceScroll(joueur.getIndiceScroll() + 4);

				debut = joueur.getIndiceScroll();
				for (Carte c : joueur.getCartes())
					if (cpt > debut) affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
					else cpt++;
			}
			else if (message.split(" ")[1].equals("-"))
			{
				if (joueur.getIndiceScroll() < 4) return;
				joueur.setIndiceScroll(joueur.getIndiceScroll() - 4);

				debut = joueur.getIndiceScroll();
				for (Carte c : joueur.getCartes())
					if (cpt > debut) affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
					else cpt++;
			}
			else if (message.split(" ")[1].equals("0"))
			{
				joueur.setIndiceScroll(0);
				for (Carte c : joueur.getCartes())
					affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
			}
			joueur.getSortie().println(affichage);
		}
		else if (message.equals("go"))
		{
			this.addJoueurRecherche(joueur);
		}
		else if (message.substring(0,"co ".length()).equals("co "))
		{
			if (joueur.ouvrirCoffre(message.split("  ")[1]))
				this.lire("cos", joueur);
		}
		else if (message.equals("cos"))
		{
			String affichage = "@co#";
			for (Coffre c : joueur.getCoffres()) affichage += c.getNom() + "#";
			joueur.getSortie().println(affichage.substring(0,affichage.length() - 1));
		}
		else if (message.substring(0,2).equals("ameliorer".substring(0,2)))
		{
			if (joueur.ameliorer(joueur.getCarteParNom(message.split("  ")[1])))
				joueur.getSortie().println("Carte améliorée");
			else joueur.getSortie().println("Cette carte ne peut pas être améliorée");
			
			int cpt = 1;
			int debut = joueur.getIndiceScroll();
			String affichage = "@to#"+joueur.getTri()+"#"+joueur.getOr()+"#";
			for (Carte c : joueur.getCartes())
				if (cpt > debut) affichage += c.getNom()+"¤"+c.getRarete()+"¤"+c.getNiveau()+"¤"+c.getDoublons()+"¤"+c.getPV()+"¤"+c.getDeg()+"¤"+c.getVitAtt()+"#";
				else cpt++;

			joueur.getSortie().println(affichage);
		}
		else if (message.equals("nextTri"))
		{
			joueur.trier();
			this.lire("to 0", joueur);
		}
		else if (message.equals("accueil"))
		{
			joueur.getSortie().println("connexion#accepted");
		}
	
		this.sauverJoueur(joueur);
	}

	private synchronized void addJoueurRecherche(Joueur joueur)
	{
		this.joueurEnRecherche.add(joueur);
		if ( this.joueurEnRecherche.size() >= 2 ) this.nouvellePartie();
	}

	private void nouvellePartie()
	{
		Joueur j1 = this.joueurEnRecherche.get(0);
		Joueur j2 = this.joueurEnRecherche.get(1);
		this.joueurEnRecherche.remove(j1);
		this.joueurEnRecherche.remove(j2);
		Jeu jeuTMP = new Jeu(j1,j2);
		this.jeux.add( jeuTMP );
		jeuTMP.start();
	}

	public void deconnecter(Thread tj, Joueur j)
	{
		tj.interrupt();
	}

	private Joueur chargerJoueur(String pseudo, Socket socket, ClashRoyale cr, Thread t, BufferedReader entree, PrintWriter sortie)
	{
		Joueur j = null;

		File f = new File("./data/sauvegarde/"+pseudo+".account");
		if(f.isFile())
		{
			try {
				Scanner sc = new Scanner( new FileReader("./data/sauvegarde/"+pseudo+".account"));
				String mdp = sc.nextLine();
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
							tmp2 = new Carte(tmp2.getNom(), tmp2.getRarete(), tmp2.getPV(), tmp2.getDeg(), tmp2.getVitAtt(), tmp2.getVitDep(), 0, 0, tmp2.getPrix());
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
		String mdp = "";
		try {mdp = entree.readLine();}catch(Exception e){}
		j = new Joueur(this, socket, cr, t, mdp, entree, sortie);
		j.setNom(pseudo);
		
		j.ajouterCoffre(this.cr.getCoffreParNom("Coffre stylax"));
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