package Sources;
public class ClashRoyale {
    private Joueur yvan;
    private Joueur nono;
    private Joueur enzo;
    private Coffre coffre;
    private ClashRoyale()
    {
        coffre = new Coffre("Coffre cheat", 1500, 100, 30, 5, 1);
        yvan = new Joueur("Yvan");
        nono = new Joueur("Nono");
        enzo = new Joueur("Enzo");

        this.attribuerCarteDebut();

        System.out.println(yvan.toString());
        System.out.println(nono.toString());
        System.out.println(enzo.toString());
    }

    private void attribuerCarteDebut()
    {
        yvan.ajouterCarte(coffre.ouvrirCoffre());
        nono.ajouterCarte(coffre.ouvrirCoffre());
        enzo.ajouterCarte(coffre.ouvrirCoffre());
    }
    public static void main(String[] args) {
        new ClashRoyale();
    }
}
