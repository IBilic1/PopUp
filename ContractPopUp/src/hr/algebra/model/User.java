/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.utils.ImageUtils;
import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.image.Image;

/**
 *
 * @author HT-ICT
 */
public class User extends Person {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private byte[] picture;
    private transient Image image;

    public User(int idUser, String firstName, String lastName, LocalDate birthDate, String email, String password, byte[] picture) {
        super(idUser, firstName, lastName, birthDate);
        validatePassword(password);
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    public User(String email, String password, byte[] picture, String firstName, String lastName, LocalDate birthDate) {
        super(firstName, lastName, birthDate);
        validatePassword(password);
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Image getImage() throws IOException {
        return ImageUtils.ByteArrayToBufferedImage(picture);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(getIdUser()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.getIdUser() == ((User) obj).getIdUser();
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" + "email=" + email + ", password=" + password + ", picture=" + picture + ", image=" + image + '}';
    }

    private void validatePassword(String password) {
        if (password.length() < 6) {
            throw new RuntimeException("Password need to be more then 6 charcaters.");
        }
    }

}
