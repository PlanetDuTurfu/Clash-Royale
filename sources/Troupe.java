package sources;

public class Troupe extends Thread {
    private Jeu jeu;
    private int id;
    private int posX;
    private int posY;
    private JoueurTMP adv;
    private boolean boolVie;
    private Troupe cible;
    private int pv;
    private int deg;
    private double vit_att;

    public Troupe(Jeu jeu, Carte c, int id, int posX, int posY, JoueurTMP adv)
    {
        this.jeu = jeu;
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.adv = adv;
        this.pv = c.getPV();
        this.deg = c.getDeg();
        this.vit_att = c.getVitAtt();
    }

    public void run()
    {
        this.boolVie = true;
        try { sleep(1000); } catch(Exception e) {}
        while(this.boolVie)
        {
            this.cibler();
            if (this.peutFrapper()) this.frapper();
            else this.deplacer();
            System.out.println("Coordonnées : "+this.posX+";"+this.posY+" | cible : " + this.cible.getId() + " | Coos"+this.cible.getPosX()+";"+this.cible.getPosY());
        }
    }

    private void cibler()
    {
        this.cible = this.jeu.trouverEnnemiProche(this,this.adv);
    }

    private boolean peutFrapper()
    {
        if (this.cible != null)
        {
            int difX = this.cible.getPosX() - this.posX;
            int difY = this.cible.getPosY() - this.posY;
            if (difX > -2 && difX < 2 && difY > -2 && difY < 2) return true;
        }
        else
        {
            int difX = this.adv.getPosX() - this.posX;
            int difY = this.adv.getPosY() - this.posY;
            if (difX > -2 && difX < 2 && difY > -2 && difY < 2) return true;
        }
        return false;
    }

    private void frapper()
    {
        this.cible.seFaireFrapper(this.deg);
        try { sleep((int)(1000/this.vit_att)); } catch(Exception e) {}
    }

    private void seFaireFrapper(int deg)
    {
        this.pv -= deg;
        if (this.pv <= 0)
        {
            this.boolVie = false;
            this.jeu.mourir(this.id);
        }
    }

    private void deplacer()
    {
        if (this.cible == null)
        {
            
        }
    }

    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }

    public JoueurTMP getAdv() { return this.adv; }
}
