/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.PopUpApplication;
import hr.algebra.utils.MessageUtils;
import hr.algebra.utils.ReflectionUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class MenuController {

    private static final String FILE_DOCUMENTATIONE = "dokumentacija.html";

    @FXML
    public void prikaziEkranZaUnosOsoba() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(PopUpApplication.class.getResource("view/PopUp.fxml"));
        Parent parent = fxmlLoader.load();
        PopUpController popUpController = fxmlLoader.getController();
        popUpController.initProfile();
        popUpController.initFriends();
        Scene scene = new Scene(parent);
        PopUpApplication.getStage().setScene(scene);
    }

    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PopUpApplication.class.getResource("view/Search.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PopUpApplication.getStage().setScene(scene);
    }

    @FXML
    public void prikaziIgricu() throws IOException {
        //FXMLLoader loader = new FXMLLoader(PopUpApplication.class.getResource("view/Game.fxml"));
        //Parent root = loader.load();
        //Scene scene = new Scene(root, 600, 400, Color.WHITESMOKE);
        //PopUpApplication.getStage().setScene(scene);
        //GameController controller = loader.getController();
        //controller.resolveScene(scene, PopUpController.user);
        FXMLLoader loader = new FXMLLoader(PopUpApplication.class.getResource("view/FrontScreenGame.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400, Color.WHITESMOKE);
        PopUpApplication.getStage().setScene(scene);
        FrontScreenGameController controller = loader.getController();
        controller.resolveScene(scene, PopUpController.user);
    }

    @FXML
    private void generirajDokumentaciju(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>").append("\n");
        sb.append("<html>").append("\n");
        sb.append("<head>").append("\n");
        sb.append("<title>Projektna dokumentacija</title>").append("\n");
        sb.append("</head>").append("\n");
        sb.append("<body>").append("\n");
        sb.append("<h1>Popis paketa</h1>").append("\n");
        try {
            List<Path> paths = Files.walk(Paths.get(".\\src\\hr\\algebra\\")).collect(Collectors.toList());
            for (Path path : paths) {
                if (Files.isDirectory(path)) {
                    File[] children = path.toFile().listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".java");
                        }
                    }
                    );

                    if (children.length > 0) {
                        sb.append("<h2>");
                        sb.append("Paket: " + path.getFileName());
                        sb.append("</h2>").append("\n");
                        sb.append("Popis klasa u paketu:").append("\n");
                        for (File file : children) {
                            sb.append("<h3>");
                            sb.append(file.getName());
                            sb.append("</h3>").append("\n");

                            writeClass(sb, path, file);

                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

        sb.append(
                "<p></p>").append("\n");
        sb.append(
                "</body>").append("\n");
        sb.append(
                "</html>").append("\n");

        try (FileWriter fileWriter = new FileWriter(FILE_DOCUMENTATIONE)) {
            fileWriter.write(sb.toString());
            MessageUtils.showAlert("Reflection", "Done", "Successfully generated documentation", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void writeClass(StringBuilder sb, Path path, File file) {
        if (ReflectionUtils.getClass(path, file).isPresent()) {
            Class clazz = ReflectionUtils.getClass(path, file).get();
            writeFields(sb, clazz);
            writeConstructors(sb, clazz);
            writeMethods(sb, clazz);
        }
    }

    private void writeFields(StringBuilder sb, Class clazz) {
        List<Field> fields = ReflectionUtils.getFields(clazz);
        fields.forEach((v) -> {
            sb.append("<b>");
            sb.append(Modifier.toString(v.getModifiers())).append(" ");
            sb.append("</b>");
            sb.append(v.getType().getName());
            sb.append(" ");
            sb.append("<b style=\"color:Violet;\">");
            sb.append(v.getName());
            sb.append("</b>");
            sb.append("<br />");
        });
    }

    private void writeConstructors(StringBuilder sb, Class clazz) {
        ReflectionUtils.getConstructors(clazz).forEach(con -> {
            sb.append("\n\n");
            writeAnnotations(con, sb);
            sb.append("<b>");
            sb.append(Modifier.toString(con.getModifiers())).append(" ");
            sb.append("</b>");
            sb.append("<b style=\"color:DodgerBlue;\">");
            sb.append(con.getName());
            sb.append("</b>");
            sb.append("(");

            if (ReflectionUtils.getParameter(con).isPresent()) {
                ReflectionUtils.getParameter(con).get().forEach(param -> {
                    sb.append(Modifier.toString(param.getModifiers())).append(" ");
                    sb.append(param.getType().getName());
                    sb.append(" ");
                    sb.append(param.getName()).append(",");
                });
                sb.setLength(sb.length() - 1);
            }
            sb.append(")");
            sb.append("<br />");
            appendableappendExceptions(con, sb);
        });
    }

    private void writeMethods(StringBuilder sb, Class clazz) {
        ReflectionUtils.getMethods(clazz).forEach(m -> {
            sb.append("\n\n");
            writeAnnotations(m, sb);
            sb.append("<b>");
            sb.append(Modifier.toString(m.getModifiers())).append(" ");
            sb.append("</b>");
            sb.append(m.getReturnType().getName());
            sb.append(" ");
            sb.append("<b style=\"color:DodgerBlue;\">");
            sb.append(m.getName());
            sb.append("</b>");
            sb.append("(");
            if (ReflectionUtils.getParameter(m).isPresent()) {
                ReflectionUtils.getParameter(m).get().forEach(param -> {
                    sb.append(Modifier.toString(param.getModifiers())).append(" ");
                    sb.append(param.getType().getName());
                    sb.append(" ");
                    sb.append(param.getName()).append(",");
                });
                sb.setLength(sb.length() - 1);
            }
            sb.append(")");
            appendableappendExceptions(m, sb);
            sb.append("<br />");

        });
    }

    private void writeAnnotations(Executable executable, StringBuilder sb) {
        if (ReflectionUtils.getAnnotations(executable).isPresent()) {
            sb.append(
                    ReflectionUtils.getAnnotations(executable).get().stream().map(a -> "<b style=\"color:SlateBlue;\">" + a.toString() + "</b>").collect(Collectors.joining("\n"))
            );
            sb.append("<br />");
        }
    }

    private void appendableappendExceptions(Executable executable, StringBuilder sb) {
        if (ReflectionUtils.getExceptions(executable).isPresent()) {
            sb.append(" throws ");
            sb.append(
                    ReflectionUtils.getExceptions(executable).get().stream().collect(Collectors.joining("  "))
            );
        }

    }

}
