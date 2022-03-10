package sources;

import java.util.ArrayList;
import sources.Reseau.Joueur;

public class Jeu
{
    private String[][] plateau;
    private ArrayList<Troupe> alTroupes;
	private JoueurTMP joueur1;
    private JoueurTMP joueur2;
    private int idTroupe;

    public Jeu(Joueur j1, Joueur j2)
    {
        this.plateau = new String[40][19];
        this.alTroupes = new ArrayList<Troupe>();
        this.joueur1 = new JoueurTMP(j1);
        this.joueur2 = new JoueurTMP(j2);
        this.idTroupe = 0;
    }

    public void lancerPartie(Joueur j1, Joueur j2)
    {
        this.initialiser();
        this.plateau[2 ][10] = "TourJ1";
        this.joueur1.setTourPos(2,10);
        this.plateau[38][10] = "TourJ2";
        this.joueur2.setTourPos(38,10);
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
            this.alTroupes.add(new Troupe(this,carte,idTroupe++,lig,col, new JoueurTMP(j)));
            this.alTroupes.get(this.alTroupes.size()).start();
            this.plateau[lig][col] = "" + this.alTroupes.get(this.alTroupes.size()).getId();
            return true;
        }
        
        return false;
 	}

    public synchronized void mourir(int id)
    {
        for (Troupe t : alTroupes)
        {
            if (t.getId() == id)
            {
                this.plateau[t.getPosX()][t.getPosY()] = "";
                this.alTroupes.remove(t);
                break;
            }
        }
    }

    public Troupe trouverEnnemiProche(Troupe troupe,JoueurTMP adv)
    {
        int posX = troupe.getPosX();
        int posY = troupe.getPosY();

        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                int tmpX = posX-i, tmpY = posY;
                Troupe t = this.scannerTableau(tmpX, tmpY, adv);
                if (t != null) return t;
            }
        }

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
            if (t.getId() == id) return t;
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
}