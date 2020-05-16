package client;

import com.sun.javafx.iio.png.PNGIDATChunkInputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class ClientChat extends Application {
    PrintWriter pw;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Client Chat");
        BorderPane borderPane=new BorderPane();
        Label labelHost=new Label("Host:");
        TextField textFieldHost=new TextField("localhost");
        Label labelPort=new Label("Port:");
        TextField textFieldPort=new TextField("1234");
        Button buttonConnecter=new Button("Connecter");

        HBox hBox=new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setBackground(new Background(new BackgroundFill(Color.ORANGE,null,null)));
        hBox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonConnecter);

        VBox vBox=new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,10,10,10));
        borderPane.setTop(hBox);
        ObservableList<String> listModel= FXCollections.observableArrayList();
        ListView<String> listView=new ListView<String>(listModel);
        vBox.getChildren().add(listView);
        borderPane.setCenter(vBox);
        Label labelMessage=new Label("Message:");
        TextField textFieldMessage=new TextField();
        textFieldMessage.setPrefSize(400,30);
        Button buttonEnvoyer=new Button("Envoyer");
        HBox hBox2=new HBox();
        hBox2.setSpacing(10);
        hBox2.setPadding(new Insets(10));
        hBox2.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer);
        borderPane.setBottom(hBox2);


        Scene scene=new Scene(borderPane,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();

        buttonConnecter.setOnAction((evt)-> {
            String host=textFieldHost.getText();
            int port= Integer.parseInt(textFieldPort.getText());
            try {
                Socket socket = new Socket(host, port);
                InputStream inputStream=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(isr);
                pw=new PrintWriter(socket.getOutputStream(),true);
                new Thread(()->{

                       while(true) {
                           Platform.runLater(()-> {
                               try {
                                   String response = bufferedReader.readLine();
                                   listModel.add(response);
                               }catch(IOException e){

                               }
                           });
                       }


                }).start();

            }catch (IOException e){

            }


        });
        buttonEnvoyer.setOnAction((evt)->{
            String message=textFieldMessage.getText();
            pw.println(message);
        });
    }

}
