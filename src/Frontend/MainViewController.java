
package Frontend;

import App.TheApp;
import RSA.*;
import RSA.User.ImageAndReceipt;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainViewController extends MainViewDisplay
{
    private class AddPrivateKeyAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new TextFileFilter());
            fileChooser.setDialogTitle("Import Private Key");
            int chooserDialog = fileChooser.showOpenDialog(MainViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                String fileContent = FileManager.readTextFromFile(fileChooser.getSelectedFile().getAbsolutePath()).trim();
                System.out.println(fileContent);
                if(fileContent.matches("[0-9]+"))
                {
                    LoginManager.getUser().getKeyPair().setPrivateKey(new BigInteger(fileContent));
                    privateKeyField.setText(LoginManager.getUser().getKeyPair().getPrivateKey().toString());
                    removePrivateKeyButton.setEnabled(true);
                }
                else
                    JOptionPane.showMessageDialog(null, "File is not in proper key format.");
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not open file, importing was cancelled");
            }
        }
    }
    
    private class RemovePrivateKeyAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            LoginManager.getUser().getKeyPair().setPrivateKey(null);
            privateKeyField.setText("");
            removePrivateKeyButton.setEnabled(false);
        }
    }
    
    private class ViewInboxImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            try
            {
                TheApp.openDisplay(new ImageViewController(LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getImage()));
            } catch (IOException ex) {
                Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private class ToggleReceiptBodyAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(inboxPreviewField.getText().equals(LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getReceiptText()))
                inboxPreviewField.setText(LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getBodyText());
            else
                inboxPreviewField.setText(LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getReceiptText());
            inboxPreviewField.setCaretPosition(0);
        }
    }
    
    private class DeleteInboxImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            int selectedIndex = inboxList.getSelectedIndex();
            if(LoginManager.getUser().getInbox().get(selectedIndex).getImage()!=null)
                FileManager.deleteFile(LoginManager.getUser().getInbox().get(selectedIndex).getImage());
            if(LoginManager.getUser().getInbox().get(selectedIndex).getMessage()!=null)
                FileManager.deleteFile(LoginManager.getUser().getInbox().get(selectedIndex).getMessage());
            
            inboxListModel.remove(selectedIndex);
            LoginManager.getUser().getInbox().remove(selectedIndex);
            if(inboxListModel.getSize()>0)
            {
                if(selectedIndex==0)
                    inboxList.setSelectedIndex(0);
                else
                    inboxList.setSelectedIndex(inboxListModel.getSize()-1);
            }
            InboxSelectionListener.valueChanged(null);
        }
    }
    
    private class DecryptInboxImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if
            (
                LoginManager.getUser().getKeyPair().getPrivateKey()!=null &&
                inboxListModel.getSize()!=0 &&
                !inboxList.isSelectionEmpty() &&
                inboxList.getSelectedIndex()>=0 &&
                LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getMessage() == null
            )
            {
                try
                {
                    TheApp.openDisplay(new DecryptorViewController(MainViewController.this));
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog
                (
                    null,
                    "You decrypt the encrypted image must have:\n\n" + 
                    "\u2022 A private key within the \"Private Key\" Field.\n" +
                    "\u2022 An unencrypted message in your \"Inbox\" selected.\n"
                );
            }
        }
    }
    
    private class SaveInboxTextAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new TextFileFilter());
            fileChooser.setDialogTitle("Save Message Text");
            int chooserDialog = fileChooser.showSaveDialog(MainViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".txt"))
                        fileName += ".txt";
                    File file = new File(fileName);
                    FileChannel end;
                    try (FileChannel start = new FileInputStream(LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getMessage()).getChannel())
                    {
                        end = new FileOutputStream(file).getChannel();
                        end.transferFrom(start, 0, start.size());
                    }
                    end.close();
                    JOptionPane.showMessageDialog(null, "Save Succesful");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not save, saving of text was cancelled");
            }
        }
    }
    
    private class AddEncryptTextAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new TextFileFilter());
            fileChooser.setDialogTitle("Import Text File");
            int chooserDialog = fileChooser.showOpenDialog(MainViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                String fileContent = FileManager.readTextFromFile(fileChooser.getSelectedFile().getAbsolutePath());
                encryptTextField.append(fileContent);
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not open file, importing was cancelled");
            }
        }
    }
    
    private class ClearEncryptTextAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            encryptTextField.setText("");
            clearEncryptTextButton.setEnabled(false);
        }
    }
    
    private class AddImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));
            fileChooser.setDialogTitle("Import Image File");
            int chooserDialog = fileChooser.showOpenDialog(MainViewController.this);
            if (chooserDialog == JFileChooser.APPROVE_OPTION)
            {
                imagePreviewPanel.setImage(fileChooser.getSelectedFile());
                imagePreviewPanel.repaint();
                removeImageButton.setEnabled(true);
            }
            else if (chooserDialog == JFileChooser.CANCEL_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Could not load image, importing was cancelled");
            }
        }
    }
    
    private class RemoveImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            imagePreviewPanel.removeImage();
            imagePreviewPanel.repaint();
            removeImageButton.setEnabled(false);
        }
    }
    
    private class InstructionsAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JOptionPane.showMessageDialog
            (
                null,
                "This program is designed to allow multiple uniquely identified users to be able to send encrypted messages between each other.\n" + 
                "\n" + 
                "To send an encrypted message to another user, you must:\n" + 
                "<html>\u2022 Upload your previously assigned <u>Private Key</u> by pressing the <i>Add Private Key</i> button.\n" +
                "<html>\u2022 Select at least one <u>Bit Place</u> from the array of <font color=red>red</font>, <font color=green>green</font> and <font color=blue>blue</font> boxes.\n" +
                "<html>\u2022 Upload or type your <u>Message Text</u> in the <i>Text To Encrypt</i> area.\n" + 
                "<html>\u2022 Select the <u>User</u> you want to receive your message from the <i>Select Recipient User</i> list.\n" + 
                "<html>\u2022 Upload the <u>Image</u> you want encrypted with your message by pressing the <i>Add Image</i> button.\n" +
                "<html>\u2022 Press the <i>Encrypt And Send</i> button.\n" +
                "\n" +
                "To decrypt an encrypted message in your inbox, you must:\n" +
                "<html>\u2022 Upload your previously assigned <u>Private Key</u> by pressing the <i>Add Private Key</i> button.\n" +
                "<html>\u2022 Select a <u>message</u> from your <i>Inbox</i>.\n" +
                "<html>\u2022 Press the <i>Decrypt Image</i> button.\n",
                "INSTRUCTIONS",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private class ViewEncryptedImageAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            int[] bitPlaces = listToIntArray(bitPanelsToList(bitPlacePanels));
            String encryptText = encryptTextField.getText();
            if(bitPlaces.length!=0 && !encryptText.equals("") && imagePreviewPanel.getImage()!=null)
            {
                String randomText = this.randomBinaryString(encryptText.length());
                try
                {
                    TheApp.openDisplay(new ImageViewController(ImageEncryptor.ImageEncrypt(randomText, imageToBufferedImage(imagePreviewPanel.getImage()), 0, bitPlaces)));
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog
                (
                    null,
                    "You preview the encrypted image must have:\n\n" + 
                    "\u2022 Text within the \"Text To Encrypt\" Field.\n" +
                    "\u2022 At least one \"Bit Place\" selected.\n" +
                    "\u2022 An image inside the \"Image To Encrypt\" box."
                );
            }
        }
        
        public String randomBinaryString(int plainTextLength)
        {
            StringBuilder randomText = new StringBuilder(plainTextLength);
            Random r = new Random();
            for(int i=0; i<plainTextLength*8; i++)
            {
                randomText.append(r.nextInt(2));
            }
            return randomText.toString();
        }
    }
    
    private class EncryptAndSendAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if
            (
                LoginManager.getUser().getKeyPair().getPrivateKey()!=null &&
                listToIntArray(bitPanelsToList(bitPlacePanels)).length!=0 &&
                !encryptTextField.getText().equals("") &&
                !selectRecipientList.isSelectionEmpty() &&
                imagePreviewPanel.getImage()!=null
            )
            {
                BufferedImage theImage = imageToBufferedImage(imagePreviewPanel.getImage());
                int potentialBits = theImage.getHeight()*theImage.getWidth()*listToIntArray(bitPanelsToList(bitPlacePanels)).length;
                if(potentialBits<=encryptTextField.getText().length()*8)
                    JOptionPane.showMessageDialog(null, "Not all text will be encrypted within the image: " + Double.toString(((double)potentialBits/(double)(encryptTextField.getText().length()*8))*100) + "%");
                try
                {
                    TheApp.openDisplay(new EncryptorViewController(MainViewController.this));
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog
                (
                    null,
                    "To encrypt an image must have:\n\n" +
                    "\u2022 A private key within the \"Private Key\" Field.\n" +
                    "\u2022 Text within the \"Text To Encrypt\" Field.\n" +
                    "\u2022 At least one \"Bit Place\" selected.\n" +
                    "\u2022 A \"Recipient User\" selected.\n" +
                    "\u2022 An image inside the \"Image To Encrypt\" box."
                );
            }
        }
    }
    
    public final ListSelectionListener InboxSelectionListener = new ListSelectionListener()
    {
        @Override
        public void valueChanged(ListSelectionEvent arg0)
        {
            boolean enableInbox;
            boolean enablePreview;
            String previewText;
            
            if (inboxListModel.getSize()==0 || inboxList.isSelectionEmpty() || inboxList.getSelectedIndex()<0)
            {
                enableInbox = false;
                enablePreview = false;
                previewText = "";
            }
            else if (LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getMessage() != null)
            {
                enableInbox = true;
                enablePreview = true;
                previewText = LoginManager.getUser().getInbox().get(inboxList.getSelectedIndex()).getReceiptText();
            }
            else
            {
                enableInbox = true;
                enablePreview = false;
                previewText = "";
            }
            
            viewInboxImageButton.setEnabled(enableInbox);
            deleteInboxImageButton.setEnabled(enableInbox);
            toggleReceiptBodyButton.setEnabled(enablePreview);
            saveInboxTextButton.setEnabled(enablePreview);
            inboxPreviewField.setText(previewText);
            inboxPreviewField.setCaretPosition(0);
        }
    };
    
    public final ListSelectionListener SelectRecipientSelectionListener = new ListSelectionListener()
    {
        @Override
        public void valueChanged(ListSelectionEvent arg0)
        {
            if (!selectRecipientList.isSelectionEmpty() && TheApp.users.get(selectRecipientList.getSelectedIndex()).getUsername().equals(LoginManager.getUser().getUsername()))
            {
                selectRecipientList.clearSelection();
            }
        }
    };
    
    public final DocumentListener EncryptTextDocumentListener = new DocumentListener()
    {
        @Override
        public void changedUpdate(DocumentEvent e)
        {
            checkIfEmpty();
        }
        
        @Override
        public void removeUpdate(DocumentEvent e)
        {
            checkIfEmpty();
        }
        
        @Override
        public void insertUpdate(DocumentEvent e)
        {
            checkIfEmpty();
        }
        
        public void checkIfEmpty()
        {
            if (encryptTextField.getText().length()<=0)
                clearEncryptTextButton.setEnabled(false);
            else
                clearEncryptTextButton.setEnabled(true);
        }
    };
    
    public final MouseAdapter BitPlaceMouseAdapter = new MouseAdapter()
    {
        @Override
        public void mouseReleased(MouseEvent me)
        {
            BitPanel panel = (BitPanel)me.getSource();
            Color c = panel.getBackground();
            if(c.equals(Color.YELLOW))
            {
                if(panel.getPosition()<8)
                    panel.setBackground(new Color((int)((((double)panel.getPosition()+1.0)/8.0)*255.0),0,0));
                else if(panel.getPosition()<16)
                    panel.setBackground(new Color(0,(int)((((double)panel.getPosition()-8.0+1.0)/8.0)*255.0),0));
                else if(panel.getPosition()<24)
                    panel.setBackground(new Color(0,0,(int)((((double)panel.getPosition()-16.0+1.0)/8.0)*255.0)));
            }
            else
                panel.setBackground(Color.YELLOW);
        }
    };
    
    // Initializes the functions
    public MainViewController() throws IOException
    {
        super();
        
        this.publicKeyField.setText(LoginManager.getUser().getKeyPair().getPublicKey().toString());
        this.addPrivateKeyButton.addActionListener(new AddPrivateKeyAction());
        this.removePrivateKeyButton.addActionListener(new RemovePrivateKeyAction());
        this.inboxList.addListSelectionListener(this.InboxSelectionListener);
        this.viewInboxImageButton.addActionListener(new ViewInboxImageAction());
        this.toggleReceiptBodyButton.addActionListener(new ToggleReceiptBodyAction());
        this.deleteInboxImageButton.addActionListener(new DeleteInboxImageAction());
        this.saveInboxTextButton.addActionListener(new SaveInboxTextAction());
        this.addEncryptTextButton.addActionListener(new AddEncryptTextAction());
        this.clearEncryptTextButton.addActionListener(new ClearEncryptTextAction());
        this.encryptTextField.getDocument().addDocumentListener(this.EncryptTextDocumentListener);
        this.selectRecipientList.addListSelectionListener(this.SelectRecipientSelectionListener);
        this.addImageButton.addActionListener(new AddImageAction());
        this.removeImageButton.addActionListener(new RemoveImageAction());
        this.previewImageButton.addActionListener(new ViewEncryptedImageAction());
        this.encryptImageButton.addActionListener(new EncryptAndSendAction());
        this.decryptInboxImageButton.addActionListener(new DecryptInboxImageAction());
        this.instructionsButton.addActionListener(new InstructionsAction());
        
        for(ImageAndReceipt message : LoginManager.getUser().getInbox())
        {
            if(message.getMessage()==null)
                this.inboxListModel.insertElementAt(message.getImage().getName(), this.inboxListModel.getSize());
            else
                this.inboxListModel.insertElementAt(message.getImage().getName() + " (decrypted)", this.inboxListModel.getSize());
        }
        if(this.inboxListModel.getSize()>0)
        {
            this.inboxList.setSelectedIndex(0);
            this.InboxSelectionListener.valueChanged(null);
        }
        
        for(JPanel bitPanel : this.bitPlacePanels)
        {
            bitPanel.addMouseListener(this.BitPlaceMouseAdapter);
        }
        
        for(User user : TheApp.users)
        {
            if(user.getUsername().equals(LoginManager.getUser().getUsername()))
                this.selectRecipientModel.insertElementAt("<html><strike>" + user.getUsername() + "</strike></html>", this.selectRecipientModel.getSize());
            else
                this.selectRecipientModel.insertElementAt(user.getUsername(), this.selectRecipientModel.getSize());
        }
        if(this.selectRecipientModel.getSize()>0)
        {
            this.selectRecipientList.setSelectedIndex(0);
            this.SelectRecipientSelectionListener.valueChanged(null);
        }
    }
    
    public static List bitPanelsToList(MainViewDisplay.BitPanel[] bitPanels)
    {
        ArrayList<Integer> bitPlaceList = new ArrayList<>();
        for (int i = 0; i < bitPanels.length; i++)
        {
            if (bitPanels[i].getBackground().equals(Color.YELLOW))
                bitPlaceList.add(i);
        }
        return bitPlaceList;
    }
    
    public BufferedImage imageToBufferedImage(Image image)
    {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;
        BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bImage;
    }
}
