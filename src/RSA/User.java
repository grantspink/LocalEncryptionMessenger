
package RSA;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User
{
    private String username;
    private String passwordHash;
    private File userDirectory;
    private KeyPair keyPair;
    private List<ImageAndReceipt> inbox;
    private List<File> outbox;
    
    public User(String username, String passwordHash, File userDirectory, int bitLength)
    {
        setUsername(username);
        setPasswordHash(passwordHash);
        setUserDirectory(userDirectory);
        setKeyPair(bitLength);
        constructInbox(userDirectory);
        outbox = FileManager.getImageFiles(FileManager.getOutbox(userDirectory));
    }
    
    public User(String name)
    {
        this.username = name;
    }
    
    public User(String username, String passwordHash, File userDirectory, int bitLength, BigInteger publicKey, BigInteger modulus)
    {
        setUsername(username);
        setPasswordHash(passwordHash);
        setUserDirectory(userDirectory);
        setKeyPair(publicKey, modulus, bitLength);
        constructInbox(userDirectory);
        outbox = FileManager.getImageFiles(FileManager.getOutbox(userDirectory));
    }
    
    public String getUsername(){ return this.username; }
    public String getPasswordHash(){ return this.passwordHash; }
    public File getUserDirectory(){ return this.userDirectory; }
    public KeyPair getKeyPair(){ return this.keyPair; }
    public List<ImageAndReceipt> getInbox(){ return this.inbox; }
    public List<File> getOutbox(){ return this.outbox; }
    
    public final void setUsername(String username){ this.username = username; }
    public final void setPasswordHash(String passwordHash){ this.passwordHash = passwordHash; }
    public final void setUserDirectory(File userDirectory){ this.userDirectory = userDirectory; }
    public final void setKeyPair(BigInteger publicKey, BigInteger modulus, int bitLength){ this.keyPair = new KeyPair(publicKey, modulus, bitLength); }
    public final void setKeyPair(int bitLength){ this.keyPair = new KeyPair(bitLength); }
    
    public final void constructInbox(File userDirectory)
    {
        List<File> imageFiles = new ArrayList<>(FileManager.getImageFiles(FileManager.getInbox(userDirectory)));
        List<File> textFiles = new ArrayList<>(FileManager.getTextFiles(FileManager.getInbox(userDirectory)));
        inbox = new ArrayList<>();
        System.out.println(Arrays.toString(imageFiles.toArray()));
        System.out.println(Arrays.toString(textFiles.toArray()));
        for(File image : imageFiles)
        {
            if(textFiles.size()>0 && image.getName().substring(image.getName().length()-6, image.getName().length()-3).equals(textFiles.get(0).getName().substring(textFiles.get(0).getName().length()-6, textFiles.get(0).getName().length()-3)))
            {
                inbox.add(new ImageAndReceipt(image, textFiles.get(0)));
                textFiles.remove(0);
            }
            else
            {
                inbox.add(new ImageAndReceipt(image, null));
            }
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;
        
        User other = (User)obj;
        return username.equals(other.username);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.username);
        return hash;
    }
    
    public class ImageAndReceipt
    {
        private final File image;
        private File message;
        private String receiptText;
        private String bodyText;
        
        public ImageAndReceipt(File image, File message)
        {
            this.image = image;
            this.message = message;
            this.setMessage(message);
        }
        
        public final void setMessage(File message)
        {
            this.message = message;
            if(message!=null)
            {
                String[] receiptAndBody = FileManager.readTextFromFile(message.getAbsolutePath()).split("&&");
                if(receiptAndBody.length!=2)
                {
                    this.receiptText = "Message data file is not in proper format.";
                    this.bodyText = "Message data file is not in proper format.";
                }
                else
                {
                    this.receiptText = receiptAndBody[0];
                    this.bodyText = receiptAndBody[1];
                }
            }
        }
        
        public File getImage(){ return this.image; }
        public File getMessage(){ return this.message; }
        public String getReceiptText(){ return this.receiptText; }
        public String getBodyText(){ return this.bodyText; }
    }
}
