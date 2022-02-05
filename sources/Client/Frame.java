package sources.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Frame extends JFrame {
    private PanelBase pnlBase;
    private JScrollBar jsb;

    public Frame(Connexion c)
    {
        this.setTitle("Clash de baisé !");
		this.setLocation(0,0);
        this.setSize(1080,1080);
        this.setLayout(new FlowLayout());
        this.pnlBase = new PanelBase(c, this);
        this.add(this.pnlBase);

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setVisible(true);
    }

    public void setFrameRegister()
    {
        this.pnlBase.setFrameRegister();
    }

    public void setFrameTo(String msg)
    {
        if ( this.jsb != null ) this.remove(this.jsb);

        this.pnlBase.setFrameTo(msg);
        this.jsb = new JScrollBar(JScrollBar.VERTICAL);
        this.add(this.jsb);
        this.jsb.addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Frame.this.pnlBase.afficherLigne(jsb.getValue());
                Frame.this.repaint();
            }  
        });
    }

    public void setFrameAccueil()
    {
        this.pnlBase.setFrameAccueil();
    }
}

class PanelBase extends JPanel implements ActionListener {
    private Connexion c;
    private PanelRegister pnlReg;
    private PanelAccueil pnlAcc;
    private PanelTo pnlTo;
    private JButton btnTri;
    private Frame frm;
    private Image img;

    public PanelBase(Connexion c, Frame frm)
    {
        this.c = c;
        this.frm = frm;
        this.setLayout(new FlowLayout());
    }

    public void setFrameTo(String msg)
    {
        if ( this.pnlAcc != null ) this.remove(this.pnlAcc);
        if ( this.pnlTo  != null ) this.remove(this.pnlTo );
        if ( this.btnTri != null ) this.remove(this.btnTri);

        this.img = new ImageIcon("./data/img/fond.gif").getImage();

        String triActuel = "Trié par " + msg.split("#")[0];
        msg = msg.substring(msg.split("#")[0].length()+1);
        
        this.frm.setTitle("Clash de baisé ! - Inventaire");
        
        this.btnTri = new JButton(triActuel);
        this.pnlTo = new PanelTo(msg.split("#"), this.c);

        this.pnlTo.setOpaque(false);
        
        this.add(this.btnTri, BorderLayout.NORTH);
        this.add(this.pnlTo);

        this.btnTri.addActionListener(this); 
    }

    public void afficherLigne(int value)
    {
        this.pnlTo.afficherLigne(value);
    }

    public void setFrameRegister()
    {
        this.frm.setTitle("Clash de baisé ! - Connexion");
        this.pnlReg = new PanelRegister(this.c);
        this.add(this.pnlReg);
    }

    public void setFrameAccueil()
    {
        this.remove(this.pnlReg);
        this.frm.setTitle("Clash de baisé ! - Acceuil");
        this.pnlAcc = new PanelAccueil(this.c);
        this.add(this.pnlAcc);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnTri)
        {
            this.c.ecrire("nextTri");
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(this.img, 0, 0, this);
    }
}

class PanelRegister extends JPanel implements ActionListener {
    private JTextField txtPseudo;
    private JTextField txtMdp;
    private JButton btnValider;
    private Connexion c;

    public PanelRegister(Connexion c)
    {
        this.c = c;
        this.txtPseudo = new JTextField(20);
        this.add(new JLabel("Pseudo : "));
        this.add(this.txtPseudo);
        this.txtMdp = new JTextField(20);
        this.add(new JLabel("Mot de passe : "));
        this.add(this.txtMdp);
        this.btnValider = new JButton("Se connecter/S'inscrire");
        this.add(this.btnValider);
        this.btnValider.addActionListener(this);
        System.out.println("print");
    }

    public void actionPerformed(ActionEvent e)
    {
        this.c.ecrire(this.txtPseudo.getText());
        try { Thread.sleep(500); } catch(Exception ex) {}
        this.c.ecrire(this.txtMdp.getText());
    }
}

class PanelTo extends JPanel implements ActionListener{
    private Connexion c;
    private int nbLigne;
    private ArrayList<PanelCarte> pnlCartes = new ArrayList<PanelCarte>();
    public PanelTo(String[] msg, Connexion c)
    {
        this.c = c;
        this.nbLigne = 0;
        int nbCarte = 0;
        this.setLayout(new GridLayout(msg.length/6+1,6,5,5));
        for (String s : msg)
        {
            this.pnlCartes.add(new PanelCarte(s.split("¤"), this.c, this.nbLigne));
            this.add(this.pnlCartes.get(this.pnlCartes.size()-1));
            this.nbLigne = ++nbCarte / 6;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        this.c.ecrire("nextTri");
    }

    public int getNbLigne() { return this.nbLigne; }

    public void afficherLigne(int ligne)
    {
        for(PanelCarte pc : this.pnlCartes)
            this.remove(pc);
        for(PanelCarte pc : this.pnlCartes) if (pc.getNumLigne() >= ligne) this.add(pc);
    }
}

class PanelAccueil extends JPanel implements ActionListener {
    private Connexion c;
    private JButton btnCartes;
    private JButton btnCoffres;
    public PanelAccueil(Connexion c)
    {
        this.c = c;

        this.btnCartes  = new JButton("Cartes");
        this.btnCoffres = new JButton("Coffres");
        this.btnCartes .addActionListener(this);
        this.btnCoffres.addActionListener(this);
        this.add(this.btnCartes );
        this.add(this.btnCoffres);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnCoffres)
        {
            this.c.ecrire("co");
        }
        if (e.getSource() == this.btnCartes)
        {
            this.c.ecrire("to");
        }
    }
}