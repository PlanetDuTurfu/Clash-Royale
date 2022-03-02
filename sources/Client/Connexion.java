package sources.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
// import java.util.Scanner;

public class Connexion {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private static String OS = System.getProperty("os.name").toLowerCase();
    private Frame frm;

    public Connexion(String ip)
    {
        try {
            this.frm = new Frame(this);
            this.clientSocket = new Socket(ip,6000);
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
                            System.out.println(msg);
                            if (msg.length() > 0)
                            {
                                if (msg.equals("wait#pseudo#mdp"))
                                {
                                    Connexion.this.frm.setFrameRegister();
                                }
                                else if (msg.substring(0, "@to".length()).equals("@to"))
                                {
                                    Connexion.this.frm.setFrameTo(msg.substring("@to#".length()));
                                }
                                else if (msg.equals("connexion#accepted"))
                                {
                                    Connexion.this.frm.setFrameAccueil();
                                }
                                else if (msg.substring(0, "@co".length()).equals("@co"))
                                {
                                    Connexion.this.frm.setFrameCoffre(msg.substring("@co".length()));
                                }
                                else if (msg.substring(0, "@carte".length()).equals("@carte"))
                                {
                                    Connexion.this.frm.setFrameOuverture(msg.substring("@carte#".length()));
                                }
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
}