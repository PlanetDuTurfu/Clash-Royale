package sources.Client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class PanelCarte extends JPanel{
    private Image img;
    public PanelCarte(String[] carte, Connexion c)
    {
        this.setLayout(new FlowLayout());
        this.img = new ImageIcon(new ImageIcon("./data/img/Animation_carte.gif").getImage().getScaledInstance(290, 800, Image.SCALE_SMOOTH)).getImage();
        this.add(new PanelImage(carte, c));
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
}

class PanelImage extends JPanel
{
    public PanelImage(String[] attributs, Connexion c)
    {
        if (attributs[1].contains("Commune")) attributs[1] = "Commune";
        if (attributs[1].contains("Rare")) attributs[1] = "Rare";
        if (attributs[1].contains("Epique")) attributs[1] = "Épique";
        if (attributs[1].contains("Légendaire")) attributs[1] = "Légendaire";
        this.setLayout(new GridLayout(2,1));
        this.add(new JLabel(new ImageIcon(new ImageIcon("./data/img/" + attributs[0] + ".gif").getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT))));
        this.add(new PanelStats(attributs,c));
        this.setOpaque(false);
    }
}

class PanelStats extends JPanel
{
    public PanelStats(String[] attributs, Connexion c)
    {
        this.setLayout(new GridLayout(4,1));
        this.add(new Panel1(attributs[0],attributs[1]));
        this.add(new Panel2(attributs[2],attributs[3]));
        this.add(new Panel3(attributs[4],attributs[5],attributs[6]));
        this.add(new Panel4(attributs[0],c));
        this.setOpaque(false);
    }
}

class Panel1 extends JPanel
{
    private JLabel lblRarete;
    public Panel1(String nom, String rarete)
    {
        this.setLayout(new FlowLayout());
        this.add(new JLabel(nom));
        lblRarete = new JLabel(rarete);
        if (rarete.equals("Commune")) lblRarete.setForeground(Color.GRAY);
        if (rarete.equals("Rare")) lblRarete.setForeground(Color.RED);
        if (rarete.equals("Épique")) lblRarete.setForeground(Color.MAGENTA);
        if (rarete.equals("Légendaire")) lblRarete.setForeground(Color.CYAN);
        this.add(lblRarete);
        this.setOpaque(false);
    }
}

class Panel2 extends JPanel
{
    private JProgressBar jpb;
    public Panel2(String niveau, String doublons)
    {
        this.setLayout(new FlowLayout());
        this.add(new JLabel("Niveau " + niveau));
        int total = 0;
        switch (Integer.parseInt(niveau))
        {
            case 1 : total = 2; break;
            case 2 : total = 4; break;
            case 3 : total = 8; break;
            case 4 : total = 16; break;
            case 5 : total = 32; break;
            case 6 : total = 64; break;
            case 7 : total = 128; break;
            case 8 : total = 256; break;
            case 9 : total = 512; break;
        }
        this.add(new JLabel("("+doublons + "/" + total+")"));
        this.jpb = new JProgressBar(JProgressBar.HORIZONTAL,0,total);
        this.jpb.setValue(Integer.parseInt(doublons));
        if (Integer.parseInt(doublons) > total)
        {
            this.jpb.setString("+"+(Integer.parseInt(doublons)-total));
            this.jpb.setStringPainted(true);
        }
        this.add(this.jpb);
        this.setOpaque(false);
    }
}

class Panel3 extends JPanel
{
    public Panel3(String pv, String deg, String vit_att)
    {
        this.setLayout(new FlowLayout());
        this.add(new JLabel(pv + "PV"));
        this.add(new JLabel(deg + "dégats"));
        this.add(new JLabel(vit_att + "vitesse d'attaque"));
        this.setOpaque(false);
    }
}

class Panel4 extends JPanel implements ActionListener
{
    private Connexion c;
    private String nom;
    public Panel4(String nom, Connexion c)
    {
        this.c = c;
        this.nom = nom;

        JButton btnLevelUp = new JButton(new ImageIcon(new ImageIcon("./data/img/Level Up.jpg").getImage().getScaledInstance(100, 33, Image.SCALE_DEFAULT)));
        btnLevelUp.setSize(new Dimension(100,33));
        this.add(btnLevelUp);
        btnLevelUp.addActionListener(this);
        this.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        c.ecrire("am  "+nom);
    }
}