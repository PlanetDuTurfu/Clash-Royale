package sources.Reseau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;

import sources.Carte;
import sources.ClashRoyale;
import sources.Coffre;

public class Joueur implements Runnable, Serializable {
    private String nom;
    private ArrayList<Carte> alCartes = new ArrayList<Carte>();
    private ArrayList<Coffre> alCoffre = new ArrayList<Coffre>();
    private int or;
    private ClashRoyale cr;

    private BufferedReader entree;
	private PrintWriter    sortie;
    private Serveur serveur;
	private boolean interrompu;
    
    public Joueur (Serveur serveur, Socket socket, ClashRoyale cr)
    {
        this.or = 1000;
        this.cr = cr;
        this.serveur = serveur;
        this.alCoffre.add(this.cr.getStylax());
        this.alCoffre.add(this.cr.getRodo());
        this.alCoffre.add(this.cr.getNaze());

        try {this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));} catch (Exception e) {e.printStackTrace();}
        try
        {
            this.sortie = new PrintWriter(socket.getOutputStream(), true);
            this.sortie.println("Entrez votre pseudo : ");
            this.nom = this.entree.readLine();
            this.sortie.println("Petit aide :\n - co : ouvrir un coffre;\n - to : toString votre inventaire;\n - go : lancer une partie." +
                                "\n - am + nom : améliorer une troupe;\n - tr + type : trier l'inventaire;");
        } catch (Exception e) { e.printStackTrace(); }

		this.interrompu = false;
    }

    public void ajouterCarte(Carte[] tabCartes)
    {
        for (Carte c : tabCartes)
            if ( !this.isCarteDansLinv(c.getNom()) ) this.alCartes.add(new Carte(c.getNom(), c.getRarete(), c.getPV(), c.getDeg(), c.getVitAtt(), 1, 1, c.getPrix()));
            else
            {
                Carte cTmp = this.getCarteParNom(c.getNom());
                cTmp.addDoublon();
            }
    }

    public boolean ameliorer(Carte carte)
    {
        if (carte.getDoublons() < (int)(Math.pow(2,carte.getNiveau())) || this.or < (int)(Math.pow(2,carte.getNiveau()))*15 || carte.getNiveau() == 10) return false;

        int multiplicateur = 1;
        if (carte.getRarete().equals("\033[91mRare\033[0m")) multiplicateur = 2;
        if (carte.getRarete().equals("\033[95mEpique\033[0m")) multiplicateur = 3;
        if (carte.getRarete().equals("\033[36mLégendaire\033[0m")) multiplicateur = 5;
        this.enleverOr((int)(Math.pow(2,carte.getNiveau()))*15*multiplicateur);
        carte.retirerDoublon((int)(Math.pow(2,carte.getNiveau())));
        carte.ameliorer();
        return true;
    }

    public boolean ameliorer(int indice)
    {
        if (indice > this.alCartes.size()) return false;
        Carte carte = this.alCartes.get(indice);
        if (carte.getDoublons() < (int)(Math.pow(2,carte.getNiveau())) || this.or < (int)(Math.pow(2,carte.getNiveau()))*15 || carte.getNiveau() == 10) return false;

        int multiplicateur = 1;
        if (carte.getRarete().equals("\033[91mRare\033[0m")) multiplicateur = 2;
        if (carte.getRarete().equals("\033[95mEpique\033[0m")) multiplicateur = 3;
        if (carte.getRarete().equals("\033[36mLégendaire\033[0m")) multiplicateur = 5;
        this.enleverOr((int)(Math.pow(2,carte.getNiveau()))*15*multiplicateur);
        carte.retirerDoublon((int)(Math.pow(2,carte.getNiveau())));
        carte.ameliorer();
        return true;
    }

    private boolean isCarteDansLinv(String nom)
    {
        for (Carte c : this.alCartes)
            if (nom.equals(c.getNom())) return true;
        return false;
    }

    public void trier(int tri)
    {
        switch (tri)
        {
            case 1 : Collections.sort(this.alCartes, new TriageParRarete()); break;
            case 2 : Collections.sort(this.alCartes, new TriageParNiveau()); break;
            case 3 : Collections.sort(this.alCartes, new TriageParNom());
        }
    }

    public Carte getCarteParNom(String nom)
    {
        for (Carte c : this.alCartes)
            if (nom.equals(c.getNom())) return c;
        return null;
    }

    public void ajouterCoffre(Coffre coffre)
    {
        this.alCoffre.add(coffre);
    }
    public boolean ouvrirCoffre()
    {
        if (this.alCoffre.size() == 0) return false;
        
        this.cr.ouvrirCoffre(this.alCoffre.get(0), this);
        this.alCoffre.remove(0);
        return true;
    }
    public void ajouterOr(int or)
    {
        this.or += or;
    }

    private void enleverOr(int or)
    {
        this.or -= or;
    }

    public BufferedReader getEntree(){ return this.entree; }
	public PrintWriter    getSortie(){ return this.sortie; }

    public String toString()
    {
        String sRet = "";
        sRet += "Joueur " + this.nom + " ("+this.or+" d'or) : \n";
        for (Carte c : this.alCartes)
        {
            sRet += String.format("\t%-19s %4d %-27s ", c.getRarete(), c.getDoublons(), c.getNom());
            sRet += String.format("niveau %2d PV=%4d DEG=%4d Vit_Att=%4s Prix=%1d\n",c.getNiveau(), c.getPV(), c.getDeg(), c.getVitAtt(), c.getPrix());
        }

        sRet += "Vos coffres : \n";
        for (Coffre c : this.alCoffre)
        {
            sRet += "\t" + c.getNom() + " : " + (c.getCommune()+c.getRare()+c.getEpique()+c.getLegendaire())+ " cartes";
            if (c.getRare() > 0) sRet += ", dont au moins " + c.getRare() + " rares";
            if (c.getEpique() > 0) sRet += ", " + c.getEpique() + " épiques";
            if (c.getLegendaire() > 0) sRet += ", " + c.getLegendaire() + " légendaires";
            sRet += ";\n";
        }
        return sRet;
    }

    public void run()
	{
		while(!this.interrompu)
		{
			try
			{
				// lecture du message du client
                String msg = this.entree.readLine();
		        System.out.println(this.nom + " : " + msg);
				this.serveur.lire(msg, this);
			}
			catch(Exception e){ e.printStackTrace(); }
		}
	}
}

class TriageParNiveau implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        return b.getNiveau() - a.getNiveau();
    }
}

class TriageParRarete implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        int aa = 0;
        int bb = 0;
        switch(a.getRarete())
        {
            case "\033[90mCommune\033[0m" : aa = 4; break;
            case "\033[91mRare\033[0m" : aa = 3; break;
            case "\033[95mEpique\033[0m" : aa = 2; break;
            case "\033[36mLégendaire\033[0m" : aa = 1;
        }
        switch(b.getRarete())
        {
            case "\033[90mCommune\033[0m" : bb = 4; break;
            case "\033[91mRare\033[0m" : bb = 3; break;
            case "\033[95mEpique\033[0m" : bb = 2; break;
            case "\033[36mLégendaire\033[0m" : bb = 1;
        }

        if (aa < bb) return  1;
        if (aa > bb) return -1;
        return 0;
    }
}

class TriageParNom implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        int taille;
        String aa = a.getNom();
        String bb = b.getNom();
        if (aa.length() > bb.length()) taille = bb.length();
        else taille = aa.length();

        for (int i = 0; i < taille; i++)
        {
            if (aa.charAt(i) > bb.charAt(i)) return 1;
            if (aa.charAt(i) < bb.charAt(i)) return -1;
        }
        return 0;
    }
}