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
            int advPosX = this.adv.getPosX();
            int advPosY = this.adv.getPosY();
            int difX = advPosX - this.posX;
            int difY = advPosY - this.posY;

            if (difX > 0) this.direction(1, difY);
            else if (difX == 0) this.direction(0, difY);
            else this.direction(-1, difY);
        }
        else
        {
            int advPosX = this.cible.getPosX();
            int advPosY = this.cible.getPosY();
            int difX = advPosX - this.posX;
            int difY = advPosY - this.posY;

            if (difX > 0) this.direction(1, difY);
            else if (difX == 0) this.direction(0, difY);
            else this.direction(-1, difY);
        }

        try { sleep(1000); } catch(Exception e) {}
    }

    private void direction(int x, int difY)
    {
        if (difY > 0) this.jeu.deplacer(x, 1, this);
        else if (difY > 0) this.jeu.deplacer(x, 0, this);
        else this.jeu.deplacer(x, -1, this);
    }

    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public int getTID () { return this.id  ; }

    public void setPosX(int x) { this.posX = x; }
    public void setPosY(int y) { this.posY = y; }

    public JoueurTMP getAdv() { return this.adv; }
}