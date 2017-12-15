
package RSA;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class FileManager
{
    public static void writeImageToFile(BufferedImage image, String format, File file)
    {
        try
        {
            ImageIO.write(image, format, file);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeTextToFile(String filePath, String content)
    {
        try
        {
            PrintWriter output;
            File nf = new File(filePath);
            output = new PrintWriter(new FileOutputStream(nf, true));
            output.append(content);
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteFile(File file)
    {
        FileOutputStream output = null;

        try
        {
            output = new FileOutputStream(file);
            output.write("".getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                output.flush();
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            output = null;
            System.gc();
        }

        file.delete();
    }
    
    public static byte[] fileToBytes(File file)
    {
        byte[] bFile = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file))
        {
            fileInputStream.read(bFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bFile;
    }
    
    public static BufferedImage bytesToImage(byte[] bFile)
    {
        BufferedImage cipherImage= null;
        try
        {
            cipherImage = ImageIO.read(new ByteArrayInputStream(bFile));
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherImage;
    }
    
    public static BufferedImage fileToImage(File file)
    {
        return bytesToImage(fileToBytes(file));
    }
    
    public static String readTextFromFile(String f)
    {
        String file_content = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            file_content = sb.toString();
        } catch (FileNotFoundException e) {
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file_content;
    }
    
    public static List<File> getImageFiles(File directory)
    {
        return Arrays.asList
        (
            directory.listFiles
            (
                new FilenameFilter()
                {
                    @Override
                    public boolean accept(File dir, String name)
                    {
                        return name.toLowerCase().startsWith("message") && name.toLowerCase().endsWith(".png");
                    }
                }
            )
        );
    }
    
    public static List<File> getTextFiles(File directory)
    {
        return Arrays.asList
        (
            directory.listFiles
            (
                new FilenameFilter()
                {
                    @Override
                    public boolean accept(File dir, String name)
                    {
                        return name.toLowerCase().startsWith("receipt") && name.toLowerCase().endsWith(".txt");
                    }
                }
            )
        );
    }
    
    public static File getInbox(File directory)
    {
        return directory.listFiles
        (
            new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.toLowerCase().startsWith("inbox") && dir.isDirectory();
                }
            }
        )[0];
    }
    
    public static File getOutbox(File directory)
    {
        return directory.listFiles
        (
            new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.toLowerCase().startsWith("outbox") && dir.isDirectory();
                }
            }
        )[0];
    }
}