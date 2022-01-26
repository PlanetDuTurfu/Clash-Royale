package sources;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Cartes {
    private ArrayList<Carte> alCarteCommune = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteRare = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteEpique = new ArrayList<Carte>();
    private ArrayList<Carte> alCarteLegendaire = new ArrayList<Carte>();

    public Cartes()
    {
        this.creerCartes();
    }

    private void creerCartes()
    {
        String[] tab = new String[4];
        tab[0] = "Commune";
        tab[1] = "Rare";
        tab[2] = "Epique";
        tab[3] = "Légendaire";

        for (String s : tab)
        {
            try {
                Scanner sc = new Scanner(new FileInputStream("./data/Cartes"+s+".data"));
                while (sc.hasNextLine())
                {
                    String[] l = sc.nextLine().split("\t");
                    if (s.equals("Commune"   )) alCarteCommune   .add(new Carte(l[0], "\033[90m"+s+"\033[0m", Integer.parseInt(l[1]), Integer.parseInt(l[2]), Double.parseDouble(l[3]),1));
                    if (s.equals("Rare"      )) alCarteRare      .add(new Carte(l[0], "\033[91m"+s+"\033[0m", Integer.parseInt(l[1]), Integer.parseInt(l[2]), Double.parseDouble(l[3]),1));
                    if (s.equals("Epique"    )) alCarteEpique    .add(new Carte(l[0], "\033[95m"+s+"\033[0m", Integer.parseInt(l[1]), Integer.parseInt(l[2]), Double.parseDouble(l[3]),1));
                    if (s.equals("Légendaire")) alCarteLegendaire.add(new Carte(l[0], "\033[36m"+s+"\033[0m", Integer.parseInt(l[1]), Integer.parseInt(l[2]), Double.parseDouble(l[3]),1));
                }
                sc.close();
            } catch(Exception e) {}

        }
    }
    public ArrayList<Carte> getCartesCommune    () { return alCarteCommune   ; }
    public ArrayList<Carte> getCartesRare       () { return alCarteRare      ; }
    public ArrayList<Carte> getCartesEpique     () { return alCarteEpique    ; }
    public ArrayList<Carte> getCartesLegendaire () { return alCarteLegendaire; }
}
