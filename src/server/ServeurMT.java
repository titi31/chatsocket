package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

public class ServeurMT extends Thread {
    private int nombreClient;
    public static void main(String[] args) {
        new ServeurMT().start();
        System.out.print("suite de l'application ...");

    }
    @Override
    public void run(){
        try {
            ServerSocket ss= new ServerSocket(1234);
            System.out.println("Démarrage du serveur ...");
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

                while (true){
                    String req=br.readLine();
                    System.out.println("Le client "+IP+" a envoyé une requete "+req);
                    pw.println(req.length());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

