package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) throws Exception {
        System.out.println("je me connecte au serveur...");
        Socket socket=new Socket("localhost",1234);
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();

        Scanner scanner= new Scanner(System.in);
        System.out.println("Donner un nombre: ");
        int nb =scanner.nextInt();
        System.out.println("J'envoie le nombre "+nb+" au serveur");
        os.write(nb);
        System.out.println("j'attend la réponse du serveur ...");
        int rep=is.read();
        System.out.println("la réponse du serveur est "+rep);


    }
}
