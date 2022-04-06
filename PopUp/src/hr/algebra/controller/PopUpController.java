/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.utils.ImageUtils;
import hr.algebra.customControl.MessageControl;
import hr.algebra.customControl.ProfileControl;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.FrindsThread;
import hr.algebra.model.Message;
import hr.algebra.model.MessageType;
import hr.algebra.model.MessagingThread;
import hr.algebra.model.User;
import hr.algebra.servie.MessagingService;
import hr.algebra.utils.DOMUtils;
import hr.algebra.utils.FileUtils;
import static hr.algebra.utils.ImageUtils.BufferedImageToByteArray;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class PopUpController implements Initializable {

    @FXML
    private Label lblTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            registry = LocateRegistry.getRegistry();
            server = (MessagingService) registry.lookup("MessengerService");
            startTimelineForClock();
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Registry registry;
    public static MessagingService server;

    @FXML
    private HBox hbProfileDetails;

    @FXML
    private ListView<ProfileControl> lvFriends;

    @FXML
    private TextField tfMessage;

    @FXML
    private ListView<MessageControl> messages;

    @FXML
    private Button btnSned;

    private static ObservableList<MessageControl> messageViews = FXCollections.observableArrayList();

    public static User user;

    public static User friend;

    public static User getUser() {
        return user;
    }
    private static FrindsThread frindsThread;

    public void initData(User user) throws Exception {
        PopUpController.user = user;
        initProfile();
        new MessagingThread(messageViews).start();
        frindsThread = new FrindsThread(lvFriends, done -> {
            try {
                initFriends();
            } catch (Exception ex) {
                Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        frindsThread.start();
    }

    public void initProfile() throws IOException {
        ProfileControl profile = new ProfileControl(user.getImage(), user.getFirstName(), user.getLastName());
        hbProfileDetails.getChildren().add(profile);
    }

    public void initFriends() {
        //ako imas nesto u fxml file-u to koristi za prijetelje
        new Thread(() -> {
            try {
                if (DOMUtils.loadFriends() != null
                        && DOMUtils.loadFriends().size() == RepositoryFactory.REPOSITORY.getRepository().getFriendsSize(user.getIdUser())) {
                    for (User friend : DOMUtils.loadFriends()) {
                        makeProfile(friend);
                    }
                } else {
                    //spremi u xml file-u
                    saveFriendsInXML();
                    for (User friend : RepositoryFactory.REPOSITORY.getRepository().getFriends(user.getIdUser())) {
                        makeProfile(friend);
                    }
                }
            } catch (Exception e) {
                Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, e);
            }

        }).start();

    }

    public void makeProfile(User friend) throws IOException {
        if (lvFriends.getItems().stream().map((object) -> object.getId()).filter(id -> id.equals(String.valueOf(friend.getIdUser()))).count() == 0) {
            ProfileControl profile = new ProfileControl(friend.getImage(), friend.getFirstName(), friend.getLastName());
            profile.setId(String.valueOf(friend.getIdUser()));
            setEventListener(profile);
            lvFriends.getItems().add(profile);
        }
    }

    private void setEventListener(ProfileControl profile) {
        profile.setOnMouseClicked(event -> {
            stopAllMediaPlayers();
            messageViews.clear();
            initMessages(profile);
        });
    }

    private void stopAllMediaPlayers() {
        for (MessageControl messageControl : messageViews) {
            if (messageControl.message.getType() == MessageType.VIDEO) {
                messageControl.stopMediaPlayer();
            }
        }
    }

    private void initMessages(ProfileControl profile) {
        try {

            friend = RepositoryFactory.REPOSITORY.getRepository()
                    .get(Integer.parseInt(profile.getId())).get();

            List<Message> chatHistory = server.getChatHistory(user, friend);

            chatHistory.forEach(message -> messageViews.add(new MessageControl(
                    message,
                    user.getIdUser() == message.getSender().getIdUser() ? 1 : 2)));
            messages.setItems(messageViews);
        } catch (Exception ex) {
            Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addMessage(ActionEvent event) {
        if (!tfMessage.getText().isEmpty() || friend != null) {
            try {
                Message m = new Message(LocalDateTime.now(), user, friend, tfMessage.getText().trim().getBytes(), MessageType.MESSAGE);
                messageViews.add(new MessageControl(m, 1));
                RepositoryFactory.REPOSITORY.getRepository().createMessage(m);

                new Thread(() -> {
                    try {
                        server.sendMessage(m);
                    } catch (RemoteException ex) {
                        Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
                System.out.println("active threads: " + Thread.activeCount());
                tfMessage.clear();
            } catch (Exception ex) {
                Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean checkIfMessageIsForMe(Message message) {
        if ((message.getReceiver().getIdUser() == user.getIdUser()
                && message.getSender().getIdUser() == friend.getIdUser())
                || (message.getReceiver().getIdUser() == friend.getIdUser()
                && message.getSender().getIdUser() == user.getIdUser())) {
            return true;
        }
        return false;
    }

    @FXML
    private void handleButtonSend(KeyEvent event) {
        btnSned.setDisable(
                tfMessage.getText().trim().isEmpty()
        );
    }

    @FXML
    private void addPicture(ActionEvent event) throws IOException, Exception {
        File file = FileUtils.uploadFile(btnSned.getScene().getWindow(), "jpg", "png", "jpeg");
        if (file != null) {
            Image image = ImageUtils.createImage(file);

            String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1, file.getName().length());
            Message message = createMessage(LocalDateTime.now(),
                    user, friend,
                    BufferedImageToByteArray(image, extension), MessageType.PICTURE);
            server.sendMessage(message);
            RepositoryFactory.REPOSITORY.getRepository().createMessage(message);
        }
    }

    @FXML
    private void addVideo(ActionEvent event) {
        File file = FileUtils.uploadFile(btnSned.getScene().getWindow(), "mp4");
        if (file != null) {
            try {
                Message message = createMessage(LocalDateTime.now(),
                        user, friend,
                        hr.algebra.utils.MediaUtils.fileToByteArray(file), MessageType.VIDEO);

                server.sendMessage(message);
                RepositoryFactory.REPOSITORY.getRepository().createMessage(message);
            } catch (Exception ex) {
                Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Message createMessage(LocalDateTime now, User user, User friend, byte[] message, MessageType messageType) {
        return new Message(LocalDateTime.now(),
                user, friend,
                message,
                messageType);
    }

    private void startTimelineForClock() {
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(1000),
                        refreshEvent -> {
                            lblTime.setText(LocalDateTime.now().getHour() + ":"
                                    + LocalDateTime.now().getMinute()
                                    + ":" + LocalDateTime.now().getSecond());
                        }
                ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void saveFriendsInXML() {
        try {
            System.out.println("alalalalal");
            List<User> people = RepositoryFactory.REPOSITORY.getRepository().getFriends(user.getIdUser());
            DOMUtils.saveFriends(people);
        } catch (Exception ex) {
            Logger.getLogger(PopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
