package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;

public class ServeurJeu extends Thread {
    private int nombreClient;
    private int nombreSecret;
    private boolean fin;
    private String gagnant;

    public static void main(String[] args) {
        new ServeurJeu().start();
        System.out.print("suite de l'application ...");

    }
    @Override
    public void run(){
        try {
            ServerSocket ss= new ServerSocket(1234);
            System.out.println("Démarrage du serveur ...");
            nombreSecret=new Random().nextInt(1000);
            System.out.println(nombreSecret);

            while (true){
                Socket socket=ss.accept();
                ++nombreClient;
                new Conversation(socket,nombreClient).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class Conversation extends Thread {
        private  Socket socket;
        private int numeroClient;
        public Conversation(Socket s,int num){
            this.socket=s;
            this.numeroClient=num;
        }
        @Override
        public void run(){
            try {
                InputStream is=socket.getInputStream();
                InputStreamReader isr= new InputStreamReader(is);
                BufferedReader br= new BufferedReader(isr);

                OutputStream os=socket.getOutputStream();
                PrintWriter pw= new PrintWriter(os,true);
                String IP=socket.getRemoteSocketAddress().toString();
                System.out.println("connexion du client "+numeroClient+" IP= "+IP);

                pw.println("Bienvenue vous le client numéro "+numeroClient);
                pw.println("Devinez le nombre secret ......?");

                while (true) {
                    String req = br.readLine();

                    int nombre = 0;
                    boolean correctFormatRequest = false;
                    try {
                        nombre = Integer.parseInt(req);
                        correctFormatRequest = true;
                    } catch (NumberFormatException e) {
                        correctFormatRequest = false;
                    }
                    if (correctFormatRequest) {

                        System.out.println("Client " + IP + " Tentative avec le nombre: " + nombre);
                        if (fin == false) {
                            if (nombre > nombreSecret) {
                                pw.println("votre nombre est supérieur au nombre secret");
                            } else if (nombre < nombreSecret) {
                                pw.println("votre nombre est inférieur au nombre secret");
                            } else {
                                pw.println("BRAVO, vous avez gagné");
                                gagnant = IP;
                                System.out.println("BRAVO au gagnant, IP Client : " + IP);
                                fin = true;
                            }
                        } else {
                            pw.println("jeu terminé, le gagnant est : " + gagnant);
                        }
                    }else{
                        pw.println("format de nombre incorrect");
                    }



                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

