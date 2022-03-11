package sources;

import java.util.ArrayList;
import sources.Reseau.Joueur;

public class Jeu extends Thread
{
    private String[][] plateau;
    private ArrayList<Troupe> alTroupes;
	private JoueurTMP joueur1;
    private JoueurTMP joueur2;
    private int idTroupe;

    public Jeu(Joueur j1, Joueur j2)
    {
        this.plateau = new String[25][19];
        this.alTroupes = new ArrayList<Troupe>();
        this.joueur1 = new JoueurTMP(j1);
        this.joueur2 = new JoueurTMP(j2);
        this.idTroupe = 0;
    }

    public void run()
    {
        try { Thread.sleep(3000); } catch(Exception e) {}
        this.initialiser();
        this.plateau[2 ][10] = "TourJ1";
        this.joueur1.setTourPos(2,10);
        this.plateau[23][10] = "TourJ2";
        this.joueur2.setTourPos(23,10);
        this.envoyerInfos();

        this.placer(20, 3, new Carte("tmp", "Commune", 1, 1, 1.0, 1, 1, 1), this.joueur1.getJoueur());
        this.placer(3 , 3, new Carte("tmp", "Commune", 1, 1, 1.0, 1, 1, 1), this.joueur2.getJoueur());
    }

    private void initialiser()
    {
        for (int i = 0; i < this.plateau.length; i++)
            for (int j = 0; j < this.plateau[0].length; j++)
                this.plateau[i][j] = "";
    }

    public synchronized boolean placer (int lig, int col, Carte carte, Joueur j)
 	{
		if (this.plateau[lig][col].equals(""))
        {
            this.alTroupes.add(new Troupe(this,carte,this.idTroupe++,lig,col, this.getJoueurTMP(j)));
            this.plateau[lig][col] = "" + this.alTroupes.get(this.alTroupes.size()-1).getTID();
            this.alTroupes.get(this.alTroupes.size()-1).start();
            this.envoyerInfos();
            return true;
        }

        return false;
 	}

    public synchronized void mourir(int id)
    {
        for (Troupe t : alTroupes)
        {
            System.out.println(t.getTID() + " " + id);
            if (t.getTID() == id)
            {
                this.plateau[t.getPosX()][t.getPosY()] = "";
                this.alTroupes.remove(t);
                break;
            }
        }
        this.envoyerInfos();
    }

    public Troupe trouverEnnemiProche(Troupe troupe,JoueurTMP adv)
    {
        int posX = troupe.getPosX();
        int posY = troupe.getPosY();

        int tmpX = posX-1, tmpY = posY;
        Troupe t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;
        
        tmpX = posX+1; tmpY = posY;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;
        
        tmpX = posX+1; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+1; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+1; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+2; tmpY = posY;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-2; tmpY = posY;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY-2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+1; tmpY = posY-2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY-2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+1; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-2; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-2; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+2; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+2; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-2; tmpY = posY-2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-2; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+2; tmpY = posY+2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+2; tmpY = posY-2;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+3; tmpY = posY;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-3; tmpY = posY;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY+3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX; tmpY = posY-3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY-3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;
        
        tmpX = posX+1; tmpY = posY-3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+1; tmpY = posY+3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-1; tmpY = posY+3;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+3; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX+3; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-3; tmpY = posY+1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

        tmpX = posX-3; tmpY = posY-1;
        t = this.scannerTableau(tmpX, tmpY, adv);
        if (t != null) return t;

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
        {
            affichage += "-";
        }
        affichage += "|\n";

        for (int i = 0; i < this.plateau.length; i++)
        {
            affichage += "|";
            for(int j = 0; j < this.plateau[0].length; j++)
            {
                affichage += String.format("%1s",this.plateau[i][j]);
            }
            affichage += "|\n";
        }

        affichage += "|";
        for (int i = 0; i < this.plateau[0].length; i++)
        {
            affichage += "-";
        }
        affichage += "|\n";

        this.joueur1.getJoueur().getSortie().println(affichage);
        this.joueur2.getJoueur().getSortie().println(affichage);
    }

    public JoueurTMP getJoueurTMP(Joueur j)
    {
        if (j.equals(joueur1.getJoueur())) return joueur1;
        else return joueur2;
    }
}