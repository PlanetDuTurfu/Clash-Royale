package sources.Reseau;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Connexion {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
    private static String OS = System.getProperty("os.name").toLowerCase();

    public Connexion()
    {
        try {
            this.clientSocket = new Socket(InetAddress.getLocalHost(),6000);
            this.out = new PrintWriter(this.clientSocket.getOutputStream());
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
                        }
                        System.out.println("Serveur déconecté");
                        out.close();
                        clientSocket.close();
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
            recevoir.start();
        
            Thread envoyer = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    while(true)
                    {
                        String tmp = sc.nextLine();
                        Connexion.clearConsole();
                        out.println(tmp);
                        out.flush();
                    }
                }
            });
            envoyer.start();
     
        } catch (Exception e) { e.printStackTrace(); }
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