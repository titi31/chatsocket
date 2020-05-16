package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServeurChat extends Thread {
        private int nombreClient = 0;
        private List<Conversation> clients = new ArrayList<Conversation>();

        public static void main(String[] args) {
            new ServeurChat().start();
            System.out.print("suite de l'application ...");

        }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Démarrage du serveur ...");
            while (true) {
                Socket socket = ss.accept();
                ++nombreClient;
                Conversation conversation = new Conversation(socket, nombreClient);
                clients.add(conversation);
                conversation.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Conversation extends Thread {
        protected Socket socketClient;
        protected int numeroClient;

        public Conversation(Socket s, int num) {
            this.socketClient = s;
            this.numeroClient = num;
        }

        public void broadcastMessage(String message, Socket socket, int numClient) {
            try {
                for (Conversation client : clients) {
                    if (client.socketClient != socket) {


                        if (client.numeroClient == numClient || numClient == -1) {
                            PrintWriter printWriter = new PrintWriter(client.socketClient.getOutputStream(), true);
                            printWriter.println(message);
                        }
                    }

                }

            } catch (IOException e) {

            }

        }

        @Override
        public void run() {
            try {
                InputStream is = socketClient.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socketClient.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String IP = socketClient.getRemoteSocketAddress().toString();
                System.out.println("connexion du client " + numeroClient + " IP= " + IP);

                pw.println("Bienvenue vous le client numéro " + numeroClient);

                while (true) {
                    String req = br.readLine();
                    if (req.contains("=>")) {
                        String[] requestParams = req.split("=>");
                        if (requestParams.length == 2) {
                            String message = requestParams[1];
                            int numeroClient = Integer.parseInt(requestParams[0]);
                            broadcastMessage(message, socketClient, numeroClient);

                        }
                    } else {
                        broadcastMessage(req, socketClient, -1);
                    }


                    System.out.println("Le client " + IP + " a envoyé une requete " + req);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

