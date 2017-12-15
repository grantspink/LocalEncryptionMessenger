
package RSA;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ImageEncryptor
{
    public static BufferedImage ImageEncrypt(String cipherText, BufferedImage plainImage, int startPlace, int[] bitPlace)
    {
        BufferedImage image = imageCopy(plainImage);
        
        int[] cipherTextArray = stringToIntArray(cipherText);
        boolean isAllEncrypted = false;
        int cipherTextCount = 0;
        imageloop:
        for(int i=startPlace/image.getHeight(); i<image.getHeight(); i++)
        {
            for(int j=startPlace%image.getWidth(); j<image.getWidth(); j++)
            {
                for(int k=0; k<bitPlace.length; k++)
                {
                    if(cipherTextCount<cipherTextArray.length)
                    {
                        if(cipherTextArray[cipherTextCount]!=getByteBit(image.getRGB(j, i), bitPlace[k]))
                        {
                            int n = setByteBit(image.getRGB(j, i), bitPlace[k], -1);
                            image.setRGB(j, i, n);
                        }
                    }
                    else
                    {
                        isAllEncrypted = true;
                        break imageloop;
                    }
                    cipherTextCount++;   
                }
            }
            startPlace = 0;
        }
        if(!isAllEncrypted)
        {
            JOptionPane.showMessageDialog(null, "Not all text could be encrypted within the image: " + Double.toString(((double)cipherTextCount/(double)cipherTextArray.length)*100) + "%");
        }
        return image;
    }
    
    public static String ImageDecrypt(BufferedImage cipherImage, int[] bitPlace, int startPlace, int cipherTextLength)
    {
        StringBuilder cipherTextString = new StringBuilder(cipherTextLength);
        imageloop:
        for(int i=startPlace/cipherImage.getHeight(); i<cipherImage.getHeight(); i++)
        {
            for(int j=startPlace%cipherImage.getWidth(); j<cipherImage.getWidth(); j++)
            {
                int rgb = cipherImage.getRGB(j, i);
                for(int k=0; k<bitPlace.length; k++)
                {
                    cipherTextString.append(String.valueOf(getByteBit(rgb, bitPlace[k])));
                    if(cipherTextString.length()==cipherTextLength)
                        break imageloop;
                }
            }
            startPlace = 0;
        }
        return cipherTextString.toString();
    }
    
    public static int[] stringToIntArray(String s)
    {
        int[] arr = new int[s.length()];
        for(int i=0; i<arr.length; i++)
        {
            arr[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return arr;
    }
    
    /**
     * Converts a bitPlace array into a List object representing its contents.
     * 1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0 - > 0, 8, 16
     * @param bitPlace
     * @return
     */
    public static List bitPlacesToList(int[] bitPlace)
    {
        ArrayList<Integer> bitPlaceList = new ArrayList<>();
        for(int i=0; i<bitPlace.length; i++)
        {
            if(bitPlace[i]==1)
                bitPlaceList.add(i);
        }
        return bitPlaceList;
    }
    
    /**
     * Returns a string representation of the bits in a BGRA color.
     * @param colour
     * @param channel
     * @return 
     */
    public static String displayBGRABits(int colour, char channel)
    {
        String bits = "";
        switch(channel)
        {
            case 'b': // Blue
                for(int i=0; i<8; i++)
                {
                    bits+=getByteBit(colour,i);
                }
                break;
            case 'g': // Green
                for(int i=8; i<16; i++)
                {
                    bits+=getByteBit(colour,i);
                }
                break;
            case 'r': // Red
                for(int i=16; i<24; i++)
                {
                    bits+=getByteBit(colour,i);
                }
                break;
            case 'a': // Alpha
                for(int i=24; i<32; i++)
                {
                    bits+=getByteBit(colour,i);
                }
                break;
            case 'x': // All
                for(int i=0; i<32; i++)
                {
                    bits+=getByteBit(colour,i);
                }
                break;
        }
        return bits;
    }
    
    /**
     * Returns a single bit at a given position within a byte.
     * @param bte
     * @param pos
     * @return 
     */
    public static int getByteBit(int bte, int pos){ return ((bte>>pos) & 1); }
    
    /**
     * Alters a single bit at a given position within a byte according to the given action.
     * @param bte
     * @param pos
     * @param act
     * @return 
     */
    public static int setByteBit(int bte, int pos, int act)
    {
        if(act==0)
            return bte &~(1 << pos); // unset
        else if(act==1)
            return bte | (1 << pos); // set
        else
            return bte ^ (1 << pos); // toggle
    }
    
    /**
     * Copies the contents of a List object into an int[] array.
     * @param integers
     * @return
     */
    public static int[] listToIntArray(List<Integer> integers)
    {
        int[] arr = new int[integers.size()];
        for (int i=0; i<arr.length; i++)
            arr[i] = integers.get(i);
        return arr;
    }
    
    /**
     * Copies the contents of a BufferedImage into a new one.
     * @param image
     * @return
     */
    public static BufferedImage imageCopy(BufferedImage image)
    {
        ColorModel cm = image.getColorModel();
        return new BufferedImage
        (
            cm,
            image.copyData(null),
            cm.isAlphaPremultiplied(),
            null
        );
    }
}
