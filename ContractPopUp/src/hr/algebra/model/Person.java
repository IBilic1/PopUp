/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author HT-ICT
 */
public class Person implements Serializable {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private int idUser;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Person(String firstName, String lastName, LocalDate birthDate) {
        validate(birthDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(int idUser, String firstName, String lastName, LocalDate birthDate) {
        validate(birthDate);
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Person{" + "idUser=" + idUser + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate + '}';
    }

    private void validate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now().minusYears(10)) || birthDate.isBefore(LocalDate.now().minusYears(100))) {
            //throw new RuntimeException("Age must be in range " + LocalDate.now().minusYears(10) + " - " + LocalDate.now().minusYears(100));
        }
    }

}
