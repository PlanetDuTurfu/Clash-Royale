package sources.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
// import java.util.Scanner;

public class Connexion {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    // private final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
    private static String OS = System.getProperty("os.name").toLowerCase();
    private Frame frm;

    public Connexion()
    {
        try {
            this.frm = new Frame(this);
            this.clientSocket = new Socket(InetAddress.getLocalHost(),6000);
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread recevoir = new Thread(new Runnable() {
                String msg = "";
                @Override
                public void run() {
                    try {
                        while(msg!=null)
                        {
                            msg = in.readLine();
                            if (msg.length() > 0)
                            {
                                if (msg.equals("Entrez votre pseudo : "))
                                {
                                    Connexion.this.frm.setFrameRegister();
                                    Connexion.this.frm.pack();
                                }
                                else if (msg.substring(0, "@to#".length()).equals("@to#"))
                                {
                                    Connexion.this.frm.setFrameTo(msg.substring("@to#".length()));
                                    Connexion.this.frm.pack();
                                }
                                else if (msg.equals("Connexion acceptée"))
                                {
                                    Connexion.this.frm.setFrameAccueil();
                                    Connexion.this.frm.pack();
                                }
                                else System.out.println(msg);
                            }
                        }
                        System.out.println("Serveur déconnecté");
                        out.close();
                        clientSocket.close();
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
            recevoir.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void ecrire(String s)
    {
        System.out.println("écrire");
        try { out.println(s); } catch(Exception e) { e.printStackTrace(); }
    }

    public static void clearConsole()
    {
		if (estWindows())
		{
			try { new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); }
			catch (Exception e) {}
		}
		else
		{
			try { System.out.print("\033\143"); }
			catch (Exception e) {}
		}
	}

    private static boolean estWindows()
	{
		return (OS.indexOf("win") >= 0);	
	}

    public static void main(String[] args) { new Connexion(); }
}