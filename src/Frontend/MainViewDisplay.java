
package Frontend;

import RSA.LoginManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public abstract class MainViewDisplay extends ViewDisplay
{
    public JLabel publicKeyLabel;
    public JTextArea publicKeyField;
    public JScrollPane publicKeyScroll;
    public JPanel publicKeyPanel;
    public JLabel usernameLabel;
    
    public JLabel privateKeyLabel;
    public JButton addPrivateKeyButton;
    public JButton removePrivateKeyButton;
    public JTextArea privateKeyField;
    public JScrollPane privateKeyScroll;
    public JPanel privateKeyPanel;
    
    public JLabel inboxLabel;
    public DefaultListModel inboxListModel;
    public JList inboxList;
    public JScrollPane inboxScroll;
    public JPanel inboxPanel;
    public JButton viewInboxImageButton;
    public JButton deleteInboxImageButton;
    public JButton decryptInboxImageButton;
    
    public JLabel inboxPreviewLabel;
    public JTextArea inboxPreviewField;
    public JScrollPane inboxPreviewScroll;
    public JPanel inboxPreviewPanel;
    public JButton toggleReceiptBodyButton;
    public JButton saveInboxTextButton;
    
    public JLabel encryptTextLabel;
    public JTextArea encryptTextField;
    public JScrollPane encryptTextScroll;
    public JPanel encryptTextPanel;
    public JButton addEncryptTextButton;
    public JButton clearEncryptTextButton;
    
    public JLabel selectRecipientLabel;
    public DefaultListModel selectRecipientModel;
    public JList selectRecipientList;
    public JScrollPane selectRecipientScroll;
    public JPanel selectRecipientPanel;
    
    public JLabel imagePreviewLabel;
    public ImagePanel imagePreviewPanel;
    public JButton addImageButton;
    public JButton removeImageButton;
    
    public JLabel bitPlaceLabel;
    public JLabel[] bitPlaceIndexLabels;
    public BitPanel[] bitPlacePanels;
    public JLabel redLabel;
    public JLabel blueLabel;
    public JLabel greenLabel;
    
    public JButton instructionsButton;
    public JButton deleteUserButton;
    public JButton previewImageButton;
    public JButton encryptImageButton;
    
    // Sets GUI settings
    public MainViewDisplay() throws IOException
    {
        super(1550, 500, "Image Encryptor", false);
        
        int row3Padding = 20;
        int row5Padding = 20;
        int collumn5Padding = 20;
        
        ////////////////////////////////////////////////////////
        
        this.publicKeyLabel = new JLabel("Public Key", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.c.gridwidth = 3;
        this.mainDisplayPane.add(this.publicKeyLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.publicKeyField = new JTextArea(7, 10);
        this.publicKeyField.setLineWrap(true);
        this.publicKeyField.setEditable(false);
        
        this.publicKeyScroll = new JScrollPane(this.publicKeyField);
        this.publicKeyScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.publicKeyPanel = new JPanel();
        this.publicKeyPanel.add(this.publicKeyScroll);
        this.publicKeyPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.c.gridwidth = 3;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.publicKeyPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.usernameLabel = new JLabel("Username: " + LoginManager.getUser().getUsername(), JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 2;
        this.c.gridwidth = 3;
        this.mainDisplayPane.add(this.usernameLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.privateKeyLabel = new JLabel("Private Key", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 0;
        this.c.gridwidth = 2;
        this.mainDisplayPane.add(this.privateKeyLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.addPrivateKeyButton = new JButton("<html><center>Add<br>Private Key</center><html>");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.addPrivateKeyButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.removePrivateKeyButton = new JButton("<html><center>Remove<br>Private Key</center><html>");
        this.removePrivateKeyButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 4;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.removePrivateKeyButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.privateKeyField = new JTextArea(7,10);
        this.privateKeyField.setLineWrap(true);
        this.privateKeyField.setEditable(false);
        
        this.privateKeyScroll = new JScrollPane(this.privateKeyField);
        this.privateKeyScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.privateKeyPanel = new JPanel();
        this.privateKeyPanel.add(this.privateKeyScroll);
        this.privateKeyPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 1;
        this.c.gridwidth = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.privateKeyPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.inboxLabel = new JLabel("Inbox", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 3;
        this.c.gridwidth = 3;
        this.c.insets = new Insets(row3Padding,0,0,0);
        this.mainDisplayPane.add(this.inboxLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.inboxListModel = new DefaultListModel();
        
        this.inboxList = new JList(this.inboxListModel);
        this.inboxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.inboxList.setFixedCellWidth(10);
        
        this.inboxScroll = new JScrollPane(this.inboxList);
        this.inboxScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.inboxPanel = new JPanel();
        this.inboxPanel.add(this.inboxScroll);
        this.inboxPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 4;
        this.c.gridwidth = 3;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.inboxPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.viewInboxImageButton = new JButton("<html><center>View<br>Image</center><html>");
        this.viewInboxImageButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 5;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.viewInboxImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.deleteInboxImageButton = new JButton("<html><center>Delete<br>Image</center><html>");
        this.deleteInboxImageButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 1;
        this.c.gridy = 5;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.deleteInboxImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.decryptInboxImageButton = new JButton("<html><center>Decrypt<br>Image</center><html>");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 2;
        this.c.gridy = 5;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.decryptInboxImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.inboxPreviewLabel = new JLabel("Decrypted Text Preview", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 3;
        this.c.gridwidth = 2;
        this.c.insets = new Insets(row3Padding,0,0,0);
        this.mainDisplayPane.add(this.inboxPreviewLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.inboxPreviewField = new JTextArea(10,10);
        this.inboxPreviewField.setLineWrap(true);
        this.inboxPreviewField.setEditable(false);
        
        this.inboxPreviewScroll = new JScrollPane(this.inboxPreviewField);
        this.inboxPreviewScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.inboxPreviewPanel = new JPanel();
        this.inboxPreviewPanel.add(this.inboxPreviewScroll);
        this.inboxPreviewPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 4;
        this.c.gridwidth = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.inboxPreviewPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.toggleReceiptBodyButton = new JButton("<html><center>Receipt /<br>Body</center><html>");
        this.toggleReceiptBodyButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 5;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.toggleReceiptBodyButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.saveInboxTextButton = new JButton("<html><center>Save<br>Text</center><html>");
        this.saveInboxTextButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 4;
        this.c.gridy = 5;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.saveInboxTextButton, c);
        
        ////////////////////////////////////////////////////////
        
        
        this.encryptTextLabel = new JLabel("Text To Encrypt", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 6;
        this.c.gridy = 3;
        this.c.gridwidth = 8;
        this.c.insets = new Insets(row3Padding,0,0,0);
        this.mainDisplayPane.add(this.encryptTextLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.encryptTextField = new JTextArea(10,10);
        this.encryptTextField.setLineWrap(true);
        this.encryptTextField.setEditable(true);
        
        this.encryptTextScroll = new JScrollPane(this.encryptTextField);
        this.encryptTextScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.encryptTextPanel = new JPanel();
        this.encryptTextPanel.add(this.encryptTextScroll);
        this.encryptTextPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 6;
        this.c.gridy = 4;
        this.c.gridwidth = 8;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.encryptTextPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.addEncryptTextButton = new JButton("<html><center>Add<br>Text</center><html>");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 6;
        this.c.gridy = 5;
        this.c.gridwidth = 4;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.addEncryptTextButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.clearEncryptTextButton = new JButton("<html><center>Remove<br>Text</center><html>");
        this.clearEncryptTextButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 10;
        this.c.gridy = 5;
        this.c.gridwidth = 4;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.clearEncryptTextButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.bitPlaceLabel = new JLabel("Bit Places: ", JLabel.LEFT);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 5;
        this.c.gridy = 0;
        this.c.insets = new Insets(0,collumn5Padding,0,0);
        this.mainDisplayPane.add(this.bitPlaceLabel, c);
        
        ////////////////////////////////////////////////////////
        
        int bitPanelSize = 30;
        int numberOfPanels = 24;
        int gridx = 6;
        this.bitPlacePanels = new BitPanel[numberOfPanels];
        
        this.c = new GridBagConstraints();
        for (int i=0; i<this.bitPlacePanels.length; i++)
        {
            this.c.gridx = i + gridx;
            this.c.gridy = 1;
            this.c.fill = GridBagConstraints.BOTH;

            this.bitPlacePanels[i] = new BitPanel(i);
            Border border;
            if (i % (this.bitPlacePanels.length/3) == 0)
            {
                border = new MatteBorder(bitPanelSize/10, bitPanelSize/10*3, bitPanelSize/10, 0, Color.BLACK);
                this.bitPlacePanels[i].setPreferredSize(new Dimension(bitPanelSize + bitPanelSize/10*3, bitPanelSize + bitPanelSize/10));
            }
            else if (i == this.bitPlacePanels.length-1)
            {
                border = new MatteBorder(bitPanelSize/10, bitPanelSize/10, bitPanelSize/10, bitPanelSize/10*3, Color.BLACK);
                this.bitPlacePanels[i].setPreferredSize(new Dimension(bitPanelSize + bitPanelSize/10*4, bitPanelSize + bitPanelSize/10));
            }
            else
            {
                border = new MatteBorder(bitPanelSize/10, bitPanelSize/10, bitPanelSize/10, 0, Color.BLACK);
                this.bitPlacePanels[i].setPreferredSize(new Dimension(bitPanelSize + bitPanelSize/10, bitPanelSize + bitPanelSize/10));
            }
            this.bitPlacePanels[i].setBorder(border);
            if(i<8)
            {
                this.bitPlacePanels[i].setBackground(new Color((int)((((double)i+1.0)/8.0)*255.0),0,0));
            }
            else if(i<16)
            {
                this.bitPlacePanels[i].setBackground(new Color(0,(int)((((double)i-8.0+1.0)/8.0)*255.0),0));
            }
            else if(i<24)
            {
                this.bitPlacePanels[i].setBackground(new Color(0,0,(int)((((double)i-16.0+1.0)/8.0)*255.0)));
            }
            
            this.mainDisplayPane.add(this.bitPlacePanels[i], c);
        }
        
        ////////////////////////////////////////////////////////
        
        int numberOfLabels = 24;
        gridx = 6;
        this.bitPlaceIndexLabels = new JLabel[numberOfLabels];
        
        this.c = new GridBagConstraints();
        for (int i=0; i<this.bitPlaceIndexLabels.length; i++)
        {
            this.c.gridx = i + gridx;
            this.c.gridy = 0;
            this.bitPlaceIndexLabels[i] = new JLabel(Integer.toString(i), JLabel.CENTER);
            this.mainDisplayPane.add(this.bitPlaceIndexLabels[i], c);
        }
        
        ////////////////////////////////////////////////////////
        
        this.redLabel = new JLabel(" Least ←                   RED                   → Most", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 6;
        this.c.gridy = 2;
        this.c.gridwidth = 8;
        this.mainDisplayPane.add(this.redLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.greenLabel = new JLabel(" Least ←                  GREEN                 → Most", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 14;
        this.c.gridy = 2;
        this.c.gridwidth = 8;
        this.mainDisplayPane.add(this.greenLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.blueLabel = new JLabel(" Least ←                   BLUE                  → Most", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 22;
        this.c.gridy = 2;
        this.c.gridwidth = 8;
        this.mainDisplayPane.add(this.blueLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.selectRecipientLabel = new JLabel("Select Recipient User", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 14;
        this.c.gridy = 3;
        this.c.gridwidth = 8;
        this.c.insets = new Insets(row3Padding,0,0,0);
        this.mainDisplayPane.add(this.selectRecipientLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.selectRecipientModel = new DefaultListModel();
        
        this.selectRecipientList = new JList(this.selectRecipientModel);
        this.selectRecipientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.selectRecipientList.setFixedCellWidth(10);
        
        this.selectRecipientScroll = new JScrollPane(this.selectRecipientList);
        this.selectRecipientScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.selectRecipientPanel = new JPanel();
        this.selectRecipientPanel.add(this.selectRecipientScroll);
        this.selectRecipientPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 14;
        this.c.gridy = 4;
        this.c.gridwidth = 8;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.selectRecipientPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.imagePreviewLabel = new JLabel("Image To Encrypt", JLabel.CENTER);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 22;
        this.c.gridy = 3;
        this.c.gridwidth = 8;
        this.c.insets = new Insets(row3Padding,0,0,0);
        this.mainDisplayPane.add(this.imagePreviewLabel, c);
        
        ////////////////////////////////////////////////////////
        
        this.imagePreviewPanel = new ImagePanel();
        this.imagePreviewPanel.setLayout(new GridLayout(0, 1));
        this.imagePreviewPanel.setBackground(Color.BLACK);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 22;
        this.c.gridy = 4;
        this.c.gridwidth = 8;
        this.c.fill = GridBagConstraints.BOTH;
        this.mainDisplayPane.add(this.imagePreviewPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.addImageButton = new JButton("<html><center>Add<br>Image</center><html>");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 22;
        this.c.gridy = 5;
        this.c.gridwidth = 4;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.addImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.removeImageButton = new JButton("<html><center>Remove<br>Image</center><html>");
        this.removeImageButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 26;
        this.c.gridy = 5;
        this.c.gridwidth = 4;
        this.c.insets = new Insets(1,0,row5Padding,0);
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.removeImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.instructionsButton = new JButton("INSTRUCTIONS");
        this.instructionsButton.setPreferredSize(new Dimension(0, 35));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 6;
        this.c.gridwidth = 2;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.instructionsButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.deleteUserButton = new JButton("<html><center>Delete<br>User</center><html>");
        this.deleteUserButton.setPreferredSize(new Dimension(0, 35));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 6;
        this.c.gridwidth = 1;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.deleteUserButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.previewImageButton = new JButton("<html><center>Preview<br>Image</center><html>");
        this.previewImageButton.setPreferredSize(new Dimension(0, 35));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 22;
        this.c.gridy = 6;
        this.c.gridwidth = 4;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.previewImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.encryptImageButton = new JButton("<html><center>Encrypt<br>And Send</center><html>");
        this.encryptImageButton.setPreferredSize(new Dimension(0, 35));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 26;
        this.c.gridy = 6;
        this.c.gridwidth = 4;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.mainDisplayPane.add(this.encryptImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        getRootPane().setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.pack();
    }
}



