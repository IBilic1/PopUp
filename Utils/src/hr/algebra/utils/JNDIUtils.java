/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import com.sun.jndi.fscontext.RefFSContextFactory;
import hr.algebra.model.InitialDirContextCloseable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author HT-ICT
 */
public class JNDIUtils {

    private static final String INITIAL_CONTEXT_FACTORY = RefFSContextFactory.class.getName();
    private static final String PROVIDER_URL_PREFIX = "file:";

    private JNDIUtils() {
    }

    public static String search(String root, String file, String key) {
        try (InitialDirContextCloseable idc = new InitialDirContextCloseable(configureHashTable(root))) {
            return namingEnumeration(idc, key, root);
        } catch (Exception e) {
        }
        return null;
    }

    private static Hashtable<?, ?> configureHashTable(String root) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL_PREFIX.concat(root));
        return env;
    }

    private static String namingEnumeration(Context context, String key, String root) throws NamingException, FileNotFoundException, IOException {
        List<String> lista = new ArrayList<>();
 
        NamingEnumeration enumeration = context.listBindings("");
        while (enumeration.hasMore()) {
            Binding nextElement = (Binding) enumeration.next();
            lista.add(nextElement.getName());
        }
        String configurationFileName = lista.stream().filter(name -> new File(root + name).isDirectory() == false)
                .filter(name -> name.endsWith(".properties"))
                .findAny().get();
        Properties properties = new Properties();
        properties.load(new FileInputStream(root+"\\"+configurationFileName));
        String value = properties.getProperty(key);
        return value;
    }

}
