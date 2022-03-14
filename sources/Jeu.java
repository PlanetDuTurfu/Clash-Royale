package sources;

import java.util.ArrayList;
import sources.Reseau.Joueur;
import sources.Reseau.Serveur;

public class Jeu extends Thread
{
    private Serveur serveur;
    private String[][] plateau;
    private ArrayList<Troupe> alTroupes;
	private JoueurTMP joueur1;
    private JoueurTMP joueur2;
    private int idTroupe;

    public Jeu(Joueur j1, Joueur j2, Serveur serv)
    {
        this.serveur = serv;
        this.plateau = new String[25][19];
        this.alTroupes = new ArrayList<Troupe>();
        this.joueur1 = new JoueurTMP(j1);
        this.joueur2 = new JoueurTMP(j2);
        this.idTroupe = 0;
    }

    public void run()
    {
        try { Thread.sleep(3500); } catch(Exception e) {}
        this.initialiser();

        this.joueur1.setTour(new Tour(this,10000,200, 2,10,this.joueur2));
        this.joueur1.getTour().start();
        this.plateau[2 ][10] = "R";
        
        this.joueur2.setTour(new Tour(this,10000,200,23,10,this.joueur1));
        this.joueur2.getTour().start();
        this.plateau[23][10] = "B";

        this.envoyerInfos();

        this.placer(20, 3, new Carte("tmp", "Commune", 1, 1, 1.0, 5.0, 1, 1, 1), this.joueur1);
        this.placer(3 , 3, new Carte("tmp", "Commune", 100000, 100000, 1.0, 2.0, 1, 1, 1), this.joueur2);

        synchronized (this)
        {
            try { this.wait(); } catch (InterruptedException e) {}
        }

        this.serveur.supprimerJeu(this);
    }

    private void initialiser()
    {
        for (int i = 0; i < this.plateau.length; i++)
            for (int j = 0; j < this.plateau[0].length; j++)
                this.plateau[i][j] = "";
    }

    public synchronized boolean placer (int lig, int col, Carte carte, JoueurTMP j)
 	{
		if (this.plateau[lig][col].equals(""))
        {
            this.alTroupes.add(new Troupe(this,carte,this.idTroupe++,lig,col, j));
            this.plateau[lig][col] = "" + this.alTroupes.get(this.alTroupes.size()-1).getTID();
            this.alTroupes.get(this.alTroupes.size()-1).start();
            this.envoyerInfos();
            return true;
        }

        return false;
 	}

    public synchronized void mourir(Troupe t)
    {
        this.plateau[t.getPosX()][t.getPosY()] = "";
        this.alTroupes.remove(t);

        this.envoyerInfos();
    }

    public synchronized void exploser(Tour t)
    {
        this.plateau[t.getPosX()][t.getPosY()] = "";
        this.envoyerInfos();
        try { sleep(500); } catch(Exception e) {}

        try
        {
            while (true)
            {
                Troupe tmp = this.alTroupes.get(0);
                this.plateau[tmp.getPosX()][tmp.getPosY()] = "";
                tmp.interrupt();
                this.alTroupes.remove(tmp);
                this.envoyerInfos();
                try { sleep(200); } catch(Exception e) {}
            }
        } catch (Exception e) {}

        t.getAdv().getTour().interrupt();

        if (t.getAdv().equals(this.joueur1))
        {
            this.joueur2.getJoueur().getSortie().println("Défaite");
            this.joueur2.getJoueur().ajouterOr(10);
            this.joueur1.getJoueur().getSortie().println("Victoire");
            this.joueur1.getJoueur().ajouterOr(20);
            this.joueur1.getJoueur().ajouterRandomCoffre();
        }
        else
        {
            this.joueur1.getJoueur().getSortie().println("Défaite");
            this.joueur1.getJoueur().ajouterOr(10);
            this.joueur2.getJoueur().getSortie().println("Victoire");
            this.joueur2.getJoueur().ajouterOr(20);
            this.joueur2.getJoueur().ajouterRandomCoffre();
        }

        synchronized (this)
        {
            this.notify();
        }
    }

    public Troupe trouverEnnemiProche(int posX, int posY,JoueurTMP adv)
    {
        int tmpX = posX-1, tmpY = posY;
        Troupe t;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }
        
        tmpX = posX+1; tmpY = posY;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }
        
        tmpX = posX+1; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+1; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+1; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+2; tmpY = posY;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-2; tmpY = posY;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY-2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+1; tmpY = posY-2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY-2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+1; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-2; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-2; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+2; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+2; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-2; tmpY = posY-2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-2; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+2; tmpY = posY+2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+2; tmpY = posY-2;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+3; tmpY = posY;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-3; tmpY = posY;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY+3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX; tmpY = posY-3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY-3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }
        
        tmpX = posX+1; tmpY = posY-3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+1; tmpY = posY+3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-1; tmpY = posY+3;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+3; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX+3; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-3; tmpY = posY+1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        tmpX = posX-3; tmpY = posY-1;
        if (tmpX >= 0 && tmpY >= 0 && tmpX <= 24 && tmpY <= 24)
        {
            t = this.scannerTableau(tmpX, tmpY, adv);
            if (t != null) return t;
        }

        return null;
    }

    private Troupe scannerTableau(int tmpX, int tmpY, JoueurTMP adv)
    {
        if (this.estIndice(this.plateau[tmpX][tmpY]))
        {
            int id = Integer.parseInt(this.plateau[tmpX][tmpY]);
            if (!this.getTroupeId(id).getAdv().equals(adv)) return this.getTroupeId(id);
        }
        return null;
    }

    private Troupe getTroupeId(int id)
    {
        for (Troupe t : alTroupes)
        {
            if (t.getTID() == id) return t;
        }
        return null;
    }

    private boolean estIndice(String test)
    {
        try
        {
            Integer.parseInt(test);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void deplacer(int x, int y, Troupe t)
    {
        if (this.plateau[t.getPosX()+x][t.getPosY()+y].equals(""))
        {
            this.plateau[t.getPosX()][t.getPosY()] = "";
            this.plateau[t.getPosX()+x][t.getPosY()+y] = "" + t.getTID();
            t.setPosX(t.getPosX()+x);
            t.setPosY(t.getPosY()+y);
        }
        else System.out.println(t.getTID() + " Ne peut pas se déplacer");
        this.envoyerInfos();
    }

    public void envoyerInfos()
    {
        String affichage = "";
        affichage += "|";
        for (int i = 0; i < this.plateau[0].length; i++)
            affichage += "-";
        affichage += "|\n";

        for (int i = 0; i < this.plateau.length; i++)
        {
            affichage += "|";
            for(int j = 0; j < this.plateau[0].length; j++)
                affichage += String.format("%1s",this.plateau[i][j]);
            affichage += "|\n";
        }

        affichage += "|";
        for (int i = 0; i < this.plateau[0].length; i++)
            affichage += "-";
        affichage += "|\n";

        this.joueur1.getJoueur().getSortie().println(affichage);
        this.joueur2.getJoueur().getSortie().println(affichage);
    }
}