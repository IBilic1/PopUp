/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.model.GameState;
import hr.algebra.model.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author HT-ICT
 */
public class DOMUtils {

    private static final String PEOPLE = "people.xml";
    private static final String GAME_STATE = "gameState.xml";
    private static final String POSITION = "positions.xml";

    private static Document createDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
    }

    private static Document createDocument(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    private static Node createPersonElement(User person, Document document) throws IOException {
        Element element = document.createElement("person");
        element.setAttributeNode(createAttribute(document, "id", String.valueOf(person.getIdUser())));
        element.appendChild(createElement(document, "firstName", person.getFirstName()));
        element.appendChild(createElement(document, "lastName", person.getLastName()));
        element.appendChild(createElement(document, "birthDate", person.getBirthDate().format(DateTimeFormatter.ISO_DATE)));
        element.appendChild(createElement(document, "email", person.getEmail()));
        element.appendChild(createElement(document, "password", person.getPassword()));
        ImageUtils.saveImageIntoFile(person.getImage(), person.getEmail());
        return element;
    }

    private static void saveDocuement(Document document, String filename) throws TransformerConfigurationException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer trandformer = factory.newTransformer();
        trandformer.setOutputProperty(OutputKeys.INDENT, "yes");
        trandformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        trandformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
    }

    private static Attr createAttribute(Document document, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(data));
        return element;
    }

    private static User proccessPersonNode(Element element) {
        return new User(
                Integer.valueOf(element.getAttribute("id")),
                element.getElementsByTagName("firstName").item(0).getTextContent(),
                element.getElementsByTagName("lastName").item(0).getTextContent(),
                LocalDate.parse(element.getElementsByTagName("birthDate").item(0).getTextContent(), DateTimeFormatter.ISO_DATE),
                element.getElementsByTagName("email").item(0).getTextContent(),
                element.getElementsByTagName("password").item(0).getTextContent(),
                null
        );
    }

    private static void createFileIfNotExsists(File file) throws IOException {
        if (!file.exists()) {
            Files.createFile(Paths.get(file.getPath()));
        }
    }

    private DOMUtils() {
    }

    public static void saveFriends(List<User> people) {
        try {
            Document document = createDocument("friends");
            people.forEach(person -> {
                try {
                    document
                            .getDocumentElement()
                            .appendChild(createPersonElement(person, document));
                } catch (IOException ex) {
                    Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            saveDocuement(document, PEOPLE);
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<User> loadFriends() {
        List<User> list = new ArrayList<>();
        try {
            File file = new File(PEOPLE);
            if (file.exists()) {
                Document document = createDocument(new File(PEOPLE));
                NodeList nodes = document.getElementsByTagName("person");
                for (int i = 0; i < nodes.getLength(); i++) {
                    User user = proccessPersonNode((Element) nodes.item(i));
                    user.setPicture(ImageUtils.BufferedImageToByteArray(ImageUtils.createImage(new File("friends/" + user.getEmail()+".png")), "png"));
                    list.add(user);
                }
                return list;
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveGameState(List<GameState> gameSatues) {
        try {
            Document document = createDocument("gameState");
            gameSatues.forEach(gs
                    -> document
                            .getDocumentElement()
                            .appendChild(createGameStateElement(gs, document)));
            saveDocuement(document, GAME_STATE);
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<GameState> loadGameStatues() {
        List<GameState> list = new ArrayList<>();
        try {

            Document document = createDocument(new File(GAME_STATE));
            NodeList nodes = document.getElementsByTagName("gameStatues");

            for (int i = 0; i < nodes.getLength(); i++) {
                GameState gameState = processGameStateElement((Element) nodes.item(i));
                list.add(gameState);
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private static Node createGameStateElement(GameState gs, Document document) {
        Element element = document.createElement("gameStatues");
        element.appendChild(createElement(document, "positionX", String.valueOf(gs.getPositionX())));
        element.appendChild(createElement(document, "positionY", String.valueOf(gs.getPositionY())));
        return element;
    }

    private static GameState processGameStateElement(Element element) {
        return new GameState(
                Integer.valueOf(element.getElementsByTagName("positionX").item(0).getTextContent()),
                Integer.valueOf(element.getElementsByTagName("positionY").item(0).getTextContent())
        );
    }

    public static void saveGPOSITIONS(List<GameState> gameSatues) {
        try {
            Document document = createDocument("gameState");
            gameSatues.forEach(gs
                    -> document
                            .getDocumentElement()
                            .appendChild(createGameStateElement(gs, document)));
            saveDocuement(document, POSITION);
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<GameState> loadPOSITIONS() {
        List<GameState> list = new ArrayList<>();
        try {

            Document document = createDocument(new File(POSITION));
            NodeList nodes = document.getElementsByTagName("gameStatues");

            for (int i = 0; i < nodes.getLength(); i++) {
                GameState gameState = processGameStateElement((Element) nodes.item(i));
                list.add(gameState);
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
