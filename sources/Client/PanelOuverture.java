package sources.Client;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.util.ArrayList;

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

        for (CarteTmp ct : ancCar) if (ct.getRarete().equals("commune")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : newCar) if (ct.getRarete().equals("commune")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : ancCar) if (ct.getRarete().equals("rare")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : newCar) if (ct.getRarete().equals("rare")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : ancCar) if (ct.getRarete().equals("épique")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : newCar) if (ct.getRarete().equals("épique")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : ancCar) if (ct.getRarete().equals("légendaire")) this.affichageContenuCoffre(ct);
        for (CarteTmp ct : newCar) if (ct.getRarete().equals("légendaire")) this.affichageContenuCoffre(ct);
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
            case "rare"       : if (tpsAttendu >= 750) return;
                                try { Thread.sleep(tempsAttendre); } catch (Exception e) {};
                                this.animation(ct,tpsAttendu+tempsAttendre);
                                break;
            case "épique"     : if (tpsAttendu >= 4750) return;
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