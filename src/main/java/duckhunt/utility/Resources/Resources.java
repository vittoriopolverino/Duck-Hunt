package duckhunt.utility.Resources;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import duckhunt.utility.sound.Sound;

/**
 * 
 * @author Vittorio Polverino
 */

public class Resources {

    static Class source = Resources.class;

    private Resources() {
    }

    public static void setSourceClass(Class source) {
        Resources.source = source;
    }

    public static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getResource(path));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        return image;
    }


    public static Sound getSound(String path) {
        Sound sound = null;
        try {
            //System.out.println("\n\n\n\n" + getResource(path).toURI() + "\n\n\n\n");
            //File file = new File(getResource(path).toURI());
            sound = new Sound(getResource(path));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        return sound;
    }

    public static String getText(String path) {

        StringBuilder builder = new StringBuilder();

        try {
            InputStream byteStream = getResourceAsStream(path);
            InputStreamReader txtStream = new InputStreamReader(byteStream, "ISO-8859-1");
            BufferedReader reader = new BufferedReader(txtStream);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        return builder.toString();
    }

    public static File extract(String path) throws IOException {
        return extract(path, null);
    }

    public static File extract(String source, File destination) throws IOException {
        if (source.startsWith("/")) {
            source = source.substring(1);
        }
        File jarPath = getRunningSource();
        File homeDir = getHomeDir();
        if (destination == null) {
            destination = homeDir;
        }

        InputStream input = null;
        FileOutputStream output = null;
        try {
            if (isRunningFromJar()) {
                JarFile jar = new JarFile(jarPath);
                JarEntry sourceFile = jar.getJarEntry(source);
                String fileName = sourceFile.getName();
                fileName = fileName.substring(fileName.lastIndexOf("/"));
                input = jar.getInputStream(sourceFile);
                destination = new File(destination, fileName);
            } else {
                File sourceFile = new File(homeDir, source);
                input = new FileInputStream(sourceFile);
                destination = new File(destination, sourceFile.getName());
            }
            if (!destination.exists()) {
                destination.getParentFile().mkdirs();
                output = new FileOutputStream(destination);
                while (input.available() > 0) {
                    output.write(input.read());
                }
            }
        } finally {
            try {
                input.close();
                output.close();
            } catch (Exception e) {
            }
        }
        return destination;
    }

    public static File getRunningSource() {
        CodeSource codeSource = source.getProtectionDomain().getCodeSource();
        try {
            return new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    public static File getHomeDir() {
        File runningSource = getRunningSource();
        if (isRunningFromJar()) {
            return runningSource.getParentFile();
        }
        return runningSource;
    }

    public static File getHomeSubDir(String sub) {
        File home = getHomeDir();
        return new File(home, sub);
    }

    public static boolean isRunningFromJar() {
        String className = Resources.class.getName().replace('.', '/');
        String classJar = Resources.class.getResource("/" + className + ".class").toString();
        return classJar.startsWith("jar:");
    }


    public static URL getResource(String resource) {
        return Resources.source.getResource(resource);
    }

    public static InputStream getResourceAsStream(String resource) {
        return Resources.source.getResourceAsStream(resource);
    }
}
