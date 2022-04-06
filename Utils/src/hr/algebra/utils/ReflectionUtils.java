/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author HT-ICT
 */
public class ReflectionUtils {

    public static Optional<Class> getClass(Path packagePath, File classFile) {
        try {
            String packageName = packagePath.toString().replace("\\", ".").replaceFirst("..src.", "");
            Class c = Class.forName(packageName + "." + classFile.getName().substring(0, classFile.getName().indexOf(".")));
            return Optional.of(c);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    public static List<Field> getFields(Class clazz) {
        List<Field> fileds_ = new ArrayList<>();
        Field[] fileds = clazz.getDeclaredFields();
        System.out.println(clazz.getName() + " :" + fileds.length);
        for (Field filed : fileds) {
            System.out.println(filed);
            if (filed.getName().equals("$VALUES")) {
                continue;
            }

            fileds_.add(filed);
        }
        return fileds_;
    }

    public static List<Constructor> getConstructors(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        return Arrays.asList(constructors);
    }

    public static Optional<List<Parameter>> getParameter(Constructor constructor) {
        if (Arrays.asList(constructor.getParameters()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(constructor.getParameters()));
    }

    public static List<Method> getMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();
        Method[] methods_ = clazz.getDeclaredMethods();
        for (Method method : methods_) {
            if (method.getName().equals("values") || method.getName().toString().contains("access")) {
                continue;
            }
            methods.add(method);
        }
        return methods;
    }

    public static Optional<List<Parameter>> getParameter(Method method) {
        if (Arrays.asList(method.getParameters()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(method.getParameters()));
    }

    public static Optional<List<Annotation>> getAnnotations(Executable executable) {
        if (Arrays.asList(executable.getAnnotations()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(executable.getAnnotations()));
    }

    public static Optional<List<String>> getExceptions(Executable executable) {
        if (executable.getExceptionTypes().length > 0) {
            return Optional.of(Arrays.stream(executable.getExceptionTypes()).map(Class::getName).collect(Collectors.toList()));
        }
        return Optional.empty();
    }
}
