package sources.Client;

import javax.swing.*;
import java.awt.*;

public class PanelCoffre extends JPanel {
    public PanelCoffre(String coffre)
    {
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        JLabel tmpLabel = new JLabel(new ImageIcon(new ImageIcon("./data/img/" + coffre + ".jpg").getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT)));
        this.add(tmpLabel);
    }
}
