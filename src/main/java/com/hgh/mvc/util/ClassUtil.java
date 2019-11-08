package com.hgh.mvc.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

/**
 * 类操作工具类
 */
@Slf4j
public class ClassUtil {

    /**
     * file形式url协议？？
     */
    public static final String FILE_PROTOCOL = "file";

    /**
     * jar形式url协议？？
     */
    public static final String JAR_PROTOCOL = "jar";

    /**
     * 获取classLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取class
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取实例
     *
     * @param className
     * @param <T>
     * @return
     */
    public static <T> T newInstance(String className) {
        try {
            Class<?> clazz = loadClass(className);
            return (T) clazz.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类的属性值
     *
     * @param field      对应的属性
     * @param target     对应的类
     * @param value      属性对应的值
     * @param accessible 是否可编辑
     */
    public static void setField(Field field, Object target, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("set field error", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取包下类集合
     * @param basePackage
     * @return
     */
    public static Set<Class<?>> getPackageClass(String basePackage) {
        URL url = getClassLoader().getResource(basePackage.replace(".", "/"));
        if (null == url) {
            throw new RuntimeException("无法获取目标路径文件");
        }
        try {
            if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
                //若为普通文件夹，则遍历
                File file = new File(url.getFile());
                Path basePath = file.toPath();//根据toPath()获取WindowsPath实现类

                return Files.walk(basePath)
                        .filter(path -> path.toFile().getName().endsWith(".class"))//过滤出.class后缀的文件，生成Stream类型对象
                        .map(path -> getClassByPath(path, basePath, basePackage))//获取每个类的映射，返回处理后的Stream对象,相当于遍历集合将每个元素进行操作
                        .collect(Collectors.toSet());//生成Set集合
            } else if (url.getProtocol().equalsIgnoreCase(JAR_PROTOCOL)) {
                //若在jar包中，则解析jar包中的entry
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                return jarURLConnection.getJarFile()
                        .stream()
                        .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                        .map(ClassUtil::getClassByJar)//相当于将jarURLConnection.getJarFile()当作参数传入
                        .collect(Collectors.toSet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Path获取Class
     *
     * @param classPath   类路径
     * @param basePath    文件路径
     * @param basePackage
     * @return
     */
    private static Class<?> getClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName)
                .replace("/", ".")
                .replace("\\", ".")
                .replace(".class", "");
        return loadClass(className);
    }


    /**
     * 从jar包获取class
     * @param jarEntry
     * @return
     */
    private static Class<?> getClassByJar(JarEntry jarEntry){
        String jarEntryName = jarEntry.getName();
        // 获取类名
        String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
        return loadClass(className);
    }

}
