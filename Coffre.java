package Sources;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Coffre {
    private String nom;
    private int nbOr;
    private int nbCommune;
    private int nbRare;
    private int nbEpique;
    private int nbLegendaire;

    public Coffre(String nom, int nbOr, int nbCommune, int nbRare, int nbEpique, int nbLegendaire)
    {
        this.nom = nom;
        this.nbOr = nbOr;
        this.nbCommune = nbCommune;
        this.nbRare = nbRare;
        this.nbEpique = nbEpique;
        this.nbLegendaire = nbLegendaire;
    }

    public String[] ouvrirCoffre()
    {
        int tmpCommune = this.nbCommune;
        int tmpRare = this.nbRare;
        int tmpEpique = this.nbEpique;
        int tmpLegendaire = this.nbLegendaire;
        String[] tabCartes = new String[tmpCommune + tmpRare + tmpEpique + tmpLegendaire];

        System.out.println("Cartes dans le coffre de base : \nCommune : " + tmpCommune + "\nRare : " + tmpRare + "\nEpique : " + tmpEpique + "\nLegendaire : " + tmpLegendaire);

        for (int i = 0; i < this.nbCommune; i++)
        {
            if ((int) (Math.random() * 100 + 1) > 99)
            {
                tmpCommune--;
                tmpLegendaire++;
            }
            else if ((int) (Math.random() * 100 + 1) > 97)
            {
                tmpCommune--;
                tmpEpique++;
            }
            else if ((int) (Math.random() * 100 + 1) > 93)
            {
                tmpCommune--;
                tmpRare++;
            }
        }

        System.out.println("Cartes dans le coffre après la transformation : \nCommune : " + tmpCommune + "\nRare : " + tmpRare + "\nEpique : " + tmpEpique + "\nLegendaire : " + tmpLegendaire);

        int nbCarteTiree = 0;
        int nbCarte = 0;

        while (nbCarteTiree != tmpCommune)
        {
            nbCarte = (int) (Math.random() * (tmpCommune - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Commune"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpRare)
        {
            nbCarte = (int) (Math.random() * (tmpRare - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Rare"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpEpique)
        {
            nbCarte = (int) (Math.random() * (tmpEpique - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Epique"), tabCartes);
            nbCarteTiree += nbCarte;
        }
        nbCarteTiree = 0;

        while (nbCarteTiree != tmpLegendaire)
        {
            nbCarte = (int) (Math.random() * (tmpLegendaire - nbCarteTiree) + 1);
            tabCartes = this.ajouterAuTableau(nbCarte, this.carteAuHasard("Legendaire"), tabCartes);
            nbCarteTiree += nbCarte;
        }

        return tabCartes;
    }

    public String getNom() { return this.nom; }

    private String[] ajouterAuTableau(int nbCarte, String nomCarte, String[] tab)
    {
        while (nbCarte > 0)
        {
            for (int i = 0; i < tab.length; i++)
            {
                if (tab[i] == null)
                {
                    tab[i] = nomCarte;
                    nbCarte--;
                    break;
                }
            }
        }
        return tab;
    }

    private String carteAuHasard(String rarete)
    {
        String carte = "";

        int nbCarte = 0;

        try {
            Scanner sc = new Scanner(new FileInputStream("./Datas/nomCarte"+rarete+".data"));
            while (sc.hasNextLine())
            {
                sc.nextLine();
                nbCarte++;
            }
        } catch(Exception e) {}

        int indCarte = (int) (Math.random() * nbCarte + 1);

        try {
            Scanner sc2 = new Scanner(new FileInputStream("./Datas/nomCarte"+rarete+".data"));
            for (int i = 0; i < nbCarte; i++)
            {
                if (i == indCarte - 1) carte = sc2.nextLine();
                sc2.nextLine();
            }
        } catch(Exception e) {}

        return carte;
    }
}