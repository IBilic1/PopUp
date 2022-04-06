package hr.algebra.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author HT-ICT
 */
public class ImageUtils {

    private static String getFileExtionsion(Image file) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(file);

        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

        while (imageReaders.hasNext()) {
            ImageReader reader = (ImageReader) imageReaders.next();
            return reader.getFormatName();
        }
        return null;
    }

    private ImageUtils() {
    }

    public static Image ByteArrayToBufferedImage(byte[] picture) throws IOException {
        Image img = new Image(new ByteArrayInputStream(picture));
        return img;
    }

    public static byte[] BufferedImageToByteArray(Image image, String extension) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferimage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bufferimage, extension, os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = os.toByteArray();
        return data;
    }

    public static Image createImage(File file) throws IOException {
        return new Image(file.toURI().toString());
    }

    public static void saveImageIntoFile(Image image,String name) {
        try {
            BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
            //System.out.println(getFileExtionsion(image));
            Files.createDirectories(Paths.get("friends"));
            File outputfile = new File("friends/"+name+".png");
            if (!outputfile.exists()) {
                outputfile.createNewFile();
            }
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
