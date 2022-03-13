package sources;

public class Tour extends Thread {
    private Jeu jeu;
    private int pv;
    private int deg;
    private int posX;
    private int posY;
    private JoueurTMP adv;
    private boolean boolVie;
    private Troupe cible;

    public Tour(Jeu jeu, int pv, int deg, int posX, int posY, JoueurTMP adv)
    {
        this.jeu = jeu;
        this.pv = pv;
        this.deg = deg;
        this.posX = posX;
        this.posY = posY;
        this.adv = adv;
    }

    public void run()
    {
        this.boolVie = true;
        while (this.boolVie)
        {
            this.cible = this.jeu.trouverEnnemiProche(this.posX,this.posY,this.adv);
            if (this.cible != null) this.frapper();
            try { Thread.sleep(500); } catch(Exception e) {}
        }
    }

    private void frapper()
    {
        this.cible.seFaireFrapper(this.deg);
    }

    public void seFaireFrapper(int deg)
    {
        this.pv -= deg;
        if (this.pv <= 0)
        {
            this.boolVie = false;
            this.jeu.exploser(this);
        }
    }

    public void interrupt() { this.boolVie = false; }
    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public JoueurTMP getAdv() { return this.adv; }
}