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
    private PanelOuverture pnlOuv;
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

    public void setFrameOuverture(String msg)
    {
        if (this.pnlCof != null) this.remove(this.pnlCof);

        this.setTitle("Clash de baisé ! - Ouverture");
        this.pnlOuv = new PanelOuverture(msg, this);
        this.pnlOuv.setOpaque(false);
        this.add(this.pnlOuv);
        this.pnlOuv.afficher();
        this.remove(this.pnlOuv);
        this.actualiser();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnRetour) this.c.ecrire("accueil");
    }

    public void actualiser()
    {
        this.setSize(1459,820);
        this.setSize(1460,820);
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
    private JButton btnRet;
    private Connexion c;
    public PanelCoffres(String msg, Connexion c)
    {
        this.setLayout(new BorderLayout());
        this.setSize(1450,820);
        
        this.c = c;
        this.btnRet = new JButton("Retour");
        
        this.add(this.btnRet, BorderLayout.NORTH);
        this.add(new PanelP2(msg, c), BorderLayout.CENTER);

        this.btnRet.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
            this.c.ecrire("accueil");
    }
}

class PanelP2 extends JPanel {
    public PanelP2(String msg, Connexion c)
    {
        this.setOpaque(false);
        this.setLayout(new GridLayout(0,5,5,5));
        if (msg.equals("")) return;
        msg = msg.substring(1);
        for (String s : msg.split("#")) this.add(new PanelCoffre(s,c));
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

class PanelOuverture extends JPanel implements MouseListener {
    private Frame frm;
    private String msg;
    private boolean passerAnim;

    public PanelOuverture(String msg, Frame frm)
    {
        this.setLayout(new GridLayout(2,1));
        this.msg = msg;
        this.frm = frm;
        this.addMouseListener(this);
    }

    public void afficher()
    {
        ArrayList<CarteTmp> ancCar = new ArrayList<CarteTmp>();
        ArrayList<CarteTmp> newCar = new ArrayList<CarteTmp>();
        
        for (String s : msg.split("#"))
        {
            boolean tmp = false;
            if (s.split("¤")[2].equals("true"))
            {
                for (CarteTmp ct : newCar)
                    if (ct.getNom().equals(s.split("¤")[0]))
                    {
                        tmp = true;
                        ct.addDoublon();
                    }
                if (!tmp) newCar.add(new CarteTmp(s.split("¤")[0], s.split("¤")[1]));
            }
            else
            {
                for (CarteTmp t : newCar)
                    if (s.split("¤")[0].equals(t.getNom()))
                    {
                        t.addDoublon();
                        tmp = true;
                        break;
                    }
                
                if (!tmp)
                {
                    for (CarteTmp t : ancCar)
                    {
                        if (t.getNom().equals(s.split("¤")[0]))
                        {
                            t.addDoublon();
                            tmp = true;
                            break;
                        }
                    }
                }
                if (!tmp) ancCar.add(new CarteTmp(s.split("¤")[0], s.split("¤")[1]));
            }
        }

        // Animation de début d'ouverture du coffre
        JLabel jl = new JLabel(new ImageIcon(new ImageIcon("./data/img/Animation_carte.gif").getImage().getScaledInstance(1460, 820, Image.SCALE_DEFAULT)));
        this.add(jl);
        frm.actualiser();
        try { Thread.sleep(1000); } catch (Exception e) {}
        this.remove(jl);

        for (CarteTmp ct : ancCar)
            this.affichageContenuCoffre(ct);

        for (CarteTmp ct : newCar)
            this.affichageContenuCoffre(ct);
    }

    private void affichageContenuCoffre(CarteTmp ct)
    {
        JLabel jl;
        // Animation ouverture rareté
        this.passerAnim = false;
        jl = new JLabel(new ImageIcon(new ImageIcon("./data/img/Animation_carte_"+ct.getRarete()+".gif").getImage().getScaledInstance(1460, 820, Image.SCALE_DEFAULT)));
        this.add(jl);
        frm.actualiser();
        this.animation(ct, 0);
        this.remove(jl);

        // Affichage de la carte obtenue
        jl = new JLabel(new ImageIcon(new ImageIcon("./data/img/"+ct.getNom()+".gif").getImage().getScaledInstance(750, 750, Image.SCALE_DEFAULT)));
        JLabel lblNom = new JLabel(ct.getNom() + " x" + ct.getDoublons());
        this.add(jl);
        this.add(lblNom);
        frm.actualiser();
        synchronized (this) { try{this.wait();}catch(Exception e){} }
        this.remove(lblNom);
        this.remove(jl);
    }

    private void animation(CarteTmp ct, int tpsAttendu)
    {
        if (this.passerAnim) return;
        int tempsAttendre = 20;
        switch (ct.getRarete())
        {
            case "commune"    : if (tpsAttendu >= 750) return;
                                try { Thread.sleep(tempsAttendre); } catch (Exception e) {};
                                this.animation(ct, tpsAttendu+tempsAttendre);
                                break;
            case "rare"       : if (tpsAttendu >= 1000) return;
                                try { Thread.sleep(tempsAttendre); } catch (Exception e) {};
                                this.animation(ct,tpsAttendu+tempsAttendre);
                                break;
            case "épique"     : if (tpsAttendu >= 5500) return;
                                try { Thread.sleep(tempsAttendre); } catch (Exception e) {};
                                this.animation(ct,tpsAttendu+tempsAttendre);
                                break;
            case "légendaire" : if (tpsAttendu >= 3000) return;
                                try { Thread.sleep(tempsAttendre); } catch (Exception e) {};
                                this.animation(ct,tpsAttendu+tempsAttendre);
                                break;
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseExited (MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e)
    {
        this.passerAnim = true;
        synchronized (this) { this.notify(); }
    }
    public void mouseClicked(MouseEvent e) {}
}

class CarteTmp {
    private int nbDoublons;
    private String nom;
    private String rarete;

    public CarteTmp(String nom, String rarete)
    {
        this.nom = nom;
        this.nbDoublons = 1;
        if (rarete.contains("Rare")) this.rarete = "rare";
        if (rarete.contains("Commun")) this.rarete = "commune";
        if (rarete.contains("Epique")) this.rarete = "épique";
        if (rarete.contains("Légendaire")) this.rarete = "légendaire";
    }

    public void addDoublon()
    {
        this.nbDoublons++;
    }

    public int getDoublons()
    {
        return this.nbDoublons;
    }

    public String getNom()
    {
        return this.nom;
    }

    public String getRarete()
    {
        return this.rarete;
    }
}