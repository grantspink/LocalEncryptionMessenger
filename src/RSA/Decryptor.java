
package RSA;

import App.TheApp;
import Frontend.DecryptorViewController;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Decryptor extends Cryptor<String> 
{
    private final DecryptorViewController decryptorViewController;
    private final int selectedIndex;
    private final File cipherFile;
    
    private User sender;
    private String receiptCipherText;
    private String receiptPlainText;
    private String bodyCipherText;
    private String bodyPlainText;
    private String senderName;
    private int numChunks;
    private int chunkSize;
    private int shortChunk;
    private int[] bitPlacesArray;
    private long timeStart;
    private long timeEnd;
    private ArrayList<Long> timeStamps;
    private String warnings;
    
    public Decryptor(DecryptorViewController decryptorViewController)
    {
        super();
        
        this.decryptorViewController = decryptorViewController;
        this.selectedIndex = this.decryptorViewController.mainViewController.inboxList.getSelectedIndex();
        this.cipherFile = LoginManager.getUser().getInbox().get(this.selectedIndex).getImage();
    }
    
    @Override
    public String doInBackground()
    {
        this.decryptorViewController.startButton.setEnabled(false);
        this.decryptorViewController.cancelButton.setEnabled(true);
        this.setProgress(0);
        this.decryptMessage();
        return this.getReceipt() + this.getWarnings() + "&&" + this.getBodyPlainText();
    }

    @Override
    protected void process(List<String> progressOutput)
    {
        String currentOutput = progressOutput.get(progressOutput.size() - 1);
        this.decryptorViewController.taskOutput.append(currentOutput + "\n");
    }

    @Override
    public void done()
    {
        try 
        {
            Toolkit.getDefaultToolkit().beep();
            this.decryptorViewController.setCursor(null);
            this.decryptorViewController.cancelButton.setEnabled(false);
            
            String imageName = "receipt" + String.format("%03d", Integer.valueOf(this.cipherFile.getName().substring(this.cipherFile.getName().length()-7, this.cipherFile.getName().length()-4))) + ".txt";
            String filePath = FileManager.getInbox(LoginManager.getUser().getUserDirectory()) + "\\" + imageName;
            FileManager.writeTextToFile(filePath, this.get());
            
            LoginManager.getUser().getInbox().get(this.selectedIndex).setMessage(new File(filePath));
            this.decryptorViewController.mainViewController.inboxListModel.setElementAt(LoginManager.getUser().getInbox().get(this.selectedIndex).getImage().getName() + " (decrypted)", this.selectedIndex);
            this.decryptorViewController.mainViewController.InboxSelectionListener.valueChanged(null);
            
            JOptionPane.showMessageDialog(null, "Decryption Succesful");
            
            this.trace("Done!");
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void decryptMessage()
    {
        this.trace("Beggining to decrypt message");
        
        BufferedImage cipherImage = FileManager.fileToImage(this.cipherFile);
        
        this.trace("Finished loading image");
        
        this.receiptCipherText = ImageEncryptor.ImageDecrypt
        (
            cipherImage,
            TheApp.defaultBitPlaces,
            0,
            TheApp.chunkLength
        );
        
        this.trace("Finished retrieving receipt from image");
        
        this.receiptPlainText = new String
        (
            RSA.crypt
            (
                new BigInteger
                (
                    this.receiptCipherText,
                    2
                ),
                LoginManager.getUser().getKeyPair().getPrivateKey(),
                LoginManager.getUser().getKeyPair().getModulus()
            ).toByteArray()
        );
        
        this.trace("Finished decrypting receipt");
        
        int bitField;
        try (Scanner sc = new Scanner(this.receiptPlainText).useDelimiter("//"))
        {
            this.senderName = sc.next();
            this.numChunks = Integer.valueOf(sc.next());
            this.chunkSize = Integer.valueOf(sc.next());
            this.shortChunk = Integer.valueOf(sc.next());
            bitField = Integer.valueOf(sc.next());
            this.timeStart = sc.nextLong();
            this.timeEnd = sc.nextLong();
        }
        this.bitPlacesArray = DecryptorViewController.listToIntArray(this.bitFieldToBitList(bitField));
        this.sender = TheApp.users.get(TheApp.users.indexOf(new User(this.senderName)));
        this.timeStamps = new ArrayList<>();
        
        this.trace("Finished parsing receipt");
        
        this.bodyCipherText = ImageEncryptor.ImageDecrypt
        (
            cipherImage,
            this.bitPlacesArray,
            1366,
            this.chunkSize*this.numChunks
        );
        
        this.trace("Finished retrieving cipher text from image");
        
        StringBuilder receiverDecryptedBuilder = new StringBuilder();
        String[] cipherTextChunks = this.bodyCipherText.split(String.format("(?<=\\G.{%d})", this.chunkSize));
        
        this.trace("Finished building cipher text array");
        
        int chunkCount = 0;
        double percentCount = 0;
        int log = 6;
        double part = (double)cipherTextChunks.length / Math.pow(10, (double)log);
        while((int)part == 0)
        {
            log --;
            part = (double)cipherTextChunks.length / Math.pow(10, (double)log);
        }
        
        this.trace("Beggining to decrypt big cipher text chunks");
        
        for (String chunk : cipherTextChunks)
        {
            String receiverDecryptedChunk = RSA.crypt
            (
                new BigInteger
                (
                    chunk,
                    2
                ),
                LoginManager.getUser().getKeyPair().getPrivateKey(),
                LoginManager.getUser().getKeyPair().getModulus()
            ).toString(2);
            
            for(int i=0; i<this.shortChunk-receiverDecryptedChunk.length(); i++)
                receiverDecryptedBuilder.append('0');
            receiverDecryptedBuilder.append(receiverDecryptedChunk);
            
            if ((int)((double)chunkCount % part) == 0 && (int)percentCount != 100)
            {
                percentCount += this.printPercent("Big Cipher Chunks Decrypted", percentCount, log, chunkCount, cipherTextChunks.length);
            }
            this.setProgress((int)(percentCount) / 2);
            chunkCount ++;
        }
        this.printPercent("Big Cipher Chunks Encrypted", percentCount, log, chunkCount, cipherTextChunks.length);
        this.setProgress((int)(percentCount) / 2);
        
        String receiverDecrypted = receiverDecryptedBuilder.toString();
        String s = receiverDecrypted.substring(receiverDecrypted.length() - (this.shortChunk)).replaceFirst("^0+(?!$)", "");
        receiverDecrypted = receiverDecrypted.substring(0, receiverDecrypted.length() - (this.chunkSize - 64)) + String.format 
        (
            "%" + (s.length() + ((this.chunkSize - ((((this.shortChunk) * (this.numChunks-1)) + s.length()) % this.chunkSize)) % this.chunkSize)) + "s",
            s
        ).replace(' ', '0');
        
        this.trace("Finished decrypting cipher text");
        
        String[] receiverDecryptedChunks = receiverDecrypted.split(String.format("(?<=\\G.{%d})", this.chunkSize));
        StringBuilder bodyPlainTextString = new StringBuilder(this.numChunks*(this.chunkSize / 8));
        
        this.trace("Finished building cipher text array");
        
        chunkCount = 0;
        percentCount = 0;
        log = 6;
        part = (double)receiverDecryptedChunks.length / Math.pow(10, (double)log);
        while((int)part == 0)
        {
            log --;
            part = (double)receiverDecryptedChunks.length / Math.pow(10, (double)log);
        }
        
        this.trace("Beggining to decrypt small cipher text chunks");
        
        for (String chunk : receiverDecryptedChunks)
        {
            String senderDecryptedChunk = new String
            (
                RSA.crypt
                (
                    new BigInteger
                    (
                        chunk,
                        2
                    ),
                    this.sender.getKeyPair().getPublicKey(),
                    this.sender.getKeyPair().getModulus()
                ).toByteArray()
            );
            
            String[] senderDecryptedChunkArr = senderDecryptedChunk.split(String.format("(?<=\\G.{%d})", (senderDecryptedChunk.length() - 13)));
            bodyPlainTextString.append(senderDecryptedChunkArr[0]);
            
            long l = 0;
            try{
                l = Long.valueOf(senderDecryptedChunkArr[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                System.out.println("Exception thrown: " + e);
            } finally {
                this.timeStamps.add(l);
            }
            
            if ((int)((double)chunkCount % part) == 0 && (int)percentCount != 100)
            {
                percentCount += this.printPercent("Small Cipher Chunks Decrypted", percentCount, log, chunkCount, receiverDecryptedChunks.length);
            }
            this.setProgress(50 + (int)(percentCount) / 2);
            chunkCount ++;
        }
        this.printPercent("Small Cipher Chunks Decrypted", percentCount, log, chunkCount, receiverDecryptedChunks.length);
        this.setProgress(50 + (int)(percentCount) / 2);
        
        this.trace("Finished decrypting plain text");
        
        this.bodyPlainText = bodyPlainTextString.toString();
    }
    
    public File getCipherFile(){ return cipherFile; }
    public String getReceiptCipherText(){ return receiptCipherText; }
    public String getReceiptPlainText(){ return receiptPlainText; }
    public String getBodyCipherText(){ return bodyCipherText; }
    public String getBodyPlainText(){ return bodyPlainText; }
    public String getSenderName(){ return senderName; }
    public int getNumChunks(){ return numChunks; }
    public int getChunkSize(){ return chunkSize; }
    public int getShortChunk(){ return shortChunk; }
    public int[] getBitPlacesArray(){ return bitPlacesArray; }
    public long getTimeStart(){ return timeStart; }
    public long getTimeEnd(){ return timeEnd; }
    public ArrayList<Long> getTimeStamps(){ return timeStamps; }
    
    public List bitFieldToBitList(int bitField)
    {
        ArrayList<Integer> bitList = new ArrayList<>();
        int counter = 0;
        while(bitField>0)
        {
            if(bitField%2==1)
                bitList.add(counter);
            counter++;
            bitField/=2;
        }
        return bitList;
    }
    
    public String getWarnings()
    {
        String warning = "\n\nWarnings:\n";
        if(this.getTimeStart()>this.getTimeEnd())
            warning += "Time start and time end are unordered.\n";
        if(!isOrdered(this.getTimeStamps()))
            warning += "Time stamps are not ordered.\n";
        if(this.getNumChunks()==0)
            warning += "There are 0 chunks of text within the image.\n";
        if(this.getChunkSize()==0)
            warning += "The size of the text chunks is 0.\n";
        if(this.getBitPlacesArray().length==0)
            warning += "The text is not hidden at any bit places.\n";
        return warning;
    }
    
    public boolean isOrdered(List<Long> timeStamps)
    {
        if(timeStamps.size()==1)
            return true;
        for(int i=1; i<timeStamps.size(); i++)
        {
            if(timeStamps.get(i)<timeStamps.get(i-1))
                return false;
        }
        return true;
    }
    
    public String getReceipt()
    {
        return
                "Sent from:\n" + this.getSenderName() + 
                "\n\nTime Start:\n" + this.getTimeStart() + 
                "\n\nTime End:\n" + this.getTimeEnd() + 
                "\n\nNumber of Chunks:\n" + this.getNumChunks() +
                "\n\nBig Chunk Sizes:\n" + this.getChunkSize() + 
                "\n\nSmall Chunk Sizes:\n" + this.getShortChunk() + 
                "\n\nBit Places Hidden at:\n" + Arrays.toString(this.getBitPlacesArray()) + 
                "\n\nTime Stamps:\n" + Arrays.toString(this.getTimeStamps().toArray());
    }
}
