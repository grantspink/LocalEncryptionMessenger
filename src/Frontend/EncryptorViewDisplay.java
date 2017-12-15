
package Frontend;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.JButton;

public abstract class EncryptorViewDisplay  extends CryptorViewDisplay
{
    public JButton saveButton;
    public JButton viewImageButton;
    public JButton sendButton;
    
    public EncryptorViewDisplay(MainViewController mainViewController) throws IOException
    {
        super(mainViewController, "Encrypt");
        
        this.saveButton = new JButton("Save Image");
        this.saveButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 1;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(0, 0, pad, pad);
        this.mainDisplayPane.add(this.saveButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.viewImageButton = new JButton("View Image");
        this.viewImageButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 2;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(0, 0, pad, pad);
        this.mainDisplayPane.add(this.viewImageButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.sendButton = new JButton("Send");
        this.viewImageButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 3;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(0, 0, pad, pad);
        this.mainDisplayPane.add(this.sendButton, c);
        
        ////////////////////////////////////////////////////////
        
        this.pack();
    }
}

