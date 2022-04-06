/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HT-ICT
 */
public class SerializationUtils {

    private SerializationUtils() {
    }

    public static <T> void write(T user, String FILENAME) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(user);
        }
    }

    public static <T> T read(String FILENAME) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (T)ois.readObject();
        }
    }
    
    public static <T> void append(T t, String FILENAME) throws IOException, ClassNotFoundException {
        if (new File(FILENAME).length()==0) {
            write(t,FILENAME);
            return;
        }
        List<T> list = new ArrayList<>();
        list.add(read(FILENAME));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            list.add(t);
            oos.writeObject(list);
        }
    }
}
