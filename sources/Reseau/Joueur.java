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

public class Joueur implements Runnable, Serializable {
    private String nom;
    private ArrayList<Carte> alCartes = new ArrayList<Carte>();
    private int or;

    private BufferedReader entree;
	private PrintWriter    sortie;
    private Serveur serveur;
	private boolean interrompu;
    
    public Joueur (Serveur serveur, Socket socket)
    {
        this.or = 1000;
        this.serveur = serveur;

        try {this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));} catch (Exception e) {e.printStackTrace();}
        try
        {
            this.sortie = new PrintWriter(socket.getOutputStream(), true);
            this.sortie.println("Entrez votre pseudo : ");
            this.nom = this.entree.readLine();
            this.sortie.println("Petit aide : \n - to : toString votre inventaire;\n - go : lancer une partie.");
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

        this.enleverOr((int)(Math.pow(2,carte.getNiveau()))*15);
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
        this.trier(2);
        String sRet = "";
        sRet += "Joueur " + this.nom + " ("+this.or+" d'or) : \n";
        for (Carte c : this.alCartes)
        {
            sRet += String.format("\t%-19s %4d %-27s ", c.getRarete(), c.getDoublons(), c.getNom());
            sRet += String.format("niveau %2d PV=%4d DEG=%4d Vit_Att=%4s Prix=%1d\n",c.getNiveau(), c.getPV(), c.getDeg(), c.getVitAtt(), c.getPrix());
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