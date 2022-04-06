/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HT-ICT
 */
public class MediaUtils {

    public static byte[] fileToByteArray(File file) {

        byte[] b = new byte[(int) file.length()];

        try {
            b = Files.readAllBytes(Paths.get(file.getPath()));
        } catch (IOException ex) {
            Logger.getLogger(MediaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return b;
    }

    public static File byteArrayToFile(byte[] bytes) {
        try {
            File tempMp4 = File.createTempFile("VACA", ".mp4", null);
            tempMp4.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(tempMp4)) {
                fos.write(bytes);
            }
            return tempMp4;
        } catch (IOException ex) {
            Logger.getLogger(MediaUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
