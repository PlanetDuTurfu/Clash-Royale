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
                        out.println(sc.nextLine());
                        out.flush();
                    }
                }
            });
            envoyer.start();
     
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { new Connexion(); }
}