import java.net.InetAddress;

import sources.Client.Connexion;

public class LancerClient {
    public static void main(String[] args) {
        try { if (args.length == 0) new Connexion(("" + InetAddress.getLocalHost()).split("/")[0]);
        else new Connexion(args[0]);
        } catch(Exception e) {}
    }
}
