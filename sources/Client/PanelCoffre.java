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
        this.setLayout(new FlowLayout());
        this.setBackground(Color.ORANGE);
        this.c = c;
        this.name = coffre;
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        this.btn = new JButton(coffre, new ImageIcon(new ImageIcon("./data/img/" + coffre + ".jpg").getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT)));
        // Texte sous l'image
		this.btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		// Texte centré
		this.btn.setHorizontalTextPosition(SwingConstants.CENTER);
        this.add(this.btn);
        this.btn.setOpaque(false);
        this.btn.setContentAreaFilled(false);
        this.btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        this.c.ecrire("co  " + name);
    }
}
