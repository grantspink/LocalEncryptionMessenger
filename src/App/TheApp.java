
package App;

import Frontend.LoginViewController;
import Frontend.ViewDisplay;
import RSA.FileManager;
import RSA.User;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TheApp implements Runnable
{
    public static ViewDisplay theViewDisplay = null;
    public static ViewDisplay previewViewDisplay = null;
    public static ArrayList<User> users = new ArrayList<>();
    public static final int bitLength = 2048;
    public static final int chunkLength = bitLength*2;
    public static final int shortChunk = chunkLength-64;
    public static final int[] defaultBitPlaces = {0,8,16};
    public static final String fileDirectory = Paths.get("").toAbsolutePath().toString();
    
    public TheApp()
    {
        loadUserData();
        try {
            theViewDisplay = new LoginViewController();
        } catch (IOException ex) {
            Logger.getLogger(TheApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        theViewDisplay.setVisible(true);
    }
    
    @Override
    public void run()
    {
        
    }
    
    public static void swapDisplay(ViewDisplay viewDisplay)
    {
        theViewDisplay.setVisible(false);
        theViewDisplay.dispose();
        
        theViewDisplay = viewDisplay;
        theViewDisplay.setVisible(true);
    }
    
    public static void openDisplay(ViewDisplay viewDisplay)
    {
        previewViewDisplay = viewDisplay;
        previewViewDisplay.setVisible(true);
    }
    
    public static void loadUserData()
    {
        String fileContent = FileManager.readTextFromFile("passwords.txt");
        Scanner sc = new Scanner(fileContent).useDelimiter("//");
        while(sc.hasNext())
        {
            String[] userData = sc.next().split("&");
            if(userData.length==1)
                break;
            System.out.println(Arrays.toString(userData) + "\n");
            users.add(new User(userData[0], userData[1], new File(userData[2]), 2048, new BigInteger(userData[3]), new BigInteger(userData[4])));
        }
    }
}