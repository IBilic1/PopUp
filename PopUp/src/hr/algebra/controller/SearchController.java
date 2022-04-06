/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import hr.algebra.PopUpApplication;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class SearchController implements Initializable {

    @FXML
    private TextField tfFirstName, tfLastName;
    @FXML
    private TableView<User> tvFriends;
    @FXML
    private TableColumn<User, String> tcFirstName, tcLastName, tcEmail, tcBirthDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initColumns();
        initObservable();
    }

    @FXML
    private void filter(ActionEvent event) throws Exception {
        String firstName = tfFirstName.getText();
        String lastName = tfLastName.getText();
        List<User> filteredUsers = RepositoryFactory.REPOSITORY.getRepository().get();
        if (!firstName.isEmpty()) {
            filteredUsers = filteredUsers.stream()
                    .filter(person -> person.getFirstName().toLowerCase().contains(firstName.trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (!lastName.isEmpty()) {
            filteredUsers = filteredUsers.stream()
                    .filter(person -> person.getLastName().toLowerCase().contains(lastName.trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (!firstName.isEmpty() && !lastName.isEmpty()) {
            filteredUsers = filteredUsers.stream()
                    .filter(person -> person.getFirstName().toLowerCase().contains(firstName.trim().toLowerCase())
                    || person.getLastName().toLowerCase().contains(lastName.trim().toLowerCase()))
                    .collect(Collectors.toList());
        }

        tvFriends.setItems(FXCollections.observableArrayList(filteredUsers));

    }

    private void initColumns() {
        tcFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    public void initObservable() {
        try {
            tvFriends.setItems(FXCollections.observableArrayList(RepositoryFactory.REPOSITORY.getRepository().get()));
        } catch (Exception ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void selectUser(MouseEvent event) throws Exception {
        if (event.getClickCount() != 2 || event.getTarget().getClass().equals(TableColumnHeader.class)) {
            return;
        }
        User friend = tvFriends.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/view/PopUp.fxml"));
        Parent parent = loader.load();
        PopUpController popUpController = loader.getController();
        
        
        if (!friend.equals(PopUpController.user)) {
            popUpController.makeProfile(friend);
            popUpController.initProfile();
            popUpController.initFriends();
            Scene scene = new Scene(parent);
            PopUpApplication.getStage().setScene(scene);
        }
    }

}
