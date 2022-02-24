package sources.Client;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Frame extends JFrame implements ActionListener {
    private Connexion c;
    private PanelRegister pnlReg;
    private PanelAccueil pnlAcc;
    private PanelFondTo pnlFTo;
    private PanelCoffres pnlCof;
    private JButton btnRetour;

    public Frame(Connexion c)
    {
        this.c = c;
        this.setTitle("Clash de baisé !");
		this.setLocation(0,0);
        this.setSize(1460,820);
        
        this.setLayout(new BorderLayout());
        this.setContentPane(new JLabel(new ImageIcon(new ImageIcon("./data/img/fond_de_baise.gif").getImage().getScaledInstance(1450, 820, Image.SCALE_DEFAULT))));
        this.setLayout(new FlowLayout());
        
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setVisible(true);
    }

    public void setFrameTo(String msg)
    {
        if ( this.pnlAcc != null ) this.remove(this.pnlAcc);
        if ( this.pnlFTo != null ) this.remove(this.pnlFTo);

        this.setTitle("Clash de baisé ! - Inventaire");
        this.pnlFTo = new PanelFondTo(msg, this.c);
        this.add(this.pnlFTo);
        this.pnlFTo.setOpaque(false);
        this.actualiser();
    }

    public void setFrameRegister()
    {
        this.setTitle("Clash de baisé ! - Connexion");
        this.pnlReg = new PanelRegister(this.c);
        this.add(this.pnlReg);
        this.pnlReg.setOpaque(false);
        this.actualiser();
    }

    public void setFrameAccueil()
    {
        if (this.pnlFTo != null) this.remove(this.pnlFTo);
        if (this.pnlCof != null) this.remove(this.pnlCof);
        if (this.pnlReg != null) this.remove(this.pnlReg);
        if (this.btnRetour != null) this.remove(this.btnRetour);

        this.setTitle("Clash de baisé ! - Acceuil");
        this.pnlAcc = new PanelAccueil(this.c);
        this.add(this.pnlAcc);
        this.actualiser();
    }

    public void setFrameCoffre(String msg)
    {
        if (this.pnlAcc != null) this.remove(this.pnlAcc);
        if (this.pnlCof != null) this.remove(this.pnlCof);
        this.setTitle("Clash de baisé ! - Coffres");
        this.pnlCof = new PanelCoffres(msg, this.c);
        this.pnlCof.setOpaque(false);
        this.add(this.pnlCof);
        this.actualiser();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnRetour) this.c.ecrire("accueil");
    }

    public void actualiser()
    {
        this.setSize(1449,820);
        this.setSize(1450,820);
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
    }

    public void actionPerformed(ActionEvent e)
    {
        this.c.ecrire(this.txtPseudo.getText());
        try { Thread.sleep(500); } catch(Exception ex) {}
        this.c.ecrire(this.txtMdp.getText());
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

        this.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnCoffres) this.c.ecrire("cos");
        if (e.getSource() == this.btnCartes ) this.c.ecrire("to 0");
    }
}

class PanelCoffres extends JPanel implements ActionListener {
    private JButton btnOuvrir;
    private JButton btnRet;
    private Connexion c;
    public PanelCoffres(String msg, Connexion c)
    {
        this.setLayout(new BorderLayout());
        this.setSize(1450,820);
        this.c = c;
        this.btnRet    = new JButton("Retour");
        this.btnOuvrir = new JButton("Ouvrir ce coffre");
        
        this.add(this.btnOuvrir, BorderLayout.NORTH);
        this.add(this.btnRet, BorderLayout.SOUTH);
        this.add(new PanelP2(msg), BorderLayout.CENTER);

        this.btnOuvrir.addActionListener(this);
        this.btnRet.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnOuvrir)
        {
            this.c.ecrire("co");
        }

        if (e.getSource() == this.btnRet)
        {
            this.c.ecrire("accueil");
        }
    }
}

class PanelP2 extends JPanel {
    public PanelP2(String msg)
    {
        this.setOpaque(false);
        this.setLayout(new GridLayout(0,6,5,5));
        for (String s : msg.split("#")) this.add(new PanelCoffre(s));
    }
}

class PanelFondTo extends JPanel {
    private Connexion c;
    private JPanel pnlTo;
    
    public PanelFondTo(String msg, Connexion c)
    {
        this.c = c;
        this.setLayout(new BorderLayout());

        this.add(new PanelBoutonTo(msg,c), BorderLayout.NORTH);
        String tmpmsg = msg.split("#")[0];
        tmpmsg += msg.substring(msg.split("#")[0].length() + 1 + msg.split("#")[1].length());
        msg = tmpmsg;

        this.pnlTo = new PanelTo(msg.substring(msg.split("#")[0].length()+1).split("#"), this.c);
        this.add(this.pnlTo);
        
        this.pnlTo.setOpaque(false);
    }
}

class PanelTo extends JPanel {
    private Connexion c;
    private ArrayList<PanelCarte> pnlCartes = new ArrayList<PanelCarte>();
    
    public PanelTo(String[] msg, Connexion c)
    {
        this.c = c;
        this.setLayout(new GridLayout(0,5,5,5));
        System.out.println(msg.length);
        if (msg.length == 1 && msg[0].equals("")) return;
        for (String s : msg)
        {
            this.pnlCartes.add(new PanelCarte(s.split("¤"), this.c));
            this.add(this.pnlCartes.get(this.pnlCartes.size()-1));
        }
    }
}
class PanelBoutonTo extends JPanel implements ActionListener {
    private Connexion c;
    private JButton btnTri;
    private JButton btnRetour;
    private JButton btnLigneSuiv;
    private JButton btnLignePrec;

    public PanelBoutonTo(String msg, Connexion c)
    {
        this.c = c;

        String triActuel = "Trié par " + msg.split("#")[0];

        this.btnTri = new JButton(triActuel);
        this.btnLigneSuiv = new JButton("Ligne suivante");
        this.btnLignePrec = new JButton("Ligne précédente");
        this.btnRetour = new JButton("Retour");

        this.add(new JLabel( msg.split("#")[1] +" d'or "));
        this.add(this.btnLignePrec, BorderLayout.NORTH);
        this.add(this.btnTri      , BorderLayout.NORTH);
        this.add(this.btnLigneSuiv, BorderLayout.NORTH);
        this.add(this.btnRetour   , BorderLayout.NORTH);

        this.btnTri.addActionListener(this);
        this.btnLignePrec.addActionListener(this);
        this.btnLigneSuiv.addActionListener(this);
        this.btnRetour.addActionListener(this);
        
        this.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnTri      ) this.c.ecrire("nextTri");
        if (e.getSource() == this.btnLignePrec) this.c.ecrire("to -");
        if (e.getSource() == this.btnLigneSuiv) this.c.ecrire("to +");
        if (e.getSource() == this.btnRetour   ) this.c.ecrire("accueil");
    }
}