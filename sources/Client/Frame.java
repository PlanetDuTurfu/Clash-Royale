package sources.Client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;

public class Frame extends JFrame implements ActionListener {
    private Connexion c;
    private PanelRegister pnlReg;
    private PanelAccueil pnlAcc;
    private JButton btnTri;
    public Frame(Connexion c)
    {
        this.c = c;

        this.setTitle("Clash de baisé !");
		this.setLocation(0,0);
        this.setSize(1920,1080);
        this.setLayout(new FlowLayout());

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setVisible(true);
        
    }

    public void setFrameTo(String msg)
    {
        this.remove(this.pnlAcc);
        this.setTitle("Clash de baisé ! - Inventaire");
        this.btnTri = new JButton("Trier");
        this.add(this.btnTri, BorderLayout.NORTH);
        this.btnTri.addActionListener(this);
        // this.btnTri.setPreferredSize(new Dimension(50,25));
        PanelTo pnl = new PanelTo(msg.split("#"), this.c);
        // this.add(pnl);
        this.add(new JScrollPane(pnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    }

    public void setFrameRegister()
    {
        this.setTitle("Clash de baisé ! - Connexion");
        this.pnlReg = new PanelRegister(this.c);
        this.add(this.pnlReg);
    }

    public void setFrameAccueil()
    {
        this.remove(this.pnlReg);
        this.setTitle("Clash de baisé ! - Acceuil");
        this.pnlAcc = new PanelAccueil(this.c);
        this.add(this.pnlAcc);
        this.revalidate();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnTri)
        {
            this.c.ecrire("nextTri");
        }
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

class PanelTo extends JPanel implements ActionListener{
    private JButton btnTri;
    private Connexion c;
    public PanelTo(String[] msg, Connexion c)
    {
        this.c = c;
        this.setLayout(new GridLayout(4,7,5,5));
        Border lineborder = BorderFactory.createLineBorder(Color.black, 1);
        for (String s : msg)
        {
            PanelCarte tmp = new PanelCarte(s.split("¤"), c);
            tmp.setBorder(lineborder);
            this.add(tmp);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnTri)
        {
            System.out.println("ssooopp");
            this.c.ecrire("nextTri");
        }
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