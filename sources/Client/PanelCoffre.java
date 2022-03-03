package sources.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelCoffre extends JPanel implements ActionListener{
    private JButton btn;
    private String name;
    private Connexion c;

    public PanelCoffre(String coffre, Connexion c)
    {
        this.c = c;
        this.name = coffre;
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.btn = new JButton(new ImageIcon(new ImageIcon("./data/img/" + coffre + ".jpg").getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT)));
        this.add(this.btn);
        this.btn.setOpaque(false);
        this.btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        this.c.ecrire("co  " + name);
    }
}
