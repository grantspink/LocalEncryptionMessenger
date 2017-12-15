
package RSA;

import App.TheApp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginManager
{
    private static boolean validLogin = false;
    private static User user;
    
    public static void checkValidLogin(String username, String password)
    {
        for(User aUser: TheApp.users)
        {
            if(aUser.getUsername().equals(username) && checkPassword(password, aUser.getPasswordHash()))
            {
                user = aUser;
                System.out.println("Messages: " + user.getInbox().size());
                setValidLogin(true);
                break;
            }
        }
    }
    
    public static void setValidLogin(boolean b){ validLogin = b; }
    public static boolean getValidLogin(){ return validLogin; }
    public static User getUser(){ return user; }
    
    public static String getSaltedHash(String password)
    {
        String saltedHash = null;
        try
        {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
            saltedHash = byteArrayToHexString(salt) + "#" + getHash(password, salt);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return saltedHash;
    }
    
    public static boolean checkPassword(String password, String databaseValue)
    {
        String[] saltAndPassword = databaseValue.split("\\#");
        String inputHash = null;
        try {
            inputHash = getHash(password, hexStringToByteArray(saltAndPassword[0]));
        } catch (Exception ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputHash.equals(saltAndPassword[1]);
    }
    
    private static String getHash(String password, byte[] salt) throws Exception
    {
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret
        (
            new PBEKeySpec
            (
                password.toCharArray(),
                salt,
                2048,
                256
            )
        );
        return byteArrayToHexString(key.getEncoded());
    }
    
    public static String byteArrayToHexString(byte[] b)
    {
        String result = "";
        for (int i=0; i <b.length; i++)
            result += Integer.toString(( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        return result;
    }
    
    public static byte[] hexStringToByteArray(String s)
    {
        byte[] data = new byte[s.length()/2];
        for (int i=0; i<s.length(); i+=2)
            data[i/2] = (byte)((Character.digit(s.charAt(i), 16)<<4) + Character.digit(s.charAt(i+1), 16));
        return data;
    }
}
