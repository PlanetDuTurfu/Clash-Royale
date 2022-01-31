package sources.Reseau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import sources.Carte;
import sources.ClashRoyale;
import sources.Coffre;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class Joueur implements Runnable, Serializable {
    private String nom;
    private String mdp;
    private int or;
    private ArrayList<Carte> alCartes = new ArrayList<Carte>();
    private ArrayList<Coffre> alCoffre = new ArrayList<Coffre>();
    private ClashRoyale cr;

    private BufferedReader entree;
	private PrintWriter sortie;
    private Serveur serveur;
    private Thread thread;
    
    public Joueur (Serveur serveur, Socket socket, ClashRoyale cr, Thread thread, String mdp, BufferedReader entree, PrintWriter sortie)
    {
        this.or = 1000;
        this.cr = cr;
        this.serveur = serveur;
        this.thread = thread;
        this.mdp = mdp;
        this.entree = entree;
        this.sortie = sortie;
        this.alCoffre.add(this.cr.getCoffreParNom("coffre"));
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
    public void setCartes(ArrayList<Carte> cartes)
    {
        this.alCartes = cartes;
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
            case 3 : Collections.sort(this.alCartes, new TriageParNom()); break;
            case 4 : Collections.sort(this.alCartes, new TriageParPrix()); break;
            case 5 : Collections.sort(this.alCartes, new TriageParPv()); break;
            case 6 : Collections.sort(this.alCartes, new TriageParDeg()); break;
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
    public void    setNom(String nom){ this.nom = nom    ; }
    public String            getNom(){ return this.nom   ; }
    public String            getMdp(){ return this.mdp   ; }
    public int                getOr(){ return this.or    ; }
    public ArrayList<Carte>  getCartes () { return this.alCartes; }
    public ArrayList<Coffre> getCoffres() { return this.alCoffre; }

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
		while(true)
		{
			try
			{
				// lecture du message du client
                String msg = this.entree.readLine();
		        System.out.println(this.nom + " : " + msg);
				this.serveur.lire(msg, this);
			}
			catch(Exception e)
            {
                System.out.println(this.nom + " a quitté le jeu.");
                this.serveur.deconnecter(this.thread, this);
                try {this.entree.close();} catch (IOException e1) {}
                this.sortie.close();
                break;
            }
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

class TriageParPrix implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        return b.getPrix() - a.getPrix();
    }
}

class TriageParPv implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        return b.getPV() - a.getPV();
    }
}

class TriageParDeg implements Comparator<Carte>
{
    public int compare(Carte a, Carte b)
    {
        return b.getDeg() - a.getDeg();
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