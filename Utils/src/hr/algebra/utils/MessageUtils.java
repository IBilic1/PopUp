/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.swing.JOptionPane;

/**
 *
 * @author dnlbe
 */
public class MessageUtils {

    public static void showInformationMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirmDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showAlert(String title, String message, String contextMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(contextMessage);
        if (alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    
    
    public static Alert show(String title, String message, String contextMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(contextMessage);
        return alert;
    }
    



}
