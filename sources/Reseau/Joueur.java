package sources.Reseau;

import sources.Carte;
import sources.ClashRoyale;
import sources.Coffre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.net.Socket;
import java.time.LocalDateTime;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Joueur implements Runnable {
    private String nom;
    private String mdp;
    private String tri;
    private int or;
    private int indiceScroll;
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
        this.tri = "défaut";
        this.entree = entree;
        this.sortie = sortie;
        this.indiceScroll = 0;
    }

    public void ajouterCarte(Carte[] tabCartes)
    {
        String affichage = "@carte#";
        int nbC = 0, nbR = 0, nbE = 0, nbL =0;
        for (Carte c : tabCartes)
        {
            if (c.getRarete().equals("\033[90mCommune\033[0m")) nbC++;
            if (c.getRarete().equals("\033[91mRare\033[0m")) nbR++;
            if (c.getRarete().equals("\033[95mEpique\033[0m")) nbE++;
            if (c.getRarete().equals("\033[36mLégendaire\033[0m")) nbL++;
            if ( !this.isCarteDansLinv(c.getNom()) )
            {
                affichage += c.getNom() + "¤" + c.getRarete() + "¤true#";
                this.alCartes.add(new Carte(c.getNom(), c.getRarete(), c.getPV(), c.getDeg(), c.getVitAtt(), 1, 1, c.getPrix()));
            }
            else
            {
                affichage += c.getNom() + "¤" + c.getRarete() + "¤false#";
                this.getCarteParNom(c.getNom()).addDoublon();
            }
        }

        this.addLog("carte : " + nbC +"c " + nbR + "r " + nbE + "e " + nbL + "l");
        this.sortie.println(affichage);
    }
    public void setCartes(ArrayList<Carte> cartes)
    {
        this.alCartes = cartes;
    }

    public boolean ameliorer(Carte carte)
    {
        int multiplicateur = 1;
        if (carte.getRarete().equals("\033[91mRare\033[0m")) multiplicateur = 2;
        if (carte.getRarete().equals("\033[95mEpique\033[0m")) multiplicateur = 3;
        if (carte.getRarete().equals("\033[36mLégendaire\033[0m")) multiplicateur = 5;
        
        if (carte.getDoublons() < (int)(Math.pow(2,carte.getNiveau())) ||
            this.or < (int)(Math.pow(2,carte.getNiveau()))*15*multiplicateur ||
            carte.getNiveau() == 10) return false;

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

    public Carte getCarteParNom(String nom)
    {
        for (Carte c : this.alCartes)
            if (nom.equals(c.getNom())) return c;
        return null;
    }

    public void ajouterCoffre(Coffre coffre)
    {
        this.alCoffre.add(coffre);
        this.addLog("added coffre " + this.alCoffre.get(this.alCoffre.size()-1).getNom());
    }
    public boolean ouvrirCoffre(String nom)
    {
        if (this.alCoffre.size() == 0) return false;
        for (Coffre c : this.alCoffre)
        {
            if (c.getNom().equals(nom))
            {
                this.cr.ouvrirCoffre(c, this);
                this.alCoffre.remove(c);
                break;
            }
        }
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
    public int getIndiceScroll() { return this.indiceScroll; }
    public void setIndiceScroll(int i) { this.indiceScroll = i; }

    public BufferedReader getEntree(){ return this.entree; }
	public PrintWriter    getSortie(){ return this.sortie; }
    public void    setNom(String nom){ this.nom = nom; this.addLog("[+] " + this.nom); }
    public String            getNom(){ return this.nom   ; }
    public String            getMdp(){ return this.mdp   ; }
    public int                getOr(){ return this.or    ; }
    public ArrayList<Carte>  getCartes () { return this.alCartes; }
    public ArrayList<Coffre> getCoffres() { return this.alCoffre; }
    public String getTri() { return this.tri; }

    public void trier()
    {
        switch (this.tri)
        {
            case "défaut"   : this.tri = "Rarete";  Collections.sort(this.alCartes, new TriageParRarete ()); break;
            case "Rarete"   : this.tri = "Niveau";  Collections.sort(this.alCartes, new TriageParNiveau ()); break;
            case "Niveau"   : this.tri = "Nom";     Collections.sort(this.alCartes, new TriageParNom    ()); break;
            case "Nom"      : this.tri = "Prix";    Collections.sort(this.alCartes, new TriageParPrix   ()); break;
            case "Prix"     : this.tri = "PV";      Collections.sort(this.alCartes, new TriageParPv     ()); break;
            case "PV"       : this.tri = "DEG";     Collections.sort(this.alCartes, new TriageParDeg    ()); break;
            case "DEG"      : this.tri = "Rarete";  Collections.sort(this.alCartes, new TriageParRarete ()); break;
        }
    }

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
                this.addLog(msg);
				this.serveur.lire(msg, this);
			}
			catch(Exception e)
            {
                this.addLog("[-] " + this.nom);
                this.serveur.deconnecter(this.thread, this);
                try {this.entree.close();} catch (IOException e1) {}
                this.sortie.close();
                break;
            }
		}
	}

    private void addLog(String msg)
    {
        try {
            File file = new File("./data/logs/"+LocalDateTime.now().getYear() +"-"+ LocalDateTime.now().getMonthValue() +"-"+ LocalDateTime.now().getDayOfMonth() + ".log");
            if (!file.exists()) file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
            bw.write(String.format("%02d",LocalDateTime.now().getHour())+":"+String.format("%02d",LocalDateTime.now().getMinute())+":"+String.format("%02d",LocalDateTime.now().getSecond())+" | "+this.nom+" -> "+msg+"\n");
            bw.close();
        } catch (Exception e) {}
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