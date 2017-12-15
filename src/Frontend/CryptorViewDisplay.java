
package Frontend;

import RSA.Cryptor;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public abstract class CryptorViewDisplay extends ViewDisplay
{
    public MainViewController mainViewController;
    public Cryptor cryptor;
    public String crypt;
    
    public JButton startButton;
    public JProgressBar progressBar;
    public JTextArea taskOutput;
    public JScrollPane taskScroll;
    public JPanel taskOutputPanel;
    public JButton cancelButton;
    public int pad;

    public CryptorViewDisplay(MainViewController mainViewController, String crypt)
    {
        super(600, 400, "Image " + crypt + "ion", false);
        
        this.mainViewController = mainViewController;
        
        this.crypt = crypt;
        this.pad = 20;
        
        ////////////////////////////////////////////////////////
        
        this.startButton = new JButton("Start");
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(pad, pad, 0, pad);
        this.mainDisplayPane.add(this.startButton, c);
        
        ////////////////////////////////////////////////////////
 
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 1;
        this.c.gridy = 0;
        this.c.gridwidth = 3;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(pad, 0, 0, pad);
        this.mainDisplayPane.add(this.progressBar, c);
        
        ////////////////////////////////////////////////////////
        
        this.taskOutput = new JTextArea(10,45);
        this.taskOutput.setMinimumSize(new Dimension(this.getWidth()-pad, this.getHeight()-pad));
        this.taskOutput.setMargin(new Insets(5,5,5,5));
        this.taskOutput.setEditable(false);
        DefaultCaret caret = (DefaultCaret) this.taskOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.taskScroll = new JScrollPane(this.taskOutput);
        this.taskScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        
        this.taskOutputPanel = new JPanel();
        this.taskOutputPanel.add(this.taskScroll);
        this.taskOutputPanel.setLayout(new GridLayout(0, 1));
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 1;
        this.c.gridwidth = 4;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(pad, pad, pad, pad);
        this.mainDisplayPane.add(this.taskOutputPanel, c);
        
        ////////////////////////////////////////////////////////
        
        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setEnabled(false);
        
        this.c = new GridBagConstraints();
        this.c.gridx = 0;
        this.c.gridy = 2;
        this.c.fill = GridBagConstraints.BOTH;
        this.c.insets = new Insets(0, pad, pad, pad);
        this.mainDisplayPane.add(this.cancelButton, c);
        
        ////////////////////////////////////////////////////////
        
    }
    
    public void setCryptor(Cryptor cryptor)
    {
        this.cryptor = cryptor;
    }
    
    public void addListeners()
    {
        this.cryptor.addPropertyChangeListener(new CryptionProgressListener());
        this.startButton.addActionListener(new StartCryptionAction());
        this.cancelButton.addActionListener(new CancelCryptionAction());
    }
    
    public class StartCryptionAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            startButton.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            cryptor.execute();
        }
    }
    
    public class CancelCryptionAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent evt)
        {
            if(cryptor.cancel(true))
                cancelButton.setEnabled(false);
            else
                JOptionPane.showMessageDialog(null, crypt + "ion could not be cancelled");
        }
    }
    
    public class CryptionProgressListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            if ("progress".equals(evt.getPropertyName()))
            {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            }
        }
    }
}
