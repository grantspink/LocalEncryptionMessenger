
package Frontend;

import App.TheApp;
import RSA.Encryptor;
import RSA.FileManager;
import RSA.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class EncryptorViewController extends EncryptorViewDisplay
{
    public final User recipientUser;
    public Encryptor encryptor;
    
    public class SaveImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ImageFileFilter());
            fileChooser.setDialogTitle("Save Encrypted Image");
            int chooserDialog = fileChooser.showSaveDialog(EncryptorViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    String fileName = appendExtension(fileChooser.getSelectedFile().toString(), ".png");
                    FileManager.writeImageToFile(encryptor.get(), "png", new File(fileName));
                    JOptionPane.showMessageDialog(null, "Save Succesful");
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(EncryptorViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not save, saving of image was cancelled");
            }
        }
        
        public String appendExtension(String s, String ext)
        {
            if (!s.endsWith(ext))
                s += ext;
            return s;
        }
    }
    
    public class ViewImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            try
            {
                TheApp.openDisplay(new ImageViewController(encryptor.get()));
            } catch (IOException ex) {
                Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(EncryptorViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public class SendImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            try
            {
                String imageName = "message" + String.format("%03d", recipientUser.getInbox().size()) + ".png";
                String filePath = FileManager.getInbox(recipientUser.getUserDirectory()) + "\\" + imageName;
                FileManager.writeImageToFile(encryptor.get(), "png", new File(filePath));
                JOptionPane.showMessageDialog(null, "Send Succesful");
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(EncryptorViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public EncryptorViewController(MainViewController mainViewController) throws IOException
    {
        super(mainViewController);
        this.recipientUser = TheApp.users.get(this.mainViewController.selectRecipientList.getSelectedIndex());
        
        this.setCryptor(new Encryptor(this));
        this.addListeners();
        this.encryptor = (Encryptor) this.cryptor;
        
        this.saveButton.addActionListener(new SaveImageAction());
        this.viewImageButton.addActionListener(new ViewImageAction());
        this.sendButton.addActionListener(new SendImageAction());
    }
}
