package RSA;

import App.TheApp;
import Frontend.EncryptorViewController;
import Frontend.MainViewDisplay;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Encryptor extends Cryptor<BufferedImage> 
{
    private final EncryptorViewController encryptorViewController;
    private final User sender;
    private final User recipient;
    private final String plainText;
    private final File plainImage;
    private final int[] bitPlace;

    public Encryptor(EncryptorViewController encryptorViewController)
    {
        super();
        
        this.encryptorViewController = encryptorViewController;
        this.sender = LoginManager.getUser();
        this.recipient = this.encryptorViewController.recipientUser;
        this.plainText = this.encryptorViewController.mainViewController.encryptTextField.getText();
        this.plainImage = this.encryptorViewController.mainViewController.imagePreviewPanel.getImageFile();
        this.bitPlace = bitPanelsToArray(this.encryptorViewController.mainViewController.bitPlacePanels);
    }

    @Override
    public BufferedImage doInBackground()
    {
        this.encryptorViewController.startButton.setEnabled(false);
        this.encryptorViewController.cancelButton.setEnabled(true);
        this.setProgress(0);
        return this.encryptMessage();
    }

    @Override
    protected void process(List<String> progressOutput)
    {
        String currentOutput = progressOutput.get(progressOutput.size() - 1);
        this.encryptorViewController.taskOutput.append(currentOutput + "\n");
    }

    @Override
    public void done()
    {
        try
        {
            Toolkit.getDefaultToolkit().beep();
            this.encryptorViewController.setCursor(null);
            this.encryptorViewController.cancelButton.setEnabled(false);
            this.encryptorViewController.saveButton.setEnabled(true);
            this.encryptorViewController.viewImageButton.setEnabled(true);
            this.encryptorViewController.sendButton.setEnabled(true);
            
            String imageName = "message" + String.format("%03d", this.sender.getOutbox().size()) + ".png";
            String filePath = FileManager.getOutbox(this.sender.getUserDirectory()) + "\\" + imageName;
            System.out.println(filePath);
            FileManager.writeImageToFile(this.get(), "png", new File(filePath));
            JOptionPane.showMessageDialog(null, "Encryption Succesful");
            
            this.trace("Done!");
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage encryptMessage()
    {
        this.trace("Beggining to encrypt message");
        
        BufferedImage cipherImage = FileManager.fileToImage(this.plainImage);

        this.trace("Finished loading image");

        String[] plainTextChunks = replaceCharacters(this.plainText).split(String.format("(?<=\\G.{%d})", 5 * ((this.sender.getKeyPair().getBitLength() / 4 - String.valueOf(Calendar.getInstance().getTimeInMillis()).length()) / 5)));
        
        this.trace("Finished building plain text array");

        StringBuilder senderEncrypted = new StringBuilder();
        String startEncrypt = String.valueOf(Calendar.getInstance().getTimeInMillis());
        
        int chunkCount = 0;
        double percentCount = 0;
        int log = 6;
        double part = (double)plainTextChunks.length/Math.pow(10, (double)log);
        while((int)part==0)
        {
            log--;
            part = (double)plainTextChunks.length/Math.pow(10, (double)log);
        }
        
        this.trace("Beggining to encrypt plain text");

        for (String chunk : plainTextChunks) //---------ENCRYPTING PLAIN TEXT---------//
        {
            senderEncrypted.append
            (
                encryptAndPad
                (
                    new BigInteger
                    (
                        (
                            chunk + 
                            String.valueOf(Calendar.getInstance().getTimeInMillis())
                        ).getBytes()
                    ),
                    this.sender.getKeyPair().getPrivateKey(),
                    this.sender.getKeyPair().getModulus()
                )   
            );
            
            if ((int)((double)chunkCount%part)==0 && (int)percentCount!=100)
            {
                percentCount+=this.printPercent("Plain Chunks Encrypted", percentCount, log, chunkCount, plainTextChunks.length);
            }
            this.setProgress((int)(percentCount)/2);
            chunkCount++;
        }
        this.printPercent("Plain Chunks Encrypted", percentCount, log, chunkCount, plainTextChunks.length);
        
        this.setProgress((int)(percentCount)/2);

        this.trace("Finished encrypting plain text");

        String endEncrypt = String.valueOf(Calendar.getInstance().getTimeInMillis());

        String[] senderEncryptedChunks = senderEncrypted.toString().split(String.format("(?<=\\G.{%d})", TheApp.shortChunk));

        String receiverEncryptedReceipt = encryptAndPad //---------RECEIPT---------//
        (new BigInteger
            (
                (
                    this.sender.getUsername() + "//" +
                    senderEncryptedChunks.length + "//" +
                    TheApp.chunkLength + "//" + 
                    TheApp.shortChunk + "//" + 
                    bitPlacesToBitField(this.bitPlace) + "//" + 
                    startEncrypt + "//" + 
                    endEncrypt
                ).getBytes()
            ),
            this.recipient.getKeyPair().getPublicKey(),
            this.recipient.getKeyPair().getModulus()
        );

        this.trace("Finished encrypting receipt");

        cipherImage = ImageEncryptor.ImageEncrypt
        (
            receiverEncryptedReceipt,
            cipherImage,
            0,
            TheApp.defaultBitPlaces
        );

        this.trace("Finished inserting receipt into image");

        StringBuilder receiverEncryptedBody = new StringBuilder();
        chunkCount = 0;
        percentCount = 0;
        log = 6;
        part = (double)senderEncryptedChunks.length/Math.pow(10, (double)log);
        while((int)part==0)
        {
            log--;
            part = (double)senderEncryptedChunks.length/Math.pow(10, (double)log);
        }

        this.trace("Beggining to encrypt cipher text");

        for (String chunk : senderEncryptedChunks) //---------ENCRYPTING SMALLER CHUNKS---------//
        {
            receiverEncryptedBody.append
            (
                encryptAndPad
                (
                    new BigInteger
                    (
                        chunk,
                        2
                    ),
                    this.recipient.getKeyPair().getPublicKey(),
                    this.recipient.getKeyPair().getModulus()
                )
            );
            
            if ((int)((double)chunkCount%part)==0 && (int)percentCount!=100)
            {
                percentCount+=this.printPercent("Cipher Chunks Encrypted", percentCount, log, chunkCount, senderEncryptedChunks.length);
            }
            this.setProgress(50+(int)(percentCount)/2);
            chunkCount++;
        }
        this.printPercent("Cipher Chunks Encrypted", percentCount, log, chunkCount, senderEncryptedChunks.length);
        this.setProgress(50+(int)(percentCount)/2);

        this.trace("Finished encrypting cipher text");

        int[] bits = EncryptorViewController.listToIntArray(bitPlacesToList(bitPlace));
        cipherImage = ImageEncryptor.ImageEncrypt
        (
            receiverEncryptedBody.toString(),
            cipherImage,
            1366,
            bits
        );

        this.trace("Finished inserting message into image");

        return cipherImage;
    }
    
    public static String replaceCharacters(String input)
    {
        String output = "";
        try
        {
            output = new String(input.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        Pattern unicodeOutliers = Pattern.compile
        (
            "[^\\\\u0000-\\\\u007F]",
            Pattern.UNICODE_CASE | Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE
        );
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(output);
        output = unicodeOutlierMatcher.replaceAll(" ");
        return output;
    }
    
    public static String encryptAndPad(BigInteger text, BigInteger key, BigInteger modulus)
    {
        StringBuilder padded = new StringBuilder(TheApp.chunkLength);
        String encrypted = RSA.crypt
        (
            text,
            key,
            modulus
        ).toString(2);
        for (int i = 0; i < TheApp.chunkLength - encrypted.length(); i++)
            padded.append('0');
        padded.append(encrypted);
        return padded.toString();
    }
    
    public static int bitPlacesToBitField(int[] bitPlace)
    {
        int bitField = 0;
        for (int i = 0; i < bitPlace.length; i++)
            bitField += (1 << i) * bitPlace[i];
        return bitField;
    }
    
    public static List bitPlacesToList(int[] bitPlace)
    {
        ArrayList<Integer> bitPlaceList = new ArrayList<>();
        for (int i = 0; i < bitPlace.length; i++)
        {
            if (bitPlace[i] == 1)
                bitPlaceList.add(i);
        }
        return bitPlaceList;
    }
    
    public static int[] bitPanelsToArray(MainViewDisplay.BitPanel[] bitPanels)
    {
        int[] bitPlaceArray= new int[bitPanels.length];
        for (int i = 0; i < bitPanels.length; i++)
        {
            if (bitPanels[i].getBackground().equals(Color.YELLOW))
                bitPlaceArray[i] = 1;
        }
        return bitPlaceArray;
    }
}
