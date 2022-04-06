/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author dnlbe
 */
public class FileUtils {

    private static final String LOAD = "Load";

    public static File uploadFile(Window owner, String... extensions) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        List<String> mappedExtensions = Stream.of(extensions).map(e->"*."+e).collect(Collectors.toList());
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", mappedExtensions)
        );

        chooser.setTitle(LOAD);
        File file = chooser.showOpenDialog(owner);
        return file;
    }
    
    public static void clearFile(String FILENAME) {
        try {
            new FileOutputStream(FILENAME,false).close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
